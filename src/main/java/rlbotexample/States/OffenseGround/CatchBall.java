package rlbotexample.States.OffenseGround;

import rlbot.Bot;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.Controller.Value;
import rlbotexample.States.State;
import rlbotexample.Util;
import rlbotexample.input.Information;
import rlbotexample.prediction.Predictions;

import java.awt.*;

public class CatchBall extends State {

    public CatchBall(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "Catch Ball";
    }

    @Override
    public AbstractAction getAction() {
        Value angle = ()-> Util.cap(Util.steer(information.me.transformToLocal(predictions.ballFutureTouch()).angle2D()),-1,1);
        Value distance = ()-> information.me.location().distance(predictions.ballFutureTouch())-30;
        Value velocityDistance = ()-> information.me.location().distance(information.me.location().plus(information.me.velocity().scaled(predictions.ballTimeTillTouchGround())));
        Value throttle = ()->{
            float val = distance.val()>700? 1: (distance.val()<velocityDistance.val()?-1f:1f)*Util.cap(distance.val(),0,600)/600;
            return val;

        };
        return chain(10).addAction(action(1000).add(part(0,1000).withThrottle(throttle).withSteer(angle)));
    }

    @Override
    public void draw(Bot bot) {
        information.me.location().plus(information.me.velocity().scaled(predictions.ballTimeTillTouchGround())).draw(Color.red,bot);
    }

    @Override
    public boolean isAvailable() {
        return predictions.ballTimeTillTouchGround()>0.2&&information.ball.location().z>150;
    }

    @Override
    public double getRating() {
        return 9;
    }
}
