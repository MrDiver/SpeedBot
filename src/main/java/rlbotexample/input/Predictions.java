package rlbotexample.input;

import rlbot.Bot;
import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Objects.*;
import rlbotexample.Util;
import rlbotexample.prediction.BallPredictionHelper;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Predictions {

    Information info;
    Zone ownGoalZone;
    public Predictions(Information information)
    {
        this.info = information;
    }
    //Acceleration Constants
    final float boostConsumptionRate = 33.3f;
    final float gravity = 650;
    final float boostAcceleration = 991.666f;
    final float normalAcceleration = 525;
    final float breakingDeceleration = -3500;
    final float decelerationNormal=-525;


    /**
     * Returns the available seconds that you can boost
     * @param car the car the boost gets calculated for
     * @return boost time in seconds
     */
    public float boostSeconds(GameCar car)
    {
        return car.boost()/33.3f;
    }

    public float speedWithBoost(GameCar car, float t)
    {
        return car.velocity().y + boostAcceleration*t/1000;
    }

    public float speedWithNormal(GameCar car, float t)
    {
        float vel = car.velocity().y;
        for(int i = 1; i <= t; i+=1)
        {
            System.out.println(vel);
            vel = vel + normalAccel(vel)/t;
        }
        return vel;
    }

    private float normalAccel(float vel)
    {
        if(vel>1400)
            return 0;
        /*if(vel <600)
        {
            return (float)(1600-vel*0.7);
        }*/
        return (float)(1600-vel);
        //return (float)-(vel*vel)/1500+1600;
    }

    private float dodgeSideImpulse(GameCar car)
    {
        return (float)(500*(1+0.9*car.velocity().y/2300));
    }

    private float dodgeBackImpulse(GameCar car)
    {
        return (float)(533*(1+1.5*car.velocity().y/2300));
    }

    public Vector3 ballFutureLocation(float time)
    {
        return BallPredictionHelper.predict(time,info);
    }

    public Vector3 ballFutureTouch()
    {
        return BallPredictionHelper.predictFirstTouch(info);
    }

    public float ballTimeTillTouchGround()
    {
        float time = (float)Util.timeZ(info.ball);
        return time < 0.1f ? 0 : time;
    }

    public boolean ballOnGround()
    {
        return info.ball.location().z < 95;
    }

    public boolean possession()
    {
        for(GameCar car : info.cars) {
            if(info.me.location().distance(info.ball.location()) > car.location().distance(info.ball.location()))
            {
                return  false;
            }
        }
        return true;
    }

    public boolean wrongSide()
    {
        return (info.ball.location().y - info.me.location().y)*info.me.teamSign() > 0;
    }

    public Vector3 nearestBoostSmall()
    {
        return BoostPadManager.getNearestSmall(info.me).getLocation();
    }

    /**
     * Searching for a small boostpad on a path from the nose vector
     * @return
     */
    public Vector3 nearestBoostSmallInRange()
    {
        Vector3 loc =  BoostPadManager.getNearestSmall(info.me.location()).getLocation();
        float distance = info.me.location().distance(loc);
        float angle = Math.abs(info.me.transformToLocal(loc).angle2D());
        System.out.println("ORG: " + angle);
        for(int i = 0; i < 2500; i+=100)
        {
            Vector3 tmp = BoostPadManager.getNearestSmall(info.me.location().plus(info.me.velocity().scaledToMagnitude(i))).getLocation();
            float tmpdist = info.me.location().distance(tmp);
            float tmpangle = Math.abs(info.me.transformToLocal(tmp).angle2D());
            System.out.println("test: "+ i +" : "+ tmpangle);
            if(tmpdist < distance || tmpangle < angle) {
                distance = tmpdist;
                loc = tmp;
                angle = tmpangle;
            }
        }
        return loc;
    }

    public Vector3 nearestBoostFull()
    {
        return BoostPadManager.getNearestFull(info.me).getLocation();
    }

    public void draw(Bot bot)
    {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        int offsetx = 1600;
        int offsety = 500;
        r.drawRectangle2d(Color.white,new Point(offsetx-5,offsety-5),300,200,true);
        r.drawString2d("BallTime: "+ballTimeTillTouchGround(), Color.red,new Point(offsetx,offsety+20),1,1);
        r.drawString2d("BallOnGround: "+ballOnGround(), Color.red,new Point(offsetx,offsety+40),1,1);
        r.drawString2d("Possession: "+possession(), Color.red,new Point(offsetx,offsety+60),1,1);
        r.drawString2d("WrongSide: "+wrongSide(), Color.red,new Point(offsetx,offsety+80),1,1);
        ballFutureTouch().draw(Color.cyan,bot);
        ownGoalZone = new Zone(info.ownGoal.location(),1786,642,200);
        Impact impact = BallPredictionHelper.reachingZone(20,ownGoalZone,info);
        ownGoalZone.draw(bot,impact.isImpacting() ? Color.red : Color.green);
        impact.draw(Color.red,r);
        r.drawString2d("Reaching Owngoal: "+impact.isImpacting(), Color.red,new Point(offsetx,offsety+100),1,1);
        nearestBoostSmallInRange().draw(Color.yellow,bot);
        //info.me.location().plus(info.me.velocity()).draw(Color.yellow,bot);
        try {
            BallPredictionHelper.drawTillMoment(RLBotDll.getBallPrediction(), info.secondsElapsed() + 3, Color.cyan, r);
        }catch(RLBotInterfaceException e)
        {
            e.printStackTrace();
        }
    }

}
