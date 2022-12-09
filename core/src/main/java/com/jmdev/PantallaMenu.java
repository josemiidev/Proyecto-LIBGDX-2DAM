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

/**
 * Pantalla mostrada al inicio de la aplicación, como menú, para entrar a jugar
 */
public class PantallaMenu extends ScreenAdapter {
    private final Proyecto juego;
    private final Stage stage;

    /**
     * Constructor de la Pantalla del Menu
     *
     * @param juego Objeto de la clase proyecto que almacena información de la partida
     */
    public PantallaMenu(Proyecto juego) {
        this.juego = juego;
        stage = new Stage(new ScreenViewport());
        crearMenu(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        juego.dispose();
    }

    /**
     * Función que crea los componentes de la pantalla final
     *
     * @param width  Ancho de la pantalla
     * @param height Alto de la pantalla
     */
    private void crearMenu(float width, float height) {
        stage.clear();

        TextButton newGame = new TextButton("Nueva Partida", juego.gameSkin);
        newGame.setWidth(width / 2);
        newGame.setPosition(
                width / 2 - (newGame.getWidth() / 2),
                height / 2 + 10
        );
        newGame.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //dispose();
                juego.vidas = 2;
                juego.enemigosEliminados = 0;
                juego.setScreen(new JuegoTower(juego, 0));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(newGame);

        TextButton exitGame = new TextButton("Salir", juego.gameSkin);
        exitGame.setWidth(width / 2);
        exitGame.setPosition(
                width / 2 - (newGame.getWidth() / 2),
                newGame.getY() - exitGame.getHeight() - 10
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
        Label title = new Label("Tower Adventure", juego.gameSkin, "title");
        title.setAlignment(Align.center);
        title.setY(height / 2 + newGame.getHeight() + 25);
        title.setWidth(width);
        stage.addActor(title);

        Label autor = new Label("Create By: Jose Miguel Lorenzo Lara", juego.gameSkin, "default");
        autor.setAlignment(Align.center);
        autor.setY(autor.getHeight());
        autor.setWidth(width);
        stage.addActor(autor);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
        crearMenu(width, height);
    }
}
