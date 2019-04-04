package myGameEngine.KeyboardCommands;

import A3.MyGame;
import myGameEngine.CheckIfAbovePlane;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import net.java.games.input.Event;

public class KBMoveRightAction extends AbstractInputAction{
    private Camera camera;
    private SceneNode dolphinN;
    private MyGame obj;
    private float speedScale = 70; //The higher the number, the slower the objects move
    private SceneNode playerGroupNode;

    public KBMoveRightAction(MyGame myGameObj){
        camera = myGameObj.getEngine().getSceneManager().getCamera("MainCamera");
        obj = myGameObj;
    }

    public void performAction(float time, Event e){
        playerGroupNode = obj.getEngine().getSceneManager().getSceneNode("playerGroupNode");
        dolphinN = (SceneNode)playerGroupNode.getChild("myDolphinNode");

        if(new CheckIfAbovePlane().checkLocal(dolphinN))
            dolphinN.moveLeft(e.getValue() / speedScale);

    }
}
