package rlbotexample;

import rlbot.Bot;
import rlbot.manager.BotManager;
import rlbot.pyinterop.SocketServer;

public class SamplePythonInterface extends SocketServer {

    public SamplePythonInterface(int port, BotManager botManager) {
        super(port, botManager);
    }

    protected Bot initBot(int index, String botType, int team) {
        System.out.println(botType.contains("TestBot"));
        return botType.contains("TestBot") ? new SampleBot(index) : new SpeedBot(index);
    }


}
