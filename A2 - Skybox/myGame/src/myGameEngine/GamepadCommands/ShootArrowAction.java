package myGameEngine.GamepadCommands;
import A3.MyGame;
import myGameEngine.Physics.ArrayConversion;
import ray.input.action.AbstractInputAction;
import ray.physics.PhysicsEngine;
import ray.rage.scene.*;
import net.java.games.input.Event;



public class ShootArrowAction extends AbstractInputAction{
    private SceneNode arrowN, avatarN, rootN;
    private MyGame myGameObj;
    private int uid;
    private PhysicsEngine physicsEngine;
    private float mass = 1.0f;
    private ArrayConversion arrayConversion;


    public ShootArrowAction(MyGame myGameObj, PhysicsEngine physicsEngine){
        this.myGameObj = myGameObj;
        this.rootN = myGameObj.getEngine().getSceneManager().getRootSceneNode();
        this.avatarN = myGameObj.getEngine().getSceneManager().getSceneNode("myCubeNode");
        this.physicsEngine = physicsEngine;
//        this.uid = physicsEngine.nextUID();
        this.arrayConversion = new ArrayConversion();

    }

    public void performAction(float time, Event e){
        myGameObj.shootArrow();

    }
}
