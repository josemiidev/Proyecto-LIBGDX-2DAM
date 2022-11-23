package com.jmdev.Actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.ArrayList;

public class Hero extends Actor {
    private static final int FRAME_COLS = 3, FRAME_ROWS = 4;
    enum VerticalMovement {UP, NONE, DOWN}
    enum HorizontalMovement {LEFT, NONE, RIGHT}

    Animation<TextureRegion> animacionArriba, animacionDerecha, animacionIzquierda, animacionAbajo;
    Texture walkSheet;
    public HorizontalMovement horizontalMovement;
    public VerticalMovement verticalMovement;
    TextureRegion regionActual;
    TextureRegion[] andarArriba, andarDerecha, andarIzquierda, andarAbajo;
    float stateTime;
    ArrayList<TiledMapTileLayer> capasObstaculos;
    TiledMap mapa;
    Vector2 posicionAntigua;
    public Vector2 spawnPoint;

    public Hero(TiledMap mapa) {
        this.mapa = mapa;
        if (regionActual == null) {
            RecortarTextura();
        }
        stateTime = 0f;

        horizontalMovement = HorizontalMovement.NONE;
        verticalMovement = VerticalMovement.NONE;

        setSize(regionActual.getRegionWidth(), regionActual.getRegionHeight());

        spawnPoint = getSpawnPoint();
        setPosition(spawnPoint.x, spawnPoint.y);
        cargarColisiones(mapa);
    }

    private void cargarColisiones(TiledMap mapa) {
        capasObstaculos = new ArrayList<TiledMapTileLayer>();
        capasObstaculos.add((TiledMapTileLayer) mapa.getLayers().get("colision_filo"));
        capasObstaculos.add((TiledMapTileLayer) mapa.getLayers().get("colision_objetos"));
        capasObstaculos.add((TiledMapTileLayer) mapa.getLayers().get("colisiones_cofres"));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(regionActual, getX(), getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += Gdx.graphics.getDeltaTime();

        compruebaTeclado();
        if (!colision()) {
            posicionAntigua = new Vector2(getX(), getY());
            mover(delta);
        } else {
            setPosition(posicionAntigua.x, posicionAntigua.y);
        }
        compruebaLimites();
    }

    private boolean colision() {
        boolean colision = false;
        TiledMapTileLayer.Cell cell;
        for(TiledMapTileLayer capaObstaculos:capasObstaculos){
            cell = capaObstaculos.getCell(Math.round(getX()) / 32, Math.round(getY()) / 32);
            if (cell != null) {
                colision = true;
            }
            cell = capaObstaculos.getCell(Math.round(getX() + getWidth()) / 32, Math.round(getY()) / 32);
            if (cell != null) {
                colision = true;
            }
        }
        return colision;
    }
    public void ComprobarCofre(){
        TiledMapTileLayer cofres = (TiledMapTileLayer) mapa.getLayers().get("colisiones_cofres");
        ArrayList<TiledMapTileLayer.Cell> celdas = new ArrayList<TiledMapTileLayer.Cell>();
        celdas.add(cofres.getCell((Math.round(getX()) / 32) + 2, Math.round(getY()) / 32));
        celdas.add(cofres.getCell((Math.round(getX()) / 32) - 1, Math.round(getY()) / 32));
        celdas.add(cofres.getCell(Math.round(getX()) / 32, (Math.round(getY()) / 32) + 1));
        celdas.add(cofres.getCell((Math.round(getX()) / 32)+1, (Math.round(getY()) / 32) + 1));
        celdas.add(cofres.getCell((Math.round(getX()) / 32)-1, (Math.round(getY()) / 32) + 1));
        celdas.add(cofres.getCell(Math.round(getX()) / 32, (Math.round(getY()) / 32) - 1 ));
        for(TiledMapTileLayer.Cell cell:celdas){
            if(cell!= null){
                //SONIDO DE ABRIR COFRE
                System.out.println("HAY COFRE");
                Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("sonido/abrir_cofre.mp3"));
                dropSound.play();
            }
        }
    }

    private void compruebaLimites() {
        MapProperties properties = mapa.getProperties();
        int mapWidthInTiles = properties.get("width", Integer.class);
        int mapHeightInTiles = properties.get("height", Integer.class);
        int mapWidthInPixels = mapWidthInTiles * 32;
        int mapHeightInPixels = mapHeightInTiles * 32;
        if (getX() < 0) setX(0);
        if (getY() < 0) setY(0);
        if (getX() >= mapWidthInPixels - getWidth()) setX(mapWidthInPixels - getWidth());
        if (getY() >= mapHeightInPixels - getHeight()) setY(mapHeightInPixels - getHeight());
    }

    private void compruebaTeclado() {
        if (verticalMovement == VerticalMovement.UP) {
            regionActual = animacionArriba.getKeyFrame(stateTime, true);
        }
        if (verticalMovement == VerticalMovement.DOWN) {
            regionActual = animacionAbajo.getKeyFrame(stateTime, true);
        }
        if (horizontalMovement == HorizontalMovement.LEFT) {
            regionActual = animacionIzquierda.getKeyFrame(stateTime, true);
        }
        if (horizontalMovement == HorizontalMovement.RIGHT) {
            regionActual = animacionDerecha.getKeyFrame(stateTime, true);
        }
    }

    private void mover(float delta) {
        if (verticalMovement == VerticalMovement.UP) {
            this.moveBy(0, 200 * delta);
        }else if (verticalMovement == VerticalMovement.DOWN) {
            this.moveBy(0, -200 * delta);
        }
        if (horizontalMovement == HorizontalMovement.LEFT) {
            this.moveBy(-200 * delta, 0);
        }else if (horizontalMovement == HorizontalMovement.RIGHT) {
            this.moveBy(200 * delta, 0);
        }
    }

    private void RecortarTextura() {
        walkSheet = new Texture(Gdx.files.internal("hero.png"));
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

    private Vector2 getSpawnPoint() {
        MapLayer positionLayer = mapa.getLayers().get("puntos");
        MapObject playerSpawn = positionLayer.getObjects().get("spawn");
        return new Vector2(playerSpawn.getProperties().get("x", Float.class) - regionActual.getRegionWidth() / 2, playerSpawn.getProperties().get("y", Float.class));
    }
}
