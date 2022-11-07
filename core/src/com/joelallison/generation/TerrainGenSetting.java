package com.joelallison.generation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TerrainGenSetting extends GenSetting {
    public TerrainLayer[] layers;

    public TerrainGenSetting(String name, Long seed, int layerCount) {
        super(name, seed);
        this.layers = new TerrainLayer[layerCount];
    }

    public static class TerrainLayer {
        private String TileID;
        private float scaleVal;
        private int octavesVal;
        private float persistenceVal;
        private float lacunarityVal;
        private int wrapVal;
        private boolean invert;

        //sprite & animation system is slightly different to that of entities,
        //as most tiles don't have any sort of animation.
        private Texture spriteSheet;

        public TextureRegion[] sprites;
        float stateTime;

        public float[] bounds;

        public TerrainLayer(String TileID, float scaleVal, int octavesVal, float persistenceVal, float lacunarityVal, int wrapVal, boolean invert) {
            this.TileID = TileID;
            this.scaleVal = scaleVal;
            this.octavesVal = octavesVal;
            this.persistenceVal = persistenceVal;
            this.lacunarityVal = lacunarityVal;
            this.wrapVal = wrapVal;
            this.invert = invert;
        }

        public String getTileID() {
            return TileID;
        }

        public void setTileID(String tileID) {
            TileID = tileID;
        }

        public float[] getBounds() {
            return bounds;
        }

        public void setBounds(float[] bounds) {
            this.bounds = bounds;
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

        public float getPersistenceVal() {
            return persistenceVal;
        }

        public void setPersistenceVal(float persistenceVal) {
            this.persistenceVal = persistenceVal;
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

        public Texture getSpriteSheet() {
            return spriteSheet;
        }

        public void setSpriteSheet(Texture spriteSheet) {
            this.spriteSheet = spriteSheet;
        }

    }

}

