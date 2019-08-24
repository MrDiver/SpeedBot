package rlbotexample.States;

import rlbot.Bot;
import rlbot.gamestate.GameInfoState;
import rlbot.gamestate.GameState;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Controller.Action;
import rlbotexample.Controller.ActionPart;
import rlbotexample.Objects.BoostPadManager;
import rlbotexample.Objects.GameCar;
import rlbotexample.Util;
import rlbotexample.boost.BoostManager;
import rlbotexample.boost.BoostPad;
import rlbotexample.input.Information;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Kickoff extends State {
    public Kickoff(Information information) {
        super(information);
    }

    Action a;
    boolean first=false;

    Vector3 vectoball;
    Vector3 ballLocal;
    Vector3 target;
    @Override
    public Action getAction() {
        //System.out.println("Kickoff");
        GameCar me = information.me;
        //System.out.println(me.location().distance(information.ball.location()));
        vectoball = information.ball.location().minus(information.me.location());
        if(information.me.location().distance(information.ball.location())<200)
        {
            return Action.dodge(300);
        }
        if(me.location().distance(information.ball.location())<1000)
        {
            Vector3 ball = me.transformToLocal(information.ball);
            float angle = ball.angle2D();
            return Action.drive((float)Util.cap(angle,-1,1),1,true);
        }
        if(me.location().distance(information.ball.location())>2000&&me.velocity().magnitude()<1000)
        {
            target = BoostPadManager.getNearestSmall(me.location().plus(vectoball.scaledToMagnitude(250))).getLocation();
        }else {
            target = information.ball.location().minus(new Vector3(0, -Util.sign(me.team().ordinal()) * 250, 0));
        }
            ballLocal = information.me.transformToLocal(target);
        float angletoball = ballLocal.angle2D();
        /*if(me.velocity().magnitude()>1300&&me.velocity().magnitude()<1400)
        {
            return Action.dodge(300,0).add(new ActionPart(0,30));
        }*/
        return Action.drive((float)Util.cap(angletoball,-1,1),1,true);

/*
        if(me.location().distance(information.ball.location()) < 250)
        {
            System.out.println("dodge");
            first = false;
            return Action.dodge(100).add(new ActionPart(16,100).withBoost());
        }
        if(first)
        {
            a = new Action(0).add(new ActionPart(0,100).withBoost(me.hasWheelContact()).withSteer((float)Util.cap(angletoball,-1,1)).withYaw((float)Util.cap(angletoball,-1,1)));
            return a;
        }
        a = new Action(1500);
        //System.out.println("Kickoff Check");
        //System.out.println(me.location());
        if(me.inLocation2D(new Vector3(-2048,-2560,0))||me.inLocation2D(new Vector3(2048,2560,0)))
        {
            //System.out.println("Right Corner Kickoff");
            /*a.add(new ActionPart(0,200).withThrottle(1).withSteer(1).withBoost());*//*
        }else
        if(me.inLocation2D(new Vector3(2048,-2560,0))||me.inLocation2D(new Vector3(-2048,2560,0)))
        {
            //System.out.println("Left Corner Kickoff");
        }else
        if(me.inLocation2D(new Vector3(-256,-3840,0))||me.inLocation2D(new Vector3(256,3840,0)))
        {
            //System.out.println("Back Right Kickoff");
           /* a.add(new ActionPart(0,180).withThrottle(1).withBoost().withSteer(-1))
            .add(new ActionPart(180,800).withThrottle(1).withBoost())
            .add(new ActionPart(800,830).withJump().withBoost().withThrottle(1))
            .add(new ActionPart(830,900).withJump(false).withBoost().withThrottle(1))
            .add(new ActionPart(900,1500).withThrottle(1).withBoost().withRoll(1).withPitch(-0.3f).withJump())
            /*.add(new ActionPart(1300,3000).withBoost().withThrottle(1).withSteer(0f).withYaw(0f))*//*;
        }else
        if(me.inLocation2D(new Vector3(256,-3840,0))||me.inLocation2D(new Vector3(-256,3840,0)))
        {
            /*a.add(new ActionPart(0,180).withThrottle(1).withBoost().withSteer(1))
                    .add(new ActionPart(180,800).withThrottle(1).withBoost())
                    .add(new ActionPart(800,830).withJump().withBoost().withThrottle(1))
                    .add(new ActionPart(830,900).withJump(false).withBoost().withThrottle(1))
                    .add(new ActionPart(900,1500).withThrottle(1).withBoost().withRoll(-1).withPitch(-0.3f).withJump());*//*
        }else
        if(me.inLocation2D(new Vector3(0,-4608,0))||me.inLocation2D(new Vector3(0,4608,0)))
        {
            //System.out.println("Far Back Kickoff");
        }
        //first = true;
        return a;*/

    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        r.drawCenteredRectangle3d(Color.green,target,10,10,false);
        r.drawCenteredRectangle3d(Color.blue,(information.me.location().plus(vectoball.scaledToMagnitude(300))),10,10,false);
    }

    @Override
    public boolean isAvailable() {
       /* if(!information.isKickoffPause())
        {
            if (first)
            {
                first = false;
            }
        }*/
        return false;
        //return information.isKickoffPause();
    }

    @Override
    public double getRating() {
        return information.isKickoffPause()?10:0;
    }
}
