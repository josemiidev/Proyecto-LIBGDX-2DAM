package com.jmdev.Actores;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.jmdev.JuegoTower;
import com.jmdev.PantallaFin;
import com.jmdev.Proyecto;

import java.util.ArrayList;

public class Manager extends Actor {
    private Proyecto juego;
    private Stage stage;
    private Hero heroe;
    private TiledMap mapa;
    private ArrayList<Enemigo> enemigos;
    private static BitmapFont font;
    Label puntuacion;
    public Manager(Proyecto juego, Stage stage, TiledMap mapa, Hero heroe){
        this.juego = juego;
        this.stage = stage;
        this.mapa = mapa;
        this.heroe = heroe;
        if (font == null) {
            // Cargar fuente solamente si es la primera vez
            font = new BitmapFont();
            font.setColor(Color.RED);
        }
        stage.addActor(heroe);
        addListener(new ManagerInputListener());
        enemigos = new ArrayList<Enemigo>();
        for(int i = 1; i<=15; i++){
            Enemigo en = new Enemigo(1500,1500);
            Vector2 spawn = getSpawnEnemigo(i);
            en.setPosition(spawn.x,spawn.y);
            enemigos.add(en);
            CrearAnimacionEnemigo(en,i);
            stage.addActor(en);
        }
        puntuacion = new Label("Puntuación: ", juego.gameSkin, "default");
        puntuacion.setX(20);
        puntuacion.setY(460);
        puntuacion.toFront();
        stage.addActor(puntuacion);
    }
    private void CrearAnimacionEnemigo(Enemigo e, int i){
        MoveToAction movimiento;
        SequenceAction secuencia;
        RepeatAction repeticion;
        switch(i){
            case 1:
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() + 200, e.getY());
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX() - 200,e.getY());
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
                movimiento.setPosition(e.getX(),e.getY());
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
                movimiento.setPosition(e.getX()+600,e.getY()-550);
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(),e.getY()-550);
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(),e.getY());
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                repeticion = new RepeatAction();
                repeticion.setCount(RepeatAction.FOREVER);
                repeticion.setAction(secuencia);
                e.addAction(repeticion);
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                break;
            case 15:
                break;
        }
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        //font.draw(batch, "Enemigos: " + juego.enemigosEliminados + "/15", 20, 460);
    }

    @Override
    public void act(float delta) {
        super.act(delta); // MUY IMPORTANTE
        puntuacion.setText(juego.enemigosEliminados);
        for (Enemigo m : enemigos) {
            if(m != null){
                if (heroe.isAlive && m.isAlive && Intersector.overlaps(heroe.getShape(), m.getShape())) {
                    if(heroe.atacando){
                        m.isAlive = false;
                        m.clearActions();
                        juego.enemigosEliminados++;
                    }else{
                        heroe.isAlive = false;
                        heroe.clearActions();
                    }
                }
            }
            if(m.completo) m.remove();
        }
        if(heroe.muerto){
            juego.setScreen(new PantallaFin(juego,stage));
        }
    }
    private Vector2 getSpawnEnemigo(int i) {
        MapLayer positionLayer = mapa.getLayers().get("enemigos");
        MapObject playerSpawn = positionLayer.getObjects().get("enemigo"+i);
        return new Vector2(playerSpawn.getProperties().get("x", Float.class), playerSpawn.getProperties().get("y", Float.class));
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
                    //if(heroe.finAnimacion){
                        heroe.atacando = true;
                    //}
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
