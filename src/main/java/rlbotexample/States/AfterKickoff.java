package rlbotexample.States;

import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.input.Information;
import rlbotexample.prediction.Predictions;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class AfterKickoff extends State {

    Vector3 target;

    public AfterKickoff(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "AfterKickoff";
    }

    @Override
    public AbstractAction getAction() {
        return actionLibrary.flatToSurface();
    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        //r.drawCenteredRectangle3d(Color.green,target,10,10,false);
        //r.drawCenteredRectangle3d(Color.blue,(information.me.location().plus(vectoball.scaledToMagnitude(300))),10,10,false);
        r.drawString3d("AfterKickoff",Color.white,information.me.location().plus(new Vector3(0,0,300)),1,1);
    }

    @Override
    public boolean isAvailable() {
        return information.isAfterKickoff()&&(!information.me.hasWheelContact());
    }

    @Override
    public double getRating() {
        return information.isAfterKickoff()? 10:0;
    }
}
