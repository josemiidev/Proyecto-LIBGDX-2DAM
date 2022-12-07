package com.jmdev.Actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jmdev.JuegoTower;
import com.jmdev.Objetos.Area;
import com.jmdev.Proyecto;

public class ManagerCasa extends Actor {
    private Proyecto juego;
    private Stage stage;
    private Hero heroe;
    private TiledMap mapa;
    Area area;

    public ManagerCasa(Proyecto juego, Stage stage, TiledMap mapa, Hero heroe) {
        this.juego = juego;
        this.stage = stage;
        this.mapa = mapa;
        this.heroe = heroe;
        stage.addActor(heroe);
        cargaAreas();
        addListener(new ManagerCasa.ManagerInputListener());

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //font.draw(batch, "Enemigos: " + juego.enemigosEliminados + "/15", 20, 460);
    }

    @Override
    public void act(float delta) {
        super.act(delta); // MUY IMPORTANTE
        if (Intersector.overlaps(heroe.getShape(), area.getArea())) {
            juego.setScreen(new JuegoTower(juego, 2));
        }
    }

    private void cargaAreas() {
        MapLayer capaAreas = mapa.getLayers().get("objetos");
        MapObject objetoArea;


        objetoArea = capaAreas.getObjects().get("salida");
        area = new Area(objetoArea.getProperties().get("x", Float.class),
                objetoArea.getProperties().get("y", Float.class),
                objetoArea.getProperties().get("width", Float.class),
                objetoArea.getProperties().get("height", Float.class),
                1);

    }

    class ManagerInputListener extends InputListener {
        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            switch (keycode) {
                case Input.Keys.S:
                    heroe.verticalMovement = Hero.VerticalMovement.DOWN;
                    break;
                case Input.Keys.W:
                    heroe.verticalMovement = Hero.VerticalMovement.UP;
                    break;
                case Input.Keys.A:
                    heroe.horizontalMovement = Hero.HorizontalMovement.LEFT;
                    break;
                case Input.Keys.D:
                    heroe.horizontalMovement = Hero.HorizontalMovement.RIGHT;
                    break;
                case Input.Keys.SPACE:
                    if (!heroe.ataca) {
                        Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("sonido/espada.mp3"));
                        dropSound.play();
                        heroe.ataca = true;
                    }
                    break;
                case Input.Keys.E:
                    //heroe.comprobarCofre();
                    break;
            }
            return true;
        }

        @Override
        public boolean keyUp(InputEvent event, int keycode) {
            switch (keycode) {
                case Input.Keys.S:
                    if (heroe.verticalMovement == Hero.VerticalMovement.DOWN) {
                        heroe.verticalMovement = Hero.VerticalMovement.NONE;
                    }
                    break;
                case Input.Keys.W:
                    if (heroe.verticalMovement == Hero.VerticalMovement.UP) {
                        heroe.verticalMovement = Hero.VerticalMovement.NONE;
                    }
                    break;
                case Input.Keys.A:
                    if (heroe.horizontalMovement == Hero.HorizontalMovement.LEFT) {
                        heroe.horizontalMovement = Hero.HorizontalMovement.NONE;
                    }
                    break;
                case Input.Keys.D:
                    if (heroe.horizontalMovement == Hero.HorizontalMovement.RIGHT) {
                        heroe.horizontalMovement = Hero.HorizontalMovement.NONE;
                    }
                    break;
                case Input.Keys.SPACE:
                    heroe.atacando = false;
                    break;
            }
            return true;
        }
    }
}
