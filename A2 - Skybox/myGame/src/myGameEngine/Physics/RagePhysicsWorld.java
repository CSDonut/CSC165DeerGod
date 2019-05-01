package myGameEngine.Physics;
        import A3.MyGame;
        import myGameEngine.Physics.*;
        import javafx.scene.Scene;
        import ray.physics.PhysicsEngine;
        import ray.physics.PhysicsObject;
        import ray.physics.PhysicsEngineFactory;
        import ray.rage.scene.SceneNode;

public class RagePhysicsWorld {
    private SceneNode PlayerGroupN, RootNode, gndNode;
    private PhysicsEngine physicsEng;
    private ArrayConversion arrayConversion;
    public RagePhysicsWorld(MyGame myGameObj, PhysicsEngine physicsEng){
        this.PlayerGroupN = myGameObj.getEngine().getSceneManager().getSceneNode("playerGroupNode");
        this.RootNode = myGameObj.getEngine().getSceneManager().getRootSceneNode();
        this.physicsEng = physicsEng;
        this.gndNode = (SceneNode)RootNode.getChild("TessN");
        this.arrayConversion = new ArrayConversion();
    }
    private void createRagePhysicsWorld(){
        float mass = 1.0f;
        float up[] = {0,1,0};
        double[] temptf;
        PhysicsObject gndPlaneP;

        temptf = arrayConversion.toDoubleArray(gndNode.getLocalTransform().toFloatArray());
        gndPlaneP = physicsEng.addStaticPlaneObject(physicsEng.nextUID(),
                temptf, up, 0.0f);
        gndPlaneP.setBounciness(1.0f);
        gndNode.scale(3f, .05f, 3f);
        gndNode.setLocalPosition(0, -7, -2);
        gndNode.setPhysicsObject(gndPlaneP);
// can also set damping, friction, etc.
    }


}
