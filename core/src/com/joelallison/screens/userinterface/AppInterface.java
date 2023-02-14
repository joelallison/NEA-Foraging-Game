package com.joelallison.screens.userinterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
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

import java.text.DecimalFormat;
import java.util.*;

import static com.joelallison.screens.AppScreen.*;

public class AppInterface extends UserInterface {

    //many elements of the ui are used in multiple methods, so it's best that they're all declared globally [to the class]

    //for gen settings
    protected Label scaleLabel = new Label("Scale:", chosenSkin);
    protected Label octavesLabel = new Label("Octaves:", chosenSkin);
    protected Label lacunarityLabel = new Label("Lacunarity:", chosenSkin);
    protected Label wrapFactorLabel = new Label("Wrap factor:", chosenSkin);
    protected Label invertLabel = new Label("Invert:", chosenSkin);
    protected CheckBox invertCheck = new CheckBox("", chosenSkin);
    protected Label centerPointLabel = new Label("x: , y:", chosenSkin); // for variable names, I'm reluctantly using the American spelling of 'centre' as it feels like convention :-(
    protected TextButton centerPointButton = new TextButton("Centre coords:", chosenSkin);

    String integerFilter = "-?[0-9]+";
    final Slider scaleSlider = new Slider(0.005f, 256f, 0.001f, false, chosenSkin);
    final Slider octavesSlider = new Slider(1, 3, 1, false, chosenSkin);
    final Slider lacunaritySlider = new Slider(0.01f, 10f, 0.01f, false, chosenSkin);
    final Slider wrapFactorSlider = new Slider(1, 20, 1, false, chosenSkin);
    protected DecimalFormat floatFormat = new DecimalFormat("##0.00");
    protected DecimalFormat intFormat = new DecimalFormat("00000");
    protected Window generationSettingsPanel = new Window("Generation parameters:", chosenSkin);
    protected Window layerPanel = new Window("Layers:", chosenSkin);

    //for tile panel
    protected Window tilePanel = new Window("Tiles and aesthetics:", chosenSkin);
    final SelectBox tilesetSelect = new SelectBox(chosenSkin);
    final Slider hueSlider = new Slider(-1, 1, 0.001f, false, chosenSkin);
    String helpMsg = "Press TAB to toggle UI.\nUse '<' and '>' to zoom in and out. All window-box things are draggable and movable!\nThe * layer button is used to select that layer. The ! layer button is a shortcut to exporting the layer individually.";
    protected Label controlsTips = new Label(helpMsg, chosenSkin);
    protected Label displayedCoordinates = new Label("x: , y: ", chosenSkin);
    protected VerticalGroup layerGroup = new VerticalGroup();
    boolean layersChanged = false;
    boolean tileDataChanged = false;
    boolean updateTileList = false;
    Stage stage;
    public static int selectedLayerIndex;
    protected Label selectedLayerLabel = new Label("The currently selected layer is '[].", chosenSkin);
    VerticalGroup tiles = new VerticalGroup();

