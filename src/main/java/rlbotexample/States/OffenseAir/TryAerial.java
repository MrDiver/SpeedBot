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
        return actionLibrary.flatToSurface();
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
        return Math.abs(information.me.getRotator().roll())>0.2&&!information.me.hasWheelContact()?10:0;
    }
}
