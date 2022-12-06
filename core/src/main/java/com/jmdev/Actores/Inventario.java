package com.jmdev.Actores;

public class Inventario {
    private boolean pocion;
    private boolean carbon;
    private boolean baston;
    private boolean runa;
    private boolean llave;
    private boolean calavera;
    private boolean antorcha;

    public Inventario() {
    }

    public Inventario(boolean pocion, boolean carbon, boolean baston, boolean runa, boolean llave, boolean calavera, boolean antorcha) {
        this.pocion = pocion;
        this.carbon = carbon;
        this.baston = baston;
        this.runa = runa;
        this.llave = llave;
        this.calavera = calavera;
        this.antorcha = antorcha;
    }

    public boolean isPocion() {
        return pocion;
    }

    public void setPocion(boolean pocion) {
        this.pocion = pocion;
    }

    public boolean isCarbon() {
        return carbon;
    }

    public void setCarbon(boolean carbon) {
        this.carbon = carbon;
    }

    public boolean isBaston() {
        return baston;
    }

    public void setBaston(boolean baston) {
        this.baston = baston;
    }

    public boolean isRuna() {
        return runa;
    }

    public void setRuna(boolean runa) {
        this.runa = runa;
    }

    public boolean isLlave() {
        return llave;
    }

    public void setLlave(boolean llave) {
        this.llave = llave;
    }

    public boolean isCalavera() {
        return calavera;
    }

    public void setCalavera(boolean calavera) {
        this.calavera = calavera;
    }

    public boolean isAntorcha() {
        return antorcha;
    }

    public void setAntorcha(boolean antorcha) {
        this.antorcha = antorcha;
    }
}
