package com.joelallison.generation;

public class GenSetting {
    protected String name;
    protected Long seed;

    public GenSetting(String name, Long seed) {
        this.name = name;
        this.seed = seed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
