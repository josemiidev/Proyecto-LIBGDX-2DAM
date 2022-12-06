package com.jmdev.Objetos;

import com.badlogic.gdx.math.Rectangle;

public class Area {
    private Rectangle area;
    private int numCasa;

    public Area(float x, float y, float width, float hegiht, int numCasa) {
        this.numCasa = numCasa;
        this.area = new Rectangle(x, y, width, hegiht);
    }

    public Rectangle getArea() {
        return area;
    }

    public void setArea(Rectangle area) {
        this.area = area;
    }

    public int getNumCasa() {
        return numCasa;
    }

    public void setNumCasa(int numCasa) {
        this.numCasa = numCasa;
    }
}
