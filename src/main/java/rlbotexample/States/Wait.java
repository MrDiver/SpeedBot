package rlbotexample.States;

import rlbot.Bot;
import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.BallPrediction;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.Action;
import rlbotexample.Controller.ActionChain;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.Objects.BoostPadManager;
import rlbotexample.Objects.GameCar;
import rlbotexample.Util;
import rlbotexample.input.Information;
import rlbotexample.input.Predictions;
import rlbotexample.prediction.BallPredictionHelper;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Wait extends State {
    public Wait(Information information) {
        super(information);
    }

    Vector3 nearestBoostPad = new Vector3(0,0,0);
    Vector3 firstTouch = new Vector3(0,0,0);
    Vector3 target = new Vector3(0,0,0);
    @Override
    public AbstractAction getAction()
    {
        ActionLibrary actionLibrary = new ActionLibrary(information);
        GameCar me = information.me;
        firstTouch= BallPredictionHelper.predictFirstTouch(information);
        nearestBoostPad = BoostPadManager.getNearestFull(firstTouch).getLocation();
        if(information.me.boost()<30&&!((information.me.location().y - information.ball.location().y)*Util.sign(information.me.team().ordinal()) < 0))
        {
            target=nearestBoostPad;
        }else
        {
            target = information.ownGoal.location();
        }

        /*ActionChain a = chain(2000)
        .addAction(
                action(100).addCondition(()->Math.abs(information.me.transformToLocal(target).angle2D())<0.1)
                    .add(part(0,5000).withSteer(()->information.me.transformToLocal(target).angle2D()).withThrottle(1).withBoost(information.me.boost()>30)))
        .addAction(
                actionLibrary.diagonalFlick(1,false)
        );*/

        return actionLibrary.driveTowardsFast(target,1800,false);
        /*System.out.println(Predictions.boostSeconds(information.me));
        System.out.println(me.velocity());

        ActionChain a = chain(10000);
        System.out.println("Start: "+Predictions.speedWithNormal(me,900));
        a.addAction(action(3000).add(part(0,3000).withThrottle(1)))
        .addAction(action(3000).add(part(0,30).withThrottle(()->{
            System.out.println("End: "+me.velocity().y);return 0;})));*/
        //return a;
    }

    float lastvel=0;
    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        nearestBoostPad.draw(Color.cyan,bot);
        r.drawString3d("Wait",Color.white,information.me.location().plus(new Vector3(0,0,300)),1,1);
        try {
            // Draw 3 seconds of ball prediction
            BallPrediction ballPrediction = RLBotDll.getBallPrediction();
            BallPredictionHelper.drawTillMoment(ballPrediction, information.secondsElapsed() + 3, Color.CYAN, r);
        } catch (RLBotInterfaceException e) {
            e.printStackTrace();
        }
        firstTouch.draw(Color.RED,bot);
        target.draw(Color.green,bot);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public double getRating() {
        if(Util.timeZ(information.ball)>1)
        {
            return 5;
        }
        if (information.me.location().distance(target) < 300)
            return 0;
        if((information.me.location().y - information.ball.location().y)*Util.sign(information.me.team().ordinal()) < 0)
        {
            return 5;
        }
        return 0;
    }
}
