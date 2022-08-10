package com.joelallison.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileType {

    private int priority;

    private boolean collision;

    private float scaleVal;
    private int octavesVal;
    private float persistenceVal;
    private float lacunarityVal;
    private int wrapVal;
    private boolean invertVal;

    //sprite & animation system is slightly different to that of entities,
    //as most tiles don't have any sort of animation.
    private Texture spriteSheet;
    private Animation<TextureRegion>[] animations;
    float stateTime;


    private TileBounds[] bounds;

    public TileType(float scaleVal, int octavesVal, float persistenceVal, float lacunarityVal, int wrapVal, boolean invertVal) {
        this.scaleVal = scaleVal;
        this.octavesVal = octavesVal;
        this.persistenceVal = persistenceVal;
        this.lacunarityVal = lacunarityVal;
        this.wrapVal = wrapVal;
        this.invertVal = invertVal;

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

    public boolean isInvertVal() {
        return invertVal;
    }

    public void setInvertVal(boolean invertVal) {
        this.invertVal = invertVal;
    }

    public Texture getSpriteSheet() {
        return spriteSheet;
    }

    public void setSpriteSheet(Texture spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    public Animation<TextureRegion>[] getAnimations() {
        return animations;
    }

    public void setAnimations(Animation<TextureRegion>[] animations) {
        this.animations = animations;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

}
