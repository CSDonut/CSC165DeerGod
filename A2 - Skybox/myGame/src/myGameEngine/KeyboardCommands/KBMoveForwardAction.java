package myGameEngine.KeyboardCommands;

import A3.MyGame;
import myGameEngine.CheckIfAbovePlane;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import net.java.games.input.Event;

public class KBMoveForwardAction extends AbstractInputAction{
    private Camera camera;
    private SceneNode dolphinN;
    private SceneNode playerGroupNode;
    private MyGame obj;
    private float speedScale = 70; //The higher the number, the slower the objects move

    public KBMoveForwardAction(MyGame myGameObj){
        camera = myGameObj.getEngine().getSceneManager().getCamera("MainCamera");
        obj = myGameObj;
    }

    public void performAction(float time, Event e){
        dolphinN= obj.getEngine().getSceneManager().getSceneNode("myDolphinNode");
        if(new CheckIfAbovePlane().checkLocal(dolphinN))
            dolphinN.moveForward(e.getValue() / speedScale);

    }
}
