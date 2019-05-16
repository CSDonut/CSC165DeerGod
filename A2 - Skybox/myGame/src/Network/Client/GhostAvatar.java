package Network.Client;

import ray.rage.scene.Entity;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;
import ray.rml.Vector3f;

import java.util.UUID;

public class GhostAvatar {

    private UUID id;
    private SceneNode node;
    private Entity entity;
    private Vector3 pos;
    private float direct;
    private boolean model;


    public GhostAvatar(UUID id, Vector3 position, boolean modelType) {
        this.id = id;
        this.pos = position;
        this.model = modelType;
        this.direct = 0;
    }

    public Entity getEntity() {
        return entity;
    }

    public float getDirect(){
        return direct;
    }

    public void resetDirect(){
        direct = 0;
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

    public Boolean getModel(){
        return model;

    }

    public void setNode(SceneNode ghostN) {
        this.node = ghostN;
    }

    public void setEntity(Entity ghostE) {
        this.entity = ghostE;
    }

    public void setPosition(Vector3 newPosition) {
        pos = newPosition;
    }

    public void setRotate(float direction){
        direct = direction;
    }

}
