package rlbotexample;

import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.cppinterop.RLBotDll;
import rlbot.flat.GameTickPacket;
import rlbot.gamestate.*;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Controller.ActionController;
import rlbotexample.Objects.BoostPadManager;
import rlbotexample.States.*;
import rlbotexample.boost.BoostManager;
import rlbotexample.boost.BoostPad;
import rlbotexample.dropshot.DropshotTile;
import rlbotexample.dropshot.DropshotTileManager;
import rlbotexample.dropshot.DropshotTileState;
import rlbotexample.input.CarData;
import rlbotexample.input.DataPacket;
import rlbotexample.input.Information;
import rlbotexample.output.ControlsOutput;

import java.awt.*;
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
        //states.add(new Kickoff(information));
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

    /**
     * This is a nice example of using the rendering feature.
     */
    private void drawDebugLines(DataPacket input, CarData myCar, boolean goLeft) {
        // Here's an example of rendering debug data on the screen.
        Renderer renderer = BotLoopRenderer.forBotLoop(this);

        // Draw a line from the car to the ball
        renderer.drawLine3d(Color.LIGHT_GRAY, myCar.position, input.ball.position);

        // Draw a line that points out from the nose of the car.
        renderer.drawLine3d(goLeft ? Color.BLUE : Color.RED,
                myCar.position.plus(myCar.orientation.noseVector.scaled(150)),
                myCar.position.plus(myCar.orientation.noseVector.scaled(300)));

        renderer.drawString3d(goLeft ? "left" : "right", Color.WHITE, myCar.position, 2, 2);

        for (DropshotTile tile: DropshotTileManager.getTiles()) {
            if (tile.getState() == DropshotTileState.DAMAGED) {
                renderer.drawCenteredRectangle3d(Color.YELLOW, tile.getLocation(), 4, 4, true);
            } else if (tile.getState() == DropshotTileState.DESTROYED) {
                renderer.drawCenteredRectangle3d(Color.RED, tile.getLocation(), 4, 4, true);
            }
        }

        // Draw a rectangle on the tile that the car is on
        DropshotTile tile = DropshotTileManager.pointToTile(myCar.position.flatten());
        if (tile != null) renderer.drawCenteredRectangle3d(Color.green, tile.getLocation(), 8, 8, false);
    }


    @Override
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
        // Translate the raw packet data (which is in an unpleasant format) into our custom DataPacket class.
        // The DataPacket might not include everything from GameTickPacket, so improve it if you need to!
        //DataPacket dataPacket = new DataPacket(packet, playerIndex);

        // Do the actual logic using our dataPacket.


        ControlsOutput controlsOutput = processInput();

        return controlsOutput;
    }

    public void retire() {
        System.out.println("Retiring sample bot " + playerIndex);
    }
}
