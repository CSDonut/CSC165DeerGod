package myGameEngine.GamepadCommands;

import A3.MyGame;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class ToggleCameraAction extends AbstractInputAction{
    private Camera camera;
    private MyGame obj;




    public ToggleCameraAction(MyGame myGameObj){
        obj = myGameObj;
        camera = myGameObj.getEngine().getSceneManager().getCamera("MainCamera");
    }

    public void performAction(float time, Event e){


        SceneNode dolphinN = (SceneNode)obj.getEngine().getSceneManager().getRootSceneNode().getChild("myDolphinNode");
        SceneNode dolphCamN = (SceneNode)dolphinN.getChild("DolphCamNode");

        if(camera.getMode() == 'c'){
            System.out.println("Dolphin camera changed to n mode");
            camera.setMode('n');
            dolphinN.setLocalPosition((Vector3f)Vector3f.createFrom(dolphinN.getLocalPosition().x(),
                    dolphinN.getLocalPosition().y(), dolphinN.getLocalPosition().z()));
        }
        else{
            System.out.println("Dolphin camera changed to c mode");
            camera.setMode('c');
            camera.setPo((Vector3f)Vector3f.createFrom(dolphinN.getLocalPosition().x() + 0.30f, dolphinN.getLocalPosition().y() + 0.20f, dolphinN.getLocalPosition().z() + 0.20f));


        }


    }
}