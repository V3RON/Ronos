package pl.aitwar.ronos.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import pl.aitwar.ronos.entities.Entity;
import pl.aitwar.ronos.entities.EntityManager;
import pl.aitwar.ronos.entities.Player;
import pl.aitwar.ronos.entities.projectiles.Projectile;

import java.lang.reflect.Constructor;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Weapon {
    private float reloadTime;
    private int ammo;
    private int magAmmo;
    private boolean cummulate;
    private boolean canShoot;
    private boolean isReloading;
    private Sound shootSound;
    private Sound reloadSound;
    private Class<? extends Projectile> bullet;
    private String name;

    public Weapon(float reloadTime, int ammo, boolean cummulate) {
        this.reloadTime = reloadTime;
        this.magAmmo = ammo;
        this.ammo = ammo;
        this.cummulate = cummulate;
        this.canShoot = true;
        this.isReloading = false;
    }

    public void fire(Player p) {
        if(ammo == 0 || !canShoot) {
            return;
        }

        ammo--;
        canShoot = false;

        Vector2 projectilePos = p.getPosition().cpy();
        int flip = p.getDirection() == Entity.ENTITY_DIRECTION_LEFT ? -1 : 1;

        projectilePos.x = projectilePos.x + p.getWidth()/2 + flip * (float)Math.sin((90-p.getGunAngle())*Math.PI/180) * 25;
        projectilePos.y = projectilePos.y + (float)Math.cos((90-p.getGunAngle())*Math.PI/180) * 25 - 1;

        Vector2 projectileAcc = new Vector2(projectilePos.x - p.getPosition().x, projectilePos.y - p.getPosition().y);
        projectileAcc.scl(0.1f);

        try {
            Constructor<? extends Projectile> constructor = bullet.getConstructor(Entity.class, Vector2.class, Vector2.class);
            Projectile flyingBullet = constructor.newInstance(p, projectilePos, projectileAcc);
            EntityManager.getInstance().addEntity(flyingBullet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        shootSound.play();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                canShoot = true;
            }
        }, 200);

        if(ammo == 0) {
            if(reloadSound != null)
                reloadSound.play();
            isReloading = true;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    ammo = magAmmo;
                    isReloading = false;
                }
            }, 1500);
        }
    }

    protected void setBullet(Class<? extends Projectile> bullet) {
        this.bullet = bullet;
    }

    protected void setShootSound(Sound sound) {
        shootSound = sound;
    }

    protected void setReloadSound(Sound sound) {
        reloadSound = sound;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getMagAmmo() {
        return magAmmo;
    }

    public boolean isReloading() {
        return isReloading;
    }

    public void clear() {
        this.reloadTime = reloadTime;
        this.magAmmo = ammo;
        this.ammo = ammo;
        this.cummulate = cummulate;
        this.canShoot = true;
        this.isReloading = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
