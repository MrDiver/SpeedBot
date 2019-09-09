package rlbotexample.States.OffenseGround;

import rlbot.Bot;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.Controller.Value;
import rlbotexample.States.State;
import rlbotexample.Util;
import rlbotexample.input.Information;
import rlbotexample.prediction.Predictions;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Dribble extends State {


    public Dribble(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name ="Dribble";
    }

    @Override
    public AbstractAction getAction() {
        Vector3 tmp = information.ball.location().plus(information.me.location().minus(information.ball.location()).flatten().scaledToMagnitude(100).make3D());
        Value angle = ()-> Util.cap(Util.steer(information.me.transformToLocal(predictions.ballFutureTouch()).angle2D()),-1,1);
        Value distance = ()-> information.me.location().distance(tmp);
        Value velocityDistance = ()-> information.me.location().distance(information.me.location().plus(information.me.velocity().scaled(predictions.ballTimeTillTouchGround())));
        Value throttle = ()->{
            float val =(distance.val()<0)?-1:1*Util.cap(distance.val(),0,600)/600;
            return val;

        };
        return chain(10).addAction(action(1000).add(part(0,1000).withThrottle(throttle).withSteer(angle)));
    }

    @Override
    public void draw(Bot bot) {
        information.ball.location().plus(information.me.location().minus(information.ball.location()).flatten().scaledToMagnitude(100).make3D()).draw(Color.yellow,bot);
    }

    @Override
    public boolean isAvailable() {
        return information.me.location().flatten().distance(information.ball.location().flatten())<200&&information.ball.location().z>100;
    }

    @Override
    public double getRating() {
        return 10;
    }
}
