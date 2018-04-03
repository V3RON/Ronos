package pl.aitwar.ronos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import pl.aitwar.ronos.GameUI;
import pl.aitwar.ronos.RonosGame;
import pl.aitwar.ronos.world.World;

public class GameScreen implements Screen {
    private RonosGame game;
    private World world;
    private GameUI gameUI;

    public GameScreen(RonosGame game) {
        this.game = game;
        world = new World(RonosGame.assetManager.get("maps/wtf/level.png"), RonosGame.assetManager.get("maps/wtf/material.png"));
        gameUI = new GameUI(world);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        world.draw(delta);
        gameUI.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
