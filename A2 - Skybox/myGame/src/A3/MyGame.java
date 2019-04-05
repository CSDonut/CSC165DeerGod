package A3;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import Network.Client.GhostAvatar;
import Network.Client.ProtocolClient;
import myGameEngine.Controllers.BounceController;
import myGameEngine.Controllers.StretchController;
import myGameEngine.GamepadCommands.*;
import myGameEngine.KeyboardCommands.*;
import myGameEngine.Controllers.Camera3PController;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import ray.input.GenericInputManager;
import ray.input.action.AbstractInputAction;
import ray.rage.*;
import ray.rage.asset.texture.*;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.rendersystem.shader.GpuShaderProgram;
import ray.rage.rendersystem.states.FrontFaceState;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;
import ray.rage.util.BufferUtil;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.input.*;
import ray.networking.IGameConnection.ProtocolType;

import ray.rage.util.*;
import java.awt.geom.*;


public class MyGame extends VariableFrameRateGame {
    // to minimize variable allocation in update()
    private static final String SKYBOX_NAME = "SkyBox";
    private boolean skyBoxVisible = true;
    private String serverAddress;
    private int serverPort;
    private ProtocolType serverProtocol;
    private ProtocolClient protClient;
    private boolean isClientConnected;
    private Vector<UUID> gameObjectsToRemove;

    GL4RenderSystem rs;
    float elapsTime = 0.0f;
    String elapsTimeStr, planetsVisitedString, dispStr, collectedArtifactsString;
    int elapsTimeSec, planetsVisited = 0, collectedArtifacts = 0;
    final int MAXPLANETS = 5;

    private InputManager im;
    private Camera3PController orbitController, orbitController2;
    private Vector<GhostAvatar> ghostList = new Vector<GhostAvatar>();
    boolean ghostListEmpty = true;
    String[] textureNames = {"blue.jpeg", "hexagons.jpeg", "red.jpeg", "moon.jpeg", "chain-fence.jpeg"};
    SceneNode [] planetN, planetsVisitedN;
    SceneNode planetGroupN, playerGroupN, StretchGroupN,BounceGroupN;
    Entity [] planets;
    Entity alienArtifactsE;


    public MyGame(String serverAddr, int sPort) {
        super();
        planetsVisitedN = new SceneNode[MAXPLANETS];
        this.serverAddress = serverAddr;
        this.serverPort = sPort;
        this.serverProtocol = ProtocolType.UDP;
        System.out.println("Left joystick on gamepad controls movement");
        System.out.println("Right joystick controls camera controls");
        System.out.println("Triggers control roll");
        System.out.println("Y button controls camera toggle");
        System.out.println("Start button ends game");

    }

    public static void main(String[] args) {
        Game game = new MyGame("192.168.1.27", Integer.parseInt("59000"));
        try {
            game.startup();
            game.run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            game.shutdown();
            game.exit();
        }
    }

    @Override
    protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
        rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
    }


    @Override
    protected void setupCameras(SceneManager sm, RenderWindow rw) {
        //Camera Nodes are set up and attached inside of setupScene
        SceneNode rootNode = sm.getRootSceneNode();
        Camera camera = sm.createCamera("MainCamera", Projection.PERSPECTIVE);
        rw.getViewport(0).setCamera(camera);
        SceneNode cameraN =
                rootNode.createChildSceneNode("MainCameraNode");
        cameraN.attachObject(camera);
        camera.setMode('n');
        camera.getFrustum().setFarClipDistance(1000.0f);

//        Camera camera2 = sm.createCamera("MainCamera2", Projection.PERSPECTIVE);
//        rw.getViewport(1).setCamera(camera2);
//        SceneNode cameraN2 =
//                rootNode.createChildSceneNode("MainCamera2Node");
//        cameraN2.attachObject(camera2);
//        camera2.setMode('n');
//        camera2.getFrustum().setFarClipDistance(1000.0f);
    }

    // now we add setting up viewports in the window
