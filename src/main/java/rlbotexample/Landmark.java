package rlbotexample;

import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.vector.Vector3;

import java.awt.*;
import java.util.ArrayList;


public class Landmark {
    Vector3 location;
    String message;
    public static Bot bot;
    static ArrayList<Landmark> landmarks = new ArrayList();
    public Landmark(Vector3 location,String message)
    {
        this.location = location;
        this.message = message;
        landmarks.add(this);
    }

    public static void render()
    {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        for(Landmark l : landmarks)
        {
            r.drawString3d(l.message,Color.white,l.location,1,1);
        }
    }
}
