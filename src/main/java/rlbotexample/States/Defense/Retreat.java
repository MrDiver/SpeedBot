package rlbotexample.States.Defense;

import rlbot.Bot;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.States.State;
import rlbotexample.input.Information;
import rlbotexample.prediction.Predictions;

import java.awt.*;

public class Retreat extends State {


    public Retreat(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "Retreat";
    }

    AbstractAction tmp;
    @Override
    public AbstractAction getAction() {
        /*if(information.me.location().distance(information.ownGoal.location())>1500)
            return actionLibrary.driveTowardsFaster(predictions.nearestBoostSmallOnPath(information.ownGoal.location()),2300,false);*/
        tmp = actionLibrary.driveTowardsFaster(information.ownGoal.location(),2300,true);
        return tmp;
    }

    @Override
    public void draw(Bot bot) {
        if(information.me.location().distance(information.ownGoal.location())>1000)
            predictions.nearestBoostSmallOnPath(information.ownGoal.location()).draw(Color.yellow,bot);
    }

    @Override
    public boolean isAvailable() {
        return predictions.wrongSide()||tmp != null && tmp.isActive();
    }

    @Override
    public double getRating() {
        return predictions.isHittingOwngoal().isImpacting()||predictions.wrongSide()||predictions.enemyCanShoot()||!predictions.possession()||tmp != null && tmp.isActive()?7:0;
    }
}
