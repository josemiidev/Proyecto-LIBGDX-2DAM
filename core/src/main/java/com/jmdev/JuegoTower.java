package com.jmdev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jmdev.Actores.*;
import com.jmdev.Objetos.Mensaje;

import java.util.ArrayList;
import java.util.Objects;


public class JuegoTower extends ScreenAdapter {
    private final Proyecto juego;
    private final Stage stage;
    TiledMap map;
    private final OrthographicCamera camera, cameraHud;
    OrthogonalTiledMapRenderer mapRenderer;
    private final int mapWidthInPixels;
    private final int mapHeightInPixels;
    private float offsetX, offsetY;
    private final Hero heroe;
    private final SpriteBatch batch;
    private final BitmapFont fuenteEnemigos, fuenteVidas, fuenteMensajes;
    final int[] capas_altas = {13, 14, 15};
    final int[] capas_bajas = {0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10, 11, 12};
    Texture cuadroDialogo;

    /**
     * @param juego  Clase Proyecto donde almacenamos toda la informacion
     * @param estado Estado en el que entra al juego, 0=nuevo 1=carga 2=vuelta pause
     */
    public JuegoTower(Proyecto juego, int estado) {
        this.juego = juego;
        //MAPA
        map = new TmxMapLoader().load("mapas/mapa.tmx");
        MapProperties properties = map.getProperties();
        int tileWidth = properties.get("tilewidth", Integer.class);
        int tileHeight = properties.get("tileheight", Integer.class);
        int mapWidthInTiles = properties.get("width", Integer.class);
        int mapHeightInTiles = properties.get("height", Integer.class);
        mapWidthInPixels = mapWidthInTiles * tileWidth;
        mapHeightInPixels = mapHeightInTiles * tileHeight;
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        cuadroDialogo = new Texture("dialogo.png");
        //CAMARAS
        //CAMARA MOVIMIENTO
        camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        heroe = new Hero(map);
        if (juego.ultimaCasa != 0) {
            Vector2 spawn = getSpawnVuelta(juego.ultimaCasa);
            heroe.setPosition(spawn.x, spawn.y);
        }
        //CAMARA HUD
        cameraHud = new OrthographicCamera();
        cameraHud.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        //FUENTES
        fuenteEnemigos = new BitmapFont();
        fuenteEnemigos.setColor(Color.BLACK);
        fuenteVidas = new BitmapFont();
        fuenteVidas.setColor(Color.BLACK);
        fuenteMensajes = new BitmapFont();
        fuenteMensajes.setColor(Color.BLACK);

        //ESCENA
        stage = new Stage();
        stage.setViewport(viewport);
        Actor manager = new Manager(juego, stage, map, heroe);
        stage.addActor(manager);
        if (estado != 2) {
            juego.music = Gdx.audio.newMusic(Gdx.files.internal("sonido/musica.mp3"));
            juego.music.setLooping(true);
            juego.music.play();
            juego.enemigosEliminados = 0;

        }
        if(juego.enemigos == null){
            juego.enemigos = new ArrayList<Enemigo>();
        }
        for (int i = 1; i <= 15; i++) {
            if(estado != 0){
                if(juego.enemigos.get(i-1).isAlive){
                    Enemigo en = new Enemigo(1500, 1500,i);
                    Vector2 spawn = getSpawnEnemigo(i);
                    en.setPosition(spawn.x, spawn.y);
                    CrearAnimacionEnemigo(en, i);
                    juego.enemigos.set(i-1, en);
                    stage.addActor(en);
                }
            }else{
                Enemigo en = new Enemigo(1500, 1500,i);
                Vector2 spawn = getSpawnEnemigo(i);
                en.setPosition(spawn.x, spawn.y);
                juego.enemigos.add(en);
                CrearAnimacionEnemigo(en, i);
                stage.addActor(en);
            }

        }
        //ASIGANAMOS LOS PERMISOS DE TECLADO
        Gdx.input.setInputProcessor(stage);
        stage.setKeyboardFocus(manager);

        //POSICION CAMARA
        offsetX = heroe.getX() - Gdx.graphics.getWidth() / 2f;
        offsetY = -(mapHeightInPixels - heroe.getY() + heroe.getHeight()) + Gdx.graphics.getHeight() / 2f;
        cargaAreaMensajes();
        cargarCofres();
    }

