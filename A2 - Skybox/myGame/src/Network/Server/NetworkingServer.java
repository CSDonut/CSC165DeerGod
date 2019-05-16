package Network.Server;

import java.io.IOException;


public class NetworkingServer {
    private GameServerUDP thisUDPServer;
    private NPCController npcCtrl;
    private long lastUpdateTime;

    public NetworkingServer(int serverPort, String protocol) {
        try {
            thisUDPServer = new GameServerUDP(serverPort);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void npcLoop()
    {
        while (true)
        {
            long frameStartTime = System.nanoTime();
            float elapMilSecs = (frameStartTime-lastUpdateTime)/(1000000.0f);
            if(elapMilSecs >= 50.0f)
            {
                lastUpdateTime = frameStartTime;
                npcCtrl.updateNPCs();
//                thisUDPServer.sendNPCinfo();
            }
            Thread.yield();
        }
    }

    public static void main(String[] args) {

        String port = "59000";
        String protocol = "UDP";
        NetworkingServer app = new NetworkingServer(Integer.parseInt(port), protocol);
    }
}