
# Stegatool

Stegatool is a Java-based application for applying and verifying watermarks on images. It supports two main operations: creating watermarked images and verifying existing watermarks.

![Java](https://img.shields.io/badge/Java-8%2B-brightgreen)

## 🧐 Features

Here are some of the project's best features:

* Create Watermarked Images: Apply watermarks to a batch of images based on a provided list of people.
* Verify Watermarked Images: Check the integrity and presence of watermarks in images.

## 🛠️ Installation Steps

1. **Clone the Repository**

2. **Open your terminal or command prompt**

3. **Navigate to the directory where you want to clone the repository**

4. **Run the command to clone the repository**

    ```sh
    git clone https://github.com/yourusername/stegatool.git
    ```

5. **Change to the repository directory**

    ```sh
    cd stegatool
    ```

6. **Compile the Java Program**

    ```sh
    javac Stegatool.java
    ```

7. **Prepare the Input Directory and Files**

    1. Create a directory named `input_images` in the project directory.

        ```sh
        mkdir input_images
        ```

    2. Add images to the `input_images` directory.

    3. Create a text file named `people_list.txt` in the project directory.

        ```sh
        touch people_list.txt
        ```

    4. Open `people_list.txt` with a text editor and add the list of people, each name on a new line.

## 💻 Built with

Technologies used in the project:

* Java


## 🚀 Usage

### Create Watermarked Images

```sh
java Stegatool createwm -inputdir <inputDir> -outputdir <outputDir> -peoplelist <peopleListFile>
```

### Verify Watermarked Images

```sh
java Stegatool verifywm -inputdir <inputDir>
```

## Example

### Creating Watermarked Images

```sh
java Stegatool createwm -inputdir ./input_images -outputdir ./output_images -peoplelist ./people_list.txt
```

### Verifying Watermarked Images

```sh
java Stegatool verifywm -inputdir ./output_images
```

## Directory Structure

```sh
.
├── input_images/       # Directory containing input images
├── output_images/      # Directory to save watermarked images
├── people_list.txt     # File containing list of people for watermarking
└── Stegatool.java      # Java source file
```

## Compilation

To compile Stegatool, use the following command:

```sh
javac Stegatool.java
```

## Author
 
👨‍💻 Santosh Gurajada

