package pl.aitwar.ronos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.slf4j.Logger;
import pl.aitwar.ronos.screens.MenuScreen;

public class RonosGame extends Game {
	public static final AssetManager assetManager = new AssetManager();
	public static OrthographicCamera camera;

	public static BitmapFont font;
	private SpriteBatch batch;

	@Override
	public void create () {
		assetManager.load("ui/uiskin.json", Skin.class);
		assetManager.load("ui/menubackground.png", Texture.class);
		assetManager.finishLoading();
		batch = new SpriteBatch();
		font = new BitmapFont();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 380);
		batch.setProjectionMatrix(camera.combined);
		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor( 1, 0, 0, 1 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
		super.render();
		batch.begin();
		font.draw(batch, "FPS: "+Gdx.graphics.getFramesPerSecond(), 15, 30);
		batch.end();
	}
	
	@Override
	public void dispose () {

	}
}
