package rlbotexample;

import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Controller.ActionController;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.objects.BoostPadManager;
import rlbotexample.States.*;
import rlbotexample.States.Defense.*;
import rlbotexample.States.OffenseAir.TryAerial;
import rlbotexample.States.OffenseGround.Dribble;
import rlbotexample.States.OffenseGround.Reposition;
import rlbotexample.States.OffenseGround.TakeShot;
import rlbotexample.boost.BoostManager;
import rlbotexample.input.Information;
import rlbotexample.prediction.*;
import rlbotexample.output.ControlsOutput;
import rlbotexample.prediction.PathCalculation.NoGridAStar;
import rlbotexample.prediction.PathCalculation.Node;
import rlbotexample.prediction.PathCalculation.NodeWeight;
import rlbotexample.vector.Vector3;

import java.awt.*;
import java.util.ArrayList;

public class SpeedBot implements Bot {

    private final int playerIndex;
    private ActionController actionController;
    State state;
    private ArrayList<State>states;
    Information information;
    Predictions predictions;
    NoGridAStar noGridAStar;
    public SpeedBot(int playerIndex)
    {
        this.playerIndex = playerIndex;
        information = new Information(playerIndex);
        actionController = new ActionController(information);
        predictions = new Predictions(information);
        ActionLibrary a = new ActionLibrary(information);
        state = new Kickoff(information,a,predictions);
        states = new ArrayList<>();
        states.add(new GetBoost(information,a,predictions));
        states.add(new Kickoff(information,a,predictions));
        states.add(new AfterKickoff(information,a,predictions));
        states.add(new BallToOwnside(information,a,predictions));
        states.add(new MakeSave(information,a,predictions));
        states.add(new Retreat(information,a,predictions));
        states.add(new TryAerial(information,a,predictions));
        states.add(new Dribble(information,a,predictions));
        states.add(new Reposition(information,a,predictions));
        states.add(new TakeShot(information,a,predictions));
        states.add(new Shadowing(information,a,predictions));
        states.add(new Defending(information,a,predictions));

        noGridAStar = new NoGridAStar(information);
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
        //predictions.draw(this);
        //actionController.draw(this);
        //state.draw(this);
        draw();
        return output;
    }

    public void draw()
    {
        Renderer r = BotLoopRenderer.forBotLoop(this);
        int offx = 50;
        int offy = 300;

        r.drawRectangle2d(Color.green,new Point(offx,offy),10,400,true);
        r.drawString2d(state.name+ ": " +state.getRating(),Color.CYAN,new Point(offx+20,offy-20),1,1);
        r.drawRectangle3d(Color.white,new Vector3(0,0,0),10,10,true);
        for(State s : states)
        {
            r.drawString2d(s.name + ": " +s.getRating(),Color.white,new Point(offx+20,(int)(offy+(400-s.getRating()*40))),1,1);
        }

        Vector3 off = information.me.location().plus(information.me.velocity());
        NodeWeight weightFunction = (Node node)->{
            float weight = 0;
            weight += node.position.distance(BoostPadManager.getNearestSmall(node.position).getLocation())/2;
            weight += node.position.distance(off)/4;
            return weight;
        };
        noGridAStar.calculate(information.me.location(),information.ownGoal.location(),500,8,weightFunction);
        noGridAStar.draw();
        information.me.location().plus(information.me.velocity()).draw(Color.pink,this);
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
        //DropshotTileManager.loadGameTickPacket(packet);
        information.loadGameTickPacket(packet);
        predictions.update();

        ControlsOutput controlsOutput = processInput();

        return controlsOutput;
    }

    public void retire() {
        System.out.println("Retiring sample bot " + playerIndex);
    }
}
