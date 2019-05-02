package myGameEngine.Physics;
        import A3.MyGame;
        import myGameEngine.Physics.*;
        import javafx.scene.Scene;
        import ray.physics.PhysicsEngine;
        import ray.physics.PhysicsObject;
        import ray.physics.PhysicsEngineFactory;
        import ray.rage.scene.SceneNode;
        import ray.rml.Vector3f;

public class RagePhysicsWorld {
    private SceneNode PlayerGroupN, RootNode, gndNode, treeNode,
                        characterNode;
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
        this.characterNode = (SceneNode)PlayerGroupN.getChild("myCubeNode");

    }
    public void createRagePhysicsWorld(){
        float mass = 1.0f;
        float staticObj = 0.0f;
        float up[] = {0,1,0};
        float sizeOfCubeCharacter[] = {.3f, .3f, .3f};
        double[] temptf;
        PhysicsObject gndPlaneP, treePhysObj,characterPhysObj;

        //Making phys object for tree
        temptf = arrayConversion.toDoubleArray(treeNode.getLocalTransform().toFloatArray());
        treePhysObj = physicsEng.addSphereObject(physicsEng.nextUID(), staticObj, temptf, 2.0f);
        treePhysObj.setBounciness(1.0f);
        treeNode.setPhysicsObject(treePhysObj);

        //Making phys obj for character
//        temptf = arrayConversion.toDoubleArray(characterNode.getLocalTransform().toFloatArray());
//        characterPhysObj = physicsEng.addBoxObject(physicsEng.nextUID(), mass, temptf, sizeOfCubeCharacter);
//        characterPhysObj.setBounciness(1.0f);
//        characterNode.setPhysicsObject(characterPhysObj);


        //Making ground plane phys obj
        temptf = arrayConversion.toDoubleArray(gndNode.getLocalTransform().toFloatArray());
        gndPlaneP = physicsEng.addStaticPlaneObject(physicsEng.nextUID(),
                temptf, up, 0.0f);
        gndPlaneP.setBounciness(1.0f);
//        gndNode.setLocalPosition(0,20,0);
        gndNode.setPhysicsObject(gndPlaneP);
// can also set damping, friction, etc.
    }

}
