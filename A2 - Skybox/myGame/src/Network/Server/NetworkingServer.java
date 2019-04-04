package Network.Server;

import java.io.IOException;


public class NetworkingServer {

    private GameServerUDP thisUDPServer;

    public NetworkingServer(int serverPort, String protocol) {
        try {
            thisUDPServer = new GameServerUDP(serverPort);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String port = "56000";
        String protocol = "UDP";
        NetworkingServer app = new NetworkingServer(Integer.parseInt(port), protocol);
    }
}