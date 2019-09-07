package rlbotexample.objects;

import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Zone {
    Vector3 pos;
    float width;
    float height;
    float length;
    public Vector3 impact;
    public Zone(Vector3 pos,float width, float height,float length)
    {
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.length = length;
    }

    public boolean inSide(Vector3 loc)
    {
        Vector3 diff = pos.minus(loc);
        boolean val = Math.abs(diff.x) < width && Math.abs(diff.y)<length&& Math.abs(diff.z)<height;
        if(val)
            impact = loc;
        return val;
    }

    public void draw(Bot bot, Color c)
    {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        Vector3 p1 = new Vector3(pos.x-width/2,pos.y-length/2,pos.z);//bottom front left
        Vector3 p2 = new Vector3(pos.x+width/2,pos.y-length/2,pos.z);//bottom front right
        Vector3 p3 = new Vector3(pos.x-width/2,pos.y+length/2,pos.z);//bottom back left
        Vector3 p4 = new Vector3(pos.x+width/2,pos.y+length/2,pos.z);//bottom back right
        Vector3 p5 = new Vector3(pos.x-width/2,pos.y-length/2,pos.z+height);//top front left
        Vector3 p6 = new Vector3(pos.x+width/2,pos.y-length/2,pos.z+height);//top front right
        Vector3 p7 = new Vector3(pos.x-width/2,pos.y+length/2,pos.z+height);//top back left
        Vector3 p8 = new Vector3(pos.x+width/2,pos.y+length/2,pos.z+height);//top back right
        //bottom
        r.drawLine3d(c,p1,p2);
        r.drawLine3d(c,p2,p4);
        r.drawLine3d(c,p3,p4);
        r.drawLine3d(c,p3,p1);

        //top
        r.drawLine3d(c,p5,p6);
        r.drawLine3d(c,p6,p8);
        r.drawLine3d(c,p7,p8);
        r.drawLine3d(c,p7,p5);

        r.drawLine3d(c,p1,p5);
        r.drawLine3d(c,p2,p6);
        r.drawLine3d(c,p3,p7);
        r.drawLine3d(c,p4,p8);
    }
}
