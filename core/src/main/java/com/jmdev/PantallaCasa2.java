package com.jmdev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jmdev.Actores.Hero;
import com.jmdev.Actores.ManagerCasa1;

public class PantallaCasa2 extends ScreenAdapter {
    private  Proyecto juego;
    private  Stage stage;
    TiledMap map;
    private OrthographicCamera camera,cameraHud;
    OrthogonalTiledMapRenderer mapRenderer;
    private  int mapWidthInPixels;
    private  int mapHeightInPixels;
    private float offsetX, offsetY;
    private  Hero heroe;
    private SpriteBatch batch;
    private BitmapFont fuenteEnemigos, fuenteVidas;
    final int[] capas_altas = {3};
    final int[] capas_bajas = {0,1,2};
    public PantallaCasa2(Proyecto juego,Hero heroe){
        this.juego = juego;
        this.heroe = heroe;
        juego.ultimaCasa = 1;
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
        //CAMARA HUD
        cameraHud = new OrthographicCamera();
        cameraHud.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        //FUENTES
        fuenteEnemigos = new BitmapFont();
        fuenteEnemigos.setColor(Color.BLACK);
        fuenteVidas = new BitmapFont();
        fuenteVidas.setColor(Color.BLACK);

        //ESCENA
        stage = new Stage();
        stage.setViewport(viewport);
        Actor manager = new ManagerCasa1(juego,stage,map,heroe);
        stage.addActor(manager);
        //ASIGANAMOS LOS PERMISOS DE TECLADO
        Gdx.input.setInputProcessor(stage);
        stage.setKeyboardFocus(manager);

        //POSICION CAMARA
        offsetX = heroe.getX() - Gdx.graphics.getWidth() / 2f;
        offsetY = -(heroe.getY()+heroe.getHeight()) + Gdx.graphics.getHeight() / 2f;
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
        fuenteEnemigos.draw(batch,"Enemigos: " + juego.enemigosEliminados + "/15",20,cameraHud.viewportHeight - 15);
        fuenteVidas.draw(batch,"Vidas Restantes: " + juego.vidas,cameraHud.viewportWidth - 150,cameraHud.viewportHeight - 15);
        batch.end();
    }
    private void ubicacionCamara(){
        if (heroe.getX() < camera.position.x - camera.viewportWidth/2 + 100 && offsetX > 0){
            offsetX -= 200 * Gdx.graphics.getDeltaTime();
        }
        if(heroe.getX() + heroe.getWidth() > camera.position.x + camera.viewportWidth / 2 - 100 &&  offsetX < mapWidthInPixels - camera.viewportWidth){
            offsetX += 200 * Gdx.graphics.getDeltaTime();
        }
        if(heroe.getY() < camera.position.y -camera.viewportHeight/2 + 100 && offsetY > - mapHeightInPixels + camera.viewportHeight){
            offsetY -=200 * Gdx.graphics.getDeltaTime();
        }
        if (heroe.getY() + heroe.getHeight() > camera.position.y  +camera.viewportHeight/2 - 100 && offsetY < 0){
            offsetY += 200 * Gdx.graphics.getDeltaTime();
        }

        if (offsetX < 0) offsetX = 0;
        if (offsetY > 0) offsetY = 0;
        if (offsetX > mapWidthInPixels - camera.viewportWidth) offsetX = mapWidthInPixels - camera.viewportWidth;
        if (offsetY < -mapHeightInPixels + camera.viewportHeight) offsetY = -mapHeightInPixels + camera.viewportHeight;

        /*camera.position.x = camera.viewportWidth / 2 + offsetX;
        camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;
        camera.update();*/
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
