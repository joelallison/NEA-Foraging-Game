package com.joelallison.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



public class Entity {
    //entity position

    protected int xPos;
    protected int yPos;

    //for handling animations

    private int FRAME_COLS, FRAME_ROWS;
    private Texture spriteSheet;
    Animation<TextureRegion>[] animations;
    float stateTime;


    public Entity(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getFRAME_COLS() {
        return FRAME_COLS;
    }

    public void setFRAME_COLS(int FRAME_COLS) {
        this.FRAME_COLS = FRAME_COLS;
    }

    public int getFRAME_ROWS() {
        return FRAME_ROWS;
    }

    public void setFRAME_ROWS(int FRAME_ROWS) {
        this.FRAME_ROWS = FRAME_ROWS;
    }

    public Texture getSpriteSheet() {
        return spriteSheet;
    }

    public void setSpriteSheets(Texture spriteSheet) {
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
