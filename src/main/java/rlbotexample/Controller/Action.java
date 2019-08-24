package rlbotexample.Controller;

import rlbotexample.Landmark;
import rlbotexample.output.ControlsOutput;

import java.util.ArrayList;

public class Action {
    private long start;
    private long executionLength;
    private boolean active=false;
    private ArrayList<ActionPart> parts;
    public ControlsOutput state;
     /**
     * Creates a player action with some execution length that can be used by the ActionController or a State
     * @param executionLength the maximal time of execution after the Action gets disabled
     */
    public Action(long executionLength)
    {
        this.executionLength = executionLength;
        parts = new ArrayList<>();
    }

    /**
     * Starts the current Action sets the start time to 0 and active to true
     */
    public void start()
    {
        active = true;
        start = System.currentTimeMillis();
    }

    /**
     * Executes all ActionParts until the executionLength is exceeded
     * @param output the ControlOutput from the Car
     * @return returns the changed controls original controls are kept and new ones are added
     */
    public ControlsOutput execute(ControlsOutput output)
    {
        if(active) {
            long delta = System.currentTimeMillis() - start;
            if(delta >executionLength)
            {
                active = false;
            }
            int size = parts.size();
            for(int i = 0; i <size; i++)
            {
                output = parts.get(i).execute(delta,output);
            }
            state=output;
        }
        return output;
    }

    /**
     * Add a ActionPart to the execution pipeline
     * @param part the ActionPart
     * @return returns the new Action with the added ActionPart
     */
    public Action add(ActionPart part)
    {
        parts.add(part);
        return this;
    }


    /**
     * @return returns true iff the Action hasnt exceeded its execution length;
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Standard action for dodge
     * @param time length of the dodge (not delay between jumps)
     * @return
     */
    public static Action dodge(long time)
    {
       Action a = new Action(time).add(new ActionPart(0,16).withJump().withPitch(-1))
            .add(new ActionPart(16,50).withJump(false).withPitch(-1));
            a.add(new ActionPart(50,100).withJump().withPitch(-1));
            return a;
    }
    public static Action dodge(long time,double angle)
    {
        Action a = new Action(time).add(new ActionPart(0,16).withJump())
                .add(new ActionPart(16,50).withJump(false).withPitch(-1));
        a.add(new ActionPart(50,70).withJump().withPitch((float)-Math.cos(angle)).withYaw((float)-Math.sin(angle)));
        return a;
    }

    /**
     * Standard action for delayed dodge.
     * time between 800 and 1300 works best
     * @param time length of the dodge (not delay between jumps)
     * @param delay delay between first and second jump
     * @return
     */
    public static Action delayeddodge(long time,long delay)
    {
        Action a = new Action(time).add(new ActionPart(0,delay-100).withJump().withPitch(0))
                .add(new ActionPart(delay-100,delay).withJump(false).withPitch(-1))
                .add(new ActionPart(delay,delay+100).withJump().withPitch(-1));
        return a;

    }

    /**
     * Standard wavedash action
     * @param time
     * @return
     */
    public static Action wavedash(long time,float roll)
    {
        /*Action a = new Action(time)
                .add(new ActionPart(0,time).withThrottle(1))
                .add(new ActionPart(50,60).withPitch(1).withJump())
                .add(new ActionPart(60,280).withJump(false).withPitch(1))
                .add(new ActionPart(850,950).withJump().withPitch(-1))
                .add(new ActionPart(950,1000).withJump(false).withPitch(0)); /*
                .add(new ActionPart(20,500).withJump(false).withPitch(1))
                .add(new ActionPart(500,800).withJump().withPitch(-1));*/
        Action a = new Action(time)
                .add(new ActionPart(0,2000).withSlide().withThrottle(1))
                .add(new ActionPart(0,2).withJump())
                .add(new ActionPart(10,200).withPitch(1).withYaw(0).withRoll(roll))
                .add(new ActionPart(850,900).withPitch(-1).withYaw(0).withRoll(-roll))
                .add(new ActionPart(850,890).withJump());
        return a;
    }

    /**
     * standard drive action
     * @param steer
     * @param throttle
     * @param boost
     * @return
     */
    public static Action drive(float steer,float throttle,boolean boost)
    {
        Action a = new Action(1).add(new ActionPart(0,100).withSteer(steer).withThrottle(throttle).withBoost(boost));
        return a;
    }

    public static Action sonicflip(long time,int offset)
    {
        Action a = new Action(time)
                .add(new ActionPart(0+offset,2600+offset).withThrottle(1))
                .add(new ActionPart(100+offset,500+offset).withJump().withPitch(-0.3f))
                .add(new ActionPart(600+offset,700+offset).withJump().withPitch(-1))
                .add(new ActionPart(1800+offset,2600+offset).withSlide())
                .add(new ActionPart(2050+offset,2100+offset).withJump().withPitch(1))
                .add(new ActionPart(2450+offset,2600+offset).withJump().withPitch(-1));
        return a;
    }
}
