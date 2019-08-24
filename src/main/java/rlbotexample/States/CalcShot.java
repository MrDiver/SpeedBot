package rlbotexample.States;

import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Controller.Action;
import rlbotexample.Controller.ActionPart;
import rlbotexample.Objects.Goal;
import rlbotexample.Util;
import rlbotexample.input.Information;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class CalcShot extends State {
    public CalcShot(Information information) {
        super(information);
    }

    Vector3 goalLocal;
    Vector3 goaltoball;
    Vector3 goaltome;
    Vector3 targetLocation;
    @Override
    public Action getAction() {
        /*goalLocal = information.me.transformToLocal(information.eneGoal);
        goaltoball = information.ball.location().minus(information.eneGoal.location()).normalized();
        goaltome = information.me.location().minus(information.eneGoal.location()).normalized();

        Vector3 difference = goaltoball.minus(goaltome);
        double error = Util.cap(Math.abs(difference.x)+Math.abs(difference.y),1,10);

        double targetDistance = (100+information.ball.location().flatten().distance(information.me.location().flatten())*error*error)/1.95;
        targetLocation = information.ball.location().plus(new Vector3(Util.cap(goaltoball.x*targetDistance,-4120,4120),goaltoball.y*targetDistance,0));
        Vector3 localTarget = information.me.transformToLocal(targetLocation);
        double angletotarget = localTarget.angle2D();
        double distancetotarget = information.me.location().flatten().distance(targetLocation.flatten());
        double speedcorrection = ((1+angletotarget*angletotarget)*300);
        double targetspeed = 2000- speedcorrection + Util.cap((distancetotarget/16)*(distancetotarget/16),0,speedcorrection);
*/

        Goal eneGoal = information.eneGoal;
        double ballLeft = information.ball.location().angle2D(eneGoal.leftPost());
        double ballRight = information.ball.location().angle2D(eneGoal.rightPost());

        double ourLeft = information.me.location().angle2D(eneGoal.leftPost());
        double ourRight = information.me.location().angle2D(eneGoal.rightPost());

        Vector3 goalTarget = null;
        if(ourLeft > ballLeft && ourRight > ballRight)
            goalTarget = eneGoal.rightPost();
        else if (ourLeft < ballLeft && ourRight < ballRight)
            goalTarget = eneGoal.leftPost();

        double error;
        if(goalTarget != null)
        {
            goaltoball = information.ball.location().minus(information.eneGoal.location()).normalized();
            goaltome = information.me.location().minus(information.eneGoal.location()).normalized();
            Vector3 difference = goaltoball.minus(goaltome);
            error = Util.cap(Math.abs(difference.x)+Math.abs(difference.y),1,10);
        }else
        {
            goaltoball = information.me.location().minus(information.ball.location()).normalized();
            error = Util.cap(information.ball.location().distance(information.me.location())/1000,0,1);
        }

        double balldppskew = Util.cap(Math.abs(Util.dpp(information.ball.location(),information.ball.velocity(),information.me.location(),new Vector3()))/80,1,1.5);
        double targetDistance = Util.cap ((40 + information.ball.location().flatten().distance(information.me.location().flatten())*error*error)/1.8,0,4000);
        Vector3 tmp = new Vector3(goaltoball.x*targetDistance*balldppskew,goaltoball.y*targetDistance,0);
        targetLocation = information.ball.location().plus(tmp);

        double ballsomething = Util.dpp(targetLocation,information.ball.velocity(),information.me.location(),new Vector3());
        ballsomething = ballsomething *  ballsomething;

        //Correct target if ball is moving away from us
        if(ballsomething> 100)
        {
            ballsomething = Util.cap(ballsomething,0,80);
            Vector3 correction = information.ball.velocity().normalized();
            correction = new Vector3(correction.x*ballsomething,correction.y*ballsomething,correction.z*ballsomething);
            targetLocation = targetLocation.plus(correction);
        }


        //Ball is to close to the wall
        double extra = 4120 - Math.abs(targetLocation.x);
        if (extra < 0)
        {
            targetLocation = new Vector3(Util.cap(targetLocation.x,-4120,4120),targetLocation.y + (-Util.sign(information.me.team().ordinal())*Util.cap(extra,-500,500)),targetLocation.z);
        }

        Vector3 targetLocal = information.me.transformToLocal(targetLocation);
        double angletotarget = targetLocal.angle2D();
        double distancetotarget = information.me.location().flatten().distance(targetLocation.flatten());
        double targetspeed = 2000 - (100*(1+angletotarget)*(1+angletotarget));




        Action a = new Action(0);
        double currentspeed = information.me.velocity().flatten().magnitude();
        a.add(new ActionPart(0,100).withSteer((float)Util.steer(angletotarget)));

        if(targetspeed > currentspeed)
        {
            a.add(new ActionPart(0,100).withThrottle(1));
            if(targetspeed > 1400 && targetspeed > currentspeed && starttime > 2500 && currentspeed < 2250)
            {
                a.add(new ActionPart(0,100).withBoost());
            }
        }else
        {
            a.add(new ActionPart(0,100).withThrottle(-1));
        }
        return a;
    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        r.drawString3d("CalcShot: "+isAvailable(),Color.white,information.me.location().plus(new Vector3(0,0,300)),1,1);
        targetLocation.draw(Color.cyan,bot);
    }

    @Override
    public boolean isAvailable() {
        if(Util.ballReady(information) && Math.abs(information.ball.location().y) <5050 && Util.ballProject(information) > 500-(information.ball.location().flatten().distance(information.me.location().flatten())))
            return true;
        return false;
    }

    @Override
    public double getRating() {
        if(Util.ballReady(information)==false || Math.abs(information.ball.location().y) > 5050)
            return 0;
        return 2;
    }
}
