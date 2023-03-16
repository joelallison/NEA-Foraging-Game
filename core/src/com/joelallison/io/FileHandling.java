package com.joelallison.io;

import com.badlogic.gdx.math.Vector2;
import com.joelallison.generation.MazeLayer;
import com.joelallison.generation.TerrainLayer;
import com.joelallison.generation.World;
import com.joelallison.graphics.Tileset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.joelallison.io.JsonHandling.tilesetJsonToObject;
import static com.joelallison.screens.AppScreen.*;

public abstract class FileHandling {

    public static HashMap<String, Tileset> importTilesets(String folderName) {
        HashMap<String, Tileset> tilesets = new HashMap<>(0);
        File directory = new File(folderName);

        try {
            for (File subfolder: directory.listFiles()) {
                if (subfolder.isDirectory()) {
                    String json = jsonFileToString(subfolder.getPath() + "/data.json");
                    if (!json.equals("")) {
                        json = json.replaceAll("\\s{2,}", " "); //replace any areas with two or more consecutive spaces with just a single space
                        int nameStartIndex = json.indexOf("\"name\""); //find the location of the start of the name parameter in the string
                        int i = 0;
                        //go through the json until the end of the name value is found
                        while(json.charAt(nameStartIndex + "\"name\": \"".length() + i) != '"') {
                            i++;
                        }
                        int nameEndIndex = nameStartIndex + "\"name\": \"".length() + i;
                        String name = json.substring(nameStartIndex + "\"name\": \"".length(), nameEndIndex);
                        json = json.replace(json.substring(nameStartIndex, nameEndIndex + 3), ""); //remove the name declaration line from the json - the extra three chars are '", '
                        tilesets.put(name, tilesetJsonToObject(json));
                        tilesets.get(name).initTileset(subfolder.getPath() + "/" + tilesets.get(name).getSpritesheetName());
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Tilesets missing in directory '" + directory.getAbsolutePath() + "'...\n" + e);
        }

        return tilesets;
    }

    public static String jsonFileToString(String filename) {
        //this first part is similar to the readFromFile method, but reads the file all onto one line.
        StringBuilder fileText = new StringBuilder();
        try {
            File file = new File(filename);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String currentLine = myReader.nextLine();
                fileText.append(currentLine);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return "";
        }

        return fileText.toString();
    }

    public static String export(World world, String filename, int startX, int endX, int startY, int endY) {
        String[] arr = formatForWrite(createTileNameArray(world, startX, endX, startY, endY));

        return createFile(filename) + writeToCSVFile(filename, arr);
    }

    static String[] formatForWrite(String[][] input) {
        String[] output = new String[input.length];
        for (int i = 0; i < output.length; i++) {
            String line = Arrays.deepToString(input[i]);
            output[i] = line.substring(1, line.length() - 1);
        }

        return output;
    }

    static String[][] createTileNameArray(World world, int startX, int endX, int startY, int endY) {
        Vector2 dimensions = new Vector2(endX - startX, endY - startY);

        //init string array with '-'
        String[][] array = new String[(int) dimensions.x][(int) dimensions.y];
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                array[x][y] = "-";
            }
        }


        //generate
        for (int i = 0; i < world.layers.size(); i++) {
            switch(getLayerTypeChar(world.layers.get(i))) {
                case 'T':
                    ((TerrainLayer) world.layers.get(i)).genValueMap(world.getLayerSeed(i), dimensions, startX + (int) (world.layers.get(i).getCenter().x), startY + (int) (world.layers.get(i).getCenter().y));
                    break;
                case 'M':
                    ((MazeLayer) world.layers.get(i)).genMaze(world.getLayerSeed(i));
            }
        }

        boolean[][] tileAbove = new boolean[(int) dimensions.x][(int) dimensions.y];
        //write into array
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                // top layer to bottom layer
                for (int i = world.layers.size() - 1; i >= 0; i--) {
                    if (world.layers.get(i).layerShown()) {
                        if (tileAbove[x - startX][y - startY] == false) { // no need to draw if there's already a tile above
                            switch (getLayerTypeChar(world.layers.get(i))) {
                                case 'T':
                                    String terrainTileName = getTileNameForTerrainValue(world.layers.get(i), x - startX, y - startY);
                                    String terrainTile = formatTile(world.layers.get(i).tilesetName, terrainTileName);
                                    if (terrainTileName != "-") {
                                        array[x - startX][y - startY] = terrainTile;
                                        tileAbove[x - startX][y - startY] = true;
                                    }
                                    break;
                                case 'M':
                                    //if x is within maze
                                    if (x < (world.layers.get(i)).getCenter().x + ((MazeLayer) world.layers.get(i)).getWidth()+1 && (x > (world.layers.get(i)).getCenter().x)) {
                                        //if y is within maze
                                        if (y < (world.layers.get(i)).getCenter().y + ((MazeLayer) world.layers.get(i)).getHeight()+1 && (y > (world.layers.get(i)).getCenter().y)) {
                                            if (((MazeLayer) world.layers.get(i)).maze[(int) (y - world.layers.get(i).getCenter().y-1)][(int) (x - world.layers.get(i).getCenter().x-1)] == 1) {
                                                String mazeTileName = getTileNameForNeighbourMap(world.layers.get(i), getNeighbourMap(world.layers.get(i), ((int) (x - world.layers.get(i).getCenter().x-1) - startX), (int) (((MazeLayer) world.layers.get(i)).getHeight() - (y - world.layers.get(i).getCenter().y-1) - 1) - startY));
                                                String mazeTile = formatTile(world.layers.get(i).tilesetName, mazeTileName);
                                                if (mazeTileName != "-") {
                                                    array[x - startX][y - startY] = mazeTile;
                                                    tileAbove[x - startX][y - startY] = true;
                                                }
                                            } else if (((MazeLayer) world.layers.get(i)).isOpaque()) {
                                                tileAbove[x - startX][y - startY] = true;
                                            }
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        }

        return array;
    }

    static String formatTile(String tileset, String tile) {
        return tileset + "." + tile;
    }

    public static String createFile(String filename) {
        try {
            File file = new File(filename);
            if (file.createNewFile()) {
                return "Created file: " + file.getAbsolutePath();
            } else {
                return "File already exists";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Error creating file";
    }

    public static String writeToCSVFile(String filename, String[] text) {
        try {
            FileWriter myWriter = new FileWriter(filename, false);

            for (int i = 0; i < text.length - 1; i++) {
                myWriter.write(text[i] + "\r"); //\r is used for CSVs (rather than \n)
            } myWriter.write(text[text.length-1]); //so that last line doesn't have \r

            myWriter.close();
            return ", file exported.";
        } catch (IOException e) {
            e.printStackTrace();
            return " ...an error occured.";
        }
    }

    //eventually ended up not being used but I wrote this early in development just so I have it
    public static List<String> readFromFile(String filename) {
        List<String> fileText = new ArrayList<String>();
        try {
            File file = new File(filename);
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


    //this is never done within the app, but
    //the commented out code seen in DesktopLauncher works to export a maze to .pgm
    public static String[] mazeToStringArray(int[][] maze, String format, boolean pad, boolean invert) {
        String[] outputArray;

        String[] mazeArray = new String[maze.length];

        String padding = "";
        if(pad) { padding = " "; }
        int modVal = 0;
        if(invert) { modVal = -1; }

        for (int i = 0; i < maze.length; i++) {
            String mazeLine = "";
            for (int j = 0; j < maze[i].length; j++) {
                mazeLine = mazeLine + Math.abs(modVal + maze[i][j]) + padding;
            } mazeArray[i] = mazeLine;
        }

        outputArray = toFileFormat(mazeArray, format, true);

        return outputArray;
    }

    //this works but is never used, ended up going with only CSV
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

    //rgba is WIP
    public static String[] toFileFormat(String[] inputArray, String format, boolean bigEndian) {
        String[] outputArray;

        switch(format) {
            //RGBA has 4 separate values at each position, so it can be used for tilemaps with tileBounds to them. (with a limit of 4 tileBounds)
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
                outputArray[1] = (inputArray[0].length() / 2) + " " + inputArray.length; //div2 is because of the added space character between each character needed for PGM
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

    //never used

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
