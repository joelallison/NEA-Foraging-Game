package com.joelallison.generation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.joelallison.graphics.Tileset;
import com.joelallison.graphics.Tileset.*;
import com.joelallison.screens.AppScreen;
import tools.HSLColor;

public abstract class Layer {
    protected String name;
    protected Long seed;
    public Tileset tileset = AppScreen.tilesets.get("Trees & Rocks"); //default value that's needed due to the way libgdx runs its render process
    protected boolean showLayer = true; // shown by default
    protected Vector2 center;
    public float hueShift;
    public TileChild[] tileChildren;
    protected boolean inheritSeed = false;

    // when you move the layer, a layer gets stored separately, another gets copied over to where it will move,
    // and then the separate one gets stored in its final place, writing over the recently moved one's old duplicate

    // layer has name, settings, chosen tileset and tile children that function in a way specific to the gen type
    // the layer itself can be moved up or down
    // layer settings edited on the left, upon selecting the layer
    // children boundaries etc. are edited on the right as part of the layer box.
    // export layer button
    // clipping mode?
    // the spacing and wave collapse feature?

    public Layer(String name, Long seed) { //for the creation of a layer with specific values
        this.name = name;
        this.seed = seed;
    }
    public Layer() { //for the creation of a layer with default values

    }
    public TextureRegion getTextureFromIndex(int i) {
        return this.tileset.getTileTexture(this.tileset.map.get(this.tileChildren[i].name));
    }
    public boolean layerShown() {
        return showLayer;
    }

    public void setShowLayer(boolean showLayer) {
        this.showLayer = showLayer;
    }

    public Vector2 getCenter() {
        return center;
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
