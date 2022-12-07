package com.jmdev.Actores;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Cofre extends Actor {
    private Rectangle area;
    private boolean abierto;

    public Cofre() {
    }

    public Cofre(float x, float y, float width, float hegiht, boolean abierto) {
        this.abierto = abierto;
        this.area = new Rectangle(x, y, width, hegiht);
    }

    public Rectangle getArea() {
        return area;
    }

    public void setArea(Rectangle area) {
        this.area = area;
    }

    public boolean isAbierto() {
        return abierto;
    }

    public void setAbierto(boolean abierto) {
        this.abierto = abierto;
    }
}
