package com.joelallison.generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandling {
    public static void createFile(String filename) {
        try {
            File file = new File(filename);
            if (file.createNewFile()) {
                System.out.println("Created file: " + file.getAbsolutePath());
            } else {
                System.out.println("File already exists.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String filename, String[] text) {
        try {
            FileWriter myWriter = new FileWriter(filename, true);

            for (int i = 0; i < text.length - 1; i++) {
                myWriter.write(text[i] + "\n");
            } myWriter.write(text[text.length-1]); //so that last line doesn't have \n

            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static List<String> readFromFile(String filename) {
        List<String> fileText = new ArrayList<String>();
        try {
            File file = new File("filename.txt");
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String currentLine = myReader.nextLine();
                fileText.add(currentLine);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return fileText;
    }

    public String[] RGBA2dArrayToStringArray(RGBA[][] inputArray) {
        String[] outputArray = new String[inputArray.length];
        String line = "";

        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                line = line + inputArray[i][j].toString();
            }
            outputArray[i] = line;
        }

        return outputArray;
    }

    public static String[] mazeToStringArray(int[][] maze, String format, boolean invert) {
        String[] outputArray;

        String[] mazeArray = new String[maze.length];

        int modVal = 0;
        if(invert) { modVal = -1; }

        for (int i = 0; i < maze.length; i++) {
            String mazeLine = "";
            for (int j = 0; j < maze[i].length; j++) {
                mazeLine = mazeLine + Math.abs(modVal + maze[i][j]) + " ";
            } mazeArray[i] = mazeLine;
        }

        outputArray = toFileFormat(mazeArray, format, true);



        return outputArray;
    }

    public static String[] toFileFormat(String[] inputArray, String format, boolean bigEndian) {
        String[] outputArray;

        switch(format) {
            //RGBA has 4 separate values at each position, so it can be used for tilemaps with layers to them. (with a limit of 4 layers)
            //using RGBA is a common practice (by indie game developers especially).
            //RGBA32 format is used in BMP files, but can also be used in other file formats.
            //In my case, I only want to use RGBA in BMP files, so I will put the BMP file header inside the RGBA conversion.
            case "rgba":
                outputArray = new String[inputArray.length + 1];

                //BMP files are little-endian, meaning that the least significant bit is first [etc.]






                break;
            case "pgm": //converts to PGM file format
                outputArray = new String[inputArray.length + 3]; //the header is three lines long

                //PGM format header
                outputArray[0] = "P2";
                outputArray[1] = inputArray[0].length() + " " + inputArray.length;
                outputArray[2] = "1";

                for (int i = 0; i < inputArray.length; i++) {
                    outputArray[i+3] = inputArray[i];
                }

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + format);
        }

        return outputArray;
    }

    class RGBA {
        public int r;
        public int g;
        public int b;
        public int a;

        public RGBA(int r, int g, int b, int a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        public String toString(){
            return intToBinaryString(r, 32) + intToBinaryString(g, 32) + intToBinaryString(b, 32) + intToBinaryString(a, 32);
        }

    }

    public String reverseString(String inputString) {
        StringBuilder sb = new StringBuilder(inputString);
        return sb.reverse().toString();
    }

    public String intToBinaryString(int i, int bits) {
        return String.format("%" + bits + "s", Integer.toBinaryString(i)).replaceAll(" ", "0");
    }

}
