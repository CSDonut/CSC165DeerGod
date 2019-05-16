package Network.Server;

import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import ray.ai.behaviortrees.BTCompositeType;
import ray.ai.behaviortrees.BTSequence;
import ray.ai.behaviortrees.BehaviorTree;

import ray.ai.behaviortrees.BTCompositeType;
import ray.ai.behaviortrees.BTSequence;
import ray.ai.behaviortrees.BehaviorTree;

public class NPCController {
    private BehaviorTree bt = new BehaviorTree(BTCompositeType.SELECTOR);
    private GameServerUDP server;
    private long thinkStartTime;
    private long tickStateTime;
    private long lastThinkUpdateTime;
    private long lastTickUpdateTime;
    private int npcCount = 10;
    private Random rn = new Random();
    private NPC[] npc = new NPC[npcCount];

    public void start()
    {
        thinkStartTime = System.nanoTime();
        tickStateTime = System.nanoTime();
        lastThinkUpdateTime = thinkStartTime;
        lastTickUpdateTime = tickStateTime;
        //setupNPC();
        //npcLoop();

    }

    public void setupNPC()
    {
        for (int i = 0; i < npcCount; i++)
        {
            npc[i] = new NPC();
            npc[i].randomizeLocation(50 - rn.nextInt(100), 15, 50 - rn.nextInt(100));
            setupBehaviorTree(npc[i]);
        }
    }

    public void updateNPCs() {
        for(int i = 0; i < npcCount; i++)
            npc[i].updateLocation();

    }

    public void npcLoop()
    {
        while(true)
        {
            long currentTime = System.nanoTime();
            float elapsedThinkMilliSecs = (currentTime - lastThinkUpdateTime)/(10000000.0f);
            float elapsedTickMilliSecs = (currentTime - lastTickUpdateTime)/(1000000.0f);

            if(elapsedTickMilliSecs >= 50.0f)
            {
                lastTickUpdateTime = currentTime;
                //npc.updateLocation();
//                Have to create code to send npc information
//                server.sendNPCinfo();
            }

            if(elapsedThinkMilliSecs >= 500.0f) {
                lastThinkUpdateTime = currentTime;
                bt.update(elapsedThinkMilliSecs);
            }
            Thread.yield();
        }
    }

    public void setupBehaviorTree(NPC npc)
    {
//        A BTSequence node is a BTComposite that executes its child nodes from left to right.
//          If a failure happens at any of the child nodes then the BTSequence fails. If all child
//              nodes succeed then the BTSequence succeeds. You are basically ANDing all of the return Status's of Behaviors together.
        bt.insertAtRoot(new BTSequence(10));
        bt.insertAtRoot(new BTSequence(20));

    }

    public NPC[] getNPCs()
    {
        return npc;
    }

    public int getNumberOfNPCs()
    {
        return npcCount;
    }
}