package rlbotexample.States;

import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.Action;
import rlbotexample.Controller.ActionPart;
import rlbotexample.Objects.Ball;
import rlbotexample.Objects.GameCar;
import rlbotexample.Objects.Goal;
import rlbotexample.Util;
import rlbotexample.input.Information;
import rlbotexample.input.Team;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Position extends State {

public Position(Information information) {
        super(information);
    }
    Vector3 leftPost;
    Vector3 rightPost;
    double ballLeft;
    double ballRight;
    double ourLeft;
    double ourRight;
    double offset;
    Vector3 vectogoal;
    Vector3 target;
    double angletotarget;
    @Override
    public AbstractAction getAction() {
        //Calculating Data
        leftPost = information.eneGoal.leftPost();
        rightPost = information.eneGoal.rightPost();

        ballLeft = information.ball.location().angle2D(leftPost);
        ballRight = information.ball.location().angle2D(rightPost);

        ourLeft = information.me.location().angle2D(leftPost);
        ourRight = information.me.location().angle2D(rightPost);

        vectogoal = information.eneGoal.location().minus(information.ball.location()).normalized();
        target = information.ball.location().minus(vectogoal.scaledToMagnitude(250));

        Vector3 localTarget = information.me.transformToLocal(target);
        angletotarget = localTarget.angle2D();
        double distancetotarget = target.distance(information.me.location());
        double speedcorrection = ((1+angletotarget*angletotarget)*300);
        double targetspeed = 2000- speedcorrection + Util.cap((distancetotarget/16)*(distancetotarget/16),0,speedcorrection);

        //Calculating Action
        double currentspeed = information.me.velocity().flatten().magnitude();
        Action a = new Action(0,information);
        a.add(new ActionPart(0,100).withSteer((float)Util.steer(angletotarget)));
        if(targetspeed > 1400 && targetspeed > currentspeed && starttime > 2500 && currentspeed < 2250)
        {
            a.add(new ActionPart(0,100).withBoost());
        }
        if(targetspeed > currentspeed)
        {
            a.add(new ActionPart(0,100).withThrottle(1));
        }else
        {
            a.add(new ActionPart(0,100).withThrottle(0));
        }

        double delta = System.currentTimeMillis() - starttime;
        if(Util.ballReady(information) && delta > 2200 && distancetotarget < 270)
        {
            this.start();
            return Action.dodge(100,angletotarget,true,information);
        }
        return a;
    /*    Ball ball = information.ball;
        GameCar me = information.me;
        boolean behind = ball.location().y<me.location().y;
        double mespeed = me.velocity().magnitude();
        double ballspeed = ball.velocity().magnitude();
        long timedifference = System.currentTimeMillis() - starttime;
        //Check if ball is behind your car
        if(me.team() == Team.Blue? behind : ! behind)
        {
            target = information.ownGoal.location();
        }else
        {
            target = ball.location();
        }
        target = ball.location();
        Vector3 targetLocal = me.transformToLocal(ball.location());
        float angle = targetLocal.angle2D();
        float throttle=0;
        boolean boost=false;
        double distance = me.location().flatten().distance(target.flatten());
        double targetSpeed = ball.velocity().flatten().magnitude() + distance/2;
        if(timedifference > 4200 &&  distance> 1000&& Math.abs(angle) < 1.3)
        {
            this.start();
            return Action.dodge(200);
        }
        if(targetSpeed > mespeed)
        {
            throttle = 1;
        }else
        {
            throttle = 0;
        }
        if(timedifference > 2200 && distance > 1400 && mespeed < 2250)
        {
            boost = true;
        }



        Action a = Action.drive(angle,throttle,boost);
*/
/*
        vectoball = information.ball.location().minus(information.me.location());

        ballLocal = information.me.transformToLocal(information.ball);
        ownGoal = information.ownGoal;
        eneGoal = information.eneGoal;
        leftball = information.ball.location().minus(eneGoal.leftPost()).normalized();
        rightball = information.ball.location().minus(eneGoal.rightPost()).normalized();
        carleft = information.me.location().minus(eneGoal.leftPost()).normalized();
        carright = information.me.location().minus(eneGoal.leftPost()).normalized();
        double leftlocal = information.me.transformToLocal(eneGoal.leftPost()).normalized().angle2D();
        double rightlocal = information.me.transformToLocal(eneGoal.rightPost()).normalized().angle2D();
        //System.out.println("L: "+leftlocal+"\tB: "+ballLocal.normalized().angle2D()+"\tR: "+rightlocal);
        //System.out.println(leftlocal < ballLocal.normalized().angle2D() && ballLocal.normalized().angle2D() < rightlocal);
        target = information.ball.location().plus(information.ball.location().minus(eneGoal.location()).normalized().scaledToMagnitude(500));
        Vector3 localtarget = information.me.transformToLocal(target);
        float targetangle= localtarget.angle2D();
        if(information.me.location().distance(information.ball.location())<200)
        {
            return Action.dodge(300);
        }
        if(leftlocal < ballLocal.normalized().angle2D() && ballLocal.normalized().angle2D() < rightlocal)
        {
            return Action.drive(ballLocal.angle2D()<0?-1:1,1,true).add(new ActionPart(0,100).withSlide(Math.abs(ballLocal.angle2D())>1.5f));
        }
        float angletoball = ballLocal.angle2D();

        if(information.me.boost()>30&&information.ball.velocity().magnitude()>information.me.velocity().magnitude())
        {
            return Action.drive((float)Util.cap(targetangle,-1,1),1,true);
        }
        if(information.ball.velocity().magnitude()>information.me.velocity().magnitude()&&information.me.boost()<31&&System.currentTimeMillis()%2000<100)
        {
            return Action.dodge(300,targetangle);
        }
        return Action.drive((float)Util.cap(targetangle,-1,1),1,false).add(new ActionPart(0,100).withSlide(Math.abs(targetangle)>1.5f));
        return a;*/
    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        r.drawLine3d(Color.red,information.ball.location(),information.ball.location().minus(vectogoal.scaledToMagnitude(250)));
        r.drawString3d("Position",Color.white,information.me.location().plus(new Vector3(0,0,300)),1,1);
        target.draw(Color.pink,bot);
       /* ownGoal.draw(Color.red,bot);
        eneGoal.draw(Color.green,bot);
        ballLocal.scaledToMagnitude(100).draw(Color.red,bot);
        r.drawLine3d(Color.cyan,information.eneGoal.leftPost(),information.me.location().plus(carleft.scaledToMagnitude(150)));
        r.drawLine3d(Color.cyan,information.eneGoal.rightPost(),information.me.location().plus(carright.scaledToMagnitude(150)));
        r.drawLine3d(Color.cyan,information.eneGoal.leftPost(),information.ball.location().plus(leftball.scaledToMagnitude(150)));
        r.drawLine3d(Color.cyan,information.eneGoal.rightPost(),information.ball.location().plus(rightball.scaledToMagnitude(150)));
        target.draw(Color.RED,bot);*/
    }

    @Override
    public boolean isAvailable() {
        if(Util.ballProject(information) > -(information.ball.location().flatten().distance(information.me.location().flatten()))/2)
            return true;
        return false;
    }

    @Override
    public double getRating() {
        if(this.isAvailable())
            return 1;
        return 0;
    }
}
