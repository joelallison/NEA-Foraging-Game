package com.joelallison.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;


public class Entity {
    //entity position

    protected int xPos;
    protected int yPos;

    //animations
    protected int FRAME_COLS, FRAME_ROWS;
    protected Texture spriteSheet;

    protected static HashMap<String, Animation<TextureRegion>> animations = new HashMap<String, Animation<TextureRegion>>();


    public Entity(int xPos, int yPos, Texture spriteSheet, int FRAME_COLS, int FRAME_ROWS) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.spriteSheet = spriteSheet;
        this.FRAME_COLS = FRAME_COLS;
        this.FRAME_ROWS = FRAME_ROWS;

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

    public HashMap<String, Animation<TextureRegion>> getAnimations() {
        return animations;
    }

    public void setAnimations(HashMap<String, Animation<TextureRegion>> animations) {
        this.animations = animations;
    }
}
