package myGameEngine.KeyboardCommands;

import A3.MyGame;
import myGameEngine.CheckIfAbovePlane;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class KBCameraYawAction extends AbstractInputAction{
    private Camera camera;
    private SceneNode dolphinN;
    private MyGame obj;
    private float speedScaleInvert = -2; //The higher the number, the slower the objects move, negative to invert yaw

    Angle yawAmt, yawAmtInv;

    public KBCameraYawAction(MyGame myGameObj){
        camera = myGameObj.getEngine().getSceneManager().getCamera("MainCamera");
        obj = myGameObj;
    }

    public void performAction(float time, Event e){
        dolphinN = obj.getEngine().getSceneManager().getSceneNode("myDolphinNode");
        yawAmt = Degreef.createFrom(e.getValue()/speedScaleInvert);
        yawAmtInv = Degreef.createFrom(-(e.getValue()/speedScaleInvert));

        if(new CheckIfAbovePlane().checkLocal(dolphinN))
            dolphinN.yaw(yawAmt);
    }
}
