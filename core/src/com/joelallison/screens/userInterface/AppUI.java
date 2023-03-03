package com.joelallison.screens.userInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.joelallison.generation.Layer;
import com.joelallison.generation.TerrainLayer;
import com.joelallison.graphics.Tileset;
import com.joelallison.screens.AppScreen;
import com.joelallison.user.Database;

import java.text.DecimalFormat;
import java.util.*;

import static com.joelallison.screens.AppScreen.*;

public class AppUI extends UI {

    //many elements of the ui are used in multiple methods, so it's best that they're all declared globally [to the class]

    //for all layer type gen settings
    protected TextButton layerSeedButton = new TextButton("Seed: ", chosenSkin);
    protected CheckBox inheritSeedCheck = new CheckBox("", chosenSkin);

    //for terrain gen settings
    protected Label scaleLabel = new Label("Scale:", chosenSkin);
    protected Label octavesLabel = new Label("Octaves:", chosenSkin);
    protected Label lacunarityLabel = new Label("Lacunarity:", chosenSkin);
    protected Label wrapFactorLabel = new Label("Wrap factor:", chosenSkin);
    protected Label invertLabel = new Label("Invert:", chosenSkin);
    protected CheckBox invertCheck = new CheckBox("", chosenSkin);
    protected Label centerPointLabel = new Label("x: , y:", chosenSkin); // for variable names, I'm reluctantly using the American spelling of 'centre' as it feels like convention :-(
    protected TextButton centerPointButton = new TextButton("Centre coords:", chosenSkin);
    String integerFilter = "[0-9]+";
    final Slider scaleSlider = new Slider(TerrainLayer.SCALE_MIN, TerrainLayer.SCALE_MAX, 0.001f, false, chosenSkin);
    final Slider octavesSlider = new Slider(TerrainLayer.OCTAVES_MIN, TerrainLayer.OCTAVES_MAX, 1, false, chosenSkin);
    final Slider lacunaritySlider = new Slider(TerrainLayer.LACUNARITY_MIN, TerrainLayer.LACUNARITY_MAX, 0.01f, false, chosenSkin);
    final Slider wrapFactorSlider = new Slider(TerrainLayer.WRAP_MIN, TerrainLayer.WRAP_MAX, 1, false, chosenSkin);
    protected DecimalFormat floatFormat = new DecimalFormat("##0.00");
    protected DecimalFormat intFormat = new DecimalFormat("00000");
    protected Window generationSettingsPanel = new Window("Generation parameters:", chosenSkin);
    protected Window layerPanel = new Window("Layers:", chosenSkin);
    protected Label selectedLayerLabel = new Label("The currently selected layer is '[].", chosenSkin);

    //for tile panel
    protected Window tilePanel = new Window("Tiles and aesthetics: ", chosenSkin);
    final SelectBox tilesetSelect = new SelectBox(chosenSkin);
    final Slider hueSlider = new Slider(-1, 1, 0.001f, false, chosenSkin);
    VerticalGroup tiles = new VerticalGroup();
    boolean updateTileList = false;
    //layer panel
    protected VerticalGroup layerGroup = new VerticalGroup();
    boolean layersChanged = false;
    boolean tileDataChanged = false;
    //misc ui
    String helpMsg = "Press TAB to toggle UI.\nUse '<' and '>' to zoom in and out. All window-box things are draggable and movable!\nThe * layer button is used to select that layer. The ! layer button is a shortcut to exporting the layer individually.";
    protected Label controlsTips = new Label(helpMsg, chosenSkin);
    protected Label topLabel = new Label("Name: , Seed: \nx: , y: ", chosenSkin);
    //global vars
    Stage stage;
    public static int selectedLayerIndex;
    public static String saveProgress = ""; // for updating the user on progress of save, public static so it can be changed in other classes
    //misc ui buttons at top of screen
    TextButton saveWorldButton = new TextButton("Save", chosenSkin);
    Label saveDialogText = new Label(saveProgress, chosenSkin);
    TextButton exportWorldButton = new TextButton("Export", chosenSkin);

