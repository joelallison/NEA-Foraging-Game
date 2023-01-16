package com.joelallison.screens.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.joelallison.generation.Layer;
import com.joelallison.screens.MainScreen;

import java.util.Objects;

import static com.joelallison.screens.MainScreen.layers;

public class UserInterface {

    public static Color defaultBackgroundColor = new Color(0.1215686f, 0.09411765f, 0.07843137f, 1);
    public static Skin chosenSkin = new Skin(Gdx.files.internal("data/defaultUI/uiskin.json"));

    public Stage genStage(Stage stage) {
        setupSkins();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        return stage;
    }

    private void setupSkins(){
        Skin defaultSkin = new Skin(Gdx.files.internal("data/defaultUI/uiskin.json"));
        chosenSkin = defaultSkin;
    }

    protected Table constructMenuBar(MenuMethod[] menuButtons, Vector2 position) {
        Table table = new Table();

        for (MenuMethod m:menuButtons) {
            table.add(processMenuBarItem(m));
        }

        //table.setFillParent(true);
        table.setDebug(true);


        table.setPosition(position.x, position.y);

        return table;
    }

    protected TextButton processMenuBarItem(final MenuMethod method) {
        //GlyphLayout glyphLayout = new GlyphLayout(chosenSkin.getFont("default-font"), method.displayName);

        final TextButton methodButton = new TextButton(method.displayName, chosenSkin, "default");
        methodButton.setPosition(0, 0 - methodButton.getHeight());
        methodButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                method.method.run();
            }
        });

        return methodButton;
    }

    protected class MenuMethod {
        public String displayName;
        public boolean canBeRun;
        public Runnable method;

        public MenuMethod(String displayName, boolean canBeRun, Runnable method) {
            this.displayName = displayName;
            this.canBeRun = canBeRun;
            this.method = method;
        }
    }

    protected HorizontalGroup createLayerWidget(final Layer layer) {
        HorizontalGroup layerGroup = new HorizontalGroup();
        layerGroup.space(4);
        layerGroup.pad(8);



        TextButton select = new TextButton("*", chosenSkin);
        TextButton moveUp = new TextButton("^", chosenSkin);
        TextButton moveDown = new TextButton("v", chosenSkin);
        TextButton showOrHide = new TextButton("[show/hide]", chosenSkin);

        select.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                for (int i = 0; i < layers.size(); i++) {
                    if(Objects.equals(layers.get(i), layer)) {
                        MainInterface.selectedLayerIndex = i;
                    }
                }
                return true;

            }
        });

        layerGroup.addActor(moveUp);
        layerGroup.addActor(moveDown);


        String layerType = MainScreen.getLayerType(layer);

        switch(layerType) {
            case "Terrain":
                layerGroup.addActor(new TextField(layer.getName(), chosenSkin));

                break;
            default:
                layerGroup.addActor(new TextField("(error?) Unknown layer type: " + layer.getName(), chosenSkin));

        }

        layerGroup.addActor(showOrHide);
        layerGroup.addActor(select);



        return layerGroup;
    }
}
