package rlbotexample.States.Defense;

import rlbot.Bot;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.States.State;
import rlbotexample.input.Information;
import rlbotexample.prediction.Predictions;

public class GetBoost extends State {
    public GetBoost(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "Defensive GetBoost";
    }

    @Override
    public AbstractAction getAction() {
        if(information.me.boost() < 30)
            return actionLibrary.driveTowards(predictions.nearestBoostFull(),2300,false);
        return actionLibrary.driveTowards(predictions.nearestBoostSmallInRange(),2300,false);
    }

    @Override
    public void draw(Bot bot) {

    }

    @Override
    public boolean isAvailable() {
        return !predictions.isHittingOwngoal().isImpacting();
    }

    @Override
    public double getRating() {
        return (100 - information.me.boost())/16;
    }
}
