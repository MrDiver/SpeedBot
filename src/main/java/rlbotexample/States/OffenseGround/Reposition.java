package rlbotexample.States.OffenseGround;

import rlbot.Bot;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.ActionChain;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.States.State;
import rlbotexample.Util;
import rlbotexample.input.Information;
import rlbotexample.prediction.Predictions;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Reposition extends State {


    public Reposition(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name ="Reposition";
    }
    Vector3 tmp = new Vector3();
    @Override
    public AbstractAction getAction() {
        ActionChain actionChain = chain(10);
        if(predictions.isHittingShotZone().isImpacting()&&information.ball.velocity().magnitude()> 500)
        {
            actionChain.addAction(actionLibrary.driveTowardsFaster(predictions.ballFutureTouch(),2300,false));
            return actionChain;
        }
        int speed = (predictions.ballTimeTillTouchGround()>0.5)&&information.me.location().distance(information.ball.location())<1000?100:2300;
        float distanceToBall =information.me.location().distance(information.ball.location());
        float distanceToGoal = information.ball.location().distance(information.eneGoal.location());
        tmp = information.ball.location().plus(information.ball.location().minus(information.eneGoal.location()).scaledToMagnitude(distanceToBall/4));
        Vector3 offset = information.ball.location().minus(information.eneGoal.location()).scaledToMagnitude(100);
        tmp = information.ball.location().plus(new Vector3(offset.x,offset.y/2,offset.z));
        tmp = new Vector3(Util.cap(tmp.x*1.01,-4000,4000),Util.cap(tmp.y+1000*information.me.teamSign()*(distanceToBall/10000),-5000,5000),0);

        actionChain.addAction(actionLibrary.driveTowardsFaster(tmp,speed,false));
        return actionChain;
    }

    @Override
    public void draw(Bot bot) {
        tmp.draw(Color.red,bot);
    }

    @Override
    public boolean isAvailable() {
        return !predictions.meCanShoot();
    }

    @Override
    public double getRating() {
        //System.out.println(Util.cap(7000-information.me.location().distance(information.ownGoal.location()),0,5000)/1000);
        if(predictions.isHittingShotZone().isImpacting())
        {
            return 6;
        }
        return 5-Util.cap(5000-information.ball.location().distance(information.ownGoal.location()),0,5000)/1000;
    }
}
