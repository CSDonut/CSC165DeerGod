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
    private String direct;
    private boolean model;
    float posN00 = .6f,posN02 = .5f,posN20 = .5f,posN22 = -.6f;


    public GhostAvatar(UUID id, Vector3 position, boolean modelType) {
        this.id = id;
        this.pos = position;
        this.model = modelType;
    }

    public Entity getEntity() {
        return entity;
    }

    public float getDirect(int i){

        if (i == 0){
            return posN22;
        }else
        if (i == 2){
            return posN20;
        }else
        if (i == 6){
            return posN02;
        }else
        if (i == 8){
            return posN00;
        }
        else return 1;
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

    public void setRotate(float pos00, float pos02, float pos20,float pos22){
        posN00 = pos00;
        posN02 = pos02;
        posN20 = pos20;
        posN22 = pos22;
    }

}
