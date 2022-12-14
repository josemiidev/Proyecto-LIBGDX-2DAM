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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jmdev.JuegoTower;
import com.jmdev.Objetos.Area;
import com.jmdev.Objetos.Mensaje;
import com.jmdev.PantallaFin;
import com.jmdev.Proyecto;

public class ManagerCasa extends Actor {
    private final Proyecto juego;
    private final Stage stage;
    private final Hero heroe;
    private final TiledMap mapa;
    Area salida;
    Area salida_patio;
    Area salida_patio_2;

    public ManagerCasa(Proyecto juego, Stage stage, TiledMap mapa, Hero heroe) {
        this.juego = juego;
        this.stage = stage;
        this.mapa = mapa;
        this.heroe = heroe;
        stage.addActor(heroe);
        cargaAreas();
        addListener(new ManagerCasa.ManagerInputListener());

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

    private void compruebaCofre(){
        for(Cofre c : juego.cofres){
            if (Intersector.overlaps(heroe.getShape(), c.getArea())) {
                if (!c.isAbierto()) {
                    c.setAbierto(true);
                    Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("sonido/abrir_cofre.mp3"));
                    dropSound.play();

                    if(juego.inventario == null){
                        juego.inventario = new Inventario();
                        juego.mensajes.get(6).setTexto("Has encontrado una mochila, llenala y completa la mision.");
                        juego.mensajes.get(6).setActivo(true);
                    }else{
                        if(juego.inventario.getRuna() == null){
                            juego.inventario.setRuna(new Texture("objetos/runa.png"));
                            juego.mensajes.get(6).setTexto("Has encontrado una runa.");
                            juego.mensajes.get(6).setActivo(true);
                        }else if(juego.inventario.getAntorcha() == null){
                            juego.inventario.setAntorcha(new Texture("objetos/antorcha.png"));
                            juego.mensajes.get(6).setTexto("Has encontrado una antorcha.");
                            juego.mensajes.get(6).setActivo(true);
                        }else if(juego.inventario.getBaston() == null){
                            juego.inventario.setBaston(new Texture("objetos/baston.png"));
                            juego.mensajes.get(6).setTexto("Has encontrado un baston.");
                            juego.mensajes.get(6).setActivo(true);
                        }else if(juego.inventario.getCalavera() == null){
                            juego.inventario.setCalavera(new Texture("objetos/calavera.png"));
                            juego.mensajes.get(6).setTexto("Has encontrado una calavera.");
                            juego.mensajes.get(6).setActivo(true);
                        }else if(juego.inventario.getCarbon() == null){
                            juego.inventario.setCarbon(new Texture("objetos/carbon.png"));
                            juego.mensajes.get(6).setTexto("Has encontrado unpedazo de carbon.");
                            juego.mensajes.get(6).setActivo(true);
                        }else if(juego.inventario.getLlave() == null){
                            juego.inventario.setLlave(new Texture("objetos/llave.png"));
                            juego.mensajes.get(6).setTexto("Has encontrado una llave.");
                            juego.mensajes.get(6).setActivo(true);
                        }else if(juego.inventario.getPocion() == null){
                            juego.inventario.setPocion(new Texture("objetos/pocion.png"));
                            juego.mensajes.get(6).setTexto("Has encontrado una pocion.");
                            juego.mensajes.get(6).setActivo(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {}

    @Override
    public void act(float delta) {
        super.act(delta); // MUY IMPORTANTE
        if (juego.enemigosEliminados == 15 && compruebaInventario()) {
            juego.music.pause();
            juego.setScreen(new PantallaFin(juego, stage, false));
        }

        if (Intersector.overlaps(heroe.getShape(), salida.getArea())) {
            juego.setScreen(new JuegoTower(juego, 2));
        }
        if(salida_patio!= null){
            if (Intersector.overlaps(heroe.getShape(), salida_patio.getArea())) {
                juego.ultimaCasa = 3;
                juego.setScreen(new JuegoTower(juego, 2));
            }
        }
        if(salida_patio_2 != null){
            if (Intersector.overlaps(heroe.getShape(), salida_patio_2.getArea())) {
                juego.ultimaCasa = 4;
                juego.setScreen(new JuegoTower(juego, 2));
            }
        }
    }

    private void cargaAreas() {
        MapLayer capaAreas = mapa.getLayers().get("objetos");
        MapObject objetoArea;


        objetoArea = capaAreas.getObjects().get("salida");
        salida = new Area(objetoArea.getProperties().get("x", Float.class),
                objetoArea.getProperties().get("y", Float.class),
                objetoArea.getProperties().get("width", Float.class),
                objetoArea.getProperties().get("height", Float.class),
                1);
        objetoArea = capaAreas.getObjects().get("salida_patio");
        if (objetoArea != null) {
            salida_patio = new Area(objetoArea.getProperties().get("x", Float.class),
                    objetoArea.getProperties().get("y", Float.class),
                    objetoArea.getProperties().get("width", Float.class),
                    objetoArea.getProperties().get("height", Float.class),
                    1);
        }
        objetoArea = capaAreas.getObjects().get("salida_patio_2");
        if (objetoArea != null) {
            salida_patio_2 = new Area(objetoArea.getProperties().get("x", Float.class),
                    objetoArea.getProperties().get("y", Float.class),
                    objetoArea.getProperties().get("width", Float.class),
                    objetoArea.getProperties().get("height", Float.class),
                    1);
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
                    if (!heroe.ataca) {
                        Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("sonido/espada.mp3"));
                        dropSound.play();
                        heroe.ataca = true;
                    }
                    break;
                case Input.Keys.E:
                    compruebaCofre();
                case Input.Keys.P:
                    heroe.colisiones = true;
                    break;
                case Input.Keys.ENTER:
                    for(Mensaje men : juego.mensajes){
                        if(men.isActivo()){
                            men.setActivo(false);
                        }
                    }
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
                    heroe.colisiones = false;
                    break;
            }
            return true;
        }
    }
}
