package myGameEngine;

import javafx.scene.Scene;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class CheckIfAbovePlane {
    private SceneNode sceneNode;
    private boolean aboveYAxis;

    public CheckIfAbovePlane(){
        aboveYAxis = false;
    }

    public CheckIfAbovePlane(SceneNode sceneNode){
        this.sceneNode = sceneNode;
        aboveYAxis = false;
    }

    public boolean checkLocal(SceneNode sceneNode){
        this.sceneNode = sceneNode;
        if(sceneNode.getWorldPosition().y() >= 0.0f) {
            aboveYAxis = true;
        }
        else
            sceneNode.setLocalPosition(sceneNode.getWorldPosition().x(),(sceneNode.getWorldPosition().y() +.01f) ,sceneNode.getWorldPosition().z()); //Makes sure you don't get stuck at y = 0.00


        return aboveYAxis;
    }
}

