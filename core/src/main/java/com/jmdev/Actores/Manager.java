package com.jmdev.Actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
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
import com.jmdev.Objetos.Area;
import com.jmdev.Objetos.Mensaje;
import com.jmdev.PantallaCasa1;
import com.jmdev.PantallaFin;
import com.jmdev.Proyecto;

import java.util.ArrayList;

public class Manager extends Actor {
    private Proyecto juego;
    private Stage stage;
    private Hero heroe;
    private TiledMap mapa;
    private ArrayList<Enemigo> enemigos;
    private ArrayList<Mensaje> mensajes;
    private ArrayList<Area> areas;
    private Textos texto;
    private boolean inmortal;
    public Manager(Proyecto juego, Stage stage, TiledMap mapa, Hero heroe){
        this.juego = juego;
        this.stage = stage;
        this.mapa = mapa;
        this.heroe = heroe;
        this.inmortal = false;
        stage.addActor(heroe);
        addListener(new ManagerInputListener());
        enemigos = new ArrayList<Enemigo>();
        mensajes = new ArrayList<Mensaje>();
        areas = new ArrayList<Area>();
        cargaAreaMensajes();
        cargaAreas();
        for(int i = 1; i<=15; i++){
            Enemigo en = new Enemigo(1500,1500);
            Vector2 spawn = getSpawnEnemigo(i);
            en.setPosition(spawn.x,spawn.y);
            enemigos.add(en);
            CrearAnimacionEnemigo(en,i);
            stage.addActor(en);
        }
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
                secuencia = new SequenceAction();
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()+1000, e.getY());
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()+800, e.getY() + 400);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()+460, e.getY() + 400 );
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
                movimiento.setPosition(e.getX()+700, e.getY());
                movimiento.setDuration(4f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()+700, e.getY() + 300);
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
                movimiento.setPosition(e.getX()-200, e.getY()+200);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()+200, e.getY() +200);
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
                movimiento.setPosition(e.getX()+200, e.getY());
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
                movimiento.setPosition(e.getX()-200, e.getY()-200);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY()-300);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()-200, e.getY()-500);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY()-700);
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
                movimiento.setPosition(e.getX()+1000, e.getY());
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
                movimiento.setPosition(e.getX()+175, e.getY()-200);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY()-300);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()+175, e.getY()-600);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY()-800);
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
                movimiento.setPosition(e.getX()+200, e.getY()+200);
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()+200, e.getY()+400);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()-200, e.getY()+100);
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
                movimiento.setPosition(e.getX()-100, e.getY());
                movimiento.setDuration(1f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()-100, e.getY()+460);
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()+300, e.getY()+460);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()-100, e.getY()+460);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()-100, e.getY());
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
                movimiento.setPosition(e.getX()+900, e.getY());
                movimiento.setDuration(4.5f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()+900, e.getY()+300);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()+440, e.getY()+300);
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()+200, e.getY());
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
                movimiento.setPosition(e.getX()-200, e.getY()-100);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX(), e.getY()-300);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()-150, e.getY()-400);
                movimiento.setDuration(2f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()-100, e.getY());
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
                movimiento.setPosition(e.getX()+250, e.getY()-300);
                movimiento.setDuration(3f);
                secuencia.addAction(movimiento);
                movimiento = new MoveToAction();
                movimiento.setPosition(e.getX()-200, e.getY() -300);
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
    @Override
    public void draw(Batch batch, float parentAlpha) {}

    @Override
    public void act(float delta) {
        super.act(delta); // MUY IMPORTANTE
        if(juego.enemigosEliminados == 15){
            juego.setScreen(new PantallaFin(juego,stage,false));
        }
        for(Mensaje men :mensajes){
            if(Intersector.overlaps(heroe.getShape(), men.getArea())){
                if(!men.isMostrado()){
                    if(texto!=null){
                        texto.remove();
                        texto = null;
                    }
                    texto = new Textos(men.getTexto());
                    texto.setPosition(heroe.getX() + heroe.getWidth(),heroe.getY()+heroe.getHeight());
                    texto.toFront();
                    stage.addActor(texto);
                    men.setMostrado(true);
                }
            }
        }
        for(Area a :areas){
            if(Intersector.overlaps(heroe.getShape(), a.getArea())){
                switch (a.getNumCasa()){
                    case 1:
                        juego.setScreen(new PantallaCasa1(juego,heroe));
                        break;
                    case 2:
                        break;
                }
            }
        }

        for(Mensaje men :mensajes){
            if(Intersector.overlaps(heroe.getShape(), men.getArea())){
                if(!men.isMostrado()){
                    if(texto!=null){
                        texto.remove();
                        texto = null;
                    }
                    texto = new Textos(men.getTexto());
                    texto.setPosition(heroe.getX() + heroe.getWidth(),heroe.getY()+heroe.getHeight());
                    texto.toFront();
                    stage.addActor(texto);
                    men.setMostrado(true);
                }
            }
        }

        if(texto!=null){
            texto.setX(heroe.getX() + heroe.getWidth());
            texto.setY(heroe.getY()+heroe.getHeight());
        }
        for (Enemigo m : enemigos) {
            if(m != null){
                if (heroe.isAlive && m.isAlive && Intersector.overlaps(heroe.getShape(), m.getShape())) {
                    if(heroe.atacando){
                        Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("sonido/muerte_enemigo.mp3"));
                        dropSound.play();
                        m.isAlive = false;
                        m.clearActions();
                        juego.enemigosEliminados++;
                    }else{
                        if(!inmortal){
                            Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("sonido/muerte_heroe.mp3"));
                            dropSound.play();
                            heroe.isAlive = false;
                            heroe.horizontalMovement = Hero.HorizontalMovement.NONE;
                            heroe.verticalMovement = Hero.VerticalMovement.NONE;
                            Gdx.input.setInputProcessor(null);
                            stage.setKeyboardFocus(null);
                        }
                    }
                }
            }
            if(m.completo) m.remove();
        }
        if(heroe.muerto){
            juego.music.dispose();
            juego.setScreen(new PantallaFin(juego,stage,true));
        }
    }
    private void cargaAreas(){
        MapLayer capaAreas = mapa.getLayers().get("objetos");
        MapObject objetoArea;
        Area area;

        objetoArea = capaAreas.getObjects().get("puerta_1");
        area = new Area(objetoArea.getProperties().get("x",Float.class),
                objetoArea.getProperties().get("y",Float.class),
                objetoArea.getProperties().get("width",Float.class),
                objetoArea.getProperties().get("height",Float.class),
                1);
        areas.add(area);

        objetoArea = capaAreas.getObjects().get("puerta_2");
        area = new Area(objetoArea.getProperties().get("x",Float.class),
                objetoArea.getProperties().get("y",Float.class),
                objetoArea.getProperties().get("width",Float.class),
                objetoArea.getProperties().get("height",Float.class),
                2);
        areas.add(area);
    }
    private void cargaAreaMensajes(){
        MapLayer capaMensajes = mapa.getLayers().get("objetos");
        MapObject objetoMensajes;
        Mensaje mensaje;

        objetoMensajes = capaMensajes.getObjects().get("decision_camino");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x",Float.class),
                objetoMensajes.getProperties().get("y",Float.class),
                objetoMensajes.getProperties().get("width",Float.class),
                objetoMensajes.getProperties().get("height",Float.class),
                "¿Que camino debo coger primero? \nTendremos que probar suerte...");
        mensajes.add(mensaje);

        objetoMensajes= capaMensajes.getObjects().get("cartel_casa_1");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x",Float.class),
                objetoMensajes.getProperties().get("y",Float.class),
                objetoMensajes.getProperties().get("width",Float.class),
                objetoMensajes.getProperties().get("height",Float.class),
                "'Casa de los Meintron... No pasar...' \nParece que no hay nadie...");
        mensajes.add(mensaje);
        objetoMensajes= capaMensajes.getObjects().get("cementerio_1");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x",Float.class),
                objetoMensajes.getProperties().get("y",Float.class),
                objetoMensajes.getProperties().get("width",Float.class),
                objetoMensajes.getProperties().get("height",Float.class),
                "¿Un cementerio al lado de casa? ¿Serán familiares?\n Anda que asomarte por la ventana y ver el jardín lleno de tumbas...");
        mensajes.add(mensaje);
        objetoMensajes= capaMensajes.getObjects().get("laberinto_1");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x",Float.class),
                objetoMensajes.getProperties().get("y",Float.class),
                objetoMensajes.getProperties().get("width",Float.class),
                objetoMensajes.getProperties().get("height",Float.class),
                "El que plantó los arbustos así era \nun cachondo eh!");
        mensajes.add(mensaje);
        objetoMensajes= capaMensajes.getObjects().get("lapida_1");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x",Float.class),
                objetoMensajes.getProperties().get("y",Float.class),
                objetoMensajes.getProperties().get("width",Float.class),
                objetoMensajes.getProperties().get("height",Float.class),
                "¿Tanto arbusto para esto? ¿Qué habrá en el cofre?");
        mensajes.add(mensaje);
        objetoMensajes= capaMensajes.getObjects().get("cartel_casa_2");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x",Float.class),
                objetoMensajes.getProperties().get("y",Float.class),
                objetoMensajes.getProperties().get("width",Float.class),
                objetoMensajes.getProperties().get("height",Float.class),
                "Se vende... \n¿Pero quien va a comparar esto?");
        mensajes.add(mensaje);
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
                    if(!heroe.ataca){
                        Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("sonido/espada.mp3"));
                        dropSound.play();
                        heroe.ataca = true;
                    }
                    break;
                case Input.Keys.E:
                    heroe.comprobarCofre();
                    break;
                case Input.Keys.ESCAPE:
                    if(texto!=null){
                        texto.remove();
                        texto = null;
                    }
                    break;
                case Input.Keys.P:
                    inmortal  = true;
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
                case Input.Keys.P:
                    inmortal  = false;
                    break;
            }
            return true;
        }
    }
}
