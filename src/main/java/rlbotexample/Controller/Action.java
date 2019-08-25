package rlbotexample.Controller;

import rlbotexample.Landmark;
import rlbotexample.input.Information;
import rlbotexample.output.ControlsOutput;

import javax.sound.sampled.Line;
import java.util.ArrayList;

public class Action extends AbstractAction{

     /**
     * Creates a player action with some execution length that can be used by the ActionController or a State
     * @param executionLength the maximal time of execution after the Action gets disabled
     */
     public Action(float executionLength, Information information)
     {
         super(executionLength,information);
     }



    /**
     * Executes all ActionParts until the executionLength is exceeded
     * @param output the ControlOutput from the Car
     * @return returns the changed controls original controls are kept and new ones are added
     */
    public ControlsOutput execute(ControlsOutput output)
    {
        if(active) {
            float delta = information.secondsElapsed() - start;
            if(delta >executionLength && condition.test())
            {
                active = false;
            }
            int size = parts.size();
            for(int i = 0; i <size; i++)
            {
                output = parts.get(i).execute(delta,output);
            }
            state=output;
           // System.out.println(delta);
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

    public Action addCondition(Condition condition)
    {
        this.condition = condition;
        return this;
    }


    /**
     * Standard action for dodge
     * @param time length of the dodge (not delay between jumps)
     * @return
     */
    public static Action dodge(float time,Information information)
    {
       Action a = dodge(time,0,true,information);
            return a;
    }

    public static Action dodge(float time,double angle,boolean tillGround,Information information)
    {
        Action a = Action.dodge(time,100,50,50,angle,true,information);
        if(tillGround)
            a.addCondition(()->information.me.hasWheelContact());
        return a;
    }

    public static Action dodge(float time,float first,float wait,float second,double angle,boolean withThrottle,Information information)
    {
        Action a = new Action(time,information).add(new ActionPart(0,first).withJump().withPitch((float)-Math.cos(angle)).withYaw((float)-Math.sin(angle)))
                .add(new ActionPart(first,first+wait).withJump(false).withPitch((float)-Math.cos(angle)).withYaw((float)-Math.sin(angle)))
                .add(new ActionPart(first+wait,first+wait+second).withJump().withPitch((float)-Math.cos(angle)).withYaw((float)-Math.sin(angle)))
                .add(new ActionPart(0,time).withThrottle(withThrottle?1:0));
        return a;
    }

    /**
     * Standard action for delayed dodge.
     * time between 800 and 1300 works best
     * @param time length of the dodge (not delay between jumps)
     * @param delay delay between first and second jump
     * @return
     */
    public static Action delayeddodge(float time,long delay,Information information)
    {
        Action a = new Action(time,information).add(new ActionPart(0,delay-100).withJump().withPitch(0))
                .add(new ActionPart(delay-100,delay).withJump(false).withPitch(-1))
                .add(new ActionPart(delay,delay+100).withJump().withPitch(-1));
        return a;

    }

    /**
     * Standard wavedash action
     * @param time
     * @return
     */
    public static Action wavedash(long time,float roll,Information information)
    {
        /*Action a = new Action(time)
                .add(new ActionPart(0,time).withThrottle(1))
                .add(new ActionPart(50,60).withPitch(1).withJump())
                .add(new ActionPart(60,280).withJump(false).withPitch(1))
                .add(new ActionPart(850,950).withJump().withPitch(-1))
                .add(new ActionPart(950,1000).withJump(false).withPitch(0)); /*
                .add(new ActionPart(20,500).withJump(false).withPitch(1))
                .add(new ActionPart(500,800).withJump().withPitch(-1));*/
        Action a = new Action(time,information)
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
    public static Action drive(float steer,float throttle,boolean boost,Information information)
    {
        Action a = new Action(1,information).add(new ActionPart(0,100).withSteer(steer).withThrottle(throttle).withBoost(boost));
        return a;
    }

    public static Action sonicflip(float time,float offset,Information information)
    {
        Action a = new Action(time,information)
                .add(new ActionPart(0+offset,2600+offset).withThrottle(1))
                .add(new ActionPart(100+offset,500+offset).withJump().withPitch(-0.3f))
                .add(new ActionPart(600+offset,700+offset).withJump().withPitch(-1))
                .add(new ActionPart(1800+offset,2600+offset).withSlide())
                .add(new ActionPart(2050+offset,2100+offset).withJump().withPitch(1))
                .add(new ActionPart(2450+offset,2600+offset).withJump().withPitch(-1));
        return a;
    }

    public Action delay(float delay)
    {
        for(ActionPart ap : parts)
        {
            ap.delay(delay);
        }
        executionLength+= delay/1000;
        return this;
    }

}
