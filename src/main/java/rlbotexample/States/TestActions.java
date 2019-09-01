package rlbotexample.States;

import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Controller.*;
import rlbotexample.input.Information;
import rlbotexample.input.Predictions;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class TestActions extends State {

    public TestActions(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
    }

    @Override
    public AbstractAction getAction() {
        ActionLibrary actionLibrary = new ActionLibrary(information);
        return actionLibrary.diagonalFlick(-1,false);
    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        r.drawString3d("Test",Color.white,information.me.location().plus(new Vector3(0,0,300)),1,1);
        //System.out.println(Math.abs(information.me.rotation().z));
        new Vector3(2000,0,0).draw(Color.red,bot);
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
