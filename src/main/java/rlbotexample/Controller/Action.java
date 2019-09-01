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
