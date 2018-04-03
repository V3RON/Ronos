package pl.aitwar.ronos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.viewport.FitViewport;
import pl.aitwar.ronos.RonosGame;

public class LoadingScreen implements Screen {
    private Stage stage;
    private ProgressBar progressBar;
    private RonosGame game;

    public LoadingScreen(RonosGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        progressBar = new ProgressBar(0, 1, 0.1f, false,
                (Skin) RonosGame.assetManager.get("ui/uiskin.json"));
        Label label = new Label("Loading...", (Skin) RonosGame.assetManager.get("ui/uiskin.json"));
        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.setOrigin(verticalGroup.getWidth()/2f,
                verticalGroup.getHeight()/2f);
        verticalGroup.setPosition(Gdx.graphics.getWidth()/2f - (verticalGroup.getWidth()/2f),
                Gdx.graphics.getHeight()/2f - (verticalGroup.getHeight()/2f));
        verticalGroup.addActor(label);
        verticalGroup.addActor(progressBar);
        stage.addActor(verticalGroup);
        stage.setDebugAll(true);

        RonosGame.assetManager.load("player/player.atlas", TextureAtlas.class);
        RonosGame.assetManager.load("maps/wtf/level.png", Texture.class);
        RonosGame.assetManager.load("maps/wtf/material.png", Texture.class);
        RonosGame.assetManager.load("sounds/weapons/mp4/shot.wav", Sound.class);
        RonosGame.assetManager.load("sounds/weapons/mp4/reload.wav", Sound.class);
        RonosGame.assetManager.load("sounds/death1.ogg", Sound.class);
        RonosGame.assetManager.load("sounds/weapons/exp4.ogg", Sound.class);
        RonosGame.assetManager.load("sounds/weapons/bazooka/bazooka.ogg", Sound.class);
        RonosGame.assetManager.load("sounds/weapons/exp2.ogg", Sound.class);
    }

    @Override
    public void render(float delta) {
        if(RonosGame.assetManager.update()) {
            game.setScreen(new GameScreen(game));
            return;
        }
        progressBar.setValue(RonosGame.assetManager.getProgress());

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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
        stage.dispose();
    }
}
