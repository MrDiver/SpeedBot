package rlbotexample.States;

import rlbot.Bot;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.Action;
import rlbotexample.Controller.ActionChain;
import rlbotexample.Controller.ActionPart;
import rlbotexample.input.Information;

import javax.sound.sampled.Line;

public abstract class State {

    Information information;
    float starttime;
    public State(Information information)
    {
        this.information = information;
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
