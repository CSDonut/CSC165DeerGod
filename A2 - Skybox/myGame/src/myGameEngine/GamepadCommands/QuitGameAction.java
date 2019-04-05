package myGameEngine.GamepadCommands;

import A3.MyGame;
import Network.Client.ProtocolClient;
import ray.input.action.AbstractInputAction;
import ray.rage.game.*;
import net.java.games.input.Event;

import java.io.IOException;

public class QuitGameAction extends AbstractInputAction {
    private MyGame game;

    public QuitGameAction(MyGame myGameObj){
        game = myGameObj;
    }

    public void performAction(float time, Event event){
        System.out.println("Shutdown requested");
        if(game.getisClientConnected() && game.getProtClient() != null){
            try {
                game.getProtClient().sendByeMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        game.setState(Game.State.STOPPING);
    }
}
