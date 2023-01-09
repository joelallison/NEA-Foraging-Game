package com.joelallison.screens.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.joelallison.generation.Layer;
import com.joelallison.generation.TerrainLayer;

import java.text.DecimalFormat;

import static com.badlogic.gdx.utils.Align.center;
import static com.joelallison.screens.MainScreen.layers;


public class MainInterface extends UserInterface {

    //many elements of the ui are used in multiple methods, so it's best that they're all declared globally [to the class]
    protected Label scaleLabel = new Label("Scale:", chosenSkin);
    protected Label octavesLabel = new Label("Octaves:", chosenSkin);
    protected Label lacunarityLabel = new Label("Lacunarity:", chosenSkin);
    protected Label wrapFactorLabel = new Label("Wrap factor:", chosenSkin);
    protected Label invertLabel = new Label("Invert:", chosenSkin);
    protected CheckBox invertCheck = new CheckBox("", chosenSkin);
    protected Label centerPointLabel = new Label("Centre point:", chosenSkin); // for variable names, I'm reluctantly using the American spelling of 'centre' as it feels like convention :-(
    protected TextField xCoordField = new TextField("", chosenSkin);
    protected TextField yCoordField = new TextField("", chosenSkin);
    protected DecimalFormat floatFormat = new DecimalFormat("##0.00");
    protected DecimalFormat intFormat = new DecimalFormat("00000");
    protected Window generationSettingsPanel = new Window("Generation parameters:", chosenSkin);
    protected Window layerPanel = new Window("Layers:", chosenSkin);
    protected Label controlsTips = new Label("Press TAB to toggle UI. All window-box things are draggable and movable!", chosenSkin);
    protected VerticalGroup layerGroup = new VerticalGroup();
    Layer[] oldLayers;
    Stage stage;
    int selectedLayerIndex = 0;

    public void genUI(final Stage stage) { //stage is made final here so that it can be accessed within inner classes
        //menu bar, using custom method in UserInterface

        this.stage = stage;
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
        layerPanel.add(layerGroup);
        doLayerPanel();
        stage.addActor(layerPanel);


        // TextFields don't lose focus by default when you click out, so...
        stage.getRoot().addCaptureListener(new InputListener() {
            // this code was gratefully found as a response to this post: https://github.com/libgdx/libgdx/issues/2173
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField)) stage.setKeyboardFocus(null);
                return false;
            }
        });

        // text to tell users of otherwise hidden keybinds
        controlsTips.setPosition(8, 8);

        stage.addActor(controlsTips);
    }

    public void update(float delta) {
        updateGenerationSettingsPanel();
        updateLayerPanel();

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
        String selectedLayerType = layers[selectedLayerIndex].getClass().getName().replace("com.joelallison.generation.","").replace("Layer", "");

        switch (selectedLayerType) {
            case "Terrain":
                loadTerrainSettings();

                break;
            default:
                // 0_o
        }



        generationSettingsPanel.setPosition(5f, 500f);
        generationSettingsPanel.padLeft(4f);
        generationSettingsPanel.padRight(4f);
        generationSettingsPanel.setSize(generationSettingsPanel.getPrefWidth() * 1.2f, generationSettingsPanel.getPrefHeight());
    }

    protected void loadTerrainSettings() {
        generationSettingsPanel.add(scaleLabel);

        final Slider scaleSlider = new Slider(0.005f, 256f, 0.001f, false, chosenSkin);
        scaleSlider.setValue(((TerrainLayer) layers[selectedLayerIndex]).getScaleVal());

        scaleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) layers[selectedLayerIndex]).setScaleVal(scaleSlider.getValue());
            }
        });

        generationSettingsPanel.add(scaleSlider);
        generationSettingsPanel.row();

        generationSettingsPanel.add(octavesLabel);

        final Slider octavesSlider = new Slider(1, 3, 1, false, chosenSkin);
        octavesSlider.setValue(((TerrainLayer) layers[selectedLayerIndex]).getOctavesVal());

        octavesSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) layers[selectedLayerIndex]).setOctavesVal((int) octavesSlider.getValue());
            }
        });

        generationSettingsPanel.add(octavesSlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(lacunarityLabel);

        final Slider lacunaritySlider = new Slider(0.01f, 10f, 0.01f, false, chosenSkin);
        lacunaritySlider.setValue(((TerrainLayer) layers[selectedLayerIndex]).getLacunarityVal());

        lacunaritySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) layers[selectedLayerIndex]).setLacunarityVal(lacunaritySlider.getValue());
            }
        });

        generationSettingsPanel.add(lacunaritySlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(wrapFactorLabel);

        final Slider wrapFactorSlider = new Slider(1, 20, 1, false, chosenSkin);
        wrapFactorSlider.setValue(((TerrainLayer) layers[selectedLayerIndex]).getWrapVal());

        wrapFactorSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) layers[selectedLayerIndex]).setWrapVal((int) wrapFactorSlider.getValue());
            }
        });

        generationSettingsPanel.add(wrapFactorSlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(invertLabel);
        invertCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) layers[selectedLayerIndex]).setInvert(invertCheck.isChecked());
            }
        });

        generationSettingsPanel.add(invertCheck);

        generationSettingsPanel.row();
        generationSettingsPanel.row();
        generationSettingsPanel.add(centerPointLabel);

        //add sliders to input coords


    }

    protected void updateGenerationSettingsPanel(){
        updateGenerationSettingsTerrain();
    }

    protected void updateGenerationSettingsTerrain() {
        if (((TerrainLayer) layers[selectedLayerIndex]).getOctavesVal() < 2) { // lacunarity has no effect if octaves is less than 2, this visual update attempts to indicate that to the user
            lacunarityLabel.setColor(0.45f, 0.45f, 0.45f, 1);
        } else {
            lacunarityLabel.setColor(1, 1, 1, 1);
        }

        scaleLabel.setText("Scale: " + floatFormat.format(((TerrainLayer) layers[selectedLayerIndex]).getScaleVal()));
        octavesLabel.setText("Octaves: " + intFormat.format(((TerrainLayer) layers[selectedLayerIndex]).getOctavesVal()));
        lacunarityLabel.setText("Lacunarity: " + floatFormat.format(((TerrainLayer) layers[selectedLayerIndex]).getLacunarityVal()));
        wrapFactorLabel.setText("Wrap Factor: " + floatFormat.format(((TerrainLayer) layers[selectedLayerIndex]).getWrapVal()));
    }

    protected void doLayerPanel() {
        oldLayers = layers;
        for (int i = 0; i < layers.length; i++) {
            layerGroup.addActor(createLayer((layers[i])));
        }

        layerPanel.setSize(layerPanel.getPrefWidth(), layerPanel.getPrefHeight());

    }

    protected void updateLayerPanel() {
        if (oldLayers != layers) {
            layerGroup.clear();
            for (int i = 0; i < layers.length; i++) {
                layerGroup.addActor(createLayer((layers[i])));
            }
        }

        oldLayers = layers;
    }
}