    public void genUI(final Stage stage) { //stage is made final so that it can be accessed within inner classes
        this.stage = stage;

        //box to edit generation settings for selected layer
        doGenerationSettingsPanel();
        stage.addActor(generationSettingsPanel);

        //box for managing the different layers of generation
        layerPanel.add(layerGroup);
        doLayerPanel();
        stage.addActor(layerPanel);

        //box to edit tiles and visuals
        doTilePanel();
        stage.addActor(tilePanel);

        topLabel.setPosition(48, Gdx.graphics.getHeight() - (2 * topLabel.getPrefHeight()));
        stage.addActor(topLabel);

        saveWorldButton.setPosition(Gdx.graphics.getWidth() - saveWorldButton.getPrefWidth() * 1.5f, Gdx.graphics.getHeight() - saveWorldButton.getPrefHeight() * 1.5f);
        saveWorldButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Dialog saveDialog = new Dialog("Saving...", chosenSkin){
                    public void result(Object obj) {
                        if (obj.equals(true)) {
                            if (!saveProgress.equals("Done!")) {
                                //if saving isn't finished, the 'OK' button does nothing
                                cancel();
                            }
                        }
                    }
                };
                saveDialog.add(saveDialogText);
                saveDialog.row();
                saveDialog.button("OK", true);
                saveDialog.show(stage);

                Database.saveWorld(username, world);
                return true;
            }
        });

        stage.addActor(saveWorldButton);

        exportWorldButton.setPosition(saveWorldButton.getX(), saveWorldButton.getY() - exportWorldButton.getPrefHeight() - 8);
        stage.addActor(exportWorldButton);

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
        //updating the ui
        updateGenerationSettingsPanel();
        updateLayerPanel();
        updateTilePanel();

        topLabel.setText("Name: " + world.name + ", Seed: " + world.seed + "\nx: " + userInput.getxPosition() + " y: " + userInput.getyPosition());

        saveDialogText.setText(saveProgress);

        //hide/show ui elements, just using one of their 'isVisible' booleans as a measure of if they're hidden or not
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            if (generationSettingsPanel.isVisible()) {
                generationSettingsPanel.setVisible(false);
                topLabel.setVisible(false);
                layerPanel.setVisible(false);
                tilePanel.setVisible(false);
                controlsTips.setText("Press TAB to toggle UI.");
            } else {
                generationSettingsPanel.setVisible(true);
                topLabel.setVisible(true);
                layerPanel.setVisible(true);
                tilePanel.setVisible(true);
                controlsTips.setText(helpMsg);
            }
        }
    }

    protected void doGenerationSettingsPanel() {
        String selectedLayerType = world.layers.get(selectedLayerIndex).getClass().getName().replace("com.joelallison.generation.", "").replace("Layer", "");

        Label inheritLabel = new Label("Inherit global seed: ", chosenSkin);
        generationSettingsPanel.add(inheritLabel);
        inheritSeedCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                world.layers.get(selectedLayerIndex).setInheritSeed(inheritSeedCheck.isChecked());
            }
        });
        generationSettingsPanel.add(inheritSeedCheck);
        generationSettingsPanel.row();

        layerSeedButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                final TextField inputField = new TextField("", chosenSkin);
                inputField.setTextFieldFilter(new TextField.TextFieldFilter() {
                    // Accepts all Alphanumeric Characters except
                    public boolean acceptChar(TextField textField, char c) {
                        if (Character.toString(c).matches(integerFilter)) {
                            return true;
                        }
                        return false;
                    }
                });
                Dialog dialog = new Dialog("Edit seed", chosenSkin){
                    public void result(Object obj) {
                        if (obj.equals(true)) {
                            world.layers.get(selectedLayerIndex).setSeed(Long.parseLong(inputField.getText()));
                        }
                    }
                };
                dialog.text("Current seed is " + world.layers.get(selectedLayerIndex).getSeed() + "\nEnter new seed:");
                dialog.add(inputField);
                dialog.button("OK", true);
                dialog.show(stage);
                return true;
            }
        });

        generationSettingsPanel.add(layerSeedButton).colspan(2).align(Align.center);
        generationSettingsPanel.row();

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

        scaleSlider.setValue(((TerrainLayer) world.layers.get(selectedLayerIndex)).getScale());
        scaleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) world.layers.get(selectedLayerIndex)).setScale(scaleSlider.getValue());
            }
        });

        generationSettingsPanel.add(scaleSlider);
        generationSettingsPanel.row();

        generationSettingsPanel.add(octavesLabel);

        octavesSlider.setValue(((TerrainLayer) world.layers.get(selectedLayerIndex)).getOctaves());

        octavesSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) world.layers.get(selectedLayerIndex)).setOctaves((int) octavesSlider.getValue());
            }
        });

        generationSettingsPanel.add(octavesSlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(lacunarityLabel);

        lacunaritySlider.setValue(((TerrainLayer) world.layers.get(selectedLayerIndex)).getLacunarity());

        lacunaritySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) world.layers.get(selectedLayerIndex)).setLacunarity(lacunaritySlider.getValue());
            }
        });

        generationSettingsPanel.add(lacunaritySlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(wrapFactorLabel);

        wrapFactorSlider.setValue(((TerrainLayer) world.layers.get(selectedLayerIndex)).getWrap());

        wrapFactorSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) world.layers.get(selectedLayerIndex)).setWrap((int) wrapFactorSlider.getValue());
            }
        });

        generationSettingsPanel.add(wrapFactorSlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(invertLabel);
        invertCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) world.layers.get(selectedLayerIndex)).setInvert(invertCheck.isChecked());
            }
        });

        generationSettingsPanel.add(invertCheck);

        generationSettingsPanel.row();
        generationSettingsPanel.add(centerPointButton);
        centerPointButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Dialog dialog = new Dialog("Editing centre coords", chosenSkin, "dialog") {
                    //AAAAAAA
                };
                dialog.show(stage);
                return true;
            }
        });


        generationSettingsPanel.add(centerPointLabel);
    }

    protected void updateGenerationSettingsPanel() {
        String layerShown = "(hidden)";
        if (world.layers.get(selectedLayerIndex).layerShown()) {
            layerShown = "(shown)";
        }

        generationSettingsPanel.getTitleLabel().setText("Generation Settings: " + world.layers.get(selectedLayerIndex).getName() + " " + layerShown);
        inheritSeedCheck.setChecked(world.layers.get(selectedLayerIndex).inheritSeed());
        layerSeedButton.setText("Seed: " + world.layers.get(selectedLayerIndex).getSeed());

        updateGenerationSettingsTerrain();
    }

    protected void updateGenerationSettingsTerrain() {
        if (((TerrainLayer) world.layers.get(selectedLayerIndex)).getOctaves() < 2) { // lacunarity has no effect if octaves is less than 2, this visual update attempts to indicate that to the user
            lacunarityLabel.setColor(0.45f, 0.45f, 0.45f, 1);
        } else {
            lacunarityLabel.setColor(1, 1, 1, 1);
        }

        scaleLabel.setText("Scale: " + floatFormat.format(((TerrainLayer) world.layers.get(selectedLayerIndex)).getScale()));
        octavesLabel.setText("Octaves: " + intFormat.format(((TerrainLayer) world.layers.get(selectedLayerIndex)).getOctaves()));
        lacunarityLabel.setText("Lacunarity: " + floatFormat.format(((TerrainLayer) world.layers.get(selectedLayerIndex)).getLacunarity()));
        wrapFactorLabel.setText("Wrap Factor: " + floatFormat.format(((TerrainLayer) world.layers.get(selectedLayerIndex)).getWrap()));

        scaleSlider.setValue(((TerrainLayer) world.layers.get(selectedLayerIndex)).getScale());
        octavesSlider.setValue(((TerrainLayer) world.layers.get(selectedLayerIndex)).getOctaves());
        lacunaritySlider.setValue(((TerrainLayer) world.layers.get(selectedLayerIndex)).getLacunarity());
        wrapFactorSlider.setValue(((TerrainLayer) world.layers.get(selectedLayerIndex)).getWrap());
        invertCheck.setChecked(((TerrainLayer) world.layers.get(selectedLayerIndex)).isInverted());

    }

    protected void doLayerPanel() {
        for (int i = world.layers.size() - 1; i >= 0; i--) {
            layerGroup.addActor(createLayerWidget((world.layers.get(i))));
        }

        HorizontalGroup layerFunctions = new HorizontalGroup();
        layerFunctions.space(2);
        layerFunctions.pad(4);
        layerFunctions.align(Align.center);

        TextButton addLayer = new TextButton("+", chosenSkin);
        addLayer.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                world.layers.add(new TerrainLayer(world.seed));
                layersChanged = true;
                return true;
            }
        });

        TextButton removeLayer = new TextButton("-", chosenSkin);
        removeLayer.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (world.layers.size() > 1) {
                    world.layers.remove(selectedLayerIndex);
                    layersChanged = true;
                    if (selectedLayerIndex != 0) {
                        selectedLayerIndex = selectedLayerIndex - 1;
                    } else {
                        selectedLayerIndex = 0;
                    }
                }

                return true;
            }
        });

        layerFunctions.addActor(addLayer);
        layerFunctions.addActor(removeLayer);

        layerGroup.addActor(layerFunctions);

        layerGroup.addActor(selectedLayerLabel);

        layerPanel.setPosition(Gdx.graphics.getWidth() - layerPanel.getPrefWidth() - 5, Gdx.graphics.getHeight() - 400f);
        layerPanel.setSize(layerPanel.getPrefWidth(), layerPanel.getPrefHeight());
    }

    protected void updateLayerPanel() {

        // only make updates to the layers if anything has been edited, otherwise it's unnecessary as there's no change
        if (layersChanged) {
            tileDataChanged = true;
            layerGroup.clear();
            doLayerPanel();

            layersChanged = false;
        }

        selectedLayerLabel.setText("The currently selected layer is '" + world.layers.get(selectedLayerIndex).getName() + "'.");
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
                layer.showLayer(!layer.layerShown());
                return true;

            }
        });

        select.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                for (int i = 0; i < world.layers.size(); i++) {
                    if (Objects.equals(world.layers.get(i), layer)) {
                        AppUI.selectedLayerIndex = i;
                    }
                }
                return true;
            }
        });

        moveUp.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                int layerIndex = world.layers.indexOf(layer);
                if (layerIndex < world.layers.size() - 1) {
                    layersChanged = true;
                    world.swapLayers(layerIndex, layerIndex + 1);
                }
                return true;
            }
        });

        moveDown.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                int layerIndex = world.layers.indexOf(layer);
                if (layerIndex > 0) {
                    layersChanged = true;
                    world.swapLayers(layerIndex - 1, layerIndex);
                }
                return true;
            }
        });

        layerGroup.addActor(moveUp);
        layerGroup.addActor(moveDown);

        String layerType = AppScreen.getLayerType(layer);

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

    protected void doTilePanel() {
        tilesetSelect.setItems(tilesets.keySet().toArray());
        tilePanel.add(tilesetSelect);
        tilePanel.row();

        /*hueSlider.setValue((world.layers.get(selectedLayerIndex)).hueShift);
        hueSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                (world.layers.get(selectedLayerIndex)).hueShift = (hueSlider.getValue());
            }
        });

        tilePanel.add(hueSlider);*/

        genTileList();
        tilePanel.add(tiles);

        tilePanel.setSize(tilePanel.getPrefWidth() * 1.2f, tilePanel.getPrefHeight());
    }

    public void genTileList() {
        // sorting by a certain parameter -- threshold for terrain, alphabetically for maze
        world.layers.get(selectedLayerIndex).sortTileSpecs();

        // in the list of tilechildren, create a widget for each tile
        for (int i = 0; i < ((TerrainLayer) world.layers.get(selectedLayerIndex)).tileSpecs.size(); i++) {
            tiles.addActor(createTileLine(tilesets.get(world.layers.get(selectedLayerIndex).tilesetName), i));
        }

    }

    protected void updateTilePanel() {

        // only make updates to the tiles if anything has been edited, otherwise it's unnecessary as there's no change

        if (tileDataChanged) {
            for (int i = 0; i < tiles.getChildren().size; i++) {
                Tileset.TerrainTileSpec tile = ((TerrainLayer) world.layers.get(selectedLayerIndex)).tileSpecs.get(i);
                ((Label) ((HorizontalGroup) tiles.getChild(i)).getChild(1)).setText("name: " + tile.name + ", thresh: " + floatFormat.format(tile.lowerBound));
            }

            /*if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT) || updateTileList) {
                tilePanel.clear();
                genTileList();
                updateTileList = false;
            } else {
                updateTileList = true;
            }*/

            tileDataChanged = false;
        }

        tilesetSelect.setSelected(world.layers.get(selectedLayerIndex));
        //hueSlider.setValue((world.layers.get(selectedLayerIndex)).hueShift);
    }

    private HorizontalGroup createTileLine(Tileset tileset, final int selectedTile) {
        HorizontalGroup tileDataGroup = new HorizontalGroup();
        tileDataGroup.space(4);
        tileDataGroup.pad(2);

        final Tileset.TerrainTileSpec tile = ((TerrainLayer) world.layers.get(selectedLayerIndex)).tileSpecs.get(selectedTile);

        TextureRegionDrawable tileImgDrawable = new TextureRegionDrawable(tileset.getTileTexture(tileset.map.get(tile.name)));
        tileImgDrawable.setMinSize(tileImgDrawable.getMinWidth() * 2, tileImgDrawable.getMinHeight() * 2);
        Image tileImg = new Image(tileImgDrawable);
        tileDataGroup.addActor(tileImg);

        Label tileName = new Label("name: " + tile.name + ", thresh: " + floatFormat.format(tile.lowerBound), chosenSkin);
        tileDataGroup.addActor(tileName);

        final Slider lowerBoundSlider = new Slider(0, 1, 0.01f, false, chosenSkin);
        lowerBoundSlider.setValue(tile.lowerBound);
        lowerBoundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) world.layers.get(selectedLayerIndex)).tileSpecs.set(selectedTile, new Tileset.TerrainTileSpec(tile.name, lowerBoundSlider.getValue()));
            }
        });

        tileDataGroup.addActor(lowerBoundSlider);

        return tileDataGroup;
    }
}