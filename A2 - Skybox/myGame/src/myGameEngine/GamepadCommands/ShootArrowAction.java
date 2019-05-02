package myGameEngine.GamepadCommands;

import A3.MyGame;
import Network.Client.ProtocolClient;
import graphicslib3D.Vector3D;
import myGameEngine.CheckIfAbovePlane;
import myGameEngine.Physics.ArrayConversion;
import ray.input.action.AbstractInputAction;
import ray.physics.PhysicsEngine;
import ray.physics.PhysicsObject;
import ray.rage.scene.*;
import net.java.games.input.Event;
import ray.rml.Vector3;
import ray.rml.Vector3f;

import java.io.IOException;


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
        double[] temptf;

        try{
            Entity arrowE = myGameObj.getEngine().getSceneManager().createEntity("arrow " + physicsEngine.nextUID(), "cube.obj");
            arrowN = rootN.createChildSceneNode("arrow " + physicsEngine.nextUID());
            arrowN.scale(0.3f, 0.3f, 0.3f);
            arrowN.attachObject(arrowE);
            arrowN.setLocalPosition(avatarN.getLocalPosition());
            arrowN.setLocalRotation(avatarN.getLocalRotation());
            arrowN.moveUp(0.5f);
            arrowN.moveLeft(0.1f);
            //Creating phys object for arrow
            temptf = arrayConversion.toDoubleArray(arrowN.getLocalTransform().toFloatArray());
            PhysicsObject arrowPhysObj = physicsEngine.addSphereObject(physicsEngine.nextUID(), mass, temptf, 1.0f);
            Vector3f velocity = (Vector3f)arrowN.getLocalRotation().mult(Vector3f.createFrom(0.0f, 0.0f, 20.0f));
            arrowPhysObj.setLinearVelocity(new float []{velocity.x(), velocity.y(), velocity.z()});
            arrowN.setPhysicsObject(arrowPhysObj);
            System.out.println("Shoot gun");
        }catch(Exception err){
            err.printStackTrace();
        }
    }
}
