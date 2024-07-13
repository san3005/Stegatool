

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.util.List;


public class Stegatool {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: stegatool [createwm|verifywm] <other_arguments>");
            System.exit(1);
        }

        String command = args[0];
        
        switch (command) {
            case "createwm":
                if (args.length < 7) {
                    System.out.println("Usage: stegatool createwm -inputdir <inputDir> -outputdir <outputDir> -peoplelist <peopleListFile>");
                    System.exit(1);
                }

                createWatermarkedImages(args[2], args[4], args[6]);
                break;
            case "verifywm":
                if (args.length < 3) {
                    System.out.println("Usage: stegatool verifywm -inputdir <inputDir>");
                    System.exit(1);
                }

                verifyWatermarkedImages(args[2]);
                break;
            default:
                System.out.println("Invalid command. Use 'createwm' or 'verifywm'.");
                System.exit(1);
        }
    }
    private static void createWatermarkedImages(String inputDir, String peopleListFile, String outputDir) {
        List<String> people = readPeopleList(peopleListFile);
        File inputPath = new File(inputDir);
        File outputPath = new File(outputDir);
        
        if (!inputPath.exists() || !outputPath.exists()) {
            System.out.println("Input and output directories must exist.");
            System.exit(1);
        }

        File[] imageFiles = inputPath.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg") || name.toLowerCase().endsWith(".png"));

        for (File imgFile : imageFiles) {
            for (int i = 0; i < people.size(); i++) {
                String person = people.get(i);
                String watermarkText = person;
                String outputFileName = imgFile.getName().substring(0, imgFile.getName().lastIndexOf('.')) + "_" + person + ".png";
                File tempFile = new File("example.txt");

                try (FileWriter fileWriter = new FileWriter(tempFile)) {
                    fileWriter.write(watermarkText);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String cmd = "java -jar openstego.jar embed -a RandomLSB -mf " + tempFile.getName() + " -cf " + imgFile.getAbsolutePath() + " -sf " + outputFileName + " -p 1234";
                // System.out.println(cmd);
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder(cmd.split(" "));
                    Process process = processBuilder.start();
                    int exitCode = process.waitFor();
                    Files.copy(Paths.get(outputFileName), Paths.get(outputDir, outputFileName));
                } catch (IOException | InterruptedException e) {
                    // e.printStackTrace();
                    System.out.println("File exists "+outputFileName);
                }

                tempFile.delete();
                new File(outputFileName).delete();
            }
        }
    }


    private static List<String> readPeopleList(String peopleListFile) {
        List<String> people = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(peopleListFile))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                people.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return people;
    }

   private static void verifyWatermarkedImages(String inputDir) {
        File inputPath = new File(inputDir);

        if (!inputPath.exists()) {
            System.out.println("Input directory must exist.");
            System.exit(1);
        }

        File[] imageFiles = inputPath.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg") || name.toLowerCase().endsWith(".png"));

        for (File imgFile : imageFiles) {
            String cwd = System.getProperty("user.dir");
            String cmd = "java -jar openstego.jar extract -a RandomLSB -sf " + imgFile.getAbsolutePath() + " -xd " + cwd + " -p 1234";
            
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(cmd.split(" "));
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                String output2 = captureOutput(process);
               
                String result = output2.trim();
                
                if (result.length() == 0 || result.contains("Embedded data is corrupt") ) {
                    System.out.println(imgFile.getName() + " ---> " + "No watermark found");
                    continue;
                }
              
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    File tempFile = new File("example.txt");
                    List<String> output1 = Files.readAllLines(tempFile.toPath());
                    System.out.println(imgFile.getName() + " ---> " + String.join(",", output1));
                    tempFile.delete();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String captureOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString();
        }
    }
}