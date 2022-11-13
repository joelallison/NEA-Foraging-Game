package com.joelallison.screens.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public abstract class GameInterface extends UserInterface {

    public static void genUI(){
        //toolbar
        final TextButton file = new TextButton("File", chosenSkin, "default");
        file.setSize(55f, 25f);
        file.setPosition(0, Gdx.graphics.getHeight() - file.getHeight());

        file.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                file.setText("Bingus!");
            }
        });

        stage.addActor(file);

        final TextButton edit = new TextButton("Edit", chosenSkin, "default");
        edit.setSize(55f, 25f);
        edit.setPosition(0 + file.getWidth(), Gdx.graphics.getHeight() - edit.getHeight());

        edit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                edit.setText("Bingus?");
            }
        });

        stage.addActor(edit);

        //box to edit parameters of generation
        final Window parameters = new Window("Edit parameters:", chosenSkin);
        parameters.setSize(250f, 500f);
        parameters.setPosition(5f ,200f);
        parameters.setMovable(false);

        final Label scaleLabel = new Label("Scale:                  " + values[0], chosenSkin);
        scaleLabel.setPosition(parameters.getX(), parameters.getY() + parameters.getHeight() / 2);
        parameters.addActor(scaleLabel);

        final Slider scaleSlider = new Slider(0.0005f, 128f, 0.0001f, false, chosenSkin);
        scaleSlider.setPosition(scaleLabel.getX() + 44f, scaleLabel.getY()-2);

        scaleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                values[0] = Float.toString(scaleSlider.getValue());
            }
        });

        parameters.addActor(scaleSlider);

        stage.addActor(parameters);
    }
}
