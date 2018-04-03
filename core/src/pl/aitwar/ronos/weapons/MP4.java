package pl.aitwar.ronos.weapons;

import com.badlogic.gdx.audio.Sound;
import pl.aitwar.ronos.RonosGame;
import pl.aitwar.ronos.entities.projectiles.Bullet;
import pl.aitwar.ronos.entities.projectiles.Rocket;

public class MP4 extends Weapon {
    public MP4(float reloadTime, int ammo, boolean cummulate) {
        super(reloadTime, ammo, cummulate);
        setBullet(Bullet.class);
        setShootSound(RonosGame.assetManager.get("sounds/weapons/mp4/shot.wav", Sound.class));
        setReloadSound(RonosGame.assetManager.get("sounds/weapons/mp4/reload.wav", Sound.class));
        setName("MP4");
    }
}
