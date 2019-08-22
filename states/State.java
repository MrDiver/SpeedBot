package rlbotexample.states;

import rlbot.Bot;
import rlbotexample.Car;
import rlbotexample.MyBot;
import rlbotexample.Obj;
import rlbotexample.Util;
import rlbotexample.output.ControlsOutput;
import rlbotexample.vector.Vector3;

import static rlbotexample.Util.*;

public abstract class State {
    public boolean expired;
    long start;

    public abstract boolean available(MyBot bot);
    public abstract ControlsOutput execute(MyBot bot);
    public State()
    {
        expired = false;
        start = System.currentTimeMillis();
    }
    public <T> ControlsOutput exampleController(Car car, T target, double targetSpeed)
    {
        Vector3 location = toLocal(target,car);
        ControlsOutput output = new ControlsOutput();
        double currentspeed = car.velocity.flatten().magnitude();
        double angletoball = Math.atan2(location.y,location.x);
        double distance =toLocation(target).distance(car.location);

        //Steering
        if(angletoball > 0.1)
        {
            output.withYaw(1);
            output.withSteer(1);
        }
        else if(angletoball < -0.1)
        {
            output.withYaw(-1);
            output.withSteer(-1);
        }
        else
        {
            output.withYaw(0);
            output.withSteer(0);
        }

        //Throttle
        if (targetSpeed >= currentspeed)
        {
            output.withThrottle(1);
        }else if(targetSpeed > 1400  && start > 2200 && currentspeed < 2250)
        {
            output.withBoost();
        }else if(targetSpeed <= currentspeed)
        {
            output.withThrottle(0);
        }
        //dodging
        long delta = System.currentTimeMillis() - start;
        if(delta > 2200 && distance > 1000 && Math.abs(angletoball) < 1.3)
            start = System.currentTimeMillis();
        else if(delta < 100)
        {
            output.withJump();
            output.withPitch(-1);
        }else if(delta > 100 && delta < 150)
        {
            output.withJump(false);
            output.withPitch(-1);
        }else if(delta > 150 && delta < 1000)
        {
            output.withJump();
            output.withYaw(output.getSteer());
            output.withPitch(-1);
        }
        return output;
    }

    public <T> ControlsOutput shotController(Car car, T target, double targetSpeed)
    {
        double[] tmp = {0,-sign(car.team)*FIELD_LENGTH/2,100};
        Vector3 goalLocal = toLocal(tmp,car);
        double goalAngle = goalLocal.angle2D();

        Vector3 location = toLocal(target,car);
        ControlsOutput output = new ControlsOutput();
        double currentspeed = car.velocity.flatten().magnitude();
        double angletotarget = location.angle2D();
        double distance =toLocation(target).distance(car.location);

        //Steering
        if(angletotarget > 0.1)
        {
            output.withYaw(1);
            output.withSteer(1);
        }
        else if(angletotarget < -0.1)
        {
            output.withYaw(-1);
            output.withSteer(-1);
        }
        else
        {
            output.withYaw(0);
            output.withSteer(0);
        }

        //Throttle
        if(angletotarget >= 1.4)
        {
            targetSpeed -= 1400;
        }else if(targetSpeed > 1400  && start > 2200 && currentspeed < 2250)
        {
            output.withBoost();
        }
        if (targetSpeed >= currentspeed)
        {
            output.withThrottle(1);
        }else if(targetSpeed <= currentspeed)
        {
            output.withThrottle(0);
        }
        //dodging
        long delta = System.currentTimeMillis() - start;
        if(delta > 2200 && (distance <= 270 || distance >= 2000))
            start = System.currentTimeMillis();
        else if(delta < 100)
        {
            output.withJump();
            output.withPitch(-1);
        }else if(delta > 100 && delta < 150)
        {
            output.withJump(false);
            output.withPitch(-1);
        }else if(delta > 150 && delta < 1000)
        {
            output.withJump();
            output.withYaw((float)Math.sin(goalAngle));
            output.withPitch((float)-Math.abs(Math.cos(goalAngle)));
        }
        return output;
    }

    public <T> ControlsOutput calcController(Car car, T target, double targetSpeed)
    {
        ControlsOutput output = new ControlsOutput();
        Vector3 location = toLocal(target,car);
        double angletoball = location.angle2D();

        double currentspeed = car.velocity.magnitude();
        output.withSteer((float)angletoball);

        //Throttle
        if (targetSpeed >= currentspeed)
        {
            output.withThrottle(1);
        }else if(targetSpeed > 1400  && start > 2200 && currentspeed < 2250)
        {
            output.withBoost();
        }else if(targetSpeed <= currentspeed)
        {
            output.withThrottle(-1);
        }

        return output;
    }
}
