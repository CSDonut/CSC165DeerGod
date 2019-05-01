package myGameEngine.Physics;
import ray.physics.PhysicsEngine;
import ray.physics.PhysicsObject;
import ray.physics.PhysicsEngineFactory;

public class PhysicsSystem {
    public void initPhysicsSystem(PhysicsEngine physicsEng){
        String engine = "ray.physics.JBullet.JBulletPhysicsEngine";
        float[] gravity = {0, -3f, 0};
        physicsEng = PhysicsEngineFactory.createPhysicsEngine(engine);
        physicsEng.initSystem();
        physicsEng.setGravity(gravity);
    }

}
