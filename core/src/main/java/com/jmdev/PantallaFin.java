package com.jmdev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

/**
 * Pantalla Mostrada al final del juego, ya sea si has muerto o has ganado
 */
public class PantallaFin extends ScreenAdapter {
    Proyecto game;
    Stage stage;
    boolean muerto;

    /**
     * Constructor de la pantalla final
     *
     * @param game   Clase Proyecto en la que almacenamos informacion
     * @param stage  El Stage actual
     * @param muerto Valor que nos indica como entramos a la pantalla, por muerte o por juego ganado
     */
    public PantallaFin(Proyecto game, Stage stage, boolean muerto) {
        this.game = game;
        this.stage = stage;
        this.muerto = muerto;
    }

    @Override
    public void show() {
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

    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
        crearMenu(width, height);
    }

    /**
     * Función que crea los componentes de la pantalla final
     *
     * @param width  Ancho de la pantalla
     * @param height Alto de la pantalla
     */
    private void crearMenu(float width, float height) {
        stage.clear();

        Label title = new Label("¡Has Ganado!", game.gameSkin, "title");

        title.setAlignment(Align.center);
        title.setY(height - title.getHeight() - 20);
        title.setWidth(width);
        stage.addActor(title);

        Label autor = new Label("Create By: Jose Miguel Lorenzo Lara", game.gameSkin, "default");
        autor.setAlignment(Align.center);
        autor.setY(autor.getHeight());
        autor.setWidth(width);
        stage.addActor(autor);

        TextButton salir = new TextButton("Salir", game.gameSkin);
        salir.setWidth(width / 2);
        salir.setPosition(
                width / 2 - (salir.getWidth() / 2),
                autor.getHeight() + salir.getHeight() + 10
        );
        salir.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(salir);

        TextButton nueva = new TextButton("Nueva Partida", game.gameSkin);
        nueva.setWidth(width / 2);
        nueva.setPosition(
                width / 2 - (nueva.getWidth() / 2),
                salir.getY() + salir.getHeight() + 10
        );
        nueva.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.vidas = 2;
                game.enemigosEliminados = 0;
                game.enemigos = null;
                game.ultimaCasa = 0;
                game.setScreen(new JuegoTower(game, 0));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(nueva);

        TextButton reintentar = new TextButton("Reintentar", game.gameSkin);
        reintentar.setWidth(width / 2);
        reintentar.setPosition(
                width / 2 - (reintentar.getWidth() / 2),
                nueva.getY() + nueva.getHeight() + 10
        );
        reintentar.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.vidas--;
                game.ultimaCasa = 0;
                game.setScreen(new JuegoTower(game, 1));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(reintentar);
        if (muerto) {
            if (game.vidas > 0) {
                title.setText("¡Has Muerto!");
            } else {
                title.setText("¡Has Perdido!");
                reintentar.setVisible(false);
            }

            title.setColor(Color.RED);
            autor.setColor(Color.RED);
            salir.setColor(Color.RED);
            nueva.setColor(Color.RED);
            reintentar.setColor(Color.RED);
        } else {
            reintentar.setVisible(false);
        }
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        game.dispose();
    }
}
