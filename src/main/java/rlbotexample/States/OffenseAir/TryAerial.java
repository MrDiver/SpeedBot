package rlbotexample.States.OffenseAir;

import rlbot.Bot;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.States.State;
import rlbotexample.input.Information;
import rlbotexample.input.Predictions;

public class TryAerial extends State {

    public TryAerial(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "TryAerial";
    }

    @Override
    public AbstractAction getAction() {
        return chain(1);
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
