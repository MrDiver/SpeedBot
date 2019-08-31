package rlbotexample.States;

import rlbot.Bot;
import rlbot.gamestate.GameInfoState;
import rlbot.gamestate.GameState;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Controller.*;
import rlbotexample.Objects.Ball;
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

    Vector3 target;
    @Override
    public AbstractAction getAction() {
        GameCar me = information.me;
        Ball ball = information.ball;
        target = new Vector3();
        ActionChain a = chain(3500);
        //AdjustmentSpeed for the dodge before the ball
        //0.7f standard
        float aS = 0.6f;
        //Ball adjustment angle lower means more in the direction of the balls center
        //0.05f standard
        float bA = 0.03f;
        //distance to the ball before the car dodges
        // 400 standard
        int distanceToDodge = 400;

        float lastJumpAngle = 1.0f;

        int firstBoostDiagonal = 2450;
        int firstBoostBack = 3200;
        if(me.inLocation2D(new Vector3(-2048,-2560,0))||me.inLocation2D(new Vector3(2048,2560,0)))
        {
            //System.out.println("Right Corner Kickoff");
            Value steerToBall = ()->{
                float angle = me.transformToLocal(ball).angle2D();
                return angle > 0.1 ? aS: angle < -0.1 ? -aS : 0;
            };

            a = a.addAction(
                    //Drive until first boost reached
                    action(50).addCondition(()->Math.abs(me.location().y)<firstBoostDiagonal)
                            .add(part(0,1000).withThrottle(1).withBoost())
            ).addAction(
                    action(10).addCondition(()->{
                        float angle = me.transformToLocal(ball).angle2D();
                        return angle < bA;
                    }).add(part(0,1000).withThrottle(1).withSteer(1).withYaw(1).withBoost())
            ).addAction(
                    action(150).add(part(0,100).withJump().withYaw(1)).add(part(0,150).withBoost())
            ).addAction(
                    action(800).add(part(0,400).withJump().withRoll(-0.7f).withPitch(-0.65f).withBoost())
            ).addAction(
                    action(400)
                            .addCondition(()->me.location().distance(new Vector3())<distanceToDodge)
                            .add(part(0,1000).withThrottle(1).withSteer(steerToBall).withYaw(steerToBall).withBoost())
            ).addAction(
                    Action.dodge(300,-lastJumpAngle,false,information)
            );
        }else
        if(me.inLocation2D(new Vector3(2048,-2560,0))||me.inLocation2D(new Vector3(-2048,2560,0)))
        {
            //System.out.println("Left Corner Kickoff");
            Value steerToBall = ()->{
                float angle = me.transformToLocal(ball).angle2D();
                return angle > 0.1 ? aS: angle < -0.1 ? -aS : 0;
            };

            a = a.addAction(
                    //Drive until first boost reached
                    action(50).addCondition(()->Math.abs(me.location().y)<firstBoostDiagonal)
                            .add(part(0,1000).withThrottle(1).withBoost())
            ).addAction(
                    action(10).addCondition(()->{
                        float angle = me.transformToLocal(ball).angle2D();
                        return angle > -bA;
                    }).add(part(0,1000).withThrottle(1).withSteer(-1).withYaw(-1).withBoost())
            ).addAction(
                    action(150).add(part(0,100).withJump().withYaw(-1)).add(part(0,150).withBoost())
            ).addAction(
                    action(800).add(part(0,400).withJump().withRoll(0.7f).withPitch(-0.65f).withBoost())
            ).addAction(
                    action(400)
                            .addCondition(()->me.location().distance(new Vector3())<distanceToDodge)
                            .add(part(0,1000).withThrottle(1).withSteer(steerToBall).withYaw(steerToBall).withBoost())
            ).addAction(
                    Action.dodge(300,lastJumpAngle,false,information)
            );
        }else
        if(me.inLocation2D(new Vector3(-256,-3840,0))||me.inLocation2D(new Vector3(256,3840,0)))
        {
            //System.out.println("Back Right Kickoff");
            Value steerToBall = ()->{
                float angle = me.transformToLocal(ball).angle2D();
                return angle > 0.1 ? aS: angle < -0.1 ? -aS : 0;
            };

            Vector3 nearestCenter = new Vector3(0,me.location().y - 2816 > 0 ? 2816:-2816,70);
            a = a.addAction(
                    action(10).addCondition(()->me.transformToLocal(nearestCenter).angle2D()>-0.03)
                            .add(part(0,1000).withThrottle(1).withBoost().withSteer(-0.5f))
            ).addAction(
                    action(10).addCondition(()->Math.abs(me.location().y)<3200)
                            .add(part(0,1000).withThrottle(1).withBoost())
            ).addAction(
                    action(100).add(part(0,40).withJump()).add(part(0,150).withBoost())
            )
                    .addAction(
                            action(700).add(part(0,300).withJump().withRoll(1).withPitch(-0.35f).withBoost())
                    ).addAction(
                            action(400)
                                    .addCondition(()->me.location().distance(new Vector3())<distanceToDodge+40)
                                    .add(part(0,1000).withThrottle(1).withSteer(steerToBall).withYaw(steerToBall).withBoost())
                    ).addAction(
                            Action.dodge(300,lastJumpAngle,false,information)
                    );
        }else
        if(me.inLocation2D(new Vector3(256,-3840,0))||me.inLocation2D(new Vector3(-256,3840,0)))
        {
            //System.out.println("Back Left Kickoff");
            Value steerToBall = ()->{
                float angle = me.transformToLocal(ball).angle2D();
                return angle > 0.1 ? aS: angle < -0.1 ? -aS : 0;
            };

            Vector3 nearestCenter = new Vector3(0,me.location().y - 2816 > 0 ? 2816:-2816,70);
            a = a.addAction(
                    action(10).addCondition(()->me.transformToLocal(nearestCenter).angle2D()<0.03)
                    .add(part(0,1000).withThrottle(1).withBoost().withSteer(0.5f))
            ).addAction(
                    action(10).addCondition(()->Math.abs(me.location().y)<3200)
                    .add(part(0,1000).withThrottle(1).withBoost())
            ).addAction(
                    action(100).add(part(0,40).withJump()).add(part(0,150).withBoost())
            )
            .addAction(
                    action(700).add(part(0,300).withJump().withRoll(-1).withPitch(-0.35f).withBoost())
            ).addAction(
                            action(400)
                                    .addCondition(()->me.location().distance(new Vector3())<distanceToDodge+40)
                                    .add(part(0,1000).withThrottle(1).withSteer(steerToBall).withYaw(steerToBall).withBoost())
                    ).addAction(
                            Action.dodge(300,-lastJumpAngle,false,information)
                    );
        }else
        if(me.inLocation2D(new Vector3(0,-4608,0))||me.inLocation2D(new Vector3(0,4608,0)))
        {
            //System.out.println("Far Back Kickoff");
            Value steerToBall = ()->{
                float angle = me.transformToLocal(ball).angle2D();
                return angle > 0.1 ? aS: angle < -0.1 ? -aS : 0;
            };
            a = a.addAction(
                    action(10).addCondition(()->Math.abs(me.location().y)<3500)
                            .add(part(0,1000).withThrottle(1).withBoost())
            ).addAction(
                Action.dodge(700,0,false,information).add(part(0,100).withBoost())
            ).addAction(
                    action(400)
                            .addCondition(()->me.location().distance(new Vector3())<distanceToDodge+40)
                            .add(part(0,1000).withThrottle(1).withSteer(steerToBall).withYaw(steerToBall).withBoost())
            ).addAction(
                    Action.dodge(300,0,false,information)
            );
        }
        return a;


        /*//System.out.println("Kickoff");
        GameCar me = information.me;
        //System.out.println(me.location().distance(information.ball.location()));
        vectoball = information.ball.location().minus(information.me.location());
        if(information.me.location().distance(information.ball.location())<200)
        {
            return Action.dodge(300,information);
        }
        if(me.location().distance(information.ball.location())<1000)
        {
            Vector3 ball = me.transformToLocal(information.ball);
            float angle = ball.angle2D();
            return Action.drive((float)Util.cap(angle,-1,1),1,true,information);
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
        }*//*
        return Action.drive((float)Util.cap(angletoball,-1,1),1,true,information);
*/
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
        //r.drawCenteredRectangle3d(Color.green,target,10,10,false);
        //r.drawCenteredRectangle3d(Color.blue,(information.me.location().plus(vectoball.scaledToMagnitude(300))),10,10,false);
        r.drawString3d("Kickoff",Color.white,information.me.location().plus(new Vector3(0,0,300)),1,1);
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
        //return false;
        return information.isKickoffPause();
    }

    @Override
    public double getRating() {
        return information.isKickoffPause()?10:0;
    }
}
