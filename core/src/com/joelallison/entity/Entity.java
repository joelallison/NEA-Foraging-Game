package com.joelallison.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;


public class Entity {
    //entity position

    protected Vector2 position;

    //animations
    protected int FRAME_COLS, FRAME_ROWS;
    protected Texture spriteSheet;

    protected static HashMap<String, Animation<TextureRegion>> animations = new HashMap<String, Animation<TextureRegion>>();


    public Entity(Vector2 position, Texture spriteSheet, int FRAME_COLS, int FRAME_ROWS) {
        this.position = position;
        this.spriteSheet = spriteSheet;
        this.FRAME_COLS = FRAME_COLS;
        this.FRAME_ROWS = FRAME_ROWS;

    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public HashMap<String, Animation<TextureRegion>> getAnimations() {
        return animations;
    }

    public void setAnimations(HashMap<String, Animation<TextureRegion>> animations) {
        this.animations = animations;
    }
}
