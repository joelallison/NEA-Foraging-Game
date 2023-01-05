package com.joelallison.screens.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.joelallison.display.Tileset;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;

import java.text.DecimalFormat;

public class MainInterface extends UserInterface {

    //many elements of the ui are used in multiple methods, so it's best that they're all declared globally [to the class]
    protected Label scaleLabel = new Label("Scale:", chosenSkin);
    protected Label octavesLabel = new Label("Octaves:", chosenSkin);
    protected Label lacunarityLabel = new Label("Lacunarity:", chosenSkin);
    protected Label wrapFactorLabel = new Label("Wrap factor:", chosenSkin);
    protected Label invertLabel = new Label("Invert:", chosenSkin);
    protected CheckBox invertCheck = new CheckBox("", chosenSkin);
    protected DecimalFormat floatFormat = new DecimalFormat("##0.00");
    protected DecimalFormat intFormat = new DecimalFormat("00000");
    protected Window generationSettingsPanel = new Window("Generation parameters:", chosenSkin);
    protected Window layerPanel = new Window("Layers:", chosenSkin);
    protected Label controlsTips = new Label("Press TAB to toggle UI. All window-box things are draggable and movable!", chosenSkin);

    public void genUI(Stage stage) {
        //menu bar, using custom method in UserInterface
        stage.addActor(constructMenuBar(new MenuMethod[]{new MenuMethod("File", true, new Runnable() {
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

        //box to edit generation settings for selected layer
        doGenerationSettingsPanel();
        stage.addActor(generationSettingsPanel);

        //box for managing the different layers of generation
        doLayerPanel();
        stage.addActor(layerPanel);

        //text to tell users of otherwise hidden keybinds
        controlsTips.setPosition(8, 8);

        stage.addActor(controlsTips);
    }

    public void update() {
        if (Integer.parseInt(values[1]) < 2) { //lacunarity has no effect if octaves is less than 2, this visual update attempts to indicate that to the user
            lacunarityLabel.setColor(0.45f, 0.45f, 0.45f, 1);
        } else {
            lacunarityLabel.setColor(1, 1, 1, 1);
        }

        scaleLabel.setText("Scale: " + floatFormat.format(Float.parseFloat(values[0])));
        octavesLabel.setText("Octaves: " + intFormat.format(Integer.parseInt(values[1])));
        lacunarityLabel.setText("Lacunarity: " + floatFormat.format(Float.parseFloat(values[2])));
        wrapFactorLabel.setText("Wrap Factor: " + floatFormat.format(Integer.parseInt(values[3])));

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            if (generationSettingsPanel.isVisible()) {
                generationSettingsPanel.setVisible(false);
                controlsTips.setText("Press TAB to toggle UI.");
            } else {
                generationSettingsPanel.setVisible(true);
                controlsTips.setText("Press TAB to toggle UI. All window-box things are draggable and movable!"); //should be added to the hashmap
            }
        }
    }

    protected void doGenerationSettingsPanel() {
        generationSettingsPanel.add(scaleLabel);

        final Slider scaleSlider = new Slider(0.005f, 256f, 0.001f, false, chosenSkin);
        scaleSlider.setValue(Float.parseFloat(values[0]));

        scaleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                values[0] = Float.toString(scaleSlider.getValue());
            }
        });

        generationSettingsPanel.add(scaleSlider);
        generationSettingsPanel.row();

        generationSettingsPanel.add(octavesLabel);

        final Slider octavesSlider = new Slider(1, 3, 1, false, chosenSkin);
        octavesSlider.setValue(Float.parseFloat(values[1]));

        octavesSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                values[1] = Integer.toString((int) octavesSlider.getValue());
            }
        });

        generationSettingsPanel.add(octavesSlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(lacunarityLabel);

        final Slider lacunaritySlider = new Slider(0.01f, 10f, 0.01f, false, chosenSkin);
        lacunaritySlider.setValue(Float.parseFloat(values[2]));

        lacunaritySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                values[2] = Float.toString(lacunaritySlider.getValue());
            }
        });

        generationSettingsPanel.add(lacunaritySlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(wrapFactorLabel);

        final Slider wrapFactorSlider = new Slider(1, 20, 1, false, chosenSkin);
        wrapFactorSlider.setValue(Float.parseFloat(values[3]));

        wrapFactorSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                values[3] = Integer.toString((int) wrapFactorSlider.getValue());
            }
        });

        generationSettingsPanel.add(wrapFactorSlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(invertLabel);
        invertCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                values[4] = Boolean.toString(invertCheck.isChecked());
            }
        });

        generationSettingsPanel.add(invertCheck);

        generationSettingsPanel.setPosition(5f, 500f);
        generationSettingsPanel.padLeft(4f);
        generationSettingsPanel.padRight(4f);
        generationSettingsPanel.setSize(generationSettingsPanel.getPrefWidth() * 1.2f, generationSettingsPanel.getPrefHeight());
    }

    protected void doLayerPanel() {

    }


    @Override
    public void valuesDeclaration() {
        values[0] = "20f"; //'scale'
        values[1] = "2"; //'octaves'
        values[2] = "2f"; //'lacunarity'
        values[3] = "1"; //'wrap factor'
        values[4] = "false"; //'invert?'
    }
}
