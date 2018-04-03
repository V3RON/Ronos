package pl.aitwar.ronos.weapons;

import com.badlogic.gdx.audio.Sound;
import pl.aitwar.ronos.RonosGame;
import pl.aitwar.ronos.entities.projectiles.Bullet;
import pl.aitwar.ronos.entities.projectiles.Rocket;

public class Bazooka extends Weapon {
    public Bazooka(float reloadTime, int ammo, boolean cummulate) {
        super(reloadTime, ammo, cummulate);
        setBullet(Rocket.class);
        setShootSound(RonosGame.assetManager.get("sounds/weapons/bazooka/bazooka.ogg", Sound.class));
        setReloadSound(null);
        setName("Bazooka");
    }
}
