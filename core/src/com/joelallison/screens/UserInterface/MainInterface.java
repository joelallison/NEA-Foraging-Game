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
import com.badlogic.gdx.utils.Align;
import com.joelallison.generation.Layer;
import com.joelallison.generation.TerrainLayer;
import com.joelallison.screens.MainScreen;

import java.text.DecimalFormat;
import java.util.Objects;

import static com.joelallison.screens.MainScreen.layers;
import static com.joelallison.screens.MainScreen.userControls;

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
    String integerFilter = "-?[0-9]+";
    final Slider scaleSlider = new Slider(0.005f, 256f, 0.001f, false, chosenSkin);
    final Slider octavesSlider = new Slider(1, 3, 1, false, chosenSkin);
    final Slider lacunaritySlider = new Slider(0.01f, 10f, 0.01f, false, chosenSkin);
    final Slider wrapFactorSlider = new Slider(1, 20, 1, false, chosenSkin);


    protected DecimalFormat floatFormat = new DecimalFormat("##0.00");
    protected DecimalFormat intFormat = new DecimalFormat("00000");
    protected Window generationSettingsPanel = new Window("Generation parameters:", chosenSkin);
    protected Window layerPanel = new Window("Layers:", chosenSkin);
    String helpMsg = "Press TAB to toggle UI.\nUse '<' and '>' to zoom in and out. All window-box things are draggable and movable!\nThe * layer button is used to select that layer. The ! layer button is a shortcut to exporting the layer individually.";
    protected Label controlsTips = new Label(helpMsg, chosenSkin);
    protected Label displayedCoordinates = new Label("x: , y: ", chosenSkin);
    protected VerticalGroup layerGroup = new VerticalGroup();
    boolean layersChanged = false;
    Stage stage;
    public static int selectedLayerIndex;
    protected Label selectedLayerLabel = new Label("The currently selected layer is '[].", chosenSkin);

    public void genUI(final Stage stage) { //stage is made final here so that it can be accessed within inner classes
        //menu bar, using custom method in UserInterface

        this.stage = stage;
        stage.addActor(constructMenuBar(new MenuMethod[]{new MenuMethod("File", true, new Runnable() {
            @Override
            public void run() {
                System.out.println("test");
            }
        }), new MenuMethod("Edit", true, new Runnable() {
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

        displayedCoordinates.setPosition(Gdx.graphics.getWidth() - (4 * displayedCoordinates.getPrefWidth()), Gdx.graphics.getHeight() - (2 * displayedCoordinates.getPrefHeight()));
        stage.addActor(displayedCoordinates);

        // TextFields don't lose focus by default when you click out, so...
        stage.getRoot().addCaptureListener(new InputListener() {
            // this code was gratefully found on a GitHub forum --> https://github.com/libgdx/libgdx/issues/2173
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

        displayedCoordinates.setText("x: " + userControls.getxPosition() + " y: " + userControls.getyPosition());

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            if (generationSettingsPanel.isVisible()) {
                generationSettingsPanel.setVisible(false);
                displayedCoordinates.setVisible(false);
                layerPanel.setVisible(false);
                controlsTips.setText("Press TAB to toggle UI.");
            } else {
                generationSettingsPanel.setVisible(true);
                displayedCoordinates.setVisible(true);
                layerPanel.setVisible(true);
                controlsTips.setText(helpMsg);
            }
        }
    }

    protected void doGenerationSettingsPanel() {
        String selectedLayerType = layers.get(selectedLayerIndex).getClass().getName().replace("com.joelallison.generation.", "").replace("Layer", "");

        switch (selectedLayerType) {
            case "Terrain":
                loadTerrainSettings();

                break;
            default:
                // 0_o
        }


        generationSettingsPanel.setPosition(5f, Gdx.graphics.getHeight() - 400f);
        generationSettingsPanel.padLeft(4f);
        generationSettingsPanel.padRight(4f);
        generationSettingsPanel.setSize(generationSettingsPanel.getPrefWidth() * 1.2f, generationSettingsPanel.getPrefHeight());
    }

    protected void loadTerrainSettings() {
        generationSettingsPanel.add(scaleLabel);

        scaleSlider.setValue(((TerrainLayer) layers.get(selectedLayerIndex)).getScale());
        scaleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) layers.get(selectedLayerIndex)).setScale(scaleSlider.getValue());
            }
        });

        generationSettingsPanel.add(scaleSlider);
        generationSettingsPanel.row();

        generationSettingsPanel.add(octavesLabel);

        octavesSlider.setValue(((TerrainLayer) layers.get(selectedLayerIndex)).getOctaves());

        octavesSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) layers.get(selectedLayerIndex)).setOctaves((int) octavesSlider.getValue());
            }
        });

        generationSettingsPanel.add(octavesSlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(lacunarityLabel);

        lacunaritySlider.setValue(((TerrainLayer) layers.get(selectedLayerIndex)).getLacunarity());

        lacunaritySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) layers.get(selectedLayerIndex)).setLacunarity(lacunaritySlider.getValue());
            }
        });

        generationSettingsPanel.add(lacunaritySlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(wrapFactorLabel);

        wrapFactorSlider.setValue(((TerrainLayer) layers.get(selectedLayerIndex)).getWrap());

        wrapFactorSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) layers.get(selectedLayerIndex)).setWrap((int) wrapFactorSlider.getValue());
            }
        });

        generationSettingsPanel.add(wrapFactorSlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(invertLabel);
        invertCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) layers.get(selectedLayerIndex)).setInvert(invertCheck.isChecked());
            }
        });

        generationSettingsPanel.add(invertCheck);

        generationSettingsPanel.row();
        generationSettingsPanel.row();
        generationSettingsPanel.add(centerPointLabel);
        generationSettingsPanel.row();

        generationSettingsPanel.add(xCoordField);
        generationSettingsPanel.add(yCoordField);

    }

    protected void updateGenerationSettingsPanel() {
        generationSettingsPanel.getTitleLabel().setText("Generation Settings: " + layers.get(selectedLayerIndex).getName());

        updateGenerationSettingsTerrain();
    }

    protected void updateGenerationSettingsTerrain() {
        if (((TerrainLayer) layers.get(selectedLayerIndex)).getOctaves() < 2) { // lacunarity has no effect if octaves is less than 2, this visual update attempts to indicate that to the user
            lacunarityLabel.setColor(0.45f, 0.45f, 0.45f, 1);
        } else {
            lacunarityLabel.setColor(1, 1, 1, 1);
        }

        scaleLabel.setText("Scale: " + floatFormat.format(((TerrainLayer) layers.get(selectedLayerIndex)).getScale()));
        octavesLabel.setText("Octaves: " + intFormat.format(((TerrainLayer) layers.get(selectedLayerIndex)).getOctaves()));
        lacunarityLabel.setText("Lacunarity: " + floatFormat.format(((TerrainLayer) layers.get(selectedLayerIndex)).getLacunarity()));
        wrapFactorLabel.setText("Wrap Factor: " + floatFormat.format(((TerrainLayer) layers.get(selectedLayerIndex)).getWrap()));

        scaleSlider.setValue(((TerrainLayer) layers.get(selectedLayerIndex)).getScale());
        octavesSlider.setValue(((TerrainLayer) layers.get(selectedLayerIndex)).getOctaves());
        lacunaritySlider.setValue(((TerrainLayer) layers.get(selectedLayerIndex)).getLacunarity());
        wrapFactorSlider.setValue(((TerrainLayer) layers.get(selectedLayerIndex)).getWrap());
        invertCheck.setChecked(((TerrainLayer) layers.get(selectedLayerIndex)).isInverted());

    }

    protected void doLayerPanel() {
        for (int i = layers.size() - 1; i >= 0; i--) {
            layerGroup.addActor(createLayerWidget((layers.get(i))));
        }

        HorizontalGroup layerFunctions = new HorizontalGroup();
        layerFunctions.space(2);
        layerFunctions.pad(4);
        layerFunctions.align(Align.center);

        TextButton addLayer = new TextButton("+", chosenSkin);
        addLayer.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                layers.add(new TerrainLayer(3L));
                layersChanged = true;
                return true;
            }
        });

        TextButton removeLayer = new TextButton("-", chosenSkin);

        layerFunctions.addActor(addLayer);
        layerFunctions.addActor(removeLayer);

        layerGroup.addActor(layerFunctions);

        layerGroup.addActor(selectedLayerLabel);

        layerPanel.setPosition(Gdx.graphics.getWidth() - layerPanel.getPrefWidth() - 5, Gdx.graphics.getHeight() - 400f);
        layerPanel.setSize(layerPanel.getPrefWidth(), layerPanel.getPrefHeight());
    }

    protected void updateLayerPanel() {
        if (layersChanged) {
            layerGroup.clear();
            doLayerPanel();

            layersChanged = false;
        }

        selectedLayerLabel.setText("The currently selected layer is '" + layers.get(selectedLayerIndex).getName() + "'.");
    }

    protected HorizontalGroup createLayerWidget(final Layer layer) {
        HorizontalGroup layerGroup = new HorizontalGroup();
        layerGroup.space(4);
        layerGroup.pad(8);

        TextButton select = new TextButton("*", chosenSkin);
        TextButton moveUp = new TextButton("^", chosenSkin);
        TextButton moveDown = new TextButton("v", chosenSkin);
        TextButton showOrHide = new TextButton("[show/hide]", chosenSkin);

        showOrHide.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                layer.setShowLayer(!layer.layerShown());
                return true;

            }
        });

        select.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                for (int i = 0; i < layers.size(); i++) {
                    if (Objects.equals(layers.get(i), layer)) {
                        MainInterface.selectedLayerIndex = i;
                    }
                }
                return true;

            }
        });

        moveUp.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                moveLayerUp(layers.indexOf(layer));
                return true;
            }
        });

        moveUp.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                moveLayerDown(layers.indexOf(layer));
                return true;
            }
        });

        layerGroup.addActor(moveUp);
        layerGroup.addActor(moveDown);

        String layerType = MainScreen.getLayerType(layer);

        switch (layerType) {
            case "Terrain":
                final TextField nameField = new TextField(layer.getName(), chosenSkin);
                nameField.setTextFieldListener(new TextField.TextFieldListener() {
                    @Override
                    public void keyTyped(TextField field, char c) {
                        layer.setName(nameField.getText());
                    }
                });

                layerGroup.addActor(nameField);

                break;
            default:
                layerGroup.addActor(new TextField("(error?) Unknown layer type: " + layer.getName(), chosenSkin));
        }

        layerGroup.addActor(showOrHide);
        layerGroup.addActor(select);

        return layerGroup;
    }

    public void moveLayerUp(int index) {
        if (index < layers.size()-1) {
            Layer tempLayer = layers.get(index);
            layers.set(index+1, layers.get(index));
            layers.set(index, tempLayer);
        }
    }

    public void moveLayerDown(int index) {
    }
}
