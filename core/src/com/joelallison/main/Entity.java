package com.joelallison.main;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



public class Entity {
    //entity position
    private int xPos;
    private int yPos;

    //for handling animations

    private int FRAME_COLS, FRAME_ROWS;
    private Texture[] spriteSheets;
    Animation<TextureRegion>[] animations;

    float stateTime;

    public Entity(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }



}
