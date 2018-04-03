package pl.aitwar.ronos.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    public static final int ENTITY_DIRECTION_RIGHT = 1;
    public static final int ENTITY_DIRECTION_LEFT = 0;

    private Vector2 position = new Vector2(0, 0);
    private Vector2 acceleration = new Vector2(0, 0);
    private int direction = ENTITY_DIRECTION_RIGHT;
    private float health = 10f;

    public abstract void draw(SpriteBatch spriteBatch, float delta);

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
