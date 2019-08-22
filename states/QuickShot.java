package rlbotexample.states;

import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.MyBot;
import rlbotexample.output.ControlsOutput;
import rlbotexample.vector.Vector3;


import java.awt.*;

import static rlbotexample.Util.*;


public class QuickShot extends State{
    @Override
    public boolean available(MyBot bot) {
        if (ballReady(bot) && ballProject(bot) > -500)
            return true;
        return false;
    }

    @Override
    public ControlsOutput execute(MyBot bot) {
        Vector3 leftPost = new Vector3(sign(bot.car.team)*GOAL_WIDTH/2,-sign(bot.car.team)*FIELD_LENGTH/2,100);
        Vector3 rightPost = new Vector3(-sign(bot.car.team)*GOAL_WIDTH/2,-sign(bot.car.team)*FIELD_LENGTH/2,100);
        Vector3 targetLocation;
        double ballLeft = bot.ball.location.angle2D(leftPost);
        double ballRight = bot.ball.location.angle2D(rightPost);

        double ourLeft = bot.car.location.angle2D(leftPost);
        double ourRight = bot.car.location.angle2D(rightPost);
        int targetSpeed = 1399;

        double offset = bot.ball.location.x/FIELD_BREADTH*3.141;
        double x = bot.ball.location.x + 100*Math.abs(Math.cos(offset))*sign(bot.car.team);
        double y = bot.ball.location.y + 100*Math.abs(Math.sin(offset))*sign(bot.car.team);

        targetLocation = new Vector3(x,y,bot.ball.location.z);

        Vector3 location = toLocal(targetLocation,bot.car);
        double angletotarget = location.angle2D();
        double distancetotarget = bot.car.location.distance(targetLocation);

        double speedCorrection = ((1+Math.abs(angletotarget*angletotarget))*300);
        double speed = 1400 - speedCorrection + cap((distancetotarget/16)*(distancetotarget/16),0,speedCorrection);

        if(bot.car.location.distance(bot.ball.location)<400 && Math.abs(angletotarget)>2)
            expired = true;
        else if(bot.calcShot.available(bot))
            expired = true;
        if((bot.car.location.y-bot.ball.location.y)*sign(bot.car.team) < -1000)
        {
            targetLocation = new Vector3(0,sign(bot.car.team)*FIELD_LENGTH/2,100);
            speed = 2300;
        }/*else
        /*if(ourLeft <= ballLeft && ourRight >= ballRight)
        {
            targetLocation = toLocation(bot.ball);
            targetSpeed = 2300;
        }
        else if (ourLeft > ballLeft && ourRight >= ballRight)// Ball to far right
        {
            double [] tmp = {bot.ball.location.x,bot.ball.location.y + sign(bot.car.team)*150,bot.ball.location.z};
            targetLocation = toLocation(tmp);
        }else if (ourLeft < ballLeft && ourRight <= ballRight)// Ball to far right
        {
            double [] tmp = {bot.ball.location.x,bot.ball.location.y + sign(bot.car.team)*150,bot.ball.location.z};
            targetLocation = toLocation(tmp);
        }else        {
            targetLocation = new Vector3(0,sign(bot.car.team)*FIELD_LENGTH/2,100);
        }*/


        Renderer renderer = BotLoopRenderer.forBotLoop(bot);
        renderer.drawCenteredRectangle3d(Color.green,targetLocation,10,10,false);

        return shotController(bot.car,targetLocation,speed);
    }
}
