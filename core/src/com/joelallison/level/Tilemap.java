package com.joelallison.level;

public class Tilemap {

    class RGBA {
        public int r;
        public int g;
        public int b;
        public int a;

        public RGBA(int r, int g, int b, int a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        public String intToBinaryString(int i){
            return String.format("%32s", Integer.toBinaryString(i)).replaceAll(" ", "0");
        }
    }
}
