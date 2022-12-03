package com.jmdev.Actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.jmdev.Objetos.Mensaje;
import com.jmdev.PantallaFin;
import com.jmdev.Proyecto;

import java.util.ArrayList;

public class ManagerCasa1  extends Actor {
    private Proyecto juego;
    private Stage stage;
    private Hero heroe;
    private TiledMap mapa;
    private static BitmapFont font;
    public ManagerCasa1(Proyecto juego, Stage stage, TiledMap mapa, Hero heroe){
        this.juego = juego;
        this.stage = stage;
        this.mapa = mapa;
        this.heroe = heroe;
        stage.addActor(heroe);
        addListener(new ManagerCasa1.ManagerInputListener());

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //font.draw(batch, "Enemigos: " + juego.enemigosEliminados + "/15", 20, 460);
    }

    @Override
    public void act(float delta) {
        super.act(delta); // MUY IMPORTANTE
        if(juego.enemigosEliminados == 15){
            juego.setScreen(new PantallaFin(juego,stage,false));
        }
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
                    if(!heroe.ataca){
                        Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("sonido/espada.mp3"));
                        dropSound.play();
                        heroe.ataca = true;
                    }
                    break;
                case Input.Keys.E:
                    heroe.comprobarCofre();
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
