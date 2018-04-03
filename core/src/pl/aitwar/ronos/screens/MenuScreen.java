package pl.aitwar.ronos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pl.aitwar.ronos.RonosGame;

public class MenuScreen implements Screen {
    private Stage stage;
    private RonosGame game;
    private SpriteBatch spriteBatch;

    private Texture background;

    public MenuScreen(RonosGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(RonosGame.camera.combined);
        background = RonosGame.assetManager.get("ui/menubackground.png");

        stage = new Stage(new StretchViewport(640, 280, RonosGame.camera), spriteBatch);
        Table verticalGroup = new Table();
        verticalGroup.setPosition(RonosGame.camera.viewportWidth - 75,
                75);
        Label gameName = new Label("Ronos", (Skin) RonosGame.assetManager.get("ui/uiskin.json"));
        TextButton startGame = new TextButton("Start game!", (Skin) RonosGame.assetManager.get("ui/uiskin.json"));
        startGame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoadingScreen(game));
            }
        });
        TextButton settingsButton = new TextButton("Settings", (Skin) RonosGame.assetManager.get("ui/uiskin.json"));
        TextButton exitButton = new TextButton("Quit", (Skin) RonosGame.assetManager.get("ui/uiskin.json"));
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        verticalGroup.add(gameName);
        verticalGroup.row();
        verticalGroup.add(startGame).fill().pad(5);
        verticalGroup.row();
        verticalGroup.add(settingsButton).fill().pad(5);
        verticalGroup.row();
        verticalGroup.add(exitButton).fill().pad(5);
        stage.addActor(verticalGroup);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(229, 128, 46, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, RonosGame.camera.viewportWidth, RonosGame.camera.viewportHeight);
        spriteBatch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        background.dispose();
    }
}
