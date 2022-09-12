package com.joelallison.screens;

import com.badlogic.gdx.Game;

public class Opening extends Game {

    public void create(){
        setScreen(new TitleScreen(this));
    }
}
