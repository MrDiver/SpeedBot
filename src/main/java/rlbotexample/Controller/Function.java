package rlbotexample.Controller;

import rlbotexample.output.ControlsOutput;

/**
 * Holda a Controlsoutput function that can be applied in the ActionController chain
 */
public interface Function {
    public ControlsOutput apply(ControlsOutput output);
}
