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
import rlbotexample.input.Predictions;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Kickoff extends State {

    Action a;
    boolean first=false;

    Vector3 target;
    Vector3 enemy;
    Vector3 eneToBall;

    public Kickoff(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "Kickoff";
    }

    @Override
    public AbstractAction getAction() {
        GameCar me = information.me;
        Ball ball = information.ball;
        GameCar kickoffEnemy = null;
        for(GameCar car :information.enemyList())
        {
            if(kickoffEnemy == null)
            {
                kickoffEnemy = car;
                continue;
            }
            if(car.location().distance(ball.location())<kickoffEnemy.location().distance(ball.location()))
            {
                kickoffEnemy = car;
            }
        }
        if(kickoffEnemy!=null)
            enemy = kickoffEnemy.location().plus(kickoffEnemy.velocity().scaledToMagnitude(100));
        else
            enemy = new Vector3(0,0,1);
        eneToBall = ball.location().minus(enemy);
        target = ball.location().minus(information.eneGoal.location()).normalized();
        target = new Vector3(target.x,target.y*600,target.z);
        float distanceToTarget = (float)target.distance(me.location());
        float distanceToBall = (float)ball.location().distance(me.location());
        ActionChain a = chain(1);

        if(Math.abs(me.location().x)<700)
            target = ball.location();
            if(Math.abs(me.location().x)>100&&Math.abs(me.location().y)>3000)
                target = new Vector3(target.x -me.location().x,target.y,target.z);
        if(distanceToBall>750) {
            return actionLibrary.boostTowards(target, 2300, false);
        }
        float betterAngle = enemy.x*me.teamSign()<0?1:-1;
        System.out.println(betterAngle);
        return actionLibrary.diagonalFlickNoCorrection(betterAngle,true);
    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        target.draw(Color.yellow,bot);
        enemy.draw(Color.red,bot);
        r.drawString3d("Kickoff",Color.white,information.me.location().plus(new Vector3(0,0,300)),1,1);
        r.drawLine3d(Color.red,enemy,enemy.plus(eneToBall.scaledToMagnitude(eneToBall.magnitude()+50)));
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
