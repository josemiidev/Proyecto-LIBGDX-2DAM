package com.jmdev.Actores;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

public class Hero extends Actor {
    enum VerticalMovement {UP, NONE, DOWN}
    enum HorizontalMovement {LEFT, NONE, RIGHT}
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
    public boolean atacando, finAnimacion, isAlive,muerto,muriendo,ataca,colisiones;
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
        if(mapa.getLayers().get("colisiones cofres") != null){
            capasObstaculos.add((TiledMapTileLayer) mapa.getLayers().get("colisiones cofres"));
        }
        if(mapa.getLayers().get("colisiones objetos") != null){
            capasObstaculos.add((TiledMapTileLayer) mapa.getLayers().get("colisiones objetos"));
        }
        if(mapa.getLayers().get("colisiones arbustos") != null){
            capasObstaculos.add((TiledMapTileLayer) mapa.getLayers().get("colisiones arbustos"));
        }
        if(mapa.getLayers().get("colisiones paredes") != null){
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
            muriendo=true;
            stateTime = 0;
        }
        if (ataca && !atacando) {
            atacando=true;
            stateTime = 0;
        }
        if(muriendo){
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
        if(!colisiones){
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
        }else{
            return false;
        }

    }

    public void comprobarCofre() {
        TiledMapTileLayer cofres = (TiledMapTileLayer) mapa.getLayers().get("colisiones cofres");
        ArrayList<TiledMapTileLayer.Cell> celdas = new ArrayList<TiledMapTileLayer.Cell>();
        celdas.add(cofres.getCell((Math.round(getX()) / 32) + 2, Math.round(getY()) / 32));
        celdas.add(cofres.getCell((Math.round(getX()) / 32) - 1, Math.round(getY()) / 32));
        celdas.add(cofres.getCell(Math.round(getX()) / 32, (Math.round(getY()) / 32) + 1));
        celdas.add(cofres.getCell((Math.round(getX()) / 32) + 1, (Math.round(getY()) / 32) + 1));
        celdas.add(cofres.getCell((Math.round(getX()) / 32) - 1, (Math.round(getY()) / 32) + 1));
        celdas.add(cofres.getCell(Math.round(getX()) / 32, (Math.round(getY()) / 32) - 1));
        for (TiledMapTileLayer.Cell cell : celdas) {
            if (cell != null) {
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
        //texturas andar
        Texture completo = new Texture(Gdx.files.internal("heroe.png"));
        //CREAMOS LOS OBJETOS TEXTUREREGION
        andarArriba = new TextureRegion[8];
        andarDerecha = new TextureRegion[8];
        andarIzquierda = new TextureRegion[8];
        andarAbajo = new TextureRegion[8];
        muerte = new TextureRegion[6];
        atacarArriba = new TextureRegion[7];
        atacarDerecha = new TextureRegion[7];
        atacarIzquierda = new TextureRegion[7];
        atacarAbajo = new TextureRegion[7];

        //CARGAMOS LOS OBJETOS
        andarArriba[0] = new TextureRegion(completo, 80, 523, 35, 52);
        andarArriba[1] = new TextureRegion(completo, 144, 521, 36, 55);
        andarArriba[2] = new TextureRegion(completo, 208, 524, 32, 51);
        andarArriba[3] = new TextureRegion(completo, 272, 521, 36, 54);
        andarArriba[4] = new TextureRegion(completo, 336, 523, 36, 52);
        andarArriba[5] = new TextureRegion(completo, 401, 523, 35, 52);
        andarArriba[6] = new TextureRegion(completo, 465, 524, 35, 51);
        andarArriba[7] = new TextureRegion(completo, 529, 523, 35, 52);

        andarIzquierda[0] = new TextureRegion(completo, 64, 588, 53, 51);
        andarIzquierda[1] = new TextureRegion(completo, 128, 587, 51, 52);
        andarIzquierda[2] = new TextureRegion(completo, 193, 587, 54, 52);
        andarIzquierda[3] = new TextureRegion(completo, 262, 587, 47, 52);
        andarIzquierda[4] = new TextureRegion(completo, 328, 587, 45, 52);
        andarIzquierda[5] = new TextureRegion(completo, 392, 587, 45, 52);
        andarIzquierda[6] = new TextureRegion(completo, 450, 587, 51, 52);
        andarIzquierda[7] = new TextureRegion(completo, 512, 587, 53, 52);

        andarAbajo[0] = new TextureRegion(completo, 80, 646, 31, 57);
        andarAbajo[1] = new TextureRegion(completo, 144, 646, 31, 57);
        andarAbajo[2] = new TextureRegion(completo, 209, 647, 30, 56);
        andarAbajo[3] = new TextureRegion(completo, 273, 646, 29, 57);
        andarAbajo[4] = new TextureRegion(completo, 336, 646, 29, 57);
        andarAbajo[5] = new TextureRegion(completo, 400, 646, 31, 57);
        andarAbajo[6] = new TextureRegion(completo, 465, 647, 29, 56);
        andarAbajo[7] = new TextureRegion(completo, 529, 646, 29, 57);

        andarDerecha[0] = new TextureRegion(completo, 74, 716, 53, 51);
        andarDerecha[1] = new TextureRegion(completo, 138, 715, 53, 52);
        andarDerecha[2] = new TextureRegion(completo, 202, 715, 52, 52);
        andarDerecha[3] = new TextureRegion(completo, 266, 715, 47, 52);
        andarDerecha[4] = new TextureRegion(completo, 330, 715, 39, 52);
        andarDerecha[5] = new TextureRegion(completo, 394, 715, 45, 52);
        andarDerecha[6] = new TextureRegion(completo, 458, 715, 51, 52);
        andarDerecha[7] = new TextureRegion(completo, 522, 715, 53, 52);

        muerte[0] = new TextureRegion(completo, 16, 1286, 31, 57);
        muerte[1] = new TextureRegion(completo, 81, 1288, 32, 55);
        muerte[2] = new TextureRegion(completo, 144, 1297, 34, 46);
        muerte[3] = new TextureRegion(completo, 208, 1301, 34, 42);
        muerte[4] = new TextureRegion(completo, 274, 1308, 33, 35);
        muerte[5] = new TextureRegion(completo, 338, 1305, 39, 38);

        atacarArriba[0] = new TextureRegion(completo, 75, 1420, 41, 51);
        atacarArriba[1] = new TextureRegion(completo, 255, 1421, 64, 50);
        atacarArriba[2] = new TextureRegion(completo, 448, 1421, 58, 50);
        atacarArriba[3] = new TextureRegion(completo, 629, 1420, 86, 51);
        atacarArriba[4] = new TextureRegion(completo, 820, 1404, 88, 67);
        atacarArriba[5] = new TextureRegion(completo, 996, 1406, 122, 64);
        atacarArriba[6] = andarArriba[0];

        atacarIzquierda[0] = new TextureRegion(completo, 53, 1611, 64, 53);
        atacarIzquierda[1] = new TextureRegion(completo, 269, 1613, 38, 50);
        atacarIzquierda[2] = new TextureRegion(completo, 464, 1611, 37, 52);
        atacarIzquierda[3] = new TextureRegion(completo, 646, 1611, 56, 52);
        atacarIzquierda[4] = new TextureRegion(completo, 787, 1611, 94, 52);
        atacarIzquierda[5] = new TextureRegion(completo, 978, 1611, 95, 52);
        atacarIzquierda[6] = andarIzquierda[0];

        atacarAbajo[0] = new TextureRegion(completo, 71, 1798, 40, 58);
        atacarAbajo[1] = new TextureRegion(completo, 255, 1798, 47, 57);
        atacarAbajo[2] = new TextureRegion(completo, 451, 1800, 41, 55);
        atacarAbajo[3] = new TextureRegion(completo, 632, 1799, 54, 56);
        atacarAbajo[4] = new TextureRegion(completo, 819, 1798, 80, 76);
        atacarAbajo[5] = new TextureRegion(completo, 1040, 1798, 75, 75);
        atacarAbajo[6] = andarAbajo[0];

        atacarDerecha[0] = new TextureRegion(completo, 74, 1995, 64, 53);
        atacarDerecha[1] = new TextureRegion(completo, 268, 1997, 39, 50);
        atacarDerecha[2] = new TextureRegion(completo, 458, 1995, 37, 52);
        atacarDerecha[3] = new TextureRegion(completo, 641, 1995, 56, 52);
        atacarDerecha[4] = new TextureRegion(completo, 846, 1995, 94, 52);
        atacarDerecha[5] = new TextureRegion(completo, 1038, 1995, 95, 52);
        atacarDerecha[6] = andarDerecha[0];

        //CARGAMOS LA ANIMACION
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
        //ESTABLECEMOS ESTADO ACTUAL
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
