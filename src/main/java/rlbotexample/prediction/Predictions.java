package rlbotexample.prediction;

import rlbot.Bot;
import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.objects.*;
import rlbotexample.Util;
import rlbotexample.input.Information;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Predictions {

    Information info;
    Zone ownGoalZone;
    Zone enemyGoalZone;
    Zone enemyGoalShotZone;
    Zone ownGoalShotZone;
    boolean allowed;
    public Predictions(Information information)
    {
        allowed = false;
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
            //System.out.println(vel);
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
        return info.ball.location().z < 100;
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
        //System.out.println("ORG: " + angle);
        for(int i = 0; i < 2500; i+=100)
        {
            Vector3 tmp = BoostPadManager.getNearestSmall(info.me.location().plus(info.me.velocity().scaledToMagnitude(i))).getLocation();
            float tmpdist = info.me.location().distance(tmp);
            float tmpangle = Math.abs(info.me.transformToLocal(tmp).angle2D());
            //System.out.println("test: "+ i +" : "+ tmpangle);
            if(tmpangle < angle || tmpdist < distance) {
                distance = tmpdist;
                loc = tmp;
                angle = tmpangle;
            }
        }
        return loc;
    }

    public Vector3 nearestBoostSmallOnPath(Vector3 destination)
    {
        Vector3 loc =  BoostPadManager.getNearestSmall(info.me.location()).getLocation();
        float distance = info.me.location().distance(loc);
        float angle = Math.abs(info.me.transformToLocal(loc).angle2D());
        //System.out.println("ORG: " + angle);
        for(int i = 0; i < 2500; i+=100)
        {
            Vector3 tmp = BoostPadManager.getNearestSmall(info.ownGoal.location().plus(info.me.location().minus(destination).scaledToMagnitude(i))).getLocation();
            float tmpdist = info.me.location().distance(tmp);
            float tmpangle = Math.abs(info.me.transformToLocal(tmp).angle2D());
            //System.out.println("test: "+ i +" : "+ tmpangle);
            if(tmpangle < angle || tmpdist < distance) {
                distance = tmpdist;
                loc = tmp;
                angle = tmpangle;
            }
        }
        return loc;
    }

    public Vector3 nearestBoostFullInRange()
    {
        Vector3 loc =  BoostPadManager.getNearestFull(info.me.location()).getLocation();
        float distance = info.me.location().distance(loc);
        float angle = Math.abs(info.me.transformToLocal(loc).angle2D());
        for(int i = 0; i < 2500; i+=100)
        {
            Vector3 tmp = BoostPadManager.getNearestFull(info.me.location().plus(info.me.velocity().scaledToMagnitude(i))).getLocation();
            float tmpdist = info.me.location().distance(tmp);
            float tmpangle = Math.abs(info.me.transformToLocal(tmp).angle2D());
            if(tmpangle < angle || tmpdist < distance) {
                distance = tmpdist;
                loc = tmp;
                angle = tmpangle;
            }
        }
        return loc;
    }

    public Impact isHittingOwngoal()
    {
        Impact impact = BallPredictionHelper.reachingZone(10000,ownGoalZone,info);
        return impact;
    }

    public Impact isHittingEnemygoal()
    {
        Impact impact = BallPredictionHelper.reachingZone(5000,enemyGoalZone,info);
        return impact;
    }

    public Impact isHittingShotZone()
    {
        Impact impact = BallPredictionHelper.reachingZone(2000,enemyGoalShotZone,info);
        return impact;
    }

    public Vector3 nearestBoostFull()
    {
        return BoostPadManager.getNearestFull(info.me).getLocation();
    }

    public void update()
    {
        allowed = true;
        ownGoalZone = new Zone(info.ownGoal.location(),1786,642,200);
        enemyGoalZone = new Zone(info.eneGoal.location(),1786,642,200);
        enemyGoalShotZone = new Zone(info.eneGoal.location(),1786,642,800);
        ownGoalShotZone = new Zone(info.ownGoal.location().minus(new Vector3(0,-300*info.me.teamSign(),0)),1500,800,600);
    }

    public boolean meCanShoot()
    {
        float leftball = info.ball.location().minus(info.eneGoal.leftPost()).angle2D();
        float rightball =info.ball.location().minus(info.eneGoal.rightPost()).angle2D();
        float leftme = info.me.location().minus(info.eneGoal.leftPost()).angle2D();
        float rightme =info.me.location().minus(info.eneGoal.rightPost()).angle2D();
        //System.out.println(leftball+ "\t" + ballAngle + "\t" + rightme);
        //System.out.println(leftme+ "\t" + rightball);
        return leftball < leftme && rightme < rightball;
    }

    public boolean meAlmostCanShoot()
    {
        float leftball = info.ball.location().minus(info.eneGoal.leftPost()).angle2D()-0.1f;
        float rightball =info.ball.location().minus(info.eneGoal.rightPost()).angle2D()+0.1f;
        float leftme = info.me.location().minus(info.eneGoal.leftPost()).angle2D();
        float rightme =info.me.location().minus(info.eneGoal.rightPost()).angle2D();
        //System.out.println(leftball+ "\t" + ballAngle + "\t" + rightme);
        //System.out.println(leftme+ "\t" + rightball);
        return leftball < leftme && rightme < rightball;
    }

    public boolean enemyCanShoot()
    {
        for(GameCar car: info.enemyList()) {
            float leftball = info.ball.location().minus(info.ownGoal.leftPost()).angle2D();
            float rightball = info.ball.location().minus(info.ownGoal.rightPost()).angle2D();
            float leftme = car.location().minus(info.ownGoal.leftPost()).angle2D();
            float rightme = car.location().minus(info.ownGoal.rightPost()).angle2D();
            //System.out.println(leftball + "\t" + rightme);
            //System.out.println(leftme + "\t" + rightball);
            if(leftball < leftme && rightme < rightball)
            {
                return true;
            }
        }
        return false;
    }

    public boolean ballOnOwnSide()
    {
        return info.ball.location().y*info.me.teamSign()>0;
    }

    public boolean meInGoal(){
        return ownGoalShotZone.inSide(info.me.location());
    }

    public boolean ballOnLeft()
    {
        return info.ball.location().x*info.me.teamSign() <0;
    }

    public boolean goodAngle()
    {
        return Math.abs(info.me.transformToLocal(info.ball.location()).angle2D()) < 0.2f;
    }

    public void draw(Bot bot)
    {
        if(!allowed)
            return;

        Renderer r = BotLoopRenderer.forBotLoop(bot);
        int offsetx = 1600;
        int offsety = 500;
        r.drawRectangle2d(Color.white,new Point(offsetx-5,offsety-5),300,260,true);
        r.drawString2d("BallTime: "+ballTimeTillTouchGround(), Color.red,new Point(offsetx,offsety+20),1,1);
        r.drawString2d("BallOnGround: "+ballOnGround(), Color.red,new Point(offsetx,offsety+40),1,1);
        r.drawString2d("Possession: "+possession(), Color.red,new Point(offsetx,offsety+60),1,1);
        r.drawString2d("WrongSide: "+wrongSide(), Color.red,new Point(offsetx,offsety+80),1,1);
        ballFutureTouch().draw(Color.cyan,bot);
        r.drawString2d("Reaching Owngoal: "+isHittingOwngoal().isImpacting(), Color.red,new Point(offsetx,offsety+100),1,1);
        r.drawString2d("Reaching Enemygoal: "+isHittingEnemygoal().isImpacting(), Color.red,new Point(offsetx,offsety+120),1,1);
        r.drawString2d("Me Can Shoot: "+meCanShoot(), meCanShoot()? Color.green:Color.red,new Point(offsetx,offsety+140),1,1);
        r.drawString2d("Me Almost Can Shoot: "+meAlmostCanShoot(), meAlmostCanShoot()? Color.green:Color.red,new Point(offsetx,offsety+160),1,1);
        r.drawString2d("Enemy Can Shoot: "+enemyCanShoot(), enemyCanShoot()? Color.green:Color.red,new Point(offsetx,offsety+180),1,1);
        r.drawString2d("Own Side: "+ballOnOwnSide(), Color.red,new Point(offsetx,offsety+200),1,1);
        r.drawString2d("In Goal: "+meInGoal(), Color.red,new Point(offsetx,offsety+220),1,1);
        r.drawString2d("Side: "+(ballOnLeft()? "left":"right"), Color.red,new Point(offsetx,offsety+240),1,1);
        r.drawString2d("GoodAngle: "+goodAngle(), Color.red,new Point(offsetx,offsety+260),1,1);


        r.drawLine3d(Color.red,info.ownGoal.location(),info.ownGoal.location().plus(info.me.location().minus(info.ownGoal.location()).scaledToMagnitude(2500)));

        nearestBoostSmallInRange().draw(Color.yellow,bot);
        //info.me.location().plus(info.me.velocity()).draw(Color.yellow,bot);
        enemyGoalShotZone.draw(bot,isHittingShotZone().isImpacting()?Color.red:Color.green);
        ownGoalShotZone.draw(bot,Color.RED);
        try {
            BallPredictionHelper.drawTillMoment(RLBotDll.getBallPrediction(), info.secondsElapsed() + 1, Color.cyan, r);
        }catch(RLBotInterfaceException e)
        {
            e.printStackTrace();
        }
    }

}
