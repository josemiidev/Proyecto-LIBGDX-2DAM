package com.jmdev.Actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
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
import com.jmdev.PantallaCasa2;
import com.jmdev.PantallaFin;
import com.jmdev.Proyecto;

import java.util.ArrayList;

public class Manager extends Actor {
    private Proyecto juego;
    private Stage stage;
    private Hero heroe;
    private TiledMap mapa;
    private ArrayList<Mensaje> mensajes;
    private ArrayList<Area> areas;

    private Textos texto;
    private boolean inmortal;

    public Manager(Proyecto juego, Stage stage, TiledMap mapa, Hero heroe) {
        this.juego = juego;
        this.stage = stage;
        this.mapa = mapa;
        this.heroe = heroe;
        this.inmortal = false;
        stage.addActor(heroe);
        addListener(new ManagerInputListener());

        mensajes = new ArrayList<Mensaje>();
        areas = new ArrayList<Area>();
        cargaAreaMensajes();
        cargaAreas();

    }

    private void compruebaCofre(){
        for(Cofre c : juego.cofres){
            if (Intersector.overlaps(heroe.getShape(), c.getArea())) {
                if (!c.isAbierto()) {
                    c.setAbierto(true);
                    Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("sonido/abrir_cofre.mp3"));
                    dropSound.play();
                    if(juego.inventario == null){
                        juego.inventario = new Inventario();
                        //mensaje inventario
                    }else{
                        if(juego.inventario.getRuna() == null){
                            juego.inventario.setRuna(new Texture("objetos/runa.png"));
                            //mensaje coleccion
                        }else if(juego.inventario.getAntorcha() == null){
                            juego.inventario.setAntorcha(new Texture("objetos/antorcha.png"));
                        }else if(juego.inventario.getBaston() == null){
                            juego.inventario.setBaston(new Texture("objetos/baston.png"));
                        }else if(juego.inventario.getCalavera() == null){
                            juego.inventario.setCalavera(new Texture("objetos/calavera.png"));
                        }else if(juego.inventario.getCarbon() == null){
                            juego.inventario.setCarbon(new Texture("objetos/carbon.png"));
                        }else if(juego.inventario.getLlave() == null){
                            juego.inventario.setLlave(new Texture("objetos/llave.png"));
                        }else if(juego.inventario.getPocion() == null){
                            juego.inventario.setPocion(new Texture("objetos/pocion.png"));
                            //mensaje todos conseguidos
                        }
                    }
                }
            }
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
    }

    private boolean compruebaInventario() {
        boolean sw = false;
        if (juego.inventario != null) {
            sw = true;
            if (juego.inventario.getRuna() == null) {
                sw = false;
            }
            if (juego.inventario.getAntorcha() == null) {
                sw = false;
            }
            if (juego.inventario.getBaston() == null) {
                sw = false;
            }
            if (juego.inventario.getLlave() == null) {
                sw = false;
            }
            if (juego.inventario.getCarbon() == null) {
                sw = false;
            }
            if (juego.inventario.getPocion() == null) {
                sw = false;
            }
            if (juego.inventario.getCalavera() == null) {
                sw = false;
            }
        }
        return sw;
    }

