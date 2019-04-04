package Network.Client;

import A3.MyGame;
import ray.networking.client.GameConnectionClient;
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
        System.out.println("Connected");
        if (messageTokens.length > 0) {

            if (messageTokens[0].compareTo("join") == 0) // receive “join”
            { // format: join, success or join, failure
                if (messageTokens[1].compareTo("success") == 0) {
                    game.setIsConnected(true);
                    sendCreateMessage(game.getPlayerPosition());
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
                try {
                    createGhostAvatar(ghostID, ghostPosition);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(messageTokens[0].compareTo("wsds") == 0) // rec. “create…”
            { // etc…..
            }

            if(messageTokens[0].compareTo("wsds") == 0) // rec. “wants…”
            { // etc…..
            }

            if(messageTokens[0].compareTo("move") == 0) // rec. “move...”
            { // etc…..
            }
        }
    }

    private void createGhostAvatar(UUID ghostID, Vector3 ghostPosition) throws IOException {
        GhostAvatar ghost = new GhostAvatar(ghostID, ghostPosition);
        game.addGhostAvatarToGameWorld(ghost);
        ghostAvatars.add(ghost);
    }

    private void removeGhostAvatar(UUID ghostID) {

        GhostAvatar ghost = new GhostAvatar(null, null);
        Iterator<GhostAvatar> iterator = ghostAvatars.iterator();
        boolean exist = false;

        while (iterator.hasNext()){
            GhostAvatar temp = iterator.next();
            if(temp.getId() == ghostID){
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

    public void sendCreateMessage(Vector3 pos) { // format: (create, localId, x,y,z)
        try {
            String message = "create," + id.toString();
            message += "," + pos.x()+"," + pos.y() + "," + pos.z();
            sendPacket(message);
        } catch (IOException e) { e.printStackTrace();
        }
    }


}
