package myGameEngine.Physics;
        import A3.MyGame;
        import myGameEngine.Physics.*;
        import javafx.scene.Scene;
        import ray.physics.PhysicsEngine;
        import ray.physics.PhysicsObject;
        import ray.physics.PhysicsEngineFactory;
        import ray.rage.scene.SceneNode;

public class RagePhysicsWorld {
    private SceneNode PlayerGroupN, RootNode, gndNode, treeNode;
    private PhysicsEngine physicsEng;
    private ArrayConversion arrayConversion;
    private MyGame myGameObj;
    public RagePhysicsWorld(MyGame myGameObj, PhysicsEngine physicsEng){
        this.myGameObj = myGameObj;
        this.PlayerGroupN = myGameObj.getEngine().getSceneManager().getSceneNode("playerGroupNode");
        this.RootNode = myGameObj.getEngine().getSceneManager().getRootSceneNode();
        this.physicsEng = physicsEng;
        this.arrayConversion = new ArrayConversion();

        //Game Nodes
        this.treeNode = (SceneNode)RootNode.getChild("treeNode");
        this.gndNode = (SceneNode)RootNode.getChild("TessN");
    }
    public void createRagePhysicsWorld(){
        float mass = 1.0f;
        float up[] = {0,1,0};
        double[] temptf;
        PhysicsObject gndPlaneP, treePhysObj;

        //Making phys object for tree
        temptf = arrayConversion.toDoubleArray(treeNode.getLocalTransform().toFloatArray());
        treePhysObj = physicsEng.addSphereObject(physicsEng.nextUID(), mass, temptf, 2.0f);
//        treeP.setBounciness(1.0f);
//        treeNode.scale(1.0f, 1.0f, 1.0f);
//        treeNode.setLocalPosition(treeNode.getLocalPosition());
        treeNode.setPhysicsObject(treePhysObj);

        //Making ground plane phys obj
        temptf = arrayConversion.toDoubleArray(gndNode.getLocalTransform().toFloatArray());
        gndPlaneP = physicsEng.addStaticPlaneObject(physicsEng.nextUID(),
                temptf, up, 0.0f);
        gndPlaneP.setBounciness(1.0f);
//        gndNode.scale(3f, .05f, 3f);
//        gndNode.setLocalPosition(0, -7, -2);
        gndNode.setPhysicsObject(gndPlaneP);
// can also set damping, friction, etc.
    }

}
