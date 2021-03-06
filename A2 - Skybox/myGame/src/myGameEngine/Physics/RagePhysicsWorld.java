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
    private SceneNode PlayerGroupN, RootNode, gndNode, worldObjectNodes,
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
        this.worldObjectNodes = (SceneNode) myGameObj.getEngine().getSceneManager().getSceneNode("worldObjectsN");
        this.gndNode = myGameObj.getEngine().getSceneManager().getSceneNode("TessN");
        this.characterNode = (SceneNode)PlayerGroupN.getChild("myCubeNode");

    }
    public void createRagePhysicsWorld(){
        float mass = 1.0f;
        float staticObjMass = 0.0f;
        float up[] = {0,1,0};
        float treeSize[] = {20.50f, 1000.0f, 20.50f};
        double[] temptf;
        PhysicsObject gndPlaneP, treePhysObj,characterPhysObj;

        //Making phys object for tree
        for(int i = 1; i < 25; i++){
            temptf = arrayConversion.toDoubleArray(worldObjectNodes.getChild("treeNode" + i).getLocalTransform().toFloatArray());
//        treePhysObj = physicsEng.addBoxObject(physicsEng.nextUID(), staticObjMass, temptf, treeSize);
            treePhysObj = physicsEng.addSphereObject(physicsEng.nextUID(), staticObjMass, temptf, 4.0f);
            treePhysObj.setBounciness(.20f);
            worldObjectNodes.getChild("treeNode" + i).setPhysicsObject(treePhysObj);
        }

        //Making phys obj for character
//        temptf = arrayConversion.toDoubleArray(characterNode.getLocalTransform().toFloatArray());
//        characterPhysObj = physicsEng.addBoxObject(physicsEng.nextUID(), mass, temptf, sizeOfCubeCharacter);
//        characterPhysObj.setBounciness(1.0f);
//        characterNode.setPhysicsObject(characterPhysObj);


        //Making ground plane phys obj
        temptf = arrayConversion.toDoubleArray(gndNode.getLocalTransform().toFloatArray());

//      gndPlaneP = physicsEng.addStaticPlaneObject(physicsEng.nextUID(), temptf, up, 0f);
        gndPlaneP = physicsEng.addSphereObject(physicsEng.nextUID(), staticObjMass, temptf, 5.0f);
        gndPlaneP.setBounciness(1.0f);
//        gndNode.setLocalPosition(0,20,0);
        gndNode.setLocalScale(70,100,70);
        gndNode.setLocalPosition(0,0,0);
        gndNode.setPhysicsObject(gndPlaneP);
// can also set damping, friction, etc.
    }

}
