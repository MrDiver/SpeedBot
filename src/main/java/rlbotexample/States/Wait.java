package rlbotexample.States;

import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Controller.Action;
import rlbotexample.Util;
import rlbotexample.input.Information;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Wait extends State {
    public Wait(Information information) {
        super(information);
    }

    @Override
    public Action getAction() {
        return Action.drive(0,1,false);
    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);

            Vector3 location = Util.future(information.ball, Util.timeZ(information.ball));
            r.drawLine3d(Color.CYAN, information.ball.location(), location);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public double getRating() {
        return 10;
    }
}
