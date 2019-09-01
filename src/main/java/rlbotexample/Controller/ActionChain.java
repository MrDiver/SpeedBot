package rlbotexample.Controller;

import rlbotexample.input.Information;
import rlbotexample.output.ControlsOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class ActionChain extends AbstractAction{

    ArrayDeque<AbstractAction> chain;
    AbstractAction current;

    public ActionChain(float executionLength, Information information) {
        super(executionLength, information);
        chain = new ArrayDeque<>();
    }

    @Override
    public ControlsOutput execute(ControlsOutput output) {

            if(current == null || current.isActive() == false)
            {
                if(chain.size()>0) {
                    current = chain.poll();
                    current.start();
                }else
                {
                    active = false;
                }
            }
            if(current.isActive())
            {
                current.execute(output);
            }

        return output;
    }

    public ActionChain addAction(AbstractAction a)
    {
        chain.add(a);
        return this;
    }

    @Override
    public boolean isActive()
    {
        if(getElapsed() > executionLength)
            active = false;

        /*if(chain.size()==0)
        {
            active = false;
        }*/
        return active;
    }
}
