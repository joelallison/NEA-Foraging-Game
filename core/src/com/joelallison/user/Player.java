package com.joelallison.user;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.joelallison.screens.Init;

public class Player {

    Init system;
    int xPosition;
    int yPosition;

    public Player(int xPosition, int yPosition, Init system) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.system = system;
    }

    
    public void handleInput() {

        //movement
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            setyPosition(yPosition + 1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            setyPosition(yPosition - 1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            setxPosition(xPosition - 1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            setxPosition(xPosition + 1);
        }

        //zoom
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            system.camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            system.camera.zoom -= 0.02;
        }
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
}
