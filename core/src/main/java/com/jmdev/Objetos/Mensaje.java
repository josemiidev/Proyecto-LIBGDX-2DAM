package com.jmdev.Objetos;

import com.badlogic.gdx.math.Rectangle;

public class Mensaje {
    private Rectangle area;
    private String texto;
    private boolean mostrado;

    public Mensaje(float x, float y, float width, float hegiht, String mensaje) {
        this.texto = mensaje;
        this.area = new Rectangle(x, y, width, hegiht);
        this.mostrado = false;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setArea(Rectangle area) {
        this.area = area;
    }

    public Rectangle getArea() {
        return area;
    }

    public void setMostrado(boolean mostrado) {
        this.mostrado = mostrado;
    }

    public boolean isMostrado() {
        return mostrado;
    }
}
