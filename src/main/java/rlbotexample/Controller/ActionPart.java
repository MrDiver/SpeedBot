package rlbotexample.Controller;

import rlbotexample.output.ControlsOutput;

import java.util.ArrayList;

public class ActionPart {
    private float startTime;
    private float endTime;
    private ArrayList<Function> functions;

    /**
     * A way to save ControlsOutput functions with a start and end time.
     * Holds a list of functions for execution in the time range
     * @param start relative start time
     * @param end relative end time
     */
    public ActionPart(float start,float end)
    {
        startTime=start/1000;
        endTime=end/1000;
        functions = new ArrayList<>();
    }


    /**
     * Executes all added functions on the given ControlsOutput if the given delta is between start and end time
     * @param delta the current delta time
     * @param output the ControlsOutput that the held functions get applied to
     * @return returns the new ControlsOutput with applied functions
     */
    public ControlsOutput execute(float delta,ControlsOutput output)
    {
        int functioncount;
        functioncount =  functions.size();
        if(delta >= startTime && delta <= endTime)
        {
            for(int i = 0; i <functioncount; i++)
            {
                output = functions.get(i).apply(output);
            }
        }
        return output;
    }

    public ActionPart withSteer(float steer) {
        functions.add( (ControlsOutput output)->{
                return output.withSteer(steer);
            }
        );
        return this;
    }

    public ActionPart withSteer(Value steer) {
        functions.add( (ControlsOutput output)->{
                    return output.withSteer(steer.val());
                }
        );
        return this;
    }

    public ActionPart withPitch(float pitch) {
        functions.add( (ControlsOutput output)->{
                    return output.withPitch(pitch);
                }
        );
        return this;
    }

    public ActionPart withPitch(Value pitch) {
        functions.add( (ControlsOutput output)->{
                    return output.withPitch(pitch.val());
                }
        );
        return this;
    }

    public ActionPart withYaw(float yaw) {
        functions.add( (ControlsOutput output)->{
                    return output.withYaw(yaw);
                }
        );
        return this;
    }

    public ActionPart withYaw(Value yaw) {
        functions.add( (ControlsOutput output)->{
                    return output.withYaw(yaw.val());
                }
        );
        return this;
    }

    public ActionPart withRoll(float roll) {
        functions.add( (ControlsOutput output)->{
                    return output.withRoll(roll);
                }
        );
        return this;
    }

    public ActionPart withRoll(Value roll) {
        functions.add( (ControlsOutput output)->{
                    return output.withRoll(roll.val());
                }
        );
        return this;
    }

    public ActionPart withThrottle(float throttle) {
        functions.add( (ControlsOutput output)->{
                    return output.withThrottle(throttle);
                }
        );
        return this;
    }

    public ActionPart withThrottle(Value throttle) {
        functions.add( (ControlsOutput output)->{
                    return output.withThrottle(throttle.val());
                }
        );
        return this;
    }

    public ActionPart withJump(boolean jumpDepressed) {
        functions.add( (ControlsOutput output)->{
                    return output.withJump(jumpDepressed);
                }
        );
        return this;
    }

    public ActionPart withBoost(boolean boostDepressed) {
        functions.add( (ControlsOutput output)->{
                    return output.withBoost(boostDepressed);
                }
        );
        return this;
    }

    public ActionPart withSlide(boolean slideDepressed) {
        functions.add( (ControlsOutput output)->{
                    return output.withSlide(slideDepressed);
                }
        );
        return this;
    }

    public ActionPart withUseItem(boolean useItemDepressed) {
        functions.add( (ControlsOutput output)->{
                    return output.withUseItem(useItemDepressed);
                }
        );
        return this;
    }

    public ActionPart withJump() {
        this.withJump(true);
        return this;
    }

    public ActionPart withBoost() {
        this.withBoost(true);
        return this;
    }

    public ActionPart withSlide() {
        this.withSlide(true);
        return this;
    }

    public ActionPart withUseItem() {
        this.withUseItem(true);
        return this;
    }

    public void delay(float time)
    {
        startTime += time/1000;
        endTime += time/1000;
    }
}
