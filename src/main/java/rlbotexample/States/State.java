package rlbotexample.States;

import rlbot.Bot;
import rlbotexample.Controller.Action;
import rlbotexample.input.Information;

import javax.sound.sampled.Line;

public abstract class State {

    Information information;
    long starttime;
    public State(Information information)
    {
        this.information = information;
    }
    /**
     * Returns the state that should be executed
     * @return the action that should be taken
     */
    public abstract Action getAction();

    /**
     * Draws Information about the current state should diplay name of the current state
     */
    public abstract void draw(Bot bot);

    public abstract boolean isAvailable();
    public abstract double getRating();
    public void start()
    {
        starttime = System.currentTimeMillis();
    }
}
