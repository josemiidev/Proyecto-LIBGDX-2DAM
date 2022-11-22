package com.jmdev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jmdev.Actores.Hero;
import com.jmdev.Actores.Manager;

public class JuegoTower extends ScreenAdapter {
    private Proyecto juego;
    private Stage stage;
    TiledMap map;
    private OrthographicCamera camera;
    OrthogonalTiledMapRenderer mapRenderer;
    private int mapWidthInPixels;
    private int mapHeightInPixels;
    private float offsetX, offsetY;
    Actor heroe;
    final int[] capas_altas = {7,9};
    final int[] capas_bajas = {0,1,2,3,4,5,6,8,10};


    public JuegoTower(Proyecto juego){
        this.juego = juego;

        //CREAMOS EL MANEJADOR DEL JUEGO
        //Actor manager = new Manager(juego,stage);

        //MAPA
        map = new TmxMapLoader().load("mapa_proyecto/mapa.tmx");
        MapProperties properties = map.getProperties();
        int tileWidth = properties.get("tilewidth", Integer.class);
        int tileHeight = properties.get("tileheight", Integer.class);
        int mapWidthInTiles = properties.get("width", Integer.class);
        int mapHeightInTiles = properties.get("height", Integer.class);
        mapWidthInPixels = mapWidthInTiles * tileWidth;
        mapHeightInPixels = mapHeightInTiles * tileHeight;
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        heroe = new Hero(map);

        //CAMARA
        camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);

        //ESCENA
        stage = new Stage();
        stage.setViewport(viewport);
        stage.addActor(heroe);

        //ASIGANAMOS LOS PERMISOS DE TECLADO
        Gdx.input.setInputProcessor(stage);
        stage.setKeyboardFocus(heroe);

        //POSICION CAMARA
        offsetX = heroe.getX() - 320;
        offsetY = 0;
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
        camera.position.x = camera.viewportWidth / 2 + offsetX;
        camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render(capas_bajas);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        mapRenderer.render(capas_altas);
    }
    private void ubicacionCamara(){
        if (heroe.getX() < camera.position.x - camera.viewportWidth/2 + 200 && offsetX > 0){
            offsetX -= 200 * Gdx.graphics.getDeltaTime();
        }
        if(heroe.getX() + heroe.getWidth() > camera.position.x + camera.viewportWidth / 2 - 200 &&  offsetX < mapWidthInPixels - camera.viewportWidth){
            offsetX += 200 * Gdx.graphics.getDeltaTime();
        }
        if(heroe.getY() < camera.position.y -camera.viewportHeight/2 + 200 && offsetY > - mapHeightInPixels + camera.viewportHeight){
            offsetY -=200 * Gdx.graphics.getDeltaTime();
        }
        if (heroe.getY() + heroe.getHeight() > camera.position.y  +camera.viewportHeight/2 - 200 && offsetY < 0){
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
        mapRenderer.render();
    }
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.setToOrtho(false, width, height);
        camera.position.x = camera.viewportWidth / 2 + offsetX;
        camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;
        camera.update();
    }

    @Override
    public void dispose() {
        map.dispose();
    }
}
