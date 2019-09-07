package rlbotexample.States.Defense;

import rlbot.Bot;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.States.State;
import rlbotexample.input.Information;
import rlbotexample.prediction.Predictions;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Defending extends State {
    public Defending(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "Defending";
    }

    Vector3 defPosition;
    @Override
    public AbstractAction getAction() {
        defPosition = information.ownGoal.location().plus(new Vector3(predictions.ballOnLeft()?500:-500,0,0));
        return actionLibrary.driveTowardsFaster(information.ball.location(),2300,false);
    }

    @Override
    public void draw(Bot bot) {
        defPosition.draw(Color.yellow,bot);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public double getRating() {
        return predictions.ballOnOwnSide() && !predictions.possession() && predictions.meInGoal() ? 8:2;
    }
}