    private void CrearAnimacionEnemigo(Enemigo e, int i) {
        MoveToAction movimiento;
        SequenceAction secuencia;
        RepeatAction repeticion;
        switch (i) {
            case 1:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 200, e.getY());
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() - 200, e.getY());
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 2:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY() - 970);
                movimiento.setDuration(9f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY());
                movimiento.setDuration(9f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 3:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY() - 550);
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 600, e.getY() - 550);
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY() - 550);
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY());
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 4:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 1000, e.getY());
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 800, e.getY() + 400);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 460, e.getY() + 400);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY());
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 5:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 700, e.getY());
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 700, e.getY() + 300);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY());
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 6:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() - 200, e.getY() + 200);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 200, e.getY() + 200);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY());
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 7:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 200, e.getY());
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY());
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 8:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() - 200, e.getY() - 200);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY() - 300);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() - 200, e.getY() - 500);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY() - 700);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY());
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 9:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 1000, e.getY());
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY());
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 10:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 175, e.getY() - 200);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY() - 300);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 175, e.getY() - 600);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY() - 800);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY());
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 11:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 200, e.getY() + 200);
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 200, e.getY() + 400);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() - 200, e.getY() + 100);
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY());
                movimiento.setDuration(1f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 12:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() - 100, e.getY());
                movimiento.setDuration(1f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() - 100, e.getY() + 460);
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 300, e.getY() + 460);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() - 100, e.getY() + 460);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() - 100, e.getY());
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY());
                movimiento.setDuration(1f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 13:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 900, e.getY());
                movimiento.setDuration(4.5f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 900, e.getY() + 300);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 440, e.getY() + 300);
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 200, e.getY());
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY());
                movimiento.setDuration(1.5f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 14:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() - 200, e.getY() - 100);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY() - 300);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() - 150, e.getY() - 400);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() - 100, e.getY());
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY());
                movimiento.setDuration(1f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 15:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 250, e.getY() - 300);
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() - 200, e.getY() - 300);
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY());
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
        }
    }
    private Vector2 getSpawnEnemigo(int i) {
        MapLayer positionLayer = map.getLayers().get("enemigos");
        MapObject playerSpawn = positionLayer.getObjects().get("enemigo" + i);
        return new Vector2(playerSpawn.getProperties().get("x", Float.class), playerSpawn.getProperties().get("y", Float.class));
    }
    private void cargaAreaMensajes() {
        if(juego.mensajes == null){
            juego.mensajes = new ArrayList<Mensaje>();
        }
        MapLayer capaMensajes = map.getLayers().get("objetos");
        MapObject objetoMensajes;
        Mensaje mensaje;

        objetoMensajes = capaMensajes.getObjects().get("decision_camino");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x", Float.class),
                objetoMensajes.getProperties().get("y", Float.class),
                objetoMensajes.getProperties().get("width", Float.class),
                objetoMensajes.getProperties().get("height", Float.class),
                "¿Que camino debo coger primero? \nTendremos que probar suerte...",false);
        juego.mensajes.add(mensaje);

        objetoMensajes = capaMensajes.getObjects().get("cartel_casa_1");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x", Float.class),
                objetoMensajes.getProperties().get("y", Float.class),
                objetoMensajes.getProperties().get("width", Float.class),
                objetoMensajes.getProperties().get("height", Float.class),
                "'Casa de los Meintron... No pasar...' \nPero parece que no hay nadie...",false);
        juego.mensajes.add(mensaje);
        objetoMensajes = capaMensajes.getObjects().get("cementerio_1");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x", Float.class),
                objetoMensajes.getProperties().get("y", Float.class),
                objetoMensajes.getProperties().get("width", Float.class),
                objetoMensajes.getProperties().get("height", Float.class),
                "¿Un cementerio al lado de casa? \n Vaya vistas...",false);
        juego.mensajes.add(mensaje);
        objetoMensajes = capaMensajes.getObjects().get("laberinto_1");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x", Float.class),
                objetoMensajes.getProperties().get("y", Float.class),
                objetoMensajes.getProperties().get("width", Float.class),
                objetoMensajes.getProperties().get("height", Float.class),
                "El que plantó los arbustos así era \nun cachondo eh!",false);
        juego.mensajes.add(mensaje);
        objetoMensajes = capaMensajes.getObjects().get("lapida_1");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x", Float.class),
                objetoMensajes.getProperties().get("y", Float.class),
                objetoMensajes.getProperties().get("width", Float.class),
                objetoMensajes.getProperties().get("height", Float.class),
                "¿Tanto arbusto para esto? \n¿Qué habrá en el cofre?",false);
        juego.mensajes.add(mensaje);
        objetoMensajes = capaMensajes.getObjects().get("cartel_casa_2");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x", Float.class),
                objetoMensajes.getProperties().get("y", Float.class),
                objetoMensajes.getProperties().get("width", Float.class),
                objetoMensajes.getProperties().get("height", Float.class),
                "Se vende... \n¿Pero quien va a comparar esto?",false);
        juego.mensajes.add(mensaje);
    }
    private void cargarCofres(){
        int cont = 0;
        boolean sw = true;
        if(juego.cofres == null){
            juego.cofres = new ArrayList<Cofre>();
        }
        do{
            cont++;
            MapLayer capaCofres = map.getLayers().get("cofres");
            MapObject objetoCofre = capaCofres.getObjects().get("cofre"+cont);
            if(objetoCofre != null){
                Cofre cofre = new Cofre(objetoCofre.getProperties().get("x", Float.class),
                        objetoCofre.getProperties().get("y", Float.class),
                        objetoCofre.getProperties().get("width", Float.class),
                        objetoCofre.getProperties().get("height", Float.class),
                        false,cont + "");

                juego.mensajes.add(new Mensaje(cofre.getArea().x,cofre.getArea().y,cofre.getArea().width,cofre.getArea().height,
                        "",true));
                for(Cofre c:juego.cofres){
                    if (Objects.equals(c.getIdentificador(), cofre.getIdentificador())) {
                        sw = false;
                        break;
                    }
                }
                if(sw){
                    juego.cofres.add(cofre);
                }
            }else{
                break;
            }
        }while(true);
    }
    private Vector2 getSpawnVuelta(int i) {
        MapLayer positionLayer = map.getLayers().get("objetos");
        MapObject playerSpawn = new MapObject();
        switch (i) {
            case 1:
                playerSpawn = positionLayer.getObjects().get("spawn_casa_1");
                break;
            case 2:
                playerSpawn = positionLayer.getObjects().get("spawn_casa_2");
                break;
            case 3:
                playerSpawn = positionLayer.getObjects().get("patio_casa_2");
                break;
            case 4:
                playerSpawn = positionLayer.getObjects().get("patio_casa_2_2");
                break;
        }
        return new Vector2(playerSpawn.getProperties().get("x", Float.class), playerSpawn.getProperties().get("y", Float.class));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        super.hide();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        ubicacionCamara();

        mapRenderer.render(capas_bajas);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        mapRenderer.render(capas_altas);
        //DIBUJAR EL HUD
        cameraHud.update();
        batch.setProjectionMatrix(cameraHud.combined);
        batch.begin();
        fuenteEnemigos.draw(batch, "Enemigos: " + juego.enemigosEliminados + "/15", 20, cameraHud.viewportHeight - 15);
        fuenteVidas.draw(batch, "Vidas Restantes: " + juego.vidas, cameraHud.viewportWidth - 150, cameraHud.viewportHeight - 15);

        if (juego.inventario != null) {
            batch.draw(juego.inventario.getRejilla(), cameraHud.viewportWidth / 2 - juego.inventario.getRejilla().getWidth() / 2f, 1);
            if (juego.inventario.getRuna() != null) {
                batch.draw(juego.inventario.getRuna(), cameraHud.viewportWidth / 2 - juego.inventario.getRejilla().getWidth() / 2f + 12, 15);
            }
            if (juego.inventario.getAntorcha() != null) {
                batch.draw(juego.inventario.getAntorcha(), cameraHud.viewportWidth / 2 - juego.inventario.getRejilla().getWidth() / 2f + 68, 15);
            }
            if (juego.inventario.getBaston() != null) {
                batch.draw(juego.inventario.getBaston(), cameraHud.viewportWidth / 2 - juego.inventario.getRejilla().getWidth() / 2f + 124, 15);
            }
            if (juego.inventario.getLlave() != null) {
                batch.draw(juego.inventario.getLlave(), cameraHud.viewportWidth / 2 - juego.inventario.getLlave().getWidth() / 2f, 15);
            }
            if (juego.inventario.getCarbon() != null) {
                batch.draw(juego.inventario.getCarbon(), cameraHud.viewportWidth / 2 +40, 15);
            }
            if (juego.inventario.getPocion() != null) {
                batch.draw(juego.inventario.getPocion(), cameraHud.viewportWidth / 2 +95, 15);
            }
            if (juego.inventario.getCalavera() != null) {
                batch.draw(juego.inventario.getCalavera(), cameraHud.viewportWidth / 2 +150, 15);
            }
        }
        //DIBUJAR DIALOGOS
        for(Mensaje m : juego.mensajes){
            if(m.isActivo()){
                batch.draw(cuadroDialogo,0,0,cameraHud.viewportWidth,cuadroDialogo.getHeight());
                fuenteMensajes.draw(batch,m.getTexto(),10,cuadroDialogo.getHeight() - 10);
                m.setMostrado(true);
            }
        }

        batch.end();
    }

    private void ubicacionCamara() {
        if (heroe.getX() < camera.position.x - camera.viewportWidth / 2 + 100 && offsetX > 0) {
            offsetX -= 200 * Gdx.graphics.getDeltaTime();
        }
        if (heroe.getX() + heroe.getWidth() > camera.position.x + camera.viewportWidth / 2 - 100 && offsetX < mapWidthInPixels - camera.viewportWidth) {
            offsetX += 200 * Gdx.graphics.getDeltaTime();
        }
        if (heroe.getY() < camera.position.y - camera.viewportHeight / 2 + 100 && offsetY > -mapHeightInPixels + camera.viewportHeight) {
            offsetY -= 200 * Gdx.graphics.getDeltaTime();
        }
        if (heroe.getY() + heroe.getHeight() > camera.position.y + camera.viewportHeight / 2 - 100 && offsetY < 0) {
            offsetY += 200 * Gdx.graphics.getDeltaTime();
        }

        if (offsetX < 0) offsetX = 0;
        if (offsetY > 0) offsetY = 0;
        if (offsetX > mapWidthInPixels - camera.viewportWidth) offsetX = mapWidthInPixels - camera.viewportWidth;
        if (offsetY < -mapHeightInPixels + camera.viewportHeight) offsetY = -mapHeightInPixels + camera.viewportHeight;

        camera.position.x = camera.viewportWidth / 2 + offsetX;
        camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;
        camera.update();
        mapRenderer.setView(camera);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.setToOrtho(false, width, height);
        camera.position.x = camera.viewportWidth / 2 + offsetX;
        camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;
        camera.update();

        cameraHud.setToOrtho(false, width, height);
    }

    @Override
    public void dispose() {
        map.dispose();
        juego.dispose();
        stage.dispose();
        mapRenderer.dispose();
    }
}
