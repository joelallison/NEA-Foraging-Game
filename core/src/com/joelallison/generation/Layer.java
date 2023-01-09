package com.joelallison.generation;

import com.joelallison.display.Tileset;
import com.joelallison.display.Tileset.*;

public abstract class Layer {
    protected String name;
    protected Long seed;
    protected Tileset tileSet;
    protected boolean showLayer;
    protected boolean opened;


    // when you move the layer, a layer gets stored separately, another gets copied over to where it will move,
    // and then the separate one gets stored in its final place, writing over the recently moved one's old duplicate

    // layer has name, settings, chosen tileset and tile children that function in a way specific to the gen type
    // the layer itself can be moved up or down
    // layer settings edited on the left, upon selecting the layer
    // children boundaries etc. are edited on the right as part of the layer box.
    // export layer button
    // clipping mode?
    // the spacing and wave collapse feature?

    public Layer(String name, Long seed) {
        this.name = name;
        this.seed = seed;
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

    public TileChild[] getTileChildren(){
        return null; //
    }
}
