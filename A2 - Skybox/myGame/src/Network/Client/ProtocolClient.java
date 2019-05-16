package Network.Client;

import A3.MyGame;
import graphicslib3D.Vector3D;
import ray.networking.client.GameConnectionClient;
import ray.rml.Matrix3;
import ray.rml.Vector3;
import ray.rml.Vector3f;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

public class ProtocolClient extends GameConnectionClient {

    private MyGame game;
    private UUID id;
    private Vector<GhostAvatar> ghostAvatars;

    public ProtocolClient(InetAddress remAddr, int remPort, ProtocolType pType, MyGame game) throws IOException {

        super(remAddr, remPort, pType);
        this.game = game;
        this.id = UUID.randomUUID();
        this.ghostAvatars = new Vector<GhostAvatar>();
    }

    @Override
    protected void processPacket(Object msg) {
        String strMessage = (String) msg;
        String[] messageTokens = strMessage.split(",");
        if (messageTokens.length > 0) {

            if (messageTokens[0].compareTo("join") == 0) // receive “join”
            { // format: join, success or join, failure
                if (messageTokens[1].compareTo("success") == 0) {
                    game.setIsConnected(true);
                    sendCreateMessage(game.getPlayerPosition(), game.getDeerOrHunt());
                    System.out.println("Connected to server");
                }
                if (messageTokens[1].compareTo("failure") == 0) {
                    game.setIsConnected(false);
                    System.out.println("Failed to connect to server");
                }
            }

            if (messageTokens[0].compareTo("bye") == 0) {  // receive “bye” format: bye, remoteId
                UUID ghostID = UUID.fromString(messageTokens[1]);
                removeGhostAvatar(ghostID);
            }

            if ((messageTokens[0].compareTo("dsfr") == 0) // receive “dsfr”: format: create, remoteId, x,y,z or dsfr, remoteId, x,y,z
                    || (messageTokens[0].compareTo("create") == 0)) {
                UUID ghostID = UUID.fromString(messageTokens[1]);
                Vector3 ghostPosition = Vector3f.createFrom(
                        Float.parseFloat(messageTokens[2]),
                        Float.parseFloat(messageTokens[3]),
                        Float.parseFloat(messageTokens[4]));
                Boolean modelType = Boolean.parseBoolean(messageTokens[5]);
                try {
                    createGhostAvatar(ghostID, ghostPosition, modelType);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(messageTokens[0].compareTo("wsds") == 0) // rec. “wants…”
            { // etc…..
                UUID ghostID = UUID.fromString(messageTokens[1]);
                Vector3 ghostPosition = Vector3f.createFrom(
                        game.getPlayerPosition().x(),
                        game.getPlayerPosition().y(),
                        game.getPlayerPosition().z());
                boolean charaSelect = game.getDeerOrHunt();

                try {
                    sendDetailsForMessage(ghostID, ghostPosition,charaSelect);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if(messageTokens[0].compareTo("move") == 0) // rec. “move...”
            {

                UUID ghostID = UUID.fromString(messageTokens[1]);
                Vector3 ghostPosition = Vector3f.createFrom(
                        Float.parseFloat(messageTokens[2]),
                        Float.parseFloat(messageTokens[3]),
                        Float.parseFloat(messageTokens[4]));
                try {
                    MoveAvatar(ghostID, ghostPosition);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (messageTokens[0].compareTo("spear") == 0)
            {
                UUID ghostID = UUID.fromString(messageTokens[1]);
                createGhostSpear(ghostID);
            }

            if (messageTokens[0].compareTo("rotate") == 0)
            {
                UUID ghostID = UUID.fromString(messageTokens[1]);
                float rotateAmt = Float.parseFloat(messageTokens[2]);
                try {
                    rotateGhost(ghostID,rotateAmt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createGhostAvatar(UUID ghostID, Vector3 ghostPosition, boolean modelType) throws IOException {
        GhostAvatar ghost = new GhostAvatar(ghostID, ghostPosition, modelType);
        game.addGhostAvatarToGameWorld(ghost);
        ghostAvatars.add(ghost);
    }

    private void removeGhostAvatar(UUID ghostID) {

        GhostAvatar ghost = new GhostAvatar(null, null, false);
        Iterator<GhostAvatar> iterator = ghostAvatars.iterator();
        boolean exist = false;

        while (iterator.hasNext()){
            GhostAvatar temp = iterator.next();
            if(temp.getId().toString().equals(ghostID.toString())){
                exist = true;
                ghost = temp;
            }
        }

        if (exist){
            game.removeGhostAvatarFromGameWorld(ghost);
            ghostAvatars.remove(ghost);
        }
    }

    public void sendJoinMessage() {     // format: join, localId
     try { sendPacket("join," + id.toString());
     }
    catch (IOException e) { e.printStackTrace();
     }
    }

    public void sendCreateMessage(Vector3 pos, boolean modelType) { // format: (create, localId, x,y,z)
        try {
            String message = "create," + id.toString();
            message += "," + pos.x()+"," + pos.y() + "," + pos.z() + "," + modelType;
            sendPacket(message);
        } catch (IOException e) { e.printStackTrace();
        }
    }

    public void sendMoveMessage(Vector3 position) throws IOException {
        String message = "move," + id.toString();
        message += "," + position.x()+"," + position.y() + "," + position.z();
        sendPacket(message);
    }

    public void sendShootMessage() throws IOException {
        String message = "spear," + id.toString();
        sendPacket(message);
    }

    public void createGhostSpear(UUID ghostID)
    {
        GhostAvatar ghost = new GhostAvatar(null, null, false);
        Iterator<GhostAvatar> iterator = ghostAvatars.iterator();
        boolean exist = false;

        while (iterator.hasNext()){
            GhostAvatar temp = iterator.next();

            if(temp.getId().toString().equals(ghostID.toString())){
                exist = true;
                ghost = temp;
            }
        }

        if (exist){
            game.shootArrow(ghost.getNode(), false);
        }

    }

    public void MoveAvatar(UUID ghostID, Vector3 position) throws IOException {

        GhostAvatar ghost = new GhostAvatar(null, null, false);
        Iterator<GhostAvatar> iterator = ghostAvatars.iterator();
        boolean exist = false;

        while (iterator.hasNext()){
            GhostAvatar temp = iterator.next();

            if(temp.getId().toString().equals(ghostID.toString())){
                exist = true;
                ghost = temp;
            }
        }


        if (exist){
            ghost.setPosition(position);
        }

    }

    public void sendDetailsForMessage(UUID remId, Vector3 position, boolean charaSelect) throws IOException {
        String message = "dsfr," + id.toString();
        message += "," + position.x()+"," + position.y() + "," + position.z() + "," + charaSelect;
        sendPacket(message);
        // etc…..
    }

    public void sendByeMessage() throws IOException {
        String message = "bye," + id.toString();
        sendPacket(message);
    }


    public void sendRotateMessage(float angle) throws IOException {
        String message = "rotate," + id.toString();
        message += "," + angle;
        sendPacket(message);
    }

    public void rotateGhost(UUID ghostID, float rotate) throws IOException {

        GhostAvatar ghost = new GhostAvatar(null, null, false);
        Iterator<GhostAvatar> iterator = ghostAvatars.iterator();
        boolean exist = false;

        while (iterator.hasNext()){
            GhostAvatar temp = iterator.next();

            if(temp.getId().toString().equals(ghostID.toString())){
                exist = true;
                ghost = temp;
            }
        }


        if (exist){
            ghost.setRotate(rotate);
        }

    }
}
