package com.jmdev.Actores;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jmdev.Proyecto;

public class Manager extends Actor {
    private Proyecto juego;
    private Stage stage;
    public Manager(Proyecto juego, Stage stage){
        this.juego = juego;
        this.stage = stage;
    }
}
