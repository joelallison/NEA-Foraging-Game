package com.joelallison.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import static com.joelallison.screens.AppScreen.camera;

public class UserInput {

    int xPosition;
    int yPosition;

    public UserInput(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
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
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) {
            camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) {
            camera.zoom -= 0.02;
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
