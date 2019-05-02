package myGameEngine.GamepadCommands;

import A3.MyGame;
import Network.Client.ProtocolClient;
import graphicslib3D.Vector3D;
import myGameEngine.CheckIfAbovePlane;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import net.java.games.input.Event;

import java.io.IOException;

public class MoveLeftRightAction extends AbstractInputAction{
    private Camera camera;
    private SceneNode cubeN;
    private MyGame obj;
    private ProtocolClient protClient;
    private float speedScale = 70; //The higher the number, the slower the objects move

    public MoveLeftRightAction(MyGame myGameObj, ProtocolClient p){
        camera = myGameObj.getEngine().getSceneManager().getCamera("MainCamera");
        obj = myGameObj;
        protClient = p;
    }

    public void performAction(float time, Event e){
        cubeN = obj.getEngine().getSceneManager().getSceneNode("myCubeNode");
        protClient = obj.getProtClient();
        obj.updateVerticalPosition();

        if(e.getValue() <= 0.3 || e.getValue() >= -0.3){
            if(new CheckIfAbovePlane().checkLocal(cubeN))
                cubeN.moveLeft(e.getValue() / speedScale);
            try {
                protClient.sendMoveMessage(cubeN.getWorldPosition());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
