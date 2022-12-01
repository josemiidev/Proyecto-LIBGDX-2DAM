package com.jmdev.Actores;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Textos extends Actor {
    BitmapFont font;
    String texto;
    float x,y;
    public Textos(String texto, float x, float y){
        this.texto = texto;
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        this.x = x;
        this.y = y;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.draw(batch,texto,x,y);
    }
}