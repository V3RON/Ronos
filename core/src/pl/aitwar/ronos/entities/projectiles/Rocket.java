package pl.aitwar.ronos.entities.projectiles;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import pl.aitwar.ronos.RonosGame;
import pl.aitwar.ronos.entities.Entity;
import pl.aitwar.ronos.entities.EntityManager;
import pl.aitwar.ronos.entities.Explosion;
import pl.aitwar.ronos.entities.Player;

public class Rocket extends Projectile {
    private TextureRegion t;
    private float damage;

    public Rocket(Entity owner, Vector2 position, Vector2 acceleration) {
        super(owner, position, acceleration);

        Pixmap map = new Pixmap(6, 3, Pixmap.Format.RGBA8888);
        map.setColor(Color.argb8888(1, 148, 148, 148));
        map.drawLine(0, 1, 4, 1);
        map.drawPixel(0, 0, Color.argb8888(1, 116, 116, 116));
        map.drawPixel(2, 0, Color.argb8888(1, 116, 116, 116));
        map.drawPixel(3, 0, Color.argb8888(1, 116, 116, 116));
        map.drawPixel(0, 2, Color.argb8888(1, 116, 116, 116));
        map.drawPixel(2, 2, Color.argb8888(1, 116, 116, 116));
        map.drawPixel(3, 2, Color.argb8888(1, 116, 116, 116));

        map.drawPixel(1, 0, Color.argb8888(1, 92, 92, 92));
        map.drawPixel(4, 0, Color.argb8888(1, 92, 92, 92));
        map.drawPixel(5, 1, Color.argb8888(1, 92, 92, 92));
        map.drawPixel(1, 2, Color.argb8888(1, 92, 92, 92));
        map.drawPixel(4, 2, Color.argb8888(1, 92, 92, 92));
        setMap(map);
        t = new TextureRegion(new Texture(getMap()), 6, 3);

        damage = 50f;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float delta) {
        float rotation = getAcceleration().angle();
        spriteBatch.draw(t, getPosition().x, getPosition().y, 0, 0,
                6, 3, 1, 1, rotation);
    }

    @Override
    public void onCollide(Vector2 position) {
        Explosion exp = new Explosion(getOwner(), 16);
        exp.setPosition(position);
        EntityManager.getInstance().addEntity(exp);

        Sound expSound = RonosGame.assetManager.get("sounds/weapons/exp2.ogg", Sound.class);
        expSound.play();
        super.onCollide(position);
    }
}
