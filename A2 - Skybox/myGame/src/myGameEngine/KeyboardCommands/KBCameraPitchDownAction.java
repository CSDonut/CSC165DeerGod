package myGameEngine.KeyboardCommands;

import A3.MyGame;
import myGameEngine.CheckIfAbovePlane;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Camera;
import ray.rage.scene.SceneNode;
import ray.rml.Angle;
import ray.rml.Degreef;

public class KBCameraPitchDownAction extends AbstractInputAction{
    private Camera camera;
    private SceneNode dolphinN;
    private MyGame obj;
    private float speedScaleInvert = -2; //The higher the number, the slower the objects move, negative to invert yaw
    Angle rotAmt, pitchAmt;

    public KBCameraPitchDownAction(MyGame myGameObj){
        camera = myGameObj.getEngine().getSceneManager().getCamera("MainCamera");
        obj = myGameObj;
    }

    public void performAction(float time, Event e){
        dolphinN = obj.getEngine().getSceneManager().getSceneNode("myCubeNode");
        rotAmt = Degreef.createFrom(e.getValue()/speedScaleInvert);
        pitchAmt = Degreef.createFrom(-e.getValue()/speedScaleInvert);

        if(new CheckIfAbovePlane().checkLocal(dolphinN))
            dolphinN.pitch(pitchAmt);
    }
}
