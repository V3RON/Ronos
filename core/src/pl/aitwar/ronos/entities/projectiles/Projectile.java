package pl.aitwar.ronos.entities.projectiles;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import pl.aitwar.ronos.entities.Entity;
import pl.aitwar.ronos.entities.EntityManager;

public abstract class Projectile extends Entity {
    private Pixmap map;
    private Entity owner;

    public Projectile(Entity owner, Vector2 position, Vector2 acceleration) {
        setPosition(position);
        setAcceleration(acceleration);
        this.owner = owner;
    }

    public abstract void draw(SpriteBatch spriteBatch, float delta);

    public Pixmap getMap() {
        return map;
    }

    protected void setMap(Pixmap map) {
        this.map = map;
    }

    public void onCollide(Vector2 position) {
        EntityManager.getInstance().removeEntity(this);
        map.dispose();
    }

    public void onHit(Entity e) {
        onCollide(e.getPosition());
    }

    public Entity getOwner() {
        return owner;
    }
}
