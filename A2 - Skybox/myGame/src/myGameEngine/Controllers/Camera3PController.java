package myGameEngine.Controllers;


import A3.MyGame;
import Network.Client.ProtocolClient;
import javafx.scene.Scene;
import ray.input.InputManager;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rml.*;

import ray.input.action.*;

import java.io.IOException;


public class Camera3PController {
    private Camera camera; //the camera being controlled
    private SceneNode cameraN; //the node the camera is attached to
    private MyGame games;
    private SceneNode target; //the target the camera looks at
    private SceneNode avatarN;
    private float cameraAzimuth; //rotation of camera around Y axis
    private float cameraElevation; //elevation of camera above target
    private float radias; //distance between camera and target
    private Vector3 targetPos; //target’s position in the world
    private Vector3 worldUpVec;
    private float camPitchAmt, camYawAmt, camZoomAmt;

    public Camera3PController(MyGame myGameObj, Camera cam, SceneNode camN, SceneNode targ, String controllerName, InputManager im) {
        games = myGameObj;
        camera = cam;
        cameraN = camN;
        target = targ;
        cameraAzimuth = 225.0f; // start from BEHIND and ABOVE the target
        cameraElevation = 20.0f; // elevation is in degrees
        radias = 2.0f;
        camPitchAmt = camYawAmt = 1.0f; //bigger the number, slower the values go
        camZoomAmt = 15.0f;
        worldUpVec = Vector3f.createFrom(0.0f, 1.0f, 0.0f);
        avatarN = (SceneNode)myGameObj.getEngine().getSceneManager().getSceneNode("myCubeNode");
        setupInput(im, controllerName);
        updateCameraPosition();
    }

    private void setupInput(InputManager im, String cn)
    {
        Action orbitAAction = new OrbitAroundAction();
        Action OrbitUpDownAction = new OrbitUpDownAction();

        im.associateAction(cn,
                net.java.games.input.Component.Identifier.Axis.RX, orbitAAction,
                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(cn,
                net.java.games.input.Component.Identifier.Axis.RY, OrbitUpDownAction,
                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(cn,
                net.java.games.input.Component.Identifier.Axis.Z, new RadiasAction(),
                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

    }

    private class OrbitAroundAction extends AbstractInputAction
    { // Moves the camera around the target (changes camera azimuth).
        public void performAction(float time, net.java.games.input.Event evt)
        {

            if(!games.getCharaSelect()){
                float rotAmount;
                if (evt.getValue() < -0.2) {
                    rotAmount = evt.getValue()/-camPitchAmt;
                    avatarN.yaw(Degreef.createFrom(rotAmount));
                }
                else
                { if (evt.getValue() > 0.2) {
                    rotAmount= evt.getValue()/-camPitchAmt;
                    avatarN.yaw(Degreef.createFrom(rotAmount));
                }
                else
                { rotAmount=0.0f;
                }
                }


                try {
                    games.sendRotateAmount(
                            avatarN.getLocalRotation().value(0,0),
                            avatarN.getLocalRotation().value(0,2),
                            avatarN.getLocalRotation().value(2,0),
                            avatarN.getLocalRotation().value(2,2)
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cameraAzimuth += rotAmount;
                cameraAzimuth = cameraAzimuth % 360;
                updateCameraPosition();
            }
        }
    }
    // similar for OrbitRadiasAction, OrbitElevationAction
    private class RadiasAction extends AbstractInputAction
    { // Moves the camera around the target (changes camera azimuth).
        public void performAction(float time, net.java.games.input.Event evt)
        {

            if(!games.getCharaSelect()){
                float zoomAmount;
                if (evt.getValue() < -0.2)
                { zoomAmount = evt.getValue()/camZoomAmt; }
                else
                { if (evt.getValue() > 0.2)
                { zoomAmount = evt.getValue()/camZoomAmt; }
                else
                { zoomAmount=0.1f; }
                }
                radias += zoomAmount;

                if (radias <= 0){
                    radias = .01f;
                }

                updateCameraPosition();

            }
        }
    }

    private class OrbitUpDownAction extends AbstractInputAction
    { // Moves the camera around the target (changes camera azimuth).
        public void performAction(float time, net.java.games.input.Event evt)
        {
            if(!games.getCharaSelect()){
                float rotAmount;
                if (evt.getValue() < -0.2)
                { rotAmount= evt.getValue()/camYawAmt; }
                else
                { if (evt.getValue() > 0.2)
                { rotAmount = evt.getValue()/camYawAmt; }
                else
                { rotAmount=0.0f; }
                }
                cameraElevation += rotAmount;
                cameraElevation = cameraElevation % 360;
                updateCameraPosition();
            }
        }
    }

    // Updates camera position: computes azimuth, elevation, and distance
    // relative to the target in spherical coordinates, then converts those
    // to world Cartesian coordinates and setting the camera position
    public void updateCameraPosition()
    {
        double theta = Math.toRadians(cameraAzimuth); // rot around target
        double phi = Math.toRadians(cameraElevation); // altitude angle
        double x = radias * Math.cos(phi) * Math.sin(theta);
        double y = radias * Math.sin(phi);
        double z = radias * Math.cos(phi) * Math.cos(theta);
        cameraN.setLocalPosition(Vector3f.createFrom
                ((float)x, (float)y, (float)z).add(target.getWorldPosition()));
        cameraN.lookAt(target, worldUpVec);

    }

}
