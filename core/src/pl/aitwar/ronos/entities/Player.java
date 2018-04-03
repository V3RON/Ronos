package pl.aitwar.ronos.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import pl.aitwar.ronos.PlayerKeyboard;
import pl.aitwar.ronos.RonosGame;
import pl.aitwar.ronos.weapons.Bazooka;
import pl.aitwar.ronos.weapons.MP4;
import pl.aitwar.ronos.weapons.Weapon;

import java.util.Timer;
import java.util.TimerTask;

public class Player extends Entity {
    private TextureAtlas playerAtlas;

    private float gunAngle = 0;

    private TextureRegion[][] textures = new TextureRegion[9][4];
    private Animation[] anims;
    private float elapsedTime;

    private Texture crosshairTexture;

    private Weapon[] weapons = new Weapon[5];
    private int activeWeapon = 0;
    private boolean weaponSwitchTimeout = false;

    private Sound deathSound;
    private boolean isDead;

    private PlayerKeyboard playerKeyboard;

    private int points;
    private int playerIndex;

    private GlyphLayout layout = new GlyphLayout();

    private Anchor playerAnchor;
    private boolean anchorTimeout = false;

    public Player(int playerIndex) {
        playerAtlas = RonosGame.assetManager.get("player/player.atlas");
        setHealth(100f);

        for(int i = 0; i < 9; i++)
            for(int j = 0; j < 4; j++)
                textures[i][j] = playerAtlas.findRegion((i+1)+""+(j+1));

        anims = new Animation[9];
        for(int i = 0; i < 9; i++) {
            anims[i] = new Animation<>(1/10f, textures[i]);
        }

        Pixmap crosshairMap = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
        crosshairMap.setColor(Color.WHITE);
        crosshairMap.drawRectangle(0, 0, 3, 3);
        crosshairTexture = new Texture(crosshairMap);


        for(int i = 0; i < 5; i++) {
            weapons[i] = new MP4(10f, 10, false);
        }
        weapons[1] = new Bazooka(10f, 2, false);

        deathSound = RonosGame.assetManager.get("sounds/death1.ogg");
        isDead = false;
        this.playerKeyboard = new PlayerKeyboard(playerIndex);
        points = 0;
        this.playerIndex = playerIndex;
    }

    public Player(int playerIndex, int x, int y) {
        this(playerIndex);
        setPosition(new Vector2(x, y));
    }

    @Override
    public void draw(SpriteBatch sb, float delta) {
        handleInput();

        if(Math.abs(getAcceleration().x) > MathUtils.FLOAT_ROUNDING_ERROR) {
            elapsedTime += Gdx.graphics.getDeltaTime();
        }

        int flip = getDirection() == Entity.ENTITY_DIRECTION_LEFT ? -1 : 1;
        TextureRegion currentFrame = ((TextureRegion) anims[getAngleTextureIndex()].getKeyFrame(elapsedTime, true));

        if(playerIndex == 1) {
            sb.setColor(Color.RED);
        }
        sb.draw(currentFrame,
                flip  == -1 ? getPosition().x + currentFrame.getRegionWidth() : getPosition().x, getPosition().y,
                currentFrame.getRegionWidth() * flip, currentFrame.getRegionHeight());
        sb.setColor(Color.WHITE);

        float crossHairX = getPosition().x + getWidth()/2 + flip * (float)Math.sin((90-gunAngle)*Math.PI/180) * 25;
        float crossHairY = getPosition().y + (float)Math.cos((90-gunAngle)*Math.PI/180) * 25;
        sb.draw(crosshairTexture, crossHairX, crossHairY,
                crosshairTexture.getWidth(), crosshairTexture.getHeight());

        if(Gdx.input.isKeyPressed(playerKeyboard.CHOOSE_WEAPON)) {
            layout.setText(RonosGame.font, weapons[activeWeapon].getName());
            RonosGame.font.draw(sb, weapons[activeWeapon].getName(), getPosition().x - layout.width/2 + getWidth()/2, getPosition().y + getHeight() + 15);
        }
    }

