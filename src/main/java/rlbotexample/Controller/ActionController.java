package rlbotexample.Controller;

import rlbot.Bot;
import rlbot.cppinterop.RLBotDll;
import rlbot.gamestate.*;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Landmark;
import rlbotexample.States.State;
import rlbotexample.States.TestActions;
import rlbotexample.input.Information;
import rlbotexample.output.ControlsOutput;

import javax.sound.sampled.Line;
import java.awt.*;

/**
 * ActionController switches between Actions given by a state and applies them to the given ControlsOutput
 */
public class ActionController {
    Action current;
    ControlsOutput o;
    Information information;
    /**
     * Creates an ActionController with a empty Action
     */
    public ActionController(Information information)
    {
        current = new Action(0);
        this.information = information;
    }

    /**
     * Chooses a new Action from the given State if the current Action is not active
     * @param output actions get applied to this
     * @param state some state
     * @return returns the new ControlsOutput with new settings if something has changed
     */
    int a = 0;
    private State last;
    private boolean wheelContact;
    public ControlsOutput execute(ControlsOutput output,State state)
    {
        if(state != last || current.isActive()==false)
        {
            if(a > 1&& state.getClass() == TestActions.class) {
                GameState gameState = new GameState()
                        .withCarState(information.playerIndex, new CarState()
                                .withPhysics(new PhysicsState()
                                        .withVelocity(new DesiredVector3().withZ(0F).withX(0F).withY(0F))
                                        .withRotation(new DesiredRotation((float) 0F, 300F, 0F))
                                        .withLocation(new DesiredVector3().withX(0f).withY(4000f).withZ(30f))))
                        .withBallState(new BallState().withPhysics(new PhysicsState().withLocation(new DesiredVector3(null, null, 500F))));

                RLBotDll.setGameState(gameState.buildPacket());
                a = 0;
            }
            current = state.getAction();
            current.start();
            wheelContact = information.me.hasWheelContact();
            a++;
        }

        output = current.execute(output);
        o=output;
        last = state;
        return output;
    }

    /**
     * Draws Information about the current state of the Controller draws all Information available to the Controller
     */
    public void draw(Bot bot)
    {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        int offsetx = 1500;
        int offsety = 10;
        r.drawRectangle2d(Color.white,new Point(offsetx-5,offsety-5),500,200,true);
        r.drawString2d("Steer: "+o.getSteer(), Color.red,new Point(offsetx,offsety),1,1);
        r.drawString2d("Pitch: "+o.getPitch(), Color.red,new Point(offsetx,offsety+20),1,1);
        r.drawString2d("Yaw: "+o.getYaw(), Color.red,new Point(offsetx,offsety+40),1,1);
        r.drawString2d("Roll: "+o.getRoll(), Color.red,new Point(offsetx,offsety+60),1,1);
        r.drawString2d("Throttle: "+o.getThrottle(), Color.red,new Point(offsetx,offsety+80),1,1);
        r.drawString2d("Jump: "+o.holdJump(),  o.holdJump()? Color.green:Color.red,new Point(offsetx,offsety+100),1,1);
        r.drawString2d("Boost: "+o.holdBoost(),  o.holdBoost()? Color.green:Color.red,new Point(offsetx,offsety+120),1,1);
        r.drawString2d("Handbrake: "+o.holdHandbrake(),  o.holdHandbrake()? Color.green:Color.red,new Point(offsetx,offsety+140),1,1);
        r.drawString2d("WheelContact: "+wheelContact,  wheelContact? Color.green:Color.red,new Point(offsetx,offsety+160),1,1);
        r.drawString2d("Speed: "+information.me.velocity().magnitude(),Color.red,new Point(offsetx,offsety+180),1,1);
    }
}
