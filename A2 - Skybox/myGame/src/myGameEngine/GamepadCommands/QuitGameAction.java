package myGameEngine.GamepadCommands;

import A3.MyGame;
import ray.input.action.AbstractInputAction;
import ray.rage.game.*;
import net.java.games.input.Event;

public class QuitGameAction extends AbstractInputAction {
    private MyGame game;

    public QuitGameAction(MyGame myGameObj){
        game = myGameObj;
    }

    public void performAction(float time, Event event){
        System.out.println("Shutdown requested");
        game.setState(Game.State.STOPPING);
    }
}
