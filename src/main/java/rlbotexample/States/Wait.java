package rlbotexample.States;

import rlbot.Bot;
import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.BallPrediction;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.Action;
import rlbotexample.Util;
import rlbotexample.input.Information;
import rlbotexample.prediction.BallPredictionHelper;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Wait extends State {
    public Wait(Information information) {
        super(information);
    }

    @Override
    public AbstractAction getAction()
    {
        try {
            BallPrediction ballPrediction = RLBotDll.getBallPrediction();

        } catch (RLBotInterfaceException e) {
              e.printStackTrace();
        }
        return Action.drive(0,1,false,information);
    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        r.drawString3d("Wait",Color.white,information.me.location().plus(new Vector3(0,0,300)),1,1);
        try {
            // Draw 3 seconds of ball prediction
            BallPrediction ballPrediction = RLBotDll.getBallPrediction();
            BallPredictionHelper.drawTillMoment(ballPrediction, information.secondsElapsed() + 3, Color.CYAN, r);
        } catch (RLBotInterfaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public double getRating() {
        return 5;
    }
}
