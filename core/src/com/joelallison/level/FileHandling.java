package com.joelallison.level;

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

        switch(format) {
            case "rgba":
                outputArray = new String[mazeArray.length + 1]; //CHANGE THIS

                break;
            case "pgm":
                outputArray = new String[mazeArray.length + 3];

                //PGM format header
                outputArray[0] = "P2";
                outputArray[1] = maze[0].length + " " + maze.length;
                outputArray[2] = "1";

                for (int i = 0; i < mazeArray.length; i++) {
                    outputArray[i+3] = mazeArray[i];
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
            return intToBinaryString(r) + intToBinaryString(g) + intToBinaryString(b) + intToBinaryString(a);
        }

        public String intToBinaryString(int i) {
            return String.format("%32s", Integer.toBinaryString(i)).replaceAll(" ", "0");
        }

    }
}
