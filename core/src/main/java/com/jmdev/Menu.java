package com.jmdev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Menu  extends ScreenAdapter {
    private Proyecto juego;
    private Stage stage;

    public Menu(Proyecto juego) {
        this.juego = juego;
        stage = new Stage(new ScreenViewport());
        Label title = new Label("Tower Adventure", juego.gameSkin, "title");
        title.setAlignment(Align.center);
        title.setY(Gdx.graphics.getHeight() * 2 / 3);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);
        crearMenu();
        Label autor = new Label("Create By: José Miguel Lorenzo Lara", juego.gameSkin, "default");
        autor.setAlignment(Align.center);
        autor.setY( autor.getHeight());
        autor.setWidth(Gdx.graphics.getWidth());
        stage.addActor(autor);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void crearMenu(){
        TextButton newGame = new TextButton("Nueva Partida", juego.gameSkin);
        newGame.setWidth(Gdx.graphics.getWidth() / 2);
        newGame.setPosition(
                Gdx.graphics.getWidth() / 2 - (newGame.getWidth() / 2),
                250
        );
        newGame.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                juego.tiempo = 300000;
                juego.setScreen(new JuegoTower(juego));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(newGame);

        TextButton loadGame = new TextButton("Cargar Partida", juego.gameSkin);
        loadGame.setWidth(Gdx.graphics.getWidth() / 2);
        loadGame.setPosition(
                Gdx.graphics.getWidth() / 2 - (newGame.getWidth() / 2),
                (250 - newGame.getHeight())- 10
        );
        loadGame.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //juego.setScreen(new PantallaJuego(juego));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(loadGame);

        TextButton exitGame = new TextButton("Salir", juego.gameSkin);
        exitGame.setWidth(Gdx.graphics.getWidth()/2);
        exitGame.setPosition(
                Gdx.graphics.getWidth() / 2 - (newGame.getWidth() / 2),
                loadGame.getY() - exitGame.getHeight() - 10
        );
        exitGame.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(exitGame);
    }
}
