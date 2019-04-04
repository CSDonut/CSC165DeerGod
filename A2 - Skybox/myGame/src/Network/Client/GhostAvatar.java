package Network.Client;

import ray.rage.scene.Entity;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;

import java.util.UUID;

public class GhostAvatar {

    private UUID id;
    private SceneNode node;
    private Entity entity;
    public GhostAvatar(UUID id, Vector3 position) {
        this.id = id;
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

    public void setNode(SceneNode ghostN) {
    }

    public void setEntity(Entity ghostE) {
    }

    public void setPosition(int i, int j, int k) {
    }

}
