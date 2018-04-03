package pl.aitwar.ronos.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Explosion extends Entity {
    private float size;
    private Entity owner;
    private TextureRegion texture;

    private float elapsed;
    private Interpolation interpolation;
    private static float lifeTime = 0.2f;
    private float scale;

    private boolean rubbish;

    private Set<Player> harmed;

    public Explosion(Entity owner, float size) {
        this.owner = owner;
        this.size = size;

        elapsed = 0;
        interpolation = Interpolation.exp10In;
        rubbish = false;
        scale = 0;
        harmed = new HashSet<Player>();

        // TODO Za każdym razem tworzony jest nowy obiekt.
        // TODO Dziwny pasek na teksturze
        Pixmap textureMap = new Pixmap(17, 17, Pixmap.Format.RGBA8888);
        textureMap.setColor(Color.rgba8888(240,240,180,1));
        textureMap.fillCircle(8, 8, 8);
        textureMap.setColor(Color.rgba8888(248,164,32,1));
        textureMap.drawCircle(8, 8, 8);
        texture = new TextureRegion(new Texture(textureMap));
        textureMap.dispose();
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float delta) {
        if(rubbish)
            return;

        if(elapsed > lifeTime + 0.5f) {
            EntityManager.getInstance().removeEntity(this);
            rubbish = true;
            return;
        }

        elapsed += delta;
        scale = Math.max(0f, 1 - interpolation.apply(elapsed/lifeTime));

        spriteBatch.draw(texture, getPosition().x, getPosition().y, size/2, size/2,
                size, size, scale, scale, 0);
    }

    public void onHit(Player p) {
        if(!harmed.contains(p)) {
            p.gotDamaged(owner, 20f);
            harmed.add(p);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    harmed.remove(p);
                }
            }, 50);
        }
    }

    public float getCurrentRadius() {
        return size*(float)Math.sqrt(2)/2f * scale;
    }
}
