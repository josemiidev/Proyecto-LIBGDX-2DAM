package com.jmdev.Actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Enemigo extends Actor {
    private static final int FRAME_COLS = 3, FRAME_ROWS = 4;
    Animation<TextureRegion> animacionArriba, animacionDerecha, animacionIzquierda, animacionAbajo;
    Texture walkSheet;
    Hero.HorizontalMovement horizontalMovement;
    Hero.VerticalMovement verticalMovement;
    TextureRegion regionActual;
    TextureRegion[] andarArriba, andarDerecha, andarIzquierda, andarAbajo;
    float stateTime;
    TiledMap mapa;
    public boolean isAlive;
    public Enemigo(int x,int y) {
        if (regionActual == null) {
            recortarTextura();
        }
        isAlive = true;
        stateTime = 0f;
        horizontalMovement = Hero.HorizontalMovement.NONE;
        verticalMovement = Hero.VerticalMovement.NONE;

        setSize(regionActual.getRegionWidth(), regionActual.getRegionHeight());
        setPosition(x, y);
    }
    public void morir(){

    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(regionActual, getX(), getY());
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += Gdx.graphics.getDeltaTime();

    }
    private void recortarTextura() {
        switch(MathUtils.random(1,3)){
            case 1:
                walkSheet = new Texture(Gdx.files.internal("duende.png"));
                break;
            case 2:
                walkSheet = new Texture(Gdx.files.internal("esqueleto.png"));
                break;
            case 3:
                walkSheet = new Texture(Gdx.files.internal("fantasma.png"));
                break;
        }

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);
        andarArriba = new TextureRegion[FRAME_COLS];
        andarDerecha = new TextureRegion[FRAME_COLS];
        andarIzquierda = new TextureRegion[FRAME_COLS];
        andarAbajo = new TextureRegion[FRAME_COLS];

        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                switch (i) {
                    case 0:
                        andarAbajo[j] = tmp[i][j];
                        animacionArriba = new Animation<TextureRegion>(0.1f, andarArriba);
                        break;
                    case 1:
                        andarIzquierda[j] = tmp[i][j];
                        animacionDerecha = new Animation<TextureRegion>(0.1f, andarDerecha);
                        break;
                    case 2:
                        andarDerecha[j] = tmp[i][j];
                        animacionAbajo = new Animation<TextureRegion>(0.1f, andarAbajo);
                        break;
                    case 3:
                        andarArriba[j] = tmp[i][j];
                        animacionIzquierda = new Animation<TextureRegion>(0.1f, andarIzquierda);
                        break;
                }
            }
        }
        regionActual = andarAbajo[1];
    }
    public Rectangle getShape(){
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
