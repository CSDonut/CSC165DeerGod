package myGameEngine.KeyboardCommands;

import A3.MyGame;
import myGameEngine.CheckIfAbovePlane;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import net.java.games.input.Event;

public class KBMoveBackwardAction extends AbstractInputAction{
    private Camera camera;
    private SceneNode myCubeNode;
    private MyGame obj;
    private float speedScale = 70; //The higher the number, the slower the objects move
    private SceneNode playerGroupNode;

    public KBMoveBackwardAction(MyGame myGameObj){
        camera = myGameObj.getEngine().getSceneManager().getCamera("MainCamera");
        obj = myGameObj;
    }

    public void performAction(float time, Event e){
        myCubeNode = obj.getEngine().getSceneManager().getSceneNode("myCubeNode");

        if(new CheckIfAbovePlane().checkLocal(myCubeNode))
            myCubeNode.moveBackward(e.getValue() / speedScale);

    }
}
