package com.joelallison.generation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Layer {
    protected String name;
    protected Long seed;
    public String tilesetName = "Trees & Rocks"; //default value that's needed due to the way libgdx runs its render process
    protected boolean showLayer = true; // shown by default
    protected Vector2 center = new Vector2(0, 0);
    public float hueShift; //something I want to implement but haven't [yet], because shaders are painful
    protected boolean inheritSeed = false;

    // export layer button
    // clipping mode?
    // the spacing and wave collapse feature?

    //the Layer class is never used on its own, these methods are only inherited / overriden etc.
    public Layer(String name, Long seed) { //for the creation of a layer with specific values
        this.name = name;
        this.seed = seed;
    }
    public Layer() { //for the creation of a layer with default values

    }
    public TextureRegion getTextureFromIndex(int i) {
        return null;
    }

    public void sortTileSpecs(){
        //does nothing
    }
    public boolean layerShown() {
        return showLayer;
    }

    public void showLayer(boolean showLayer) {
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

    public boolean inheritSeed() {
        return inheritSeed;
    }

    public void setInheritSeed(boolean inheritSeed) {
        this.inheritSeed = inheritSeed;
    }

}
