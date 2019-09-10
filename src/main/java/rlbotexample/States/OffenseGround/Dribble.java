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

    Vector3 targetLocation;
    @Override
    public AbstractAction getAction() {
        /*Vector3 tmp = information.ball.location().plus(information.me.location().minus(information.ball.location()).flatten().scaledToMagnitude(100).make3D());*/
        targetLocation = new Vector3(2000,3000,0);
        float targetSpeed = 600;
        //TODO:Implement steering to target location
        Value angle = ()-> Util.cap(Util.steer(information.me.transformToLocal(predictions.ballFutureLocation(1000)).angle2D()),-1,1)+0.1f;
        Value distance = ()-> (float)information.me.location().flatten().distance(information.ball.location().plus(information.ball.velocity()).flatten());
        Value velocityDistance = ()-> (float)information.me.location().flatten().distance(information.me.location().plus(information.me.velocity()).flatten())-15;
        Value speedup = ()->distance.val()<1200?(Util.cap((targetSpeed-information.me.speed())/4,-80,60)):0;
        Value throttle = ()->{
            float val = distance.val() > velocityDistance.val() +speedup.val() ? 1:targetSpeed>information.me.speed()?0:-1;
            val = val * Math.abs(Util.cap(velocityDistance.val()-distance.val(),-50,50)/30);
            return val;

        };
        return chain(10).addAction(action(1000).add(part(0,1000).withThrottle(throttle).withSteer(angle)));
    }

    @Override
    public void draw(Bot bot) {
        information.me.location().plus(information.me.velocity()).draw(Color.red,bot);
        information.ball.location().plus(information.ball.velocity()).draw(Color.yellow,bot);
        targetLocation.draw(Color.green,bot);
        targetLocation.plus(information.me.location().minus(information.ball.location())).flatten().make3D().draw(Color.yellow,bot);
        /*System.out.println(Math.acos(information.ball.velocity().normalized().x)+"\t"+Math.asin(information.ball.velocity().normalized().y)+"\t"+information.me.getRotator().yaw());*/
        //System.out.println(Math.atan2(information.ball.velocity().normalized().y,information.ball.velocity().normalized().x)+"\t"+information.me.getRotator().yaw());
        //System.out.println(information.me.transformToLocal(targetLocation).angle2D()+"\t"+information.ball.transformToLocal(targetLocation).angle2D());
    }

    @Override
    public boolean isAvailable() {
        return information.me.location().flatten().distance(information.ball.location().flatten())<200&&information.ball.location().z>100;
    }

    @Override
    public double getRating() {
        return 9;
    }
}
