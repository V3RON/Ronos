package pl.aitwar.ronos.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import pl.aitwar.ronos.PlayerKeyboard;
import pl.aitwar.ronos.RonosGame;
import pl.aitwar.ronos.entities.*;
import pl.aitwar.ronos.entities.projectiles.Projectile;

public class World {
    private Pixmap map;
    private Pixmap materialMap;

    private Texture materialTexture;

    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;

    private EntityManager entityManager = EntityManager.getInstance();
    private Player[] players;

    private boolean debugEnabled = false;
    private int debugCooldown = 0;
    private BitmapFont debugFont = new BitmapFont();

    public World(Texture map, Texture materialMap) {
        if (!map.getTextureData().isPrepared()) {
            map.getTextureData().prepare();
        }
        materialTexture = materialMap;
        if (!materialMap.getTextureData().isPrepared()) {
            materialMap.getTextureData().prepare();
        }

        this.map = map.getTextureData().consumePixmap();
        this.materialMap = materialMap.getTextureData().consumePixmap();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 380);

        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(camera.combined);

        players = new Player[2];
        players[0] = new Player(0,73, 86);
        players[1] = new Player(1, 60, 86);

        entityManager.addEntity(players[0]);
        entityManager.addEntity(players[1]);
    }

    public void draw(float delta) {
        for(Entity e : entityManager.getEntities()) {
            if(e instanceof Player)
                handlePlayerPhysics((Player)e);
            else if(e instanceof Projectile)
                handleProjectilePhysics((Projectile)e);
            else if(e instanceof Explosion)
                handleExplosionPhysics((Explosion)e);
        }

        Texture mapTexture = new Texture(map);
        spriteBatch.begin();
        spriteBatch.draw(mapTexture, 0, 0, mapTexture.getWidth(), mapTexture.getHeight());
        debug(spriteBatch);
        entityManager.draw(spriteBatch, delta);
        spriteBatch.end();
        mapTexture.dispose();
    }

    private void handleProjectilePhysics(Projectile p) {
        Vector2 fakeMove = p.getPosition().cpy();
        fakeMove.add(p.getAcceleration());
        fakeMove.set(Math.round(fakeMove.x), Math.round(fakeMove.y));

        int w = p.getMap().getWidth();
        int h = p.getMap().getHeight();

        for(Entity ent : entityManager.getEntities()) {
            if(ent instanceof Player) {
                Player pl = (Player) ent;
                if(pl.isDead())
                    continue;

                Rectangle playerRect = new Rectangle(pl.getPosition().x, pl.getPosition().y,
                        pl.getWidth(), pl.getHeight());
                Rectangle projRect = new Rectangle(p.getPosition().x, p.getPosition().y, w, h);
                if(playerRect.overlaps(projRect)) {
                    p.onHit(pl);
                    return;
                }
            }
        }

        boolean flag = false;
        for(int i = 0; i < w; i++) {
            for(int j = 0; j < h; j++) {
                if(materialMap.getPixel(Math.round(fakeMove.x + i), Math.round(materialMap.getHeight() - fakeMove.y - j)) == 255) {
                    flag = true;
                    break;
                }
            }
        }

        if(flag) {
            // Oblicz pixel, który został uderzony
            if(w == 1 && h == 1) {
                Vector2 buffer = p.getPosition().cpy();
                buffer.set(Math.round(buffer.x), Math.round(buffer.y));
                while(true) {
                    int x = (int)buffer.x;
                    int y = (int)buffer.y;

                    if(Math.abs(buffer.x - fakeMove.x) > MathUtils.FLOAT_ROUNDING_ERROR)
                        x = buffer.x < fakeMove.x ? (int)buffer.x + 1 : (int)buffer.x - 1;

                    if(Math.abs(buffer.y - fakeMove.y) > MathUtils.FLOAT_ROUNDING_ERROR)
                        y = buffer.y < fakeMove.y ? (int)buffer.y + 1 : (int)buffer.y - 1;

                    buffer.set(x, y);

                    if(materialMap.getPixel(Math.round(buffer.x), Math.round(materialMap.getHeight() - buffer.y)) == 255) {
                        // Znaleziono
                        map.drawPixel(Math.round(buffer.x), Math.round(materialMap.getHeight() - buffer.y), Color.RED.toIntBits());
                        p.setPosition(buffer);

                        Sound hit = RonosGame.assetManager.get("sounds/weapons/exp4.ogg", Sound.class);
                        hit.play();
                        break;
                    }
                }
            }

            p.onCollide(p.getPosition());
            p.getAcceleration().set(0, 0);
        } else {
            p.getPosition().add(p.getAcceleration());
        }
    }

    private void handlePlayerPhysics(Player e) {
        if(e.isDead())
            return;

        Vector2 fakeMove = e.getPosition().cpy();
        fakeMove.add(e.getAcceleration().x, 0);

        boolean flag = false;
        // Collision X
        for(int i = (int)(fakeMove.y + e.getHeight()); i >= (int)fakeMove.y + 1; i--) {
            if(materialMap.getPixel((int)fakeMove.x, materialMap.getHeight() - i) == 255
                    || materialMap.getPixel((int)(fakeMove.x + e.getWidth()), materialMap.getHeight() - i) == 255) {
                if(i == (int)fakeMove.y + 2 || i == (int)fakeMove.y + 1) {
                    // Najniższy kloceks
                    //map.drawPixel((int)(fakeMove.x + player.getWidth()), materialMap.getHeight() - i, Color.RED.toIntBits());
                    e.getPosition().add(0, 2);
                } else {
                    flag = true;
                    break;
                }
            }
            //map.drawPixel((int)fakeMove.x, materialMap.getHeight() - i, Color.RED.toIntBits());
            //map.drawPixel((int)(fakeMove.x + player.getWidth()), materialMap.getHeight() - i, Color.RED.toIntBits());
        }
        if(flag) {
            e.getAcceleration().set(0, e.getAcceleration().y);
            flag = false;
        } else {
            if(Math.abs(e.getAcceleration().x) > MathUtils.FLOAT_ROUNDING_ERROR) {
                if (e.getAcceleration().x < 0) {
                    e.getAcceleration().sub(-0.1f, 0);
                } else {
                    e.getAcceleration().sub(0.1f, 0);
                }
            }
        }

        // Collision Y
        for(int i = (int)fakeMove.x; i <= (int)fakeMove.x + e.getWidth(); i++) {
            if(materialMap.getPixel(i, materialMap.getHeight() - (int)fakeMove.y) == 255
                    || materialMap.getPixel(i, (int)(materialMap.getHeight() - fakeMove.y - e.getHeight())) == 255) {
                flag = true;
                break;
            }
            //map.drawPixel(i, materialMap.getHeight() - (int)fakeMove.y, Color.RED.toIntBits());
            //map.drawPixel(i, (int)(materialMap.getHeight() - fakeMove.y - player.getHeight()), Color.RED.toIntBits());
        }
        if(flag) {
            e.getAcceleration().set(e.getAcceleration().x, 0);
            flag = false;
        } else {
            if(e.getAcceleration().y < 0.2f) {
                e.getAcceleration().sub(0, 0.05f);
            } else {
                e.getAcceleration().set(e.getAcceleration().x, 0f);
            }
        }

        Anchor a = e.getPlayerAnchor();
        if(a != null && a.isAttached()) {
            Vector2 anchorAcc = new Vector2(a.getPosition().x - e.getPosition().x, a.getPosition().y - e.getPosition().y);
            anchorAcc.scl(0.005f);
            e.getAcceleration().add(anchorAcc);
        }
        e.getPosition().add(e.getAcceleration());
    }

    private void handleExplosionPhysics(Explosion e) {
        for(Entity ent : entityManager.getEntities()) {
            if(ent instanceof Player) {
                Player pl = (Player) ent;
                if(pl.isDead())
                    continue;

                Rectangle playerRect = new Rectangle(pl.getPosition().x, pl.getPosition().y,
                        pl.getWidth(), pl.getHeight());
                Circle projCircle = new Circle(e.getPosition().x, e.getPosition().y, e.getCurrentRadius());
                if(Intersector.overlaps(projCircle, playerRect)) {
                    e.onHit(pl);
                    return;
                }
            }
        }
    }

    private void debug(SpriteBatch spriteBatch) {
        if(Gdx.input.isKeyPressed(Input.Keys.F1) && debugCooldown == 0) {
            debugEnabled = !debugEnabled;
            debugCooldown = 5;
        }

        if(debugEnabled) {
            spriteBatch.draw(materialTexture, 0, 0, materialTexture.getWidth(), materialTexture.getHeight());
            debugFont.draw(spriteBatch, "PLAYER X="+players[0].getPosition().x+", Y="+players[0].getPosition().y,
                    10, 20);
        }

        debugCooldown = debugCooldown != 0 ? debugCooldown - 1 : 0;
    }

    public Player getPlayer(int num) {
        return players[num];
    }
}