    private void handleInput() {
        if(Gdx.input.isKeyPressed(playerKeyboard.RIGHT)) {
            setDirection(Entity.ENTITY_DIRECTION_RIGHT);
            getAcceleration().set(0.5f, getAcceleration().y);
        } else if(Gdx.input.isKeyPressed(playerKeyboard.LEFT)) {
            setDirection(Entity.ENTITY_DIRECTION_LEFT);
            getAcceleration().set(-0.5f, getAcceleration().y);
        }
        if(Gdx.input.isKeyPressed(playerKeyboard.SHOOT)) {
            weapons[activeWeapon].fire(this);
        }
        if(Gdx.input.isKeyPressed(playerKeyboard.UP)) {
            if (gunAngle < 90) {
                gunAngle += 2f;
            }
        } else if(Gdx.input.isKeyPressed(playerKeyboard.DOWN)) {
            if(gunAngle > -90) {
                gunAngle -= 2f;
            }
        }
        if(Gdx.input.isKeyPressed(playerKeyboard.CHOOSE_WEAPON)) {
            if(Gdx.input.isKeyPressed(playerKeyboard.UP)) {
                if(!weaponSwitchTimeout) {
                    activeWeapon = activeWeapon == 4 ? 1 : activeWeapon + 1;
                    weaponSwitchTimeout = true;

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            weaponSwitchTimeout = false;
                        }
                    }, 200);
                }
            } else if(Gdx.input.isKeyPressed(playerKeyboard.DOWN)) {
                if(!weaponSwitchTimeout) {
                    activeWeapon = activeWeapon == 0 ? 4 : activeWeapon - 1;
                    weaponSwitchTimeout = true;

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            weaponSwitchTimeout = false;
                        }
                    }, 200);
                }
            }
        }
        if(Gdx.input.isKeyPressed(playerKeyboard.THROW_ANCHOR)) {
            if(!anchorTimeout) {
                if(playerAnchor != null) {
                    EntityManager.getInstance().removeEntity(playerAnchor);
                    playerAnchor = null;
                }

                int flip = getDirection() == Entity.ENTITY_DIRECTION_LEFT ? -1 : 1;
                float projectilePosX = getPosition().x + getWidth() / 2 + flip * (float) Math.sin((90 - gunAngle) * Math.PI / 180) * 25;
                float projectilePosY = getPosition().y + (float) Math.cos((90 - gunAngle) * Math.PI / 180) * 25;

                Vector2 projectileAcc = new Vector2(projectilePosX - getPosition().x, projectilePosY - getPosition().y);
                projectileAcc.scl(0.2f);

                playerAnchor = new Anchor(this, new Vector2(projectilePosX, projectilePosY), projectileAcc);
                EntityManager.getInstance().addEntity(playerAnchor);

                anchorTimeout = true;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        anchorTimeout = false;
                    }
                }, 200);
            }
        }
        if(Gdx.input.isKeyPressed(playerKeyboard.DROP_ANCHOR)) {
            if(!anchorTimeout) {
                if (playerAnchor != null) {
                    EntityManager.getInstance().removeEntity(playerAnchor);
                    playerAnchor = null;
                }

                anchorTimeout = true;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        anchorTimeout = false;
                    }
                }, 200);
            }
        }
    }

    public float getHeight() {
        return ((TextureRegion)anims[0].getKeyFrame(0)).getRegionHeight();
    }

    public float getWidth() {
        return ((TextureRegion)anims[0].getKeyFrame(0)).getRegionWidth();
    }

    public Pixmap getPixMap() {
        return ((TextureRegion)anims[getAngleTextureIndex()].getKeyFrame(elapsedTime, true)).getTexture()
                .getTextureData().consumePixmap();
    }

    private int getAngleTextureIndex() {
        return Math.abs(4-Math.round(gunAngle/22.5f));
    }

    public float getGunAngle() {
        return gunAngle;
    }

    public Weapon getWeapon(int index) {
        return weapons[index];
    }

    public void gotDamaged(Entity dealer, float damage) {
        setHealth(getHealth()-damage);
        if(getHealth() <= MathUtils.FLOAT_ROUNDING_ERROR) {
            deathSound.play();
            die();

            if(dealer instanceof Player) {
                Player p = (Player)dealer;
                p.scorePoint();
                this.points--;
            }
        }
    }

    private void die() {
        isDead = true;
        EntityManager.getInstance().removeEntity(playerAnchor);
        playerAnchor = null;
        for(Weapon w : weapons) {
            w.clear();
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                resurrect();
            }
        }, 2000);
    }

    private void resurrect() {
        setPosition(new Vector2(73, 86));
        isDead = false;
        setHealth(100f);
    }

    public boolean isDead() {
        return isDead;
    }

    public void scorePoint() {
        points++;
    }

    public int getPoints() {
        return points;
    }

    public Weapon getActiveWeapon() {
        return weapons[activeWeapon];
    }

    public Anchor getPlayerAnchor() {
        return playerAnchor;
    }
}
