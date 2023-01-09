package com.joelallison.screens.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.joelallison.generation.Layer;

import java.util.HashMap;

public class UserInterface {

    public static Color defaultBackgroundColor = new Color(0.1215686f, 0.09411765f, 0.07843137f, 1);
    public static Skin chosenSkin = new Skin(Gdx.files.internal("data/defaultUI/uiskin.json"));
    protected static String[] values = new String[20];

    public Stage genStage(Stage stage) {
        setupSkins();
        valuesDeclaration();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        return stage;
    }

    private void setupSkins(){
        Skin defaultSkin = new Skin(Gdx.files.internal("data/defaultUI/uiskin.json"));
        chosenSkin = defaultSkin;
    }

    public String[] getValues() {
        return values;
    }

    protected void valuesDeclaration() {

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

    protected HorizontalGroup createLayer(Layer layer) {
        System.out.println(layer.getClass().getName().replace("com.joelallison.generation.",""));

        return new HorizontalGroup();
    }
}
