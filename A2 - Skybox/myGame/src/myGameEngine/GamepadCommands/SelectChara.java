package myGameEngine.GamepadCommands;

import A3.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;

import java.io.IOException;

public class SelectChara extends AbstractInputAction {

    private MyGame myGame;
    private boolean charaSelect;
    private boolean selectHunter;

    public SelectChara(MyGame game){
        myGame = game;
        charaSelect = game.getCharaSelect();
        selectHunter = game.getDeerOrHunt();
    }

    @Override
    public void performAction(float v, Event event) {
        charaSelect = myGame.getCharaSelect();
        selectHunter = myGame.getDeerOrHunt();
        myGame.setDeerOrHunt(false);
               try {
                   myGame.setDeerModel();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           myGame.setCharaSelect(false);
           myGame.getEngine().getSceneManager().destroySceneNode("CharaSelect");
            try {
                myGame.setupNetworking();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
