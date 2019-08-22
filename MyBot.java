package rlbotexample;

import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.FieldInfo;
import rlbot.flat.GameTickPacket;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.boost.BoostManager;
import rlbotexample.dropshot.DropshotTile;
import rlbotexample.dropshot.DropshotTileManager;
import rlbotexample.dropshot.DropshotTileState;
import rlbotexample.input.CarData;
import rlbotexample.input.DataPacket;
import rlbotexample.output.ControlsOutput;
import rlbotexample.states.CalcShot;
import rlbotexample.states.ExampleATBA;
import rlbotexample.states.QuickShot;
import rlbotexample.states.State;
import rlbotexample.vector.Vector2;
import rlbotexample.vector.Vector3;
import static rlbotexample.Util.*;

import java.awt.*;
import java.util.Vector;
public class MyBot implements Bot {

    private final int playerIndex;

    public Car car;
    public Obj ball;
    State state;
    public ExampleATBA exampleATBA;
    public QuickShot quickShot;
    public CalcShot calcShot;
    public MyBot(int playerIndex) {
        this.playerIndex = playerIndex;
        car = new Car();
        ball = new Obj();
        exampleATBA = new ExampleATBA();
        quickShot = new QuickShot();
        calcShot = new CalcShot();
        state = quickShot;
    }


    /**
     * This is where we keep the actual bot logic. This function shows how to chase the ball.
     * Modify it to make your bot smarter!
     */


    private void preprocess(GameTickPacket game)
    {
        car.location = new Vector3(game.players(playerIndex).physics().location());
        car.velocity = new Vector3(game.players(playerIndex).physics().velocity());
        car.rotation = new Vector3(game.players(playerIndex).physics().rotation().pitch(),game.players(playerIndex).physics().rotation().yaw(),game.players(playerIndex).physics().rotation().roll());
        car.rvelocity = new Vector3(game.players(playerIndex).physics().angularVelocity());
        rotator_to_matrix(car);
        car.boost = game.players(playerIndex).boost();
        car.team = game.players(playerIndex).team();

        ball.location = new Vector3(game.ball().physics().location());
        ball.velocity = new Vector3(game.ball().physics().velocity());
        ball.rotation = new Vector3(game.ball().physics().rotation().pitch(),game.ball().physics().rotation().yaw(),game.ball().physics().rotation().roll());
        ball.rvelocity = new Vector3(game.ball().physics().angularVelocity());

        ball.local_location = to_local(ball,car);
    }

    private ControlsOutput processInput(DataPacket input) {
        drawDebugLines(input,input.car,false);

        if(state.expired)
            if(calcShot.available(this))
                state = calcShot;
            else if (quickShot.available(this))
                state = quickShot;
            else
                state = quickShot;

        return state.execute(this);
    }

    /**
     * This is a nice example of using the rendering feature.
     */
    private void drawDebugLines(DataPacket input, CarData myCar, boolean goLeft){
        // Here's an example of rendering debug data on the screen.
        Renderer renderer = BotLoopRenderer.forBotLoop(this);

        // Draw a line from the car to the ball
        renderer.drawLine3d(Color.LIGHT_GRAY, myCar.position, input.ball.position);
        //renderer.drawLine3d(Color.LIGHT_GRAY, myCar.position,);

        // Draw a line that points out from the nose of the car.
        renderer.drawLine3d(goLeft ? Color.BLUE : Color.RED,
                myCar.position.plus(myCar.orientation.noseVector.scaled(150)),
                myCar.position.plus(myCar.orientation.noseVector.scaled(300)));

        renderer.drawString3d(goLeft ? "left" : "right", Color.WHITE, myCar.position, 1, 1);
        renderer.drawRectangle3d(Color.blue,ball.location,10,10,false);
        //renderer.drawString2d("Jump:"+Boolean.toString(true),Color.white,new Point(1200,10),1,1);
        renderer.drawString2d("State:"+state,Color.white,new Point(1200,30),1,1);

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
        DropshotTileManager.loadGameTickPacket(packet);

        // Translate the raw packet data (which is in an unpleasant format) into our custom DataPacket class.
        // The DataPacket might not include everything from GameTickPacket, so improve it if you need to!
        DataPacket dataPacket = new DataPacket(packet, playerIndex);

        preprocess(packet);
        // Do the actual logic using our dataPacket.
        ControlsOutput controlsOutput = processInput(dataPacket);

        return controlsOutput;
    }

    public void retire() {
        System.out.println("Retiring sample bot " + playerIndex);
    }
}
