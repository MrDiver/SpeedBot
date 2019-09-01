package rlbotexample.input;

import rlbotexample.Objects.Ball;
import rlbotexample.Objects.GameCar;
import rlbotexample.Util;
import rlbotexample.prediction.BallPredictionHelper;
import rlbotexample.vector.Vector3;

public class Predictions {

    Information info;
    GameCar me;
    Ball ball;
    public Predictions(Information information)
    {
        info = information;
        me = info.me;
        ball = info.ball;
    }
    //Acceleration Constants
    final static float boostConsumptionRate = 33.3f;
    final static float gravity = 650;
    final static float boostAcceleration = 991.666f;
    final static float normalAcceleration = 525;
    final static float breakingDeceleration = -3500;
    final static float decelerationNormal=-525;


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
        return (float)Util.timeZ(ball);
    }

    public boolean possession()
    {
        for(GameCar car : info.cars) {
            if(me.location().distance(ball.location()) > car.location().distance(ball.location()))
            {
                return  false;
            }
        }
        return true;
    }

}
