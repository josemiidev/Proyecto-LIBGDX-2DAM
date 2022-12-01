package com.jmdev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Proyecto extends Game {
    public Skin gameSkin;
    public long tiempo;
    public int enemigosEliminados;
    public SpriteBatch batch;
    @Override
    public void create() {
        gameSkin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        batch = new SpriteBatch();
        setScreen(new Menu(this));
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