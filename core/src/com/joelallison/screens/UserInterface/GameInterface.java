package com.joelallison.screens.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.text.DecimalFormat;

public abstract class GameInterface extends UserInterface {

    static Label scaleLabel = new Label("Scale:", chosenSkin);
    static Label octavesLabel = new Label("Octaves:", chosenSkin);
    static Label persistenceLabel = new Label("Persistence:", chosenSkin);
    static Label lacunarityLabel = new Label("Lacunarity:", chosenSkin);
    static Label wrapFactorLabel = new Label("Wrapping amount:", chosenSkin);
    static CheckBox invertCheck = new CheckBox("Invert the wrap:", chosenSkin);
    static DecimalFormat floatFormat = new DecimalFormat("##0.00");
    static DecimalFormat intFormat = new DecimalFormat("00000");

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

        scaleLabel = new Label("Scale:", chosenSkin);
        scaleLabel.setPosition(parameters.getX(), parameters.getY() + parameters.getHeight() / 2);
        parameters.addActor(scaleLabel);

        final Slider scaleSlider = new Slider(0.0005f, 256f, 0.0001f, false, chosenSkin);
        scaleSlider.setPosition(scaleLabel.getX() + 44f, scaleLabel.getY()-2);

        scaleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                values[0] = Float.toString(scaleSlider.getValue());
            }
        });

        parameters.addActor(scaleSlider);

        octavesLabel = new Label("Octaves:", chosenSkin);
        octavesLabel.setPosition(parameters.getX(), (parameters.getY() + parameters.getHeight() / 2) - 24);
        parameters.addActor(octavesLabel);

        final Slider octavesSlider = new Slider(1, 128, 1, false, chosenSkin);
        octavesSlider.setPosition(octavesLabel.getX() + 44f, octavesLabel.getY()-2);

        octavesSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                values[1] = Integer.toString((int) octavesSlider.getValue());
            }
        });

        parameters.addActor(octavesSlider);

        stage.addActor(parameters);
    }

    public static void update() {
        scaleLabel.setText("Scale:                                      " + floatFormat.format(Float.parseFloat(values[0])));
        octavesLabel.setText("Octaves:                                 " + intFormat.format(Integer.parseInt(values[1])));
    }
}
