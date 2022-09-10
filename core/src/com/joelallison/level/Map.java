package com.joelallison.level;

import com.badlogic.gdx.math.Vector2;
import tools.OpenSimplex2S;
import java.util.*;

import static com.joelallison.main.Main.VISIBLE_WORLD_DIMENSIONS;

public class Map {

    public ArrayList<String[][]> mapsToCombine;

    public static float[][] genNoiseMap (long seed, Vector2 Dimensions, float xOffset, float yOffset, float scale, int octaves, float persistence, float lacunarity, int wrapFactor, boolean invertWrap) {
        //greater scale zooms in, halved scale from normal could be used for map data. I think scale of 4 is best for most stuff
        //higher octaves adds more detail to the noise, but 2 is the best option for this [in most cases]
        //higher persistence makes the values tend towards higher values
        //lower lacunarity is smoother looking - higher lacunarity for things like trees, lower for grass

        //if you double the scale but double the lacunarity, the output is basically  the same. if you double the scale and halve the lacunarity, it's like you quadrupled the scale.

        OpenSimplex2S noise = new OpenSimplex2S();
        float[][] noiseMap = new float[(int) Dimensions.x][(int) Dimensions.y];

        float minNoiseHeight = Float.MIN_VALUE;
        float maxNoiseHeight = Float.MAX_VALUE;

        for (int y = (int) yOffset; y < Dimensions.y + yOffset; y++) {
            for (int x = (int) xOffset; x < Dimensions.x + xOffset; x++) {

                float amplitude = 1;
                float frequency = 1;
                float noiseHeight = 0;

                for (int i = 0; i < octaves; i++) {

                    float sampleX = (x-(Dimensions.x/2)) / scale * frequency;
                    float sampleY = (y-(Dimensions.y/2)) / scale * frequency;

                    float noiseValue = (noise.noise2_ImproveX(seed, sampleX, sampleY));
                    noiseHeight = noiseValue * amplitude;

                    amplitude *= persistence;
                    frequency *= lacunarity;
                }

                if (noiseHeight > maxNoiseHeight) {
                    maxNoiseHeight = noiseHeight;
                } else if (noiseHeight < minNoiseHeight) {
                    minNoiseHeight = noiseHeight;
                }

                noiseMap[x-(int)xOffset][y-(int)yOffset] = noiseHeight;
            }
        }

        if(wrapFactor != -1){
            for (int y = 0; y < Dimensions.y; y++) { //normalises the noise
                for (int x = 0; x < Dimensions.x; x++) {
                    noiseMap[x][y] = wrapValue((float) (inverseLERP(noiseMap[x][y], minNoiseHeight, maxNoiseHeight) * Math.pow(10, 38)), wrapFactor, invertWrap); //not sure why it's out by x10^-38 but I fixed it
                }
            }
        }else{
            for (int y = 0; y < Dimensions.y; y++) { //normalises the noise
                for (int x = 0; x < Dimensions.x; x++) {
                    noiseMap[x][y] = (float) (inverseLERP(noiseMap[x][y], minNoiseHeight, maxNoiseHeight) * Math.pow(10, 38)); //not sure why it's out by x10^-38 but I fixed it
                }
            }
        }






        return noiseMap;
    }

    public static float inverseLERP(float x, float a, float b){
        return (x - a) / (b - a);
    }

    public static float wrapValue(float input, int factor, boolean invert){
        if(invert){
            return ((Math.abs(((input * factor) - factor/2)))*-1)+1;
        }else{
            return Math.abs(((input * factor) - factor/2));
        }

    }

    public static String[][] constructTerrain (int xOffset, int yOffset, long seed, TileType[] tilesToGen) {

        String[][] outputMap = new String[(int) VISIBLE_WORLD_DIMENSIONS.x][(int) VISIBLE_WORLD_DIMENSIONS.y];
        for (int x = 0; x < VISIBLE_WORLD_DIMENSIONS.x; x++) {
            for (int y = 0; y < VISIBLE_WORLD_DIMENSIONS.y; y++) {
                outputMap[x][y] = "-";
            }
        }

        for (int i = 0; i < tilesToGen.length; i++) {

            float[][] currentMapLevel = genNoiseMap(seed, VISIBLE_WORLD_DIMENSIONS, xOffset+tilesToGen[i].getxOffset(), yOffset+tilesToGen[i].getyOffset(), tilesToGen[i].getScaleVal(), tilesToGen[i].getOctavesVal(), tilesToGen[i].getPersistenceVal(), tilesToGen[i].getLacunarityVal(), tilesToGen[i].getWrapVal(), tilesToGen[i].doInvert());

            for (int x = 0; x < VISIBLE_WORLD_DIMENSIONS.x; x++) {
                for (int y = 0; y < VISIBLE_WORLD_DIMENSIONS.y; y++) {

                    for (int j = 0; j < tilesToGen[i].sprites.length; j++) {
                        if (currentMapLevel[x][y] >= tilesToGen[i].bounds[j]){
                            outputMap[x][y] = Integer.toString(i) + "x" + Integer.toString(j);
                        }
                    }
                }
            }

        }
        return outputMap;
    }

    public static TileType[] sortByPriority(TileType[] tilesToGen) {

        return tilesToGen;
    }
}
