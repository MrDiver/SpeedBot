package rlbotexample.objects;

import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Util;
import rlbotexample.input.Team;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Goal extends GameObject {
    private Vector3 leftPost;
    private Vector3 rightPost;
    private double goalLength;
    private double goalHeight;

    public Goal(Team team) {
        super(new Vector3(), new Vector3(), new Vector3());
        switch (team)
        {
            case Orange:
                setLocation(new Vector3(0,Util.FIELD_LENGTH/2,0));
                leftPost = new Vector3(893,Util.FIELD_LENGTH/2,0);
                rightPost = new Vector3(-893,Util.FIELD_LENGTH/2,0);
                break;
            case Blue:
                setLocation(new Vector3(0,-Util.FIELD_LENGTH/2,0));
                leftPost = new Vector3(-893,-Util.FIELD_LENGTH/2,0);
                rightPost = new Vector3(893,-Util.FIELD_LENGTH/2,0);
                break;
        }
    }

    public void draw(Color c, Bot bot)
    {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        r.drawCenteredRectangle3d(c,location(),10,10,false);
        r.drawCenteredRectangle3d(Color.blue,leftPost,10,10,false);
        r.drawCenteredRectangle3d(Color.cyan,rightPost,10,10,false);
    }

    public Vector3 leftPost() {
        return leftPost;
    }

    public Vector3 rightPost() {
        return rightPost;
    }

    @Override
    public void update(Vector3 location, Vector3 velocity, Vector3 rotation) {

    }
}
