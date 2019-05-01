package myGameEngine.GamepadCommands;

import A3.MyGame;
import myGameEngine.CheckIfAbovePlane;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class CameraYawAction extends AbstractInputAction{
    private Camera camera;
    private SceneNode cubeN;
    private MyGame obj;
    private float speedScaleInvert = -1; //The higher the number, the slower the objects move, negative to invert yaw
    private float speedScale = 4; //The higher the number, the slower the objects move

    Angle yawAmt, yawAmtInv;

    public CameraYawAction(MyGame myGameObj){
        camera = myGameObj.getEngine().getSceneManager().getCamera("MainCamera");
        obj = myGameObj;
    }

    public void performAction(float time, Event e){
        cubeN = obj.getEngine().getSceneManager().getSceneNode("myCubeNode");
        yawAmt = Degreef.createFrom(e.getValue()/speedScaleInvert);

        if(new CheckIfAbovePlane().checkLocal(cubeN))
            cubeN.yaw(yawAmt);
    }
}
