package com.jmdev;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Proyecto extends Game {
    BitmapFont font;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;

    public Skin gameSkin;
    public double score;
    public String dificultad;

    @Override
    public void create() {
        font = new BitmapFont();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        score = 0;
        gameSkin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        setScreen(new Menu(this));

    }
    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
    }
}