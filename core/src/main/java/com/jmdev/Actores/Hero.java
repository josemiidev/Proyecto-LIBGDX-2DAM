package com.jmdev.Actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

public class Hero extends Actor {
    enum VerticalMovement {UP, NONE, DOWN}

    enum HorizontalMovement {LEFT, NONE, RIGHT}

    int FRAME_COLS = 9;
    int FRAME_ROWS = 4;

    Animation<TextureRegion> animacionArriba, animacionDerecha, animacionIzquierda, animacionAbajo;
    Animation<TextureRegion> animacionAtaqueArriba, animacionAtaqueDerecha, animacionAtaqueIzquierda, animacionAtaqueAbajo;
    Animation<TextureRegion> animacionMuerte;
    public HorizontalMovement horizontalMovement;
    public VerticalMovement verticalMovement;
    TextureRegion regionActual;
    TextureRegion[] andarArriba, andarDerecha, andarIzquierda, andarAbajo;
    TextureRegion[] atacarArriba, atacarDerecha, atacarIzquierda, atacarAbajo;
    TextureRegion[] muerte;
    float stateTime;
    ArrayList<TiledMapTileLayer> capasObstaculos;
    TiledMap mapa;
    Vector2 posicionAntigua;
    public Vector2 spawnPoint;
    public boolean atacando, finAnimacion, isAlive, muerto, muriendo, ataca, colisiones;
    private String ultimaPosicion;

    public Hero(TiledMap mapa) {
        this.mapa = mapa;
        if (regionActual == null) {
            recortarTextura();
        }
        isAlive = true;
        muerto = false;
        ataca = false;
        atacando = false;
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
        if (mapa.getLayers().get("colisiones cofres") != null) {
            capasObstaculos.add((TiledMapTileLayer) mapa.getLayers().get("colisiones cofres"));
        }
        if (mapa.getLayers().get("colisiones objetos") != null) {
            capasObstaculos.add((TiledMapTileLayer) mapa.getLayers().get("colisiones objetos"));
        }
        if (mapa.getLayers().get("colisiones arbustos") != null) {
            capasObstaculos.add((TiledMapTileLayer) mapa.getLayers().get("colisiones arbustos"));
        }
        if (mapa.getLayers().get("colisiones paredes") != null) {
            capasObstaculos.add((TiledMapTileLayer) mapa.getLayers().get("colisiones paredes"));
        }
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
        if (!isAlive && !muriendo) {
            muriendo = true;
            stateTime = 0;
        }
        if (ataca && !atacando) {
            atacando = true;
            stateTime = 0;
        }
        if (muriendo) {
            regionActual = animacionMuerte.getKeyFrame(stateTime, false);
            if (animacionMuerte.isAnimationFinished(stateTime)) {
                muerto = true;
            }
        }
        if (atacando) {
            switch (ultimaPosicion) {
                case "arriba":
                    regionActual = animacionAtaqueArriba.getKeyFrame(stateTime, false);
                    break;
                case "abajo":
                    regionActual = animacionAtaqueAbajo.getKeyFrame(stateTime, false);
                    break;
                case "izquierda":
                    regionActual = animacionAtaqueIzquierda.getKeyFrame(stateTime, false);
                    break;
                case "derecha":
                    regionActual = animacionAtaqueDerecha.getKeyFrame(stateTime, false);
                    break;
            }
            if (animacionAtaqueAbajo.isAnimationFinished(stateTime)) {
                atacando = false;
                ataca = false;
            } else if (animacionAtaqueDerecha.isAnimationFinished(stateTime)) {
                atacando = false;
                ataca = false;
            } else if (animacionAtaqueIzquierda.isAnimationFinished(stateTime)) {
                atacando = false;
                ataca = false;
            } else if (animacionAtaqueArriba.isAnimationFinished(stateTime)) {
                atacando = false;
                ataca = false;
            }
        }
        compruebaLimites();
    }

    private boolean colision() {
        boolean colision = false;
        if (!colisiones) {
            TiledMapTileLayer.Cell cell;
            for (TiledMapTileLayer capaObstaculos : capasObstaculos) {
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
        } else {
            return false;
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
            ultimaPosicion = "arriba";
        }
        if (verticalMovement == VerticalMovement.DOWN) {
            regionActual = animacionAbajo.getKeyFrame(stateTime, true);
            ultimaPosicion = "abajo";
        }
        if (horizontalMovement == HorizontalMovement.LEFT) {
            regionActual = animacionIzquierda.getKeyFrame(stateTime, true);
            ultimaPosicion = "izquierda";
        }
        if (horizontalMovement == HorizontalMovement.RIGHT) {
            regionActual = animacionDerecha.getKeyFrame(stateTime, true);
            ultimaPosicion = "derecha";
        }
    }

    private void mover(float delta) {
        if (verticalMovement == VerticalMovement.UP) {
            this.moveBy(0, 200 * delta);
        } else if (verticalMovement == VerticalMovement.DOWN) {
            this.moveBy(0, -200 * delta);
        }
        if (horizontalMovement == HorizontalMovement.LEFT) {
            this.moveBy(-200 * delta, 0);
        } else if (horizontalMovement == HorizontalMovement.RIGHT) {
            this.moveBy(200 * delta, 0);
        }
    }

    private void recortarTextura() {
        TextureRegion[][] tmp;
        Texture walkSheet = new Texture(Gdx.files.internal("heroe_andando.png"));
        tmp = TextureRegion.split(walkSheet,
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
                        andarArriba[j] = tmp[i][j];
                        break;
                    case 1:
                        andarIzquierda[j] = tmp[i][j];
                        break;
                    case 2:
                        andarAbajo[j] = tmp[i][j];
                        break;
                    case 3:
                        andarDerecha[j] = tmp[i][j];
                        break;
                }
            }
        }
        FRAME_COLS = 6;
        FRAME_ROWS = 4;
        walkSheet = new Texture(Gdx.files.internal("heroe_atacando.png"));
        tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);
        atacarArriba = new TextureRegion[FRAME_COLS+1];
        atacarDerecha = new TextureRegion[FRAME_COLS+1];
        atacarIzquierda = new TextureRegion[FRAME_COLS+1];
        atacarAbajo = new TextureRegion[FRAME_COLS+1];

        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                switch (i) {
                    case 0:
                        atacarArriba[j] = tmp[i][j];
                        break;
                    case 1:
                        atacarIzquierda[j] = tmp[i][j];
                        break;
                    case 2:
                        atacarAbajo[j] = tmp[i][j];
                        break;
                    case 3:
                        atacarDerecha[j] = tmp[i][j];
                        break;
                }
            }
        }
        atacarArriba[FRAME_COLS] = andarArriba[0];
        atacarIzquierda[FRAME_COLS] = andarIzquierda[0];
        atacarDerecha[FRAME_COLS] = andarDerecha[0];
        atacarAbajo[FRAME_COLS] = andarAbajo[0];

        Texture walkSheet2 = new Texture(Gdx.files.internal("heroe_muriendo.png"));
        tmp = TextureRegion.split(walkSheet2,
                walkSheet2.getWidth() / 6,
                walkSheet2.getHeight());
        muerte = new TextureRegion[6];
        for(int i = 0; i < 6; i++) {
            muerte[i] = tmp[0][i];
        }

        animacionArriba = new Animation<TextureRegion>(0.1f, andarArriba);
        animacionIzquierda = new Animation<TextureRegion>(0.1f, andarIzquierda);
        animacionAbajo = new Animation<TextureRegion>(0.1f, andarAbajo);
        animacionDerecha = new Animation<TextureRegion>(0.1f, andarDerecha);

        animacionAtaqueArriba = new Animation<TextureRegion>(0.05f, atacarArriba);
        animacionAtaqueArriba.setPlayMode(Animation.PlayMode.NORMAL);
        animacionAtaqueIzquierda = new Animation<TextureRegion>(0.05f, atacarIzquierda);
        animacionAtaqueIzquierda.setPlayMode(Animation.PlayMode.NORMAL);
        animacionAtaqueAbajo = new Animation<TextureRegion>(0.05f, atacarAbajo);
        animacionAtaqueAbajo.setPlayMode(Animation.PlayMode.NORMAL);
        animacionAtaqueDerecha = new Animation<TextureRegion>(0.05f, atacarDerecha);
        animacionAtaqueDerecha.setPlayMode(Animation.PlayMode.NORMAL);

        animacionMuerte = new Animation<TextureRegion>(0.2f, muerte);
        animacionMuerte.setPlayMode(Animation.PlayMode.NORMAL);

        regionActual = andarAbajo[1];
        ultimaPosicion = "abajo";
        finAnimacion = true;
    }

    private Vector2 getSpawnPoint() {
        MapLayer positionLayer = mapa.getLayers().get("objetos");
        MapObject playerSpawn = positionLayer.getObjects().get("spawn");
        return new Vector2(playerSpawn.getProperties().get("x", Float.class) - regionActual.getRegionWidth() / 2f, playerSpawn.getProperties().get("y", Float.class));
    }

    public Rectangle getShape() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
