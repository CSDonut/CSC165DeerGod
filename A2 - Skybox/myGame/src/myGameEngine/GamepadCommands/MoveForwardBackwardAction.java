package myGameEngine.GamepadCommands;

import A3.MyGame;
import myGameEngine.CheckIfAbovePlane;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import net.java.games.input.Event;

public class MoveForwardBackwardAction extends AbstractInputAction{
    private Camera camera;
    private SceneNode cubeN;
    private MyGame obj;
    private float speedScale = 70; //The higher the number, the slower the objects move

    public MoveForwardBackwardAction(MyGame myGameObj){
        camera = myGameObj.getEngine().getSceneManager().getCamera("MainCamera");
        obj = myGameObj;
    }

    public void performAction(float time, Event e){
        cubeN = obj.getEngine().getSceneManager().getSceneNode("myCubeNode");

        if(e.getValue() <= 0.3 || e.getValue() >= -0.3){
            if(new CheckIfAbovePlane().checkLocal(cubeN))
                cubeN.moveBackward(e.getValue() / speedScale);
        }
    }
}
