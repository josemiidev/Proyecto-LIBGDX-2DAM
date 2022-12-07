package com.jmdev.Actores;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Inventario {
    private Texture rejilla;
    private Texture pocion;
    private Texture carbon;
    private Texture baston;
    private Texture runa;
    private Texture llave;
    private Texture calavera;
    private Texture antorcha;

    public Inventario(Texture pocion, Texture carbon, Texture baston, Texture runa, Texture llave, Texture calavera, Texture antorcha) {
        this.pocion = pocion;
        this.carbon = carbon;
        this.baston = baston;
        this.runa = runa;
        this.llave = llave;
        this.calavera = calavera;
        this.antorcha = antorcha;
        rejilla = new Texture("inventario.png");
    }

    public Inventario() {
        rejilla = new Texture("inventario.png");
    }

    public Texture getPocion() {
        return pocion;
    }

    public void setPocion(Texture pocion) {
        this.pocion = pocion;
    }

    public Texture getCarbon() {
        return carbon;
    }

    public void setCarbon(Texture carbon) {
        this.carbon = carbon;
    }

    public Texture getBaston() {
        return baston;
    }

    public void setBaston(Texture baston) {
        this.baston = baston;
    }

    public Texture getRuna() {
        return runa;
    }

    public void setRuna(Texture runa) {
        this.runa = runa;
    }

    public Texture getLlave() {
        return llave;
    }

    public void setLlave(Texture llave) {
        this.llave = llave;
    }

    public Texture getCalavera() {
        return calavera;
    }

    public void setCalavera(Texture calavera) {
        this.calavera = calavera;
    }

    public Texture getAntorcha() {
        return antorcha;
    }

    public void setAntorcha(Texture antorcha) {
        this.antorcha = antorcha;
    }

    public Texture getRejilla() {
        return rejilla;
    }

    public void setRejilla(Texture rejilla) {
        this.rejilla = rejilla;
    }
}
