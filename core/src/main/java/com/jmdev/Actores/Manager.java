package com.jmdev.Actores;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jmdev.Proyecto;

import java.util.ArrayList;

public class Manager extends Actor {
    private Proyecto juego;
    private Stage stage;
    private Hero heroe;
    private ArrayList<Enemigo> enemigos;
    private static BitmapFont font;
    public Manager(Proyecto juego, Stage stage, Hero heroe){
        this.juego = juego;
        this.stage = stage;
        this.heroe = heroe;

        stage.addActor(heroe);
        addListener(new ManagerInputListener());
        Enemigo en = new Enemigo(1500,1500);
        enemigos = new ArrayList<Enemigo>();
        enemigos.add(en);

        stage.addActor(en);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        //font.draw(batch, "Time: " + juego.tiempo, 20, 460);
    }

    @Override
    public void act(float delta) {
        super.act(delta); // MUY IMPORTANTE
        for (Enemigo m : enemigos) {
            if(m != null){
                if (heroe.isAlive && m.isAlive && Intersector.overlaps(heroe.getShape(), m.getShape())) {
                    if(heroe.atacando){
                        m.isAlive = false;
                        m.clearActions();
                    }else{
                        heroe.isAlive = false;
                        heroe.clearActions();
                    }
                }
            }
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
                    if(heroe.finAnimacion){
                        heroe.atacando = true;
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
            }
            return true;
        }
    }
}
