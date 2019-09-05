package rlbotexample.States.Defense;

import rlbot.Bot;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.States.State;
import rlbotexample.input.Information;
import rlbotexample.input.Predictions;

public class GetBoost extends State {
    public GetBoost(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "Defensive GetBoost";
    }

    @Override
    public AbstractAction getAction() {
        if(information.me.boost() < 30)
            return actionLibrary.driveTowards(predictions.nearestBoostFull(),1000,true);
        return actionLibrary.driveTowards(predictions.nearestBoostSmallInRange(),1000,true);
    }

    @Override
    public void draw(Bot bot) {

    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public double getRating() {
        return (100 - information.me.boost())/11;
    }
}
