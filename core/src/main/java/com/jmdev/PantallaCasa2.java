package com.jmdev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jmdev.Actores.Cofre;
import com.jmdev.Actores.Hero;
import com.jmdev.Actores.ManagerCasa;
import com.jmdev.Objetos.Mensaje;

import java.util.ArrayList;
import java.util.Objects;

public class PantallaCasa2 extends ScreenAdapter {
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
    private final BitmapFont fuenteEnemigos, fuenteVidas, fuenteMensajes, fuenteEnter;
    final int[] capas_altas = {3};
    final int[] capas_bajas = {0, 1, 2};

    Texture cuadroDialogo;

    public PantallaCasa2(Proyecto juego, int tipoEntrada) {
        this.juego = juego;
        juego.ultimaCasa = 2;
        cuadroDialogo = new Texture("dialogo.png");
        //MAPA
        map = new TmxMapLoader().load("mapas/casa2/casa2.tmx");
        MapProperties properties = map.getProperties();
        int tileWidth = properties.get("tilewidth", Integer.class);
        int tileHeight = properties.get("tileheight", Integer.class);
        int mapWidthInTiles = properties.get("width", Integer.class);
        int mapHeightInTiles = properties.get("height", Integer.class);
        mapWidthInPixels = mapWidthInTiles * tileWidth;
        mapHeightInPixels = mapHeightInTiles * tileHeight;
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        //CAMARAS
        //CAMARA MOVIMIENTO
        camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        heroe = new Hero(map);
        if(tipoEntrada != 0){
            Vector2 spawn = getSpawnVuelta(tipoEntrada);
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
        fuenteEnter = new BitmapFont();
        fuenteEnter.setColor(Color.BLACK);

        //ESCENA
        stage = new Stage();
        stage.setViewport(viewport);
        Actor manager = new ManagerCasa(juego, stage, map, heroe);
        stage.addActor(manager);
        //ASIGANAMOS LOS PERMISOS DE TECLADO
        Gdx.input.setInputProcessor(stage);
        stage.setKeyboardFocus(manager);

        //POSICION CAMARA
        offsetX = heroe.getX() - Gdx.graphics.getWidth() / 2f;
        offsetY = -(heroe.getY() + heroe.getHeight()) + Gdx.graphics.getHeight() / 2f;

        cargarCofres();
    }
    private Vector2 getSpawnVuelta(int i) {
        MapLayer positionLayer = map.getLayers().get("objetos");
        MapObject playerSpawn = new MapObject();
        switch (i) {
            case 1:
                playerSpawn = positionLayer.getObjects().get("spawn_patio_1");
                break;
            case 2:
                playerSpawn = positionLayer.getObjects().get("spawn_patio_2");
                break;
        }
        return new Vector2(playerSpawn.getProperties().get("x", Float.class), playerSpawn.getProperties().get("y", Float.class));
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
                        false, "2" + cont);
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

        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ubicacionCamara();
        camera.position.x = camera.viewportWidth / 2 + offsetX;
        camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;
        camera.update();

        mapRenderer.setView(camera);
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
        for(Mensaje m : juego.mensajes){
            if(m.isActivo()){
                batch.draw(cuadroDialogo,0,0,cameraHud.viewportWidth,cuadroDialogo.getHeight());
                fuenteMensajes.draw(batch,m.getTexto(),10,cuadroDialogo.getHeight() - 10);
                fuenteEnter.draw(batch,"Presione ENTER para cerrar",cameraHud.viewportWidth - 200,20);
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

        mapRenderer.setView(camera);
        mapRenderer.render();
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
