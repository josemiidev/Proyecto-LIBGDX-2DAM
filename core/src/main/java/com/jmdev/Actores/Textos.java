package com.jmdev.Actores;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Textos extends Actor {
    BitmapFont font;
    String texto;
    Texture bocadillo;
    public Textos(String texto){
        this.texto = texto;
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        this.bocadillo = new Texture("bocadillo.png");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(bocadillo, getX(), getY(),275,75);
        font.draw(batch,texto,getX()+20,getY()+60);
    }
}
