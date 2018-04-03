package pl.aitwar.ronos.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import pl.aitwar.ronos.test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EntityManager {
    private static EntityManager ourInstance = new EntityManager();
    public static EntityManager getInstance() {
        return ourInstance;
    }

    private EntityManager() {
        entitySet = new HashSet<Entity>();
        entityToAdd = new HashSet<Entity>();
        entityToRemove = new HashSet<Entity>();
    }

    private Set<Entity> entitySet;
    private Set<Entity> entityToAdd;
    private Set<Entity> entityToRemove;

    public void draw(SpriteBatch spriteBatch, float delta) {
        if(!entityToRemove.isEmpty()) {
            entitySet.removeAll(entityToRemove);
            entityToRemove.clear();
        }

        if(!entityToAdd.isEmpty()) {
            entitySet.addAll(entityToAdd);
            entityToAdd.clear();
        }

        for (Entity e : entitySet) {
            if(e instanceof Player) {
                Player p = (Player)e;
                if(p.isDead())
                    continue;
            }
            e.draw(spriteBatch, delta);
        }
    }

    public void addEntity(Entity e) {
        entityToAdd.add(e);
    }

    public Set<Entity> getEntities() {
        return entitySet;
    }

    public void removeEntity(Entity e) {
        entityToRemove.add(e);
    }
}
