package com.joelallison.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameUI {
    private static Skin skin;
    private static Stage stage;

    public static Stage generateUIStage(SpriteBatch sb, String mode) {
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        stage = new Stage();

        final TextButton button = new TextButton("Click me", skin, "default");

        button.setWidth(200f);
        button.setHeight(20f);
        button.setPosition(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight()/2);

        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                button.setText("You clicked the button");
            }
        });

        stage.addActor(button);

        Gdx.input.setInputProcessor(stage);

        return stage;
    }
}
