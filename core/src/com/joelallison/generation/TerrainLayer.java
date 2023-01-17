package com.joelallison.generation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.joelallison.display.Tileset;
import com.joelallison.screens.AppScreen;
import tools.OpenSimplex2S;

public class TerrainLayer extends Layer {
    public Tileset tileset;
    private float scale;
    private int octaves;
    private float lacunarity;
    private int wrap;
    private boolean invert;

    public float[][] valueMap;

    public TerrainLayer(String name, Long seed, float scaleVal, int octavesVal, float lacunarityVal, int wrapVal, boolean invert) {
        super(name, seed);
        this.scale = scaleVal;
        this.octaves = octavesVal;
        this.lacunarity = lacunarityVal;
        this.wrap = wrapVal;
        this.invert = invert;

        defaultTileValues();
    }

    public TerrainLayer(Long seed) { //these are some [fairly bland] default values
        this.name = "Terrain Layer";
        this.seed = seed; //seed is the only value that gets specifically set, this is so that the layer can be given the overall seed value.
        this.scale = 20f;
        this.octaves = 2;
        this.lacunarity = 2f;
        this.wrap = 1;
        this.invert = false;

        defaultTileValues();
    }

    public void defaultTileValues() {
        this.tileset = AppScreen.tilesets[0];
        this.tileset.setColor(new Color(0.1215686f, 0.09411765f, 0.07843137f, 1));
        this.tileChildren = new Tileset.TileChild[] {
                new Tileset.TileChild("plant", 0.35f),
                new Tileset.TileChild("bush", 0.4f),
                new Tileset.TileChild("tree_1", 0.6f),
                new Tileset.TileChild("tree_2", 0.7f)
        };
    }

    public TextureRegion getTextureFromIndex(int i) {
        return this.tileset.getTileTexture(this.tileset.map.get(this.tileChildren[i].name));
    }

    public void generateValueMap(Vector2 dimensions, int xOffset, int yOffset) {
        valueMap = genTerrain(this.getSeed(), dimensions, xOffset, yOffset, this.getScale(), this.getOctaves(), this.getLacunarity(), this.getWrap(), this.isInverted());
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getOctaves() {
        return octaves;
    }

    public void setOctaves(int octaves) {
        this.octaves = octaves;
    }

    public float getLacunarity() {
        return lacunarity;
    }

    public void setLacunarity(float lacunarity) {
        this.lacunarity = lacunarity;
    }

    public int getWrap() {
        return wrap;
    }

    public void setWrap(int wrap) {
        this.wrap = wrap;
    }

    public boolean isInverted() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    //generation stuff here onwards

    public static float[][] genTerrain(long seed, Vector2 Dimensions, int xOffset, int yOffset, float scale, int octaves, float lacunarity, int wrapFactor, boolean invertWrap) {
        //greater scale zooms in, halved scale from normal could be used for map data. I think scale of 4 is best for most stuff
        //higher octaves adds more detail to the noise, but 2 is the best option for this [in most cases]
        //higher persistence makes the values tend towards higher values
        //lower lacunarity is smoother looking - higher lacunarity for things like trees, lower for grass

        //if you double the scale but double the lacunarity, the output is basically  the same. if you double the scale and halve the lacunarity, it's like you quadrupled the scale.

        float[][] noiseMap = new float[(int) Dimensions.x][(int) Dimensions.y];

        float minNoiseHeight = Float.MAX_VALUE;
        float maxNoiseHeight = Float.MIN_VALUE;

        for (int y = yOffset; y < Dimensions.y + yOffset; y++) {
            for (int x = xOffset; x < Dimensions.x + xOffset; x++) {

                float amplitude = 1;
                float frequency = 1;
                float noiseHeight = 0;

                for (int i = 0; i < octaves; i++) {

                    float sampleX = (x-(Dimensions.x/2)) / scale * frequency;
                    float sampleY = (y-(Dimensions.y/2)) / scale * frequency;

                    float noiseValue = (OpenSimplex2S.noise2_ImproveX(seed, sampleX, sampleY) * 2 - 1);
                    noiseHeight = noiseValue * amplitude;

                    frequency *= lacunarity;
                }

                if (noiseHeight > maxNoiseHeight) {
                    maxNoiseHeight = noiseHeight;
                } else if (noiseHeight < minNoiseHeight) {
                    minNoiseHeight = noiseHeight;
                }

                noiseMap[x-xOffset][y-yOffset] = noiseHeight;
            }
        }

        if(wrapFactor != -1){
            for (int y = 0; y < Dimensions.y; y++) { //normalises the noise
                for (int x = 0; x < Dimensions.x; x++) {
                    noiseMap[x][y] = wrapValue((inverseLERP(noiseMap[x][y], minNoiseHeight, maxNoiseHeight)), wrapFactor, invertWrap);
                }
            }
        }else{
            for (int y = 0; y < Dimensions.y; y++) { //normalises the noise
                for (int x = 0; x < Dimensions.x; x++) {
                    noiseMap[x][y] = (inverseLERP(noiseMap[x][y], minNoiseHeight, maxNoiseHeight));
                }
            }
        }

        return noiseMap;
    }

    public static float inverseLERP(float x, float a, float b){
        return (x - a) / (b - a);
    }

    public static float wrapValue(float input, int factor, boolean invert) {
        if (invert) {
            return ((Math.abs(((input * factor) - factor / 2))) * -1) + 1;
        } else {
            return Math.abs(((input * factor) - factor / 2));
        }

    }

}

