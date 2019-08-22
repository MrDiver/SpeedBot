package rlbotexample.states;

import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.MyBot;
import rlbotexample.output.ControlsOutput;
import rlbotexample.vector.Vector3;

import java.awt.*;

import static rlbotexample.Util.*;

public class CalcShot extends State{
    @Override
    public boolean available(MyBot bot) {
        if (ballReady(bot) && ballProject(bot) > 400 && Math.abs(bot.ball.location.x)<3900)
            return true;
        return false;
    }

    @Override
    public ControlsOutput execute(MyBot bot) {
        Vector3 goal = new Vector3(0,-sign(bot.car.team)*FIELD_LENGTH/2,100);
        Vector3 goalLocal = toLocal(goal,bot.car);
        Vector3 goalToBall = bot.ball.location.minus(goal).normalized();

        Vector3 goalToBot = bot.car.location.minus(goal).normalized();
        Vector3 differece = goalToBall.minus(goalToBot);
        double error = cap((Math.abs(differece.x)+Math.abs(differece.y)),1,10);

        double targetDistance = (100 + bot.ball.location.distance(bot.car.location))*error*error/1.95;

        Vector3 targetLocation = bot.ball.location.plus(new Vector3(cap(goalToBall.x*targetDistance,-4120,4120),goalToBall.y*targetDistance,0));

        Vector3 targetLocal = toLocal(targetLocation,bot.car);
        double angletotarget = targetLocal.angle2D();
        double speedCorrection = ((1+Math.abs(angletotarget*angletotarget))*300);
        double distancetotarget = bot.car.location.distance(targetLocation);
        double speed = 2300 - speedCorrection + cap((distancetotarget/16)*(distancetotarget/16),0,speedCorrection);

        Renderer renderer = BotLoopRenderer.forBotLoop(bot);
        renderer.drawLine3d(Color.red,bot.ball.location,bot.ball.location.plus(goalToBall.scaledToMagnitude(200)));
        renderer.drawCenteredRectangle3d(Color.green,targetLocation,10,10,false);

        if(ballProject(bot) < 10)
            expired = true;

        return calcController(bot.car,targetLocation,speed);
    }
}
