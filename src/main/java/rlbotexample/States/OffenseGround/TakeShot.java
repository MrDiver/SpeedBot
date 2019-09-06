package rlbotexample.States.OffenseGround;

import rlbot.Bot;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.Controller.Value;
import rlbotexample.States.State;
import rlbotexample.input.Information;
import rlbotexample.input.Predictions;
import rlbotexample.vector.Vector3;

public class TakeShot extends State {


    public TakeShot(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "TakeShot";
    }

    @Override
    public AbstractAction getAction() {
        Value angleToBall = ()->information.me.transformToLocal(information.ball).angle2D();
        if(Math.abs(angleToBall.val())>1.5f)
            return actionLibrary.turnToAngle(angleToBall);
        Vector3 tmp = information.ball.location().plus(information.ball.location().minus(information.eneGoal.location()).scaledToMagnitude(90));
        int speed = predictions.ballTimeTillTouchGround()<0.4?2300:1400;
        return actionLibrary.driveTowardsFaster(information.ball.location(),speed,false);
    }

    @Override
    public void draw(Bot bot) {

    }

    @Override
    public boolean isAvailable() {
        return predictions.meCanShoot();
    }

    @Override
    public double getRating() {
        return predictions.meCanShoot()?7:0;
    }
}
