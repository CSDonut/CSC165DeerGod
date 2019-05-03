package myGameEngine.GamepadCommands;

import A3.MyGame;
import Network.Client.ProtocolClient;
import graphicslib3D.Vector3D;
import myGameEngine.CheckIfAbovePlane;
import ray.input.action.AbstractInputAction;
import ray.physics.PhysicsEngine;
import ray.rage.scene.*;
import net.java.games.input.Event;

import java.io.IOException;
import ray.rml.*;

public class MoveForwardBackwardAction extends AbstractInputAction{
    private Camera camera;
    private SceneNode cubeN;
    private MyGame obj;
    private ProtocolClient protClient;
    private float speedScale = 50; //The higher the number, the slower the objects move
    private PhysicsEngine physicsEng;

    public MoveForwardBackwardAction(MyGame myGameObj, ProtocolClient p){
        camera = myGameObj.getEngine().getSceneManager().getCamera("MainCamera");
        obj = myGameObj;
//        this.physicsEng = physicsEng;
    }

    public void performAction(float time, Event e){
        cubeN = obj.getEngine().getSceneManager().getSceneNode("myCubeNode");
        protClient = obj.getProtClient();
        obj.updateVerticalPosition();



        if(e.getValue() <= 0.1 || e.getValue() >= -0.1){
            if(new CheckIfAbovePlane().checkLocal(cubeN)) {
                cubeN.moveBackward(e.getValue() / speedScale);
            }

            try {
                protClient.sendMoveMessage(cubeN.getWorldPosition());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
