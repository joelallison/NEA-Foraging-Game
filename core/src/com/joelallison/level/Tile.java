package com.joelallison.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tile {
    private String TileID;
    private int priority;

    private boolean collision;

    private int xOffset = 0;
    private int yOffset = 0;

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
    public Animation<TextureRegion[]>[] animations;
    float stateTime;


    public float[] bounds;

    public Tile(String TileID, int priority, boolean collision, float scaleVal, int octavesVal, float persistenceVal, float lacunarityVal, int wrapVal, boolean invert) {
        this.TileID = TileID;
        this.priority = priority;
        this.collision = collision;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
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

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public int getxOffset() {
        return xOffset;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

}
