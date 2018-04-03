package pl.aitwar.ronos.entities.projectiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import pl.aitwar.ronos.RonosGame;
import pl.aitwar.ronos.entities.Entity;
import pl.aitwar.ronos.entities.Player;

public class Bullet extends Projectile {
    private Texture t;
    private float damage;

    public Bullet(Entity owner, Vector2 position, Vector2 acceleration) {
        super(owner, position, acceleration);

        Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        map.drawPixel(0, 0, Color.rgba8888(Color.WHITE));
        setMap(map);
        t = new Texture(getMap());

        damage = 10f;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float delta) {
        spriteBatch.draw(t, getPosition().x, getPosition().y,
                t.getWidth(), t.getHeight());
    }

    @Override
    public void onHit(Entity e) {
        if(e instanceof Player) {
            Player p = (Player)e;
            p.gotDamaged(getOwner(), damage);
        }
        super.onHit(e);
    }
}
