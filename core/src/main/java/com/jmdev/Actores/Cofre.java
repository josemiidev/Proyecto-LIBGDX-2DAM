package com.jmdev.Actores;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Cofre extends Actor {
    private Rectangle area;
    private boolean abierto;
    private String identificador;

    public Cofre(float x, float y, float width, float hegiht, boolean abierto, String identificador) {
        this.abierto = abierto;
        this.area = new Rectangle(x, y, width, hegiht);
        this.identificador = identificador;
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

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}
