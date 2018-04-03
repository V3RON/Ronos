package pl.aitwar.ronos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pl.aitwar.ronos.entities.EntityManager;
import pl.aitwar.ronos.world.World;

public class GameUI {
    private World world;
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private OrthographicCamera camera;

    public GameUI(World w) {
        world = w;
        font = new BitmapFont();
        camera = RonosGame.camera;
        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(camera.combined);
    }

    public void draw() {
        spriteBatch.begin();
        font.draw(spriteBatch, "PLAYER 1", 10, camera.viewportHeight - 15);
        font.draw(spriteBatch, "HP: "+ world.getPlayer(0).getHealth(), 10, camera.viewportHeight - 30);
        font.draw(spriteBatch, "POINTS: "+ world.getPlayer(0).getPoints(), 10, camera.viewportHeight - 45);
        font.draw(spriteBatch, "AMMO: "+ world.getPlayer(0).getActiveWeapon().getAmmo()
                + "/"+world.getPlayer(0).getActiveWeapon().getMagAmmo(), 10, camera.viewportHeight - 60);
        if(world.getPlayer(0).getActiveWeapon().isReloading())
            font.draw(spriteBatch, "RELOADING", 10, camera.viewportHeight - 75);

        font.draw(spriteBatch, "PLAYER 2", camera.viewportWidth - 115, camera.viewportHeight - 15, 100f, 2, false);
        font.draw(spriteBatch, "HP: "+ world.getPlayer(1).getHealth(), camera.viewportWidth - 115, camera.viewportHeight - 30, 100f, 2, false);
        font.draw(spriteBatch, "POINTS: "+ world.getPlayer(1).getPoints(), camera.viewportWidth - 115, camera.viewportHeight - 45, 100f, 2, false);
        font.draw(spriteBatch, "AMMO: "+ world.getPlayer(1).getActiveWeapon().getAmmo()
                + "/"+world.getPlayer(1).getActiveWeapon().getMagAmmo(), camera.viewportWidth - 115, camera.viewportHeight - 60, 100f, 2, false);
        if(world.getPlayer(1).getActiveWeapon().isReloading())
            font.draw(spriteBatch, "RELOADING", camera.viewportWidth - 115, camera.viewportHeight - 75, 100f, 2, false);
        spriteBatch.end();
    }
}
