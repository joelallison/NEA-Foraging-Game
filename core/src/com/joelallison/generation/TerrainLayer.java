package com.joelallison.generation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.joelallison.display.Tileset;
import com.joelallison.display.Tileset.*;

public class TerrainLayer extends Layer {

    private String TileID;
    public Tileset tileset;
    private float scaleVal;
    private int octavesVal;
    private float lacunarityVal;
    private int wrapVal;
    private boolean invert;
    public Tileset.TileBound[] tileBounds; //these are the tile children for this gen type


    public TerrainLayer(String name, Long seed, String TileID, float scaleVal, int octavesVal, float lacunarityVal, int wrapVal, boolean invert) {
        super(name, seed);
        this.TileID = TileID;
        this.scaleVal = scaleVal;
        this.octavesVal = octavesVal;
        this.lacunarityVal = lacunarityVal;
        this.wrapVal = wrapVal;
        this.invert = invert;
    }

    public TextureRegion getTextureFromIndex(int i) {
        return this.tileset.getTileTexture(this.tileset.map.get(this.tileBounds[i].name));
    }


    public String getTileID() {
        return TileID;
    }

    public void setTileID(String tileID) {
        TileID = tileID;
    }

    public float getScaleVal() {
        return scaleVal;
    }

    public void setScaleVal(float scaleVal) {
        this.scaleVal = scaleVal;
    }

    public int getOctavesVal() {
        return octavesVal;
    }

    public void setOctavesVal(int octavesVal) {
        this.octavesVal = octavesVal;
    }

    public float getLacunarityVal() {
        return lacunarityVal;
    }

    public void setLacunarityVal(float lacunarityVal) {
        this.lacunarityVal = lacunarityVal;
    }

    public int getWrapVal() {
        return wrapVal;
    }

    public void setWrapVal(int wrapVal) {
        this.wrapVal = wrapVal;
    }

    public boolean doInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    @Override
    public TileChild[] getTileChildren() {
        return tileBounds;
    }

}