    public void genUI(final Stage stage) { //stage is made final here so that it can be accessed within inner classes
        //menu bar, using custom method in UserInterface

        this.stage = stage;

        /* ended up not being used


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

         */

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
        updateTilePanel();

        displayedCoordinates.setText("x: " + userInput.getxPosition() + " y: " + userInput.getyPosition());

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            if (generationSettingsPanel.isVisible()) {
                generationSettingsPanel.setVisible(false);
                displayedCoordinates.setVisible(false);
                layerPanel.setVisible(false);
                tilePanel.setVisible(false);
                controlsTips.setText("Press TAB to toggle UI.");
            } else {
                generationSettingsPanel.setVisible(true);
                displayedCoordinates.setVisible(true);
                layerPanel.setVisible(true);
                tilePanel.setVisible(true);
                controlsTips.setText(helpMsg);
            }
        }
    }

    protected void doGenerationSettingsPanel() {
        String selectedLayerType = creation.layers.get(selectedLayerIndex).getClass().getName().replace("com.joelallison.generation.", "").replace("Layer", "");

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

        scaleSlider.setValue(((TerrainLayer) creation.layers.get(selectedLayerIndex)).getScale());
        scaleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) creation.layers.get(selectedLayerIndex)).setScale(scaleSlider.getValue());
            }
        });

        generationSettingsPanel.add(scaleSlider);
        generationSettingsPanel.row();

        generationSettingsPanel.add(octavesLabel);

        octavesSlider.setValue(((TerrainLayer) creation.layers.get(selectedLayerIndex)).getOctaves());

        octavesSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) creation.layers.get(selectedLayerIndex)).setOctaves((int) octavesSlider.getValue());
            }
        });

        generationSettingsPanel.add(octavesSlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(lacunarityLabel);

        lacunaritySlider.setValue(((TerrainLayer) creation.layers.get(selectedLayerIndex)).getLacunarity());

        lacunaritySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) creation.layers.get(selectedLayerIndex)).setLacunarity(lacunaritySlider.getValue());
            }
        });

        generationSettingsPanel.add(lacunaritySlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(wrapFactorLabel);

        wrapFactorSlider.setValue(((TerrainLayer) creation.layers.get(selectedLayerIndex)).getWrap());

        wrapFactorSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) creation.layers.get(selectedLayerIndex)).setWrap((int) wrapFactorSlider.getValue());
            }
        });

        generationSettingsPanel.add(wrapFactorSlider);

        generationSettingsPanel.row();
        generationSettingsPanel.add(invertLabel);
        invertCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TerrainLayer) creation.layers.get(selectedLayerIndex)).setInvert(invertCheck.isChecked());
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
        generationSettingsPanel.getTitleLabel().setText("Generation Settings: " + creation.layers.get(selectedLayerIndex).getName());

        updateGenerationSettingsTerrain();
    }

    protected void updateGenerationSettingsTerrain() {
        if (((TerrainLayer) creation.layers.get(selectedLayerIndex)).getOctaves() < 2) { // lacunarity has no effect if octaves is less than 2, this visual update attempts to indicate that to the user
            lacunarityLabel.setColor(0.45f, 0.45f, 0.45f, 1);
        } else {
            lacunarityLabel.setColor(1, 1, 1, 1);
        }

        scaleLabel.setText("Scale: " + floatFormat.format(((TerrainLayer) creation.layers.get(selectedLayerIndex)).getScale()));
        octavesLabel.setText("Octaves: " + intFormat.format(((TerrainLayer) creation.layers.get(selectedLayerIndex)).getOctaves()));
        lacunarityLabel.setText("Lacunarity: " + floatFormat.format(((TerrainLayer) creation.layers.get(selectedLayerIndex)).getLacunarity()));
        wrapFactorLabel.setText("Wrap Factor: " + floatFormat.format(((TerrainLayer) creation.layers.get(selectedLayerIndex)).getWrap()));

        scaleSlider.setValue(((TerrainLayer) creation.layers.get(selectedLayerIndex)).getScale());
        octavesSlider.setValue(((TerrainLayer) creation.layers.get(selectedLayerIndex)).getOctaves());
        lacunaritySlider.setValue(((TerrainLayer) creation.layers.get(selectedLayerIndex)).getLacunarity());
        wrapFactorSlider.setValue(((TerrainLayer) creation.layers.get(selectedLayerIndex)).getWrap());
        invertCheck.setChecked(((TerrainLayer) creation.layers.get(selectedLayerIndex)).isInverted());

    }

    protected void doLayerPanel() {
        for (int i = creation.layers.size() - 1; i >= 0; i--) {
            layerGroup.addActor(createLayerWidget((creation.layers.get(i))));
        }

        HorizontalGroup layerFunctions = new HorizontalGroup();
        layerFunctions.space(2);
        layerFunctions.pad(4);
        layerFunctions.align(Align.center);

        TextButton addLayer = new TextButton("+", chosenSkin);
        addLayer.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                creation.layers.add(new TerrainLayer(3L));
                layersChanged = true;
                return true;
            }
        });

        TextButton removeLayer = new TextButton("-", chosenSkin);
        removeLayer.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(creation.layers.size() > 1){
                    creation.layers.remove(selectedLayerIndex);
                    layersChanged = true;
                    if(selectedLayerIndex != 0) {
                        selectedLayerIndex = selectedLayerIndex - 1;
                    }else {
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
            layerGroup.clear();
            doLayerPanel();

            layersChanged = false;
        }

        selectedLayerLabel.setText("The currently selected layer is '" + creation.layers.get(selectedLayerIndex).getName() + "'.");
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
                for (int i = 0; i < creation.layers.size(); i++) {
                    if (Objects.equals(creation.layers.get(i), layer)) {
                        AppInterface.selectedLayerIndex = i;
                    }
                }
                return true;
            }
        });

        moveUp.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                int layerIndex = creation.layers.indexOf(layer);
                if (layerIndex < creation.layers.size()-1) {
                    layersChanged = true;
                    creation.swapLayers(layerIndex, layerIndex + 1);
                }
                return true;
            }
        });

        moveDown.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                int layerIndex = creation.layers.indexOf(layer);
                if (layerIndex > 0) {
                    layersChanged = true;
                    creation.swapLayers(layerIndex - 1, layerIndex);
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
        tilesetSelect.setItems(tilesets.keySet());
        tilePanel.add(tilesetSelect);
        tilePanel.row();

        /*hueSlider.setValue((creation.layers.get(selectedLayerIndex)).hueShift);
        hueSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                (creation.layers.get(selectedLayerIndex)).hueShift = (hueSlider.getValue());
            }
        });

        tilePanel.add(hueSlider);*/

        genTilesList();
        tilePanel.add(tiles);

        tilePanel.setSize(tilePanel.getPrefWidth() * 1.2f, tilePanel.getPrefHeight());
    }

    public void genTilesList(){
        // sorting by a certain parameter -- threshold for terrain, alphabetically for maze
        creation.layers.get(selectedLayerIndex).sortTileChildren();

        // in the list of tilechildren, create a widget for each tile
        for (int i = 0; i < creation.layers.get(selectedLayerIndex).tileChildren.size(); i++) {
            tiles.addActor(createTileLine(tilesets.get(creation.layers.get(selectedLayerIndex).tileset), i));
        }

    }

    protected void updateTilePanel() {

        // only make updates to the tiles if anything has been edited, otherwise it's unnecessary as there's no change

        if (tileDataChanged) {
           for (int i = 0; i < tiles.getChildren().size; i++) {
                Tileset.TileChild tile = creation.layers.get(selectedLayerIndex).tileChildren.get(i);
               ((Label) ((HorizontalGroup) tiles.getChild(i)).getChild(1)).setText("name: " + tile.name + ", thresh: " + floatFormat.format(tile.lowerBound));
            }

            tileDataChanged = false;
        }

        if (updateTileList) {
            tilePanel.clear();
            genTilesList();

            updateTileList = false;
        }

        tilesetSelect.setSelected(creation.layers.get(selectedLayerIndex));
        //hueSlider.setValue((creation.layers.get(selectedLayerIndex)).hueShift);
    }

    private HorizontalGroup createTileLine(Tileset tileset, final int selectedTile) {
        HorizontalGroup tileDataGroup = new HorizontalGroup();
        tileDataGroup.space(4);
        tileDataGroup.pad(2);

        final Tileset.TileChild tile = creation.layers.get(selectedLayerIndex).tileChildren.get(selectedTile);

        TextureRegionDrawable tileImgDrawable = new TextureRegionDrawable(tileset.getTileTexture(tileset.map.get(tile.name)));
        tileImgDrawable.setMinSize(tileImgDrawable.getMinWidth()*2, tileImgDrawable.getMinHeight()*2);
        Image tileImg = new Image(tileImgDrawable);
        tileDataGroup.addActor(tileImg);

        Label tileName = new Label("name: " + tile.name + ", thresh: " + floatFormat.format(tile.lowerBound), chosenSkin);
        tileDataGroup.addActor(tileName);

        final Slider lowerBoundSlider = new Slider(0, 1, 0.05f, false, chosenSkin);
        lowerBoundSlider.setValue(tile.lowerBound);
        lowerBoundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                creation.layers.get(selectedLayerIndex).tileChildren.set(selectedTile, new Tileset.TileChild(tile.name, lowerBoundSlider.getValue()));
                tileDataChanged = true;
            }
        });

        lowerBoundSlider.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                updateTileList = true;
                System.out.println("HELLO?!?!?");
            }
        });

        tileDataGroup.addActor(lowerBoundSlider);

        return tileDataGroup;
    }
}