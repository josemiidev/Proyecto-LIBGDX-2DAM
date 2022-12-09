package com.jmdev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.jmdev.Actores.Cofre;
import com.jmdev.Actores.Enemigo;
import com.jmdev.Actores.Inventario;
import com.jmdev.Objetos.Mensaje;

import java.util.ArrayList;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Proyecto extends Game {
    public Skin gameSkin;
    public int enemigosEliminados;
    public int vidas, ultimaCasa;
    public SpriteBatch batch;
    public Music music;
    public ArrayList<Enemigo> enemigos;
    public Inventario inventario;
    public ArrayList<Cofre> cofres;
    public ArrayList<Mensaje> mensajes;

    public String ultimoObjeto;

    @Override
    public void create() {
        gameSkin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        batch = new SpriteBatch();
        ultimaCasa = 0;
        setScreen(new PantallaMenu(this));
    }

    @Override
    public void dispose() {
        gameSkin.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}