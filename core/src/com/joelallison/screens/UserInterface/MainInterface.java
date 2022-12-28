package com.joelallison.screens.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.text.DecimalFormat;

public class MainInterface extends UserInterface {

    Window leftPanel = new Window("Generation parameters:", chosenSkin);
    Label scaleLabel = new Label("Scale:", chosenSkin);
    Label octavesLabel = new Label("Octaves:", chosenSkin);
    Label lacunarityLabel = new Label("Lacunarity:", chosenSkin);
    Label wrapFactorLabel = new Label("Wrap factor:", chosenSkin);
    Label invertLabel = new Label("Invert:", chosenSkin);
    CheckBox invertCheck = new CheckBox("", chosenSkin);
    DecimalFormat floatFormat = new DecimalFormat("##0.00");
    DecimalFormat intFormat = new DecimalFormat("00000");

    public void genUI(){
        stage.addActor(constructMenuBar(new MenuMethod[]{new MenuMethod("File", true, new Runnable() { //creating the menu bar, using custom method in UserInterface
            @Override
            public void run() {
                System.out.println("test");
            }
        }),
                new MenuMethod("Edit", true, new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("other test");
                    }
                })

        }, new Vector2(32, Gdx.graphics.getHeight() - 10)));

        //box to edit leftPanel of generation
        leftPanel.setSize(250f, 500f);
        leftPanel.setPosition(5f ,200f);
        leftPanel.setMovable(false);
        leftPanel.setClip(true);

        scaleLabel.setPosition(leftPanel.getX(), leftPanel.getY() + leftPanel.getHeight() / 2);
        leftPanel.addActor(scaleLabel);

        final Slider scaleSlider = new Slider(0.005f, 256f, 0.001f, false, chosenSkin);
        scaleSlider.setPosition(scaleLabel.getX() + 44f, scaleLabel.getY()-2);
        scaleSlider.setValue(Float.parseFloat(values[0]));

        scaleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                values[0] = Float.toString(scaleSlider.getValue());
            }
        });

        leftPanel.addActor(scaleSlider);

        octavesLabel.setPosition(leftPanel.getX(), (leftPanel.getY() + leftPanel.getHeight() / 2) - 24);
        leftPanel.addActor(octavesLabel);

        final Slider octavesSlider = new Slider(1, 3, 1, false, chosenSkin);
        octavesSlider.setPosition(octavesLabel.getX() + 64f, octavesLabel.getY()-2);
        octavesSlider.setWidth(120f);
        octavesSlider.setValue(Float.parseFloat(values[1]));

        octavesSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                values[1] = Integer.toString((int) octavesSlider.getValue());
            }
        });

        leftPanel.addActor(octavesSlider);

        lacunarityLabel.setPosition(leftPanel.getX(), (leftPanel.getY() + leftPanel.getHeight() / 2) - 48);
        leftPanel.addActor(lacunarityLabel);

        final Slider lacunaritySlider = new Slider(0.01f, 10f, 0.01f, false, chosenSkin);
        lacunaritySlider.setPosition(lacunarityLabel.getX() + 80f, lacunarityLabel.getY()-2);
        lacunaritySlider.setWidth(108f);
        lacunaritySlider.setValue(Float.parseFloat(values[2]));

        lacunaritySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                values[2] = Float.toString(lacunaritySlider.getValue());
            }
        });

        leftPanel.addActor(lacunaritySlider);

        wrapFactorLabel.setPosition(leftPanel.getX(), (leftPanel.getY() + leftPanel.getHeight() / 2) - 72);
        leftPanel.addActor(wrapFactorLabel);

        final Slider wrapFactorSlider = new Slider(1, 20, 1, false, chosenSkin);
        wrapFactorSlider.setPosition(wrapFactorLabel.getX() + 92f, wrapFactorLabel.getY()-2);
        wrapFactorSlider.setWidth(96f);
        wrapFactorSlider.setValue(Float.parseFloat(values[3]));

        wrapFactorSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                values[3] = Integer.toString((int) wrapFactorSlider.getValue());
            }
        });

        leftPanel.addActor(wrapFactorSlider);

        invertLabel.setPosition(leftPanel.getX(), (leftPanel.getY() + leftPanel.getHeight() / 2) - 96);
        leftPanel.addActor(invertLabel);
        invertCheck.setPosition(leftPanel.getX() + 48, (leftPanel.getY()-2 + leftPanel.getHeight() / 2) - 96);
        invertCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                values[4] = Boolean.toString(invertCheck.isChecked());
            }
        });

        leftPanel.addActor(invertCheck);

        stage.addActor(leftPanel);

        final Label controlsTips = new Label("Press TAB to toggle UI", chosenSkin);
        controlsTips.setPosition(8, 8);

        stage.addActor(controlsTips);
    }

    public void update() {
        if (Integer.parseInt(values[1]) < 2) { //lacunarity has no effect if octaves is less than 2, this visual update attempts to indicate that to the user
            lacunarityLabel.setColor(0.45f, 0.45f, 0.45f, 1);
        } else {
            lacunarityLabel.setColor(1, 1, 1, 1);
        }

        scaleLabel.setText("Scale:                                      " + floatFormat.format(Float.parseFloat(values[0])));
        octavesLabel.setText("Octaves:                                 " + intFormat.format(Integer.parseInt(values[1])));
        lacunarityLabel.setText("Lacunarity:                             " + floatFormat.format(Float.parseFloat(values[2])));
        wrapFactorLabel.setText("Wrap Factor:                            " + floatFormat.format(Integer.parseInt(values[3])));

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            if (leftPanel.isVisible()) {
                leftPanel.setVisible(false);
            }else {
                leftPanel.setVisible(true);
            }
        }
    }


    @Override
    public void valuesDeclaration(){
        values[0] = "20f"; //'scale'
        values[1] = "2"; //'octaves'
        values[2] = "2f"; //'lacunarity'
        values[3] = "1"; //'wrap factor'
        values[4] = "false"; //'invert?'
    }
}
