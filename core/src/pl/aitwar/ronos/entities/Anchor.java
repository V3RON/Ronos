package pl.aitwar.ronos.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import pl.aitwar.ronos.RonosGame;
import pl.aitwar.ronos.entities.projectiles.Projectile;

public class Anchor extends Projectile {
    private Texture texture;
    private Pixmap line;
    private boolean attached = false;

    private Texture lineTexture;

    public Anchor(Entity owner, Vector2 position, Vector2 acceleration) {
        super(owner, position, acceleration);

        // TODO Za każdym razem nowy obiekt
        Pixmap textureMap = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
        textureMap.setColor(Color.rgba8888(240,240,180, 1));
        textureMap.drawLine(1, 0, 1, 2);
        textureMap.drawLine(0, 1, 2, 1);
        setMap(textureMap);
        texture = new Texture(textureMap);

        line = new Pixmap((int)RonosGame.camera.viewportWidth, (int)RonosGame.camera.viewportHeight,
                Pixmap.Format.RGBA8888);
        line.setBlending(Pixmap.Blending.None);
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float delta) {
        spriteBatch.draw(texture, getPosition().x, getPosition().y);

        line.setColor(Color.rgba8888(240,240,180, 1));
        line.drawLine((int)getPosition().x + 1, (int)getPosition().y + 2,
                (int)(getOwner().getPosition().x + 5), (int)(getOwner().getPosition().y + 5));
        if(lineTexture != null)
            lineTexture.dispose();
        lineTexture = new Texture(line);

        spriteBatch.draw(lineTexture, 0, lineTexture.getHeight(),
                lineTexture.getWidth(), lineTexture.getHeight() * -1);

        line.setColor(Color.CLEAR);
        line.fill();
    }

    @Override
    public void onCollide(Vector2 position) {
        attached = true;
    }

    public boolean isAttached() {
        return attached;
    }
}
