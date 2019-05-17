package myGameEngine.GamepadCommands;

import A3.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;

import java.io.IOException;

public class SelectCharaTwo extends AbstractInputAction {

    private MyGame myGame;


    public SelectCharaTwo(MyGame game){
        myGame = game;
    }

    @Override
    public void performAction(float v, Event event) {
        myGame.setCharaSelect(false);
        myGame.getEngine().getSceneManager().destroySceneNode("CharaSelect");
        try {
            myGame.setupNetworking();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
