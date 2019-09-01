package rlbotexample;

import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;
import rlbotexample.Controller.ActionController;
import rlbotexample.Objects.BoostPadManager;
import rlbotexample.States.*;
import rlbotexample.boost.BoostManager;
import rlbotexample.dropshot.DropshotTileManager;
import rlbotexample.input.Information;
import rlbotexample.output.ControlsOutput;

import java.util.ArrayList;

public class SpeedBot implements Bot {

    private final int playerIndex;
    private ActionController actionController;
    State state;
    private ArrayList<State>states;
    Information information;
    public SpeedBot(int playerIndex)
    {
        this.playerIndex = playerIndex;
        information = new Information(playerIndex);
        actionController = new ActionController(information);
        state = new Position(information);
        states = new ArrayList<>();
        states.add(new Position(information));
        states.add(new CalcShot(information));
        states.add(new Wait(information));
        //states.add(new TestActions(information));
        states.add(new Kickoff(information));
        states.add(new AfterKickoff(information));
    }

    /**
     * This is where we keep the actual bot logic. This function shows how to chase the ball.
     * Modify it to make your bot smarter!
     */
    private ControlsOutput processInput() {
        ControlsOutput output = new ControlsOutput();
        state = choosState();
        if(information.isRoundActive())
            output = actionController.execute(output,state);
        actionController.draw(this);
        state.draw(this);
        return output;
    }

    State last;
    private State choosState()
    {
        State state = states.get(0);
        for(State s : states)
        {
            if(s.isAvailable()&& s.getRating() > state.getRating())
                state = s;
        }
        if(state!= last)
        {
            state.start();
        }
        last=state;
        return state;
    }

    public int getIndex() {
        return this.playerIndex;
    }

    /**
     * This is the most important function. It will automatically get called by the framework with fresh data
     * every frame. Respond with appropriate controls!
     */
    @Override
    public ControllerState processInput(GameTickPacket packet) {

        if (packet.playersLength() <= playerIndex || packet.ball() == null || !packet.gameInfo().isRoundActive()) {
            // Just return immediately if something looks wrong with the data. This helps us avoid stack traces.
            return new ControlsOutput();
        }

        // Update the boost manager and tile manager with the latest data
        BoostManager.loadGameTickPacket(packet);
        BoostPadManager.loadGameTickPacket(packet);
        DropshotTileManager.loadGameTickPacket(packet);
        information.loadGameTickPacket(packet);

        ControlsOutput controlsOutput = processInput();

        return controlsOutput;
    }

    public void retire() {
        System.out.println("Retiring sample bot " + playerIndex);
    }
}
