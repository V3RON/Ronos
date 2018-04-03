package pl.aitwar.ronos;

import com.badlogic.gdx.Input;

public class PlayerKeyboard {
    public int UP;
    public int DOWN;
    public int SHOOT;
    public int RIGHT;
    public int LEFT;
    public int CHOOSE_WEAPON;
    public int THROW_ANCHOR;
    public int DROP_ANCHOR;

    public PlayerKeyboard(int playerIndex) {
        if(playerIndex == 1) {
            UP = Input.Keys.UP;
            DOWN = Input.Keys.DOWN;
            SHOOT = Input.Keys.CONTROL_RIGHT;
            RIGHT = Input.Keys.RIGHT;
            LEFT = Input.Keys.LEFT;
            CHOOSE_WEAPON = Input.Keys.SHIFT_RIGHT;
            THROW_ANCHOR = Input.Keys.SLASH;
            DROP_ANCHOR = Input.Keys.APOSTROPHE;
        } else {
            UP = Input.Keys.W;
            DOWN = Input.Keys.S;
            SHOOT = Input.Keys.CONTROL_LEFT;
            RIGHT = Input.Keys.D;
            LEFT = Input.Keys.A;
            CHOOSE_WEAPON = Input.Keys.SHIFT_LEFT;
            THROW_ANCHOR = Input.Keys.Z;
            DROP_ANCHOR = Input.Keys.X;
        }
    }
}
