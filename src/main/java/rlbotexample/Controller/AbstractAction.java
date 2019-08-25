package rlbotexample.Controller;

import rlbotexample.input.Information;
import rlbotexample.output.ControlsOutput;

import java.util.ArrayList;

public abstract class AbstractAction {
    protected float start;
    protected float executionLength;
    protected boolean active=false;
    protected ArrayList<ActionPart> parts;

    protected ControlsOutput state;
    protected Condition condition;
    protected Information information;

    public AbstractAction(float executionLength, Information information)
    {
        this.executionLength = executionLength/1000;
        parts = new ArrayList<>();
        this.information = information;
        this.condition = () -> true;
    }

    public abstract ControlsOutput execute(ControlsOutput output);

    /**
     * Starts the current Action sets the start time to current time and active to true
     */
    public void start()
    {
        active = true;
        start = information.secondsElapsed();
    }

    /**
     * @return returns true iff the Action hasnt exceeded its execution length;
     */
    public boolean isActive() {
        return active;
    }

    public float getElapsed()
    {
        return information.secondsElapsed() - start;
    }

    public float getExecutionLength()
    {
        return executionLength;
    }

    public ControlsOutput getState() {
        return state;
    }
}
