package rlbotexample.States;

import rlbot.Bot;
import rlbotexample.Controller.Action;
import rlbotexample.input.Information;

public class TestActions extends State {
    public TestActions(Information information) {
        super(information);
    }

    @Override
    public Action getAction() {
        return Action.sonicflip(4000,300);
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
        return 10;
    }
}
