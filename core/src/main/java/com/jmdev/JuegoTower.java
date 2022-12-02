package com.jmdev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
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
import com.jmdev.Actores.Manager;

public class JuegoTower extends ScreenAdapter {
    private final Proyecto juego;
    private final Stage stage;
    TiledMap map;
    private OrthographicCamera camera,cameraHud;
    OrthogonalTiledMapRenderer mapRenderer;
    private final int mapWidthInPixels;
    private final int mapHeightInPixels;
    private float offsetX, offsetY;
    private final Hero heroe;
    private SpriteBatch batch;
    private BitmapFont fuenteEnemigos, fuenteVidas;
    final int[] capas_altas = {13,14,15};
    final int[] capas_bajas = {0,1,2,3,4,5,6,7,8,8,9,10,11,12};


    /**
     *
     * @param juego Clase Proyecto donde almacenamos toda la informacion
     * @param estado Estado en el que entra al juego, 0=nuevo 1=carga 2=vuelta pause
     */
    public JuegoTower(Proyecto juego,int estado){

        this.juego = juego;
        juego.enemigosEliminados = 0;
        //MAPA
        map = new TmxMapLoader().load("mapa/mapa.tmx");
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
        Actor manager = new Manager(juego,stage,map,heroe);
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
        fuenteVidas.draw(batch,"Vidas: " + juego.vidas,cameraHud.viewportWidth - 75,cameraHud.viewportHeight - 15);
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

        camera.position.x = camera.viewportWidth / 2 + offsetX;
        camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
    }
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        //LIMITAMOS EL MAPA A 800x640 PARA QUE EL USUARIO NO VEA MUCHO MAPA AL PONER EL JUEGO EN PANTALLA COMPLETA
        /*if(width > 800){
            if(height > 640){
                camera.setToOrtho(false, 800, 640);
            }else{
                camera.setToOrtho(false,800,height);
            }
        }else{*/
            camera.setToOrtho(false, width, height);
       // }
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
