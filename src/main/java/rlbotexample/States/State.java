package rlbotexample.States;

import rlbot.Bot;
import rlbotexample.Controller.*;
import rlbotexample.input.Information;
import rlbotexample.input.Predictions;

import javax.sound.sampled.Line;

public abstract class State {

    Information information;
    protected ActionLibrary actionLibrary;
    protected Predictions predictions;
    float starttime;
    public State(Information information,ActionLibrary actionLibrary, Predictions predictions)
    {
        this.information = information;
        this.actionLibrary = actionLibrary;
        this.predictions = predictions;
    }
    /**
     * Returns the state that should be executed
     * @return the action that should be taken
     */
    public abstract AbstractAction getAction();

    /**
     * Draws Information about the current state should diplay name of the current state
     */
    public abstract void draw(Bot bot);

    public abstract boolean isAvailable();
    public abstract double getRating();
    public void start()
    {
        starttime = information.secondsElapsed();
    }

    protected ActionChain chain(float time)
    {
        return new ActionChain(time,information);
    }

    protected Action action(float time)
    {
        return new Action(time,information);
    }

    protected ActionPart part(float start,float end)
    {
        return new ActionPart(start,end);
    }
}
