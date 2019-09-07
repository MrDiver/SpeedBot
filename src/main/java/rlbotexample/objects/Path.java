package rlbotexample.objects;

import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.NamedRenderer;
import rlbot.render.Renderer;
import rlbotexample.vector.Vector3;

import java.awt.*;
import java.util.ArrayList;

public class Path {
    private ArrayList<Vector3> points;
    public Path()
    {
        points = new ArrayList<>();
    }

    public void draw()
    {
        NamedRenderer r = new NamedRenderer("Path");
        r.startPacket();
        for(int i = 1; i < points.size();i++)
        {
            r.drawLine3d(Color.yellow,points.get(i-1),points.get(i));
        }
        r.finishAndSend();
    }

    public void add(Vector3 vector3)
    {
        points.add(vector3);
    }
}
