package Network.Client;

import ray.rage.scene.Entity;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;

import java.util.UUID;

public class GhostAvatar {

    private UUID id;
    private SceneNode node;
    private Entity entity;
    private Vector3 pos;


    public GhostAvatar(UUID id, Vector3 position) {
        this.id = id;
        this.pos = position;
    }

    public Entity getEntity() {
        return entity;
    }

    public UUID getId() {
        return id;
    }

    public SceneNode getNode() {
        return node;
    }

    public Vector3 getPos() {
        return pos;
    }

    public void setNode(SceneNode ghostN) {
        this.node = ghostN;
    }

    public void setEntity(Entity ghostE) {
        this.entity = ghostE;
    }

    public void setPosition(float i, float j, float k) {



    }



}
