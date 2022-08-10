package com.joelallison.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player extends Entity {

    private final float zoomAmount = 0.005f;

    public Player(int xPos, int yPos){
        super(xPos, yPos);
    }

    public void handleMovement(){ //was going to make a container class but remembered the libgdx Vector2 class
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            yPos++;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S)){
            yPos--;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            xPos--;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D)){
            xPos++;
        }
    }

    public float zoom() {
        float zoomMod = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.O)){
            zoomMod = 0-zoomAmount;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.P)){
            zoomMod = zoomAmount;
        }

        return zoomMod;
    }

}
