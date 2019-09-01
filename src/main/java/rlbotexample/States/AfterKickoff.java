package rlbotexample.States;

import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.ActionChain;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.Objects.BoostPadManager;
import rlbotexample.input.Information;
import rlbotexample.input.Predictions;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class AfterKickoff extends State {
    public AfterKickoff(Information information) {
        super(information);
    }

    Vector3 target;
    @Override
    public AbstractAction getAction() {
        Predictions predictions = new Predictions(information);
        ActionLibrary actionLibrary = new ActionLibrary(information);
        Vector3 ballhit = predictions.ballFutureTouch();
        target = BoostPadManager.getNearestFull(ballhit).getLocation();
        if(information.timeAfterKickoff()<1)
        {
            ActionChain chain = chain(500);
            chain.addAction(actionLibrary.flatToSurface());
            return chain;
        }
        ActionChain chain = chain(3000);
        chain.addAction(actionLibrary.flatToSurface());
        chain.addAction(actionLibrary.driveTowardsFast(target,1600,true));
        return chain;
    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        //r.drawCenteredRectangle3d(Color.green,target,10,10,false);
        //r.drawCenteredRectangle3d(Color.blue,(information.me.location().plus(vectoball.scaledToMagnitude(300))),10,10,false);
        r.drawString3d("AfterKickoff",Color.white,information.me.location().plus(new Vector3(0,0,300)),1,1);
        target.draw(Color.yellow,bot);

    }

    @Override
    public boolean isAvailable() {
        return information.isAfterKickoff();
    }

    @Override
    public double getRating() {
        return information.isAfterKickoff()? 10:0;
    }
}
