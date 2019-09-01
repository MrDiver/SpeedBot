package rlbotexample.States.Defense;

import rlbot.Bot;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.States.State;
import rlbotexample.input.Information;
import rlbotexample.input.Predictions;

public class Shadowing extends State {
    public Shadowing(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
    }

    @Override
    public AbstractAction getAction() {
        return null;
    }

    @Override
    public void draw(Bot bot) {

    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public double getRating() {
        return 0;
    }
}