    @Override
    public void act(float delta) {
        super.act(delta); // MUY IMPORTANTE
        if (juego.enemigosEliminados == 15 && compruebaInventario()) {
            juego.music.pause();
            juego.setScreen(new PantallaFin(juego, stage, false));
        }
        for (Mensaje men : mensajes) {
            if (Intersector.overlaps(heroe.getShape(), men.getArea())) {
                if (!men.isMostrado()) {
                    if (texto != null) {
                        texto.remove();
                        texto = null;
                    }
                    texto = new Textos(men.getTexto());
                    texto.setPosition(heroe.getX() + heroe.getWidth(), heroe.getY() + heroe.getHeight());
                    texto.toFront();
                    stage.addActor(texto);
                    men.setMostrado(true);
                }
            }
        }
        for (Area a : areas) {
            if (Intersector.overlaps(heroe.getShape(), a.getArea())) {
                switch (a.getNumCasa()) {
                    case 1:
                        juego.setScreen(new PantallaCasa1(juego));
                        break;
                    case 2:
                        juego.setScreen(new PantallaCasa2(juego,0));
                        break;
                    case 3:
                        juego.setScreen(new PantallaCasa2(juego,1));
                        break;
                    case 4:
                        juego.setScreen(new PantallaCasa2(juego,2));
                        break;
                }
            }
        }
        for (Mensaje men : mensajes) {
            if (Intersector.overlaps(heroe.getShape(), men.getArea())) {
                if (!men.isMostrado()) {
                    if (texto != null) {
                        texto.remove();
                        texto = null;
                    }
                    texto = new Textos(men.getTexto());
                    texto.setPosition(heroe.getX() + heroe.getWidth(), heroe.getY() + heroe.getHeight());
                    texto.toFront();
                    stage.addActor(texto);
                    men.setMostrado(true);
                }
            }
        }

        if (texto != null) {
            texto.setX(heroe.getX() + heroe.getWidth());
            texto.setY(heroe.getY() + heroe.getHeight());
        }
        for (Enemigo m : juego.enemigos) {
            if (m != null) {
                if (heroe.isAlive && m.isAlive && Intersector.overlaps(heroe.getShape(), m.getShape())) {
                    if (heroe.atacando) {
                        Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("sonido/muerte_enemigo.mp3"));
                        dropSound.play();
                        m.isAlive = false;
                        m.clearActions();
                        juego.enemigosEliminados++;
                    } else {
                        if (!inmortal) {
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
            if (m.completo) m.remove();
        }
        if (heroe.muerto) {
            juego.music.stop();
            juego.setScreen(new PantallaFin(juego, stage, true));
        }
    }

    private void cargaAreas() {
        MapLayer capaAreas = mapa.getLayers().get("objetos");
        MapObject objetoArea;
        Area area;

        objetoArea = capaAreas.getObjects().get("puerta_1");
        area = new Area(objetoArea.getProperties().get("x", Float.class),
                objetoArea.getProperties().get("y", Float.class),
                objetoArea.getProperties().get("width", Float.class),
                objetoArea.getProperties().get("height", Float.class),
                1);
        areas.add(area);

        objetoArea = capaAreas.getObjects().get("puerta_2");
        area = new Area(objetoArea.getProperties().get("x", Float.class),
                objetoArea.getProperties().get("y", Float.class),
                objetoArea.getProperties().get("width", Float.class),
                objetoArea.getProperties().get("height", Float.class),
                2);
        areas.add(area);

        objetoArea = capaAreas.getObjects().get("entrada_casa_2");
        area = new Area(objetoArea.getProperties().get("x", Float.class),
                objetoArea.getProperties().get("y", Float.class),
                objetoArea.getProperties().get("width", Float.class),
                objetoArea.getProperties().get("height", Float.class),
                3);
        areas.add(area);

        objetoArea = capaAreas.getObjects().get("entrada_casa_2_2");
        area = new Area(objetoArea.getProperties().get("x", Float.class),
                objetoArea.getProperties().get("y", Float.class),
                objetoArea.getProperties().get("width", Float.class),
                objetoArea.getProperties().get("height", Float.class),
                4);
        areas.add(area);
    }

    private void cargaAreaMensajes() {
        MapLayer capaMensajes = mapa.getLayers().get("objetos");
        MapObject objetoMensajes;
        Mensaje mensaje;

        objetoMensajes = capaMensajes.getObjects().get("decision_camino");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x", Float.class),
                objetoMensajes.getProperties().get("y", Float.class),
                objetoMensajes.getProperties().get("width", Float.class),
                objetoMensajes.getProperties().get("height", Float.class),
                "¿Que camino debo coger primero? \nTendremos que probar suerte...");
        mensajes.add(mensaje);

        objetoMensajes = capaMensajes.getObjects().get("cartel_casa_1");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x", Float.class),
                objetoMensajes.getProperties().get("y", Float.class),
                objetoMensajes.getProperties().get("width", Float.class),
                objetoMensajes.getProperties().get("height", Float.class),
                "'Casa de los Meintron... No pasar...' \nPero parece que no hay nadie...");
        mensajes.add(mensaje);
        objetoMensajes = capaMensajes.getObjects().get("cementerio_1");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x", Float.class),
                objetoMensajes.getProperties().get("y", Float.class),
                objetoMensajes.getProperties().get("width", Float.class),
                objetoMensajes.getProperties().get("height", Float.class),
                "¿Un cementerio al lado de casa? \n Vaya vistas...");
        mensajes.add(mensaje);
        objetoMensajes = capaMensajes.getObjects().get("laberinto_1");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x", Float.class),
                objetoMensajes.getProperties().get("y", Float.class),
                objetoMensajes.getProperties().get("width", Float.class),
                objetoMensajes.getProperties().get("height", Float.class),
                "El que plantó los arbustos así era \nun cachondo eh!");
        mensajes.add(mensaje);
        objetoMensajes = capaMensajes.getObjects().get("lapida_1");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x", Float.class),
                objetoMensajes.getProperties().get("y", Float.class),
                objetoMensajes.getProperties().get("width", Float.class),
                objetoMensajes.getProperties().get("height", Float.class),
                "¿Tanto arbusto para esto? \n¿Qué habrá en el cofre?");
        mensajes.add(mensaje);
        objetoMensajes = capaMensajes.getObjects().get("cartel_casa_2");
        mensaje = new Mensaje(objetoMensajes.getProperties().get("x", Float.class),
                objetoMensajes.getProperties().get("y", Float.class),
                objetoMensajes.getProperties().get("width", Float.class),
                objetoMensajes.getProperties().get("height", Float.class),
                "Se vende... \n¿Pero quien va a comparar esto?");
        mensajes.add(mensaje);
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
                    compruebaCofre();
                    break;
                case Input.Keys.ESCAPE:
                    if (texto != null) {
                        texto.remove();
                        texto = null;
                    }
                    break;
                case Input.Keys.P:
                    inmortal = true;
                    heroe.colisiones = true;
                    break;
                case Input.Keys.Q:

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
                    inmortal = false;
                    heroe.colisiones = false;
                    break;
            }
            return true;
        }
    }
}