//    protected void setupWindowViewports(RenderWindow rw) {
//        rw.addKeyListener(this);
//        Viewport topViewport = rw.getViewport(0);
//        topViewport.setDimensions(.51f, .01f, .99f, .49f); // B,L,W,H
//        topViewport.setClearColor(new Color(1.0f, .7f, .7f));
//        Viewport botViewport = rw.createViewport(.01f, .01f, .99f, .49f);
//        botViewport.setClearColor(new Color(.5f, 1.0f, .5f));
//    }

    protected void checkDistancePlanetToPlayer(Engine engine){
        float dolphinDistX, dolphinDistY, dolphinDistZ;
        float cubeDistX, cubeDistY, cubeDistZ;
        float maxDistance = 4;
        boolean flag = false;
        SceneManager sm = engine.getSceneManager();
        SceneNode dolphinN = sm.getSceneNode("myDolphinNode");
        SceneNode cubeN = sm.getSceneNode("myCubeNode");


        for(int i = 0; i <  MAXPLANETS; i++){
            dolphinDistX = Math.abs(planetN[i].getLocalPosition().x() - dolphinN.getLocalPosition().x());
            dolphinDistY = Math.abs(planetN[i].getLocalPosition().y() - dolphinN.getLocalPosition().y());
            dolphinDistZ = Math.abs(planetN[i].getLocalPosition().z() - dolphinN.getLocalPosition().z());
            cubeDistX = Math.abs(planetN[i].getLocalPosition().x() - cubeN.getLocalPosition().x());
            cubeDistY = Math.abs(planetN[i].getLocalPosition().y() - cubeN.getLocalPosition().y());
            cubeDistZ = Math.abs(planetN[i].getLocalPosition().z() - cubeN.getLocalPosition().z());

            //Checks to see if you have visited a planet, if not, increment planetsvisited if you are close enough
            if(!(planetN[i] == planetsVisitedN[i])) {
                for(Node node : StretchGroupN.getChildNodes()){
                    if (dolphinDistX < maxDistance && dolphinDistY < maxDistance && dolphinDistZ < maxDistance) {
                        planetsVisitedN[i] = planetN[i];
                        planetN[i] = StretchGroupN.createChildSceneNode("StretchPlanetN" + i);
                        StretchGroupN.attachObject(planets[i]);
                        planetN[i].notifyAttached(StretchGroupN);
                        planetsVisited++;
                    }
                }

//                } else if (cubeDistX < maxDistance && cubeDistY < maxDistance && cubeDistZ < maxDistance) {
//                    planetsVisitedN[i] = planetN[i];
//                    planetN[i] = BounceGroupN.createChildSceneNode("BouncePlanetN " + i);
//                    BounceGroupN.attachObject(planets[i]);
//                    planetN[i].notifyAttached(BounceGroupN);
//                    planetsVisited++;
//                }
            }
        }



    }


    public boolean checkDistanceDolphinCamera(){
        float DistX, DistY, DistZ;
        DistX = DistY = DistZ = 0;
        float maxDistance = 8; // Farthest distance you can get away from the dolphin before it spawns you back on top
        Camera mainCamN = getEngine().getSceneManager().getCamera("MainCamera");
        SceneNode dolphinN = getEngine().getSceneManager().getSceneNode("myDolphinNode");


        if(mainCamN.getMode() == 'c'){
            DistX = Math.abs(mainCamN.getPo().x() - dolphinN.getLocalPosition().x());
            DistY = Math.abs(mainCamN.getPo().y() - dolphinN.getLocalPosition().y());
            DistZ = Math.abs(mainCamN.getPo().z() - dolphinN.getLocalPosition().z());

            if(DistX > maxDistance || DistY > maxDistance || DistZ > maxDistance)
                return false;
        }
        return true;
    }



    @Override
    protected void setupScene(Engine eng, SceneManager sm) throws IOException {
        TextureManager tm = eng.getTextureManager();
        RenderSystem rs = sm.getRenderSystem();
        Texture texture = tm.getAssetByPath("red.jpeg");
        TextureState state = (TextureState)rs.createRenderState(RenderState.Type.TEXTURE);
        Angle rotAmt = Degreef.createFrom(180.0f);
        ManualObject manObjGroundPlane;

        // set up sky box
        Configuration conf = eng.getConfiguration();
        tm.setBaseDirectoryPath(conf.valueOf("assets.skyboxes.path"));
        Texture front = tm.getAssetByPath("front.jpg");
        Texture back = tm.getAssetByPath("back.jpg");
        Texture left = tm.getAssetByPath("left.jpg");
        Texture right = tm.getAssetByPath("right.jpg");
        Texture top = tm.getAssetByPath("top.jpg");
        Texture bottom = tm.getAssetByPath("bottom.jpg");
        tm.setBaseDirectoryPath(conf.valueOf("assets.textures.path"));
// cubemap textures are flipped upside-down.
// All textures must have the same dimensions, so any image’s
// heights will work since they are all the same height
        AffineTransform xform = new AffineTransform();
        xform.translate(0, front.getImage().getHeight());
        xform.scale(1d, -1d);
        front.transform(xform);
        back.transform(xform);
        left.transform(xform);
        right.transform(xform);
        top.transform(xform);
        bottom.transform(xform);
        SkyBox sb = sm.createSkyBox(SKYBOX_NAME);
        sb.setTexture(front, SkyBox.Face.FRONT);
        sb.setTexture(back, SkyBox.Face.BACK);
        sb.setTexture(left, SkyBox.Face.LEFT);
        sb.setTexture(right, SkyBox.Face.RIGHT);
        sb.setTexture(top, SkyBox.Face.TOP);
        sb.setTexture(bottom, SkyBox.Face.BOTTOM);
        sm.setActiveSkyBox(sb);
//End skybox

        //Stretch planet group
        StretchGroupN = sm.getRootSceneNode().createChildSceneNode("StretchGroupNode");
        //Bounce planet group
        BounceGroupN = sm.getRootSceneNode().createChildSceneNode("BounceGroupNode");

        //Planet code
        planets = new Entity[MAXPLANETS];
        planetN = new SceneNode[MAXPLANETS];
        planetGroupN = sm.getRootSceneNode().createChildSceneNode("planetGroupNode");

        for(int i = 0; i < MAXPLANETS; i++){
            planets[i] = sm.createEntity("Planet" + i , "earth.obj");
            planets[i].setPrimitive(Primitive.TRIANGLES);
            planetN[i] = planetGroupN.createChildSceneNode(planets[i].getName() + "Node");
            planetN[i].moveForward((float)new Random().nextInt(50));
            planetN[i].moveBackward((float)new Random().nextInt(50));
            planetN[i].moveLeft((float)new Random().nextInt(10));
            planetN[i].moveRight((float)new Random().nextInt(10));
            planetN[i].attachObject(planets[i]);
        }


//        manObjGroundPlane = makePlane(eng, sm);
//        manObjGroundPlane.setPrimitive(Primitive.TRIANGLES);
//        SceneNode groundPlaneN = sm.getRootSceneNode().createChildSceneNode("groundPlaneN");
//        groundPlaneN.attachObject(manObjGroundPlane);
//        groundPlaneN.scale((float)40.0, (float)40.0, (float)40.0);

        playerGroupN = sm.getRootSceneNode().createChildSceneNode("playerGroupNode");
        //Dolphin code

 //       Entity dolphinE = sm.createEntity("myDolphin", "dolphinHighPoly.obj");
 //       dolphinE.setPrimitive(Primitive.TRIANGLES);
//        Camera camera = sm.getCamera("MainCamera");

//        SceneNode dolphinN = playerGroupN.createChildSceneNode("myDolphinNode");
//        dolphinN.moveBackward(3.0f);
//        dolphinN.attachObject(dolphinE);
//        dolphinN.yaw(rotAmt);
//        SceneNode dolphCamNode = dolphinN.createChildSceneNode("DolphCamNode");
        //dolphCamNode.attachObject(camera);
        //dolphCamNode.setLocalPosition(Vector3f.createFrom(0.0f, 0.5f, -0.5f));

        //Cube code
        Entity cubeE = sm.createEntity("myCube", "cube.obj");
        cubeE.setPrimitive(Primitive.TRIANGLES);
        //Camera camera2 = sm.getCamera("MainCamera2");

        SceneNode cubeN = playerGroupN.createChildSceneNode("myCubeNode");
        cubeN.moveBackward(5.0f);
        cubeN.moveUp(0.1f);
        cubeN.attachObject(cubeE);
        cubeN.scale(.3f,.3f,.3f);

        SceneNode CubeNode =  cubeN.createChildSceneNode("CubeCamNode");
//        CubeNode.attachObject(camera2);
        //CubeNode.setLocalPosition(Vector3f.createFrom(0.0f, 0.5f, -0.5f));




        //Light code
        sm.getAmbientLight().setIntensity(new Color(.1f, .1f, .1f));

        Light plight = sm.createLight("testLamp1", Light.Type.POINT);
        plight.setAmbient(new Color(.3f, .3f, .3f));
        plight.setDiffuse(new Color(.7f, .7f, .7f));
        plight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        plight.setRange(5f);

        SceneNode plightNode = sm.getRootSceneNode().createChildSceneNode("plightNode");
        plightNode.attachObject(plight);

        //Rotation code
        RotationController rc = new RotationController(Vector3f.createUnitVectorY(), .02f);

        //Stretch controller
        StretchController sc = new StretchController();

        //Bounce Controller
        BounceController bc = new BounceController();



        for(int i = 0; i < MAXPLANETS; i++)
            rc.addNode(planetN[i]);

        sc.addNode(StretchGroupN);
        sc.addNode(BounceGroupN);
        sm.addController(rc);
        sm.addController(sc);
        sm.addController(bc);

        setupInputs();

        //orbitController
        setupOrbitCamera(eng, sm);
        //dolphinN.yaw(Degreef.createFrom(45.0f));
        cubeN.yaw(Degreef.createFrom(45.0f));

        setupNetworking();


    }

    //============= Networking ==========================================

    private void setupNetworking()
    { gameObjectsToRemove = new Vector<UUID>();
        isClientConnected = false;
        try
        { protClient = new ProtocolClient(InetAddress.
                getByName(serverAddress), serverPort, serverProtocol, this);
        } catch (IOException e) { e.printStackTrace();
        }
        if (protClient == null)
        { System.out.println("missing protocol host"); }
        else
        { // ask client protocol to send initial join message
            //to server, with a unique identifier for this client
            protClient.sendJoinMessage();
        } }

    protected void processNetworking(float elapsTime)
    { // Process packets received by the client from the server
        if (protClient != null)
            protClient.processPackets();
        // remove ghost avatars for players who have left the game
        Iterator<UUID> it = gameObjectsToRemove.iterator();
        while(it.hasNext())
        { getEngine().getSceneManager().destroySceneNode(it.next().toString());
        }
        gameObjectsToRemove.clear();
    }

    public Vector3 getPlayerPosition()
    { SceneNode dolphinN = getEngine().getSceneManager().getSceneNode("myCubeNode");
        return dolphinN.getWorldPosition();
    }

    public void addGhostAvatarToGameWorld(GhostAvatar avatar)
            throws IOException
    { if (avatar != null) {
        ghostList.add(avatar);
        ghostListEmpty = false;
        Entity ghostE = getEngine().getSceneManager().createEntity("ghost", "dolphinHighPoly.obj");
        ghostE.setPrimitive(Primitive.TRIANGLES);
        SceneNode ghostN = getEngine().getSceneManager().getRootSceneNode().
                createChildSceneNode(avatar.getId().toString());
        System.out.println(avatar.getId());
        ghostN.attachObject(ghostE);
        ghostN.setLocalPosition(avatar.getPos().x(), avatar.getPos().y() ,avatar.getPos().z());
        avatar.setNode(ghostN);
        avatar.setEntity(ghostE);
        //avatar.setPosition(0,0,0);

    } }

    public void removeGhostAvatarFromGameWorld(GhostAvatar avatar)
    { if(avatar != null) gameObjectsToRemove.add(avatar.getId());
    }

    public void setIsConnected(boolean b) {
        isClientConnected = b;
    }

    public ProtocolClient getProtClient() {
        return protClient;
    }

    public boolean getisClientConnected() {
        return isClientConnected;
    }

    public void updateGhostPosition(){

        if(!ghostListEmpty){
           Iterator<GhostAvatar> iterate = ghostList.iterator();
           while (iterate.hasNext()){
               GhostAvatar temp = iterate.next();
               temp.getNode().setLocalPosition(temp.getPos());
           }
        }
    }

    //============ END Networking =====================================


    protected void setupOrbitCamera(Engine eng, SceneManager sm) {
//        SceneNode dolphinN = sm.getSceneNode("myDolphinNode");
//        SceneNode cameraN = sm.getSceneNode("MainCameraNode");
//        Camera camera = sm.getCamera("MainCamera");
//        String msName = im.getMouseName();
//        orbitController = new Camera3PController(camera, cameraN, dolphinN, msName, im);

        SceneNode cubeN = sm.getSceneNode("myCubeNode");
        SceneNode cameraN = sm.getSceneNode("MainCameraNode");
        Camera camera = sm.getCamera("MainCamera");
        String gpName = im.getFirstGamepadName();
        orbitController = new Camera3PController(camera, cameraN, cubeN, gpName, im);

    }
    
    protected ManualObject makePlane(Engine eng, SceneManager sm) throws IOException {
        ManualObject line = sm.createManualObject("GroundPlane");
        ManualObjectSection lineSec =
                line.createManualSection("LineSelection");
        line.setGpuShaderProgram(sm.getRenderSystem().
                getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));

        float[] vertices = new float[]{
                -1.0f, -0.01f, 1.0f, 1.0f,  -0.01f, 1.0f, -1.0f,   -0.01f, -1.0f,
                -1.0f,  -0.01f, -1.0f, 1.0f,   -0.01f, 1.0f, -1.0f,    -0.01f, 1.0f, //UF
                1.0f,    -0.01f, -1.0f, -1.0f,   -0.01f, -1.0f, 1.0f,    -0.01f, 1.0f,
                1.0f,    -0.01f, 1.0f, -1.0f,    -0.01f, -1.0f, 1.0f,    -0.01f, -1.0f //UR
        };

        float[] texcoords = new float[]{
                0.0f, 0.0f, 1.0f,0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,0.0f, 0.0f, 1.0f,
        };

        float[] normals = new float[]{
                0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f

        };

        int[] indices = new int[] {0,1,2,3,4,5,6,7,8,9,10,11};
        FloatBuffer vertBuf = BufferUtil.directFloatBuffer(vertices);
        FloatBuffer texBuf = BufferUtil.directFloatBuffer(texcoords);
        FloatBuffer normBuf = BufferUtil.directFloatBuffer(normals);
        IntBuffer indexBuf = BufferUtil.directIntBuffer(indices);
        lineSec.setVertexBuffer(vertBuf);
        lineSec.setTextureCoordsBuffer(texBuf);
        lineSec.setNormalsBuffer(normBuf);
        lineSec.setIndexBuffer(indexBuf);
        Texture tex =
                eng.getTextureManager().getAssetByPath("chain-fence.jpeg");
        TextureState texState = (TextureState)sm.getRenderSystem().
                createRenderState(RenderState.Type.TEXTURE);
        texState.setTexture(tex);
        FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().
                createRenderState(RenderState.Type.FRONT_FACE);
        line.setDataSource(DataSource.INDEX_BUFFER);
        line.setRenderState(texState);
        line.setRenderState(faceState);
        return line;
    }

    protected ManualObject makePyramid(Engine eng, SceneManager sm)
            throws IOException
    { ManualObject pyr = sm.createManualObject("Pyramid");
        ManualObjectSection pyrSec =
                pyr.createManualSection("PyramidSection");
        pyr.setGpuShaderProgram(sm.getRenderSystem().
                getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
        float[] vertices = new float[]
                { -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, //front
                        1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, //right
                        1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, //back
                        -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, //left
                        0.0f, -2.0f, 0.0f,1.0f, -1.0f, 1.0f,-1.0f, -1.0f, 1.0f, //bottom front
                        0.0f, -2.0f, 0.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, //bottom right
                        0.0f, -2.0f, 0.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, //bottom left
                        0.0f, -2.0f, 0.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f //bottom back

                };
        float[] texcoords = new float[]
                { 0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
                        0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
                        0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
                        0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f
                };
        float[] normals = new float[]
                { 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f,
                        -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f
                };
        int[] indices = new int[] { 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18, 19, 20, 21, 22, 23, 24, 25, 26};
        FloatBuffer vertBuf = BufferUtil.directFloatBuffer(vertices);
        FloatBuffer texBuf = BufferUtil.directFloatBuffer(texcoords);
        FloatBuffer normBuf = BufferUtil.directFloatBuffer(normals);
        IntBuffer indexBuf = BufferUtil.directIntBuffer(indices);
        pyrSec.setVertexBuffer(vertBuf);
        pyrSec.setTextureCoordsBuffer(texBuf);
        pyrSec.setNormalsBuffer(normBuf);
        pyrSec.setIndexBuffer(indexBuf);
        Texture tex =
                eng.getTextureManager().getAssetByPath("red.jpeg");
        TextureState texState = (TextureState)sm.getRenderSystem().
                createRenderState(RenderState.Type.TEXTURE);
        texState.setTexture(tex);
        FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().
                createRenderState(RenderState.Type.FRONT_FACE);
        pyr.setDataSource(DataSource.INDEX_BUFFER);
        pyr.setRenderState(texState);
        pyr.setRenderState(faceState);
        return pyr;
    }
    @Override
    protected void update(Engine engine) {
        // build and set HUD
        rs = (GL4RenderSystem) engine.getRenderSystem();
//        checkDistancePlanetToPlayer(engine);
        elapsTime += engine.getElapsedTimeMillis();
        elapsTimeSec = Math.round(elapsTime/1000.0f);
        elapsTimeStr = Integer.toString(elapsTimeSec);
        planetsVisitedString = Integer.toString(planetsVisited);
        collectedArtifactsString= Integer.toString(collectedArtifacts);
        dispStr = "Cube position " + elapsTimeStr;
        rs.setHUD(dispStr, 15, 15);
        dispStr = "Dolphin Time = " + elapsTimeStr;
        rs.setHUD2(dispStr, 15, (rs.getRenderWindow().getViewport(0).getActualHeight() + 20));
        im.update(elapsTime);
        orbitController.updateCameraPosition();
        updateGhostPosition();
//        orbitController2.updateCameraPosition();

        processNetworking(elapsTime);
    }

    protected void setupInputs(){
        im = new GenericInputManager();
        //Creating action objects
        MoveForwardBackwardAction moveForwardBackwardCmd = new MoveForwardBackwardAction(this, protClient);
        MoveLeftRightAction moveLeftRightCmd = new MoveLeftRightAction(this,protClient);
        QuitGameAction quitGameCmd = new QuitGameAction(this);
        CameraYawAction CameraYawCmd = new CameraYawAction(this);
        CameraPitchAction CameraPitchCmd = new CameraPitchAction(this);
        CameraTiltAction CameraTiltCmd = new CameraTiltAction(this);


        ArrayList controllers = im.getControllers();
        for(int i = 0; i < controllers.size(); i++){
            Controller c = (Controller)controllers.get(i);

            if (c.getType() == Controller.Type.KEYBOARD) {
                //Quit game action using q key on keyboard
                im.associateAction(c, Component.Identifier.Key.Q, quitGameCmd, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

                //move forward action using key W on keyboard
                im.associateAction(c, Component.Identifier.Key.W, new KBMoveForwardAction(this), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

                //move forward action using key S on keyboard
                im.associateAction(c, Component.Identifier.Key.S, new KBMoveBackwardAction(this), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

                //move forward action using key D on keyboard
                im.associateAction(c, Component.Identifier.Key.D, new KBMoveRightAction(this), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

                //move forward action using key A on keyboard
                im.associateAction(c, Component.Identifier.Key.A, new KBMoveLeftAction(this), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

                //yaw camera using left arrow  key
                im.associateAction(c, Component.Identifier.Key.LEFT, new KBCameraYawLeftAction(this), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

                //yaw camera using right arrow  key
                im.associateAction(c, Component.Identifier.Key.RIGHT, new KBCameraYawAction(this), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

                //Pitch camera down using down arrow key
                im.associateAction(c, Component.Identifier.Key.DOWN, new KBCameraPitchDownAction(this), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

                //Pitch camera down using up arrow key
                im.associateAction(c, Component.Identifier.Key.UP, new KBCameraPitchUpAction(this), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
            }
            else if (c.getType() == Controller.Type.GAMEPAD || c.getType() == Controller.Type.STICK) {
                //Code for gamepad usage

                //move forward and backward using y axis joystick on gamepad(Xbox controller)
                im.associateAction(c, Component.Identifier.Axis.Y, moveForwardBackwardCmd, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

                //move left and right using x axis joystick on gamepad(Xbox controller)
                im.associateAction(c, Component.Identifier.Axis.X, moveLeftRightCmd, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

//                //yaw camera using right joystick on gamepad(Xbox controller)
//                im.associateAction(c, Component.Identifier.Axis.RX, CameraYawCmd, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
//
//                //pitch camera using right joystick on gamepad(Xbox controller)
//                im.associateAction(c, Component.Identifier.Axis.RY, CameraPitchCmd, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
//
//                //Tilt camera using left and right triggers on gamepad(Xbox controller)
//                im.associateAction(c, Component.Identifier.Axis.Z, CameraTiltCmd, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

                //Quit game action using button 7(Start button) on gamepad
                im.associateAction(c, net.java.games.input.Component.Identifier.Button._7, quitGameCmd, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
            }
        }
    }

}
