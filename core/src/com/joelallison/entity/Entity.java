package com.joelallison.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;


public class Entity {
    //entity position

    protected int xPosition, yPosition;

    //animations
    protected int FRAME_COLS, FRAME_ROWS;
    protected Texture spriteSheet;

    protected static HashMap<String, Animation<TextureRegion>> animations = new HashMap<String, Animation<TextureRegion>>();


    public Entity(int xPosition, int yPosition, Texture spriteSheet, int FRAME_COLS, int FRAME_ROWS) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.spriteSheet = spriteSheet;
        this.FRAME_COLS = FRAME_COLS;
        this.FRAME_ROWS = FRAME_ROWS;

    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }
    public HashMap<String, Animation<TextureRegion>> getAnimations() {
        return animations;
    }

    public void setAnimations(HashMap<String, Animation<TextureRegion>> animations) {
        this.animations = animations;
    }
}
