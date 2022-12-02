package com.jmdev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class PantallaFin extends ScreenAdapter {
    Proyecto game;
    Stage stage;
    boolean muerto;

    public PantallaFin(Proyecto game, Stage stage, boolean muerto){
        this.game = game;
        this.stage = stage;
        this.muerto = muerto;
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.ENTER) {
                    game.setScreen(new Menu(game));
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        if(muerto){
            Gdx.gl.glClearColor(1f, 0.2f, 0.2f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }else{
            Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width,height,true);
        crearPantalla(width,height);
    }
    private void crearPantalla(float width, float height){
        stage.clear();

        TextButton loadGame = new TextButton("Nueva Partida", game.gameSkin);
        loadGame.setWidth(width / 2);
        loadGame.setPosition(
                width / 2 - (loadGame.getWidth() / 2),
                (height / 2 - loadGame.getHeight())
        );
        loadGame.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //juego.setScreen(new PantallaJuego(juego));
                game.setScreen(new JuegoTower(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(loadGame);

        TextButton newGame = new TextButton("Reintentar", game.gameSkin);
        newGame.setWidth(width/ 2);
        newGame.setPosition(
                width / 2 - (newGame.getWidth() / 2),
                height/ 2 + 10
        );
        newGame.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new JuegoTower(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(newGame);

        TextButton exitGame = new TextButton("Salir", game.gameSkin);
        exitGame.setWidth(width/2);
        exitGame.setPosition(
                width / 2 - (newGame.getWidth() / 2),
                height / 2 - (newGame.getHeight() + exitGame.getHeight() +10)
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

        Label title = new Label("¡Has Ganado!", game.gameSkin, "title");


        title.setAlignment(Align.center);
        title.setY(height / 2 + newGame.getHeight() + 25);
        title.setWidth(width);
        stage.addActor(title);

        Label autor = new Label("Create By: Jose Miguel Lorenzo Lara", game.gameSkin, "default");
        autor.setAlignment(Align.center);
        autor.setY( autor.getHeight());
        autor.setWidth(width);
        stage.addActor(autor);
        Gdx.input.setInputProcessor(stage);

        if(muerto){
            title.setText("¡Has Muerto!");
            title.setColor(Color.RED);
            autor.setColor(Color.RED);
            exitGame.setColor(Color.RED);
        }
    }
}
