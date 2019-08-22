package rlbotexample;

import rlbotexample.output.ControlsOutput;
import rlbotexample.vector.Vector3;

import java.lang.reflect.Array;

public class Util {

    public final static int GOAL_WIDTH = 1786;
    public final static int FIELD_LENGTH = 10240;
    public final static int FIELD_BREADTH = 8192;
    public final static int CEILING = 2044;

    /**
     * creates a rotation matrix from the rotation components of the input object
     * @param obj
     */
    public static void rotator_to_matrix(Obj obj)
    {
        Vector3 r = obj.rotation;
        double CR,SR,CP,SP,CY,SY;
        //Cos Sin Roll
        CR = Math.cos(r.z);
        SR = Math.sin(r.z);
        //Cos Sin Pitch
        CP = Math.cos(r.x);
        SP = Math.sin(r.x);
        //Cos Sin Yaw
        CY = Math.cos(r.y);
        SY = Math.sin(r.y);

        obj.matrix[0] = new Vector3(CP*CY, CP*SY, SP);
        obj.matrix[1] = new Vector3(CY*SP*SR-CR*SY, SY*SP*SR+CR*CY, -CP * SR);
        obj.matrix[2] = new Vector3(-CR*CY*SP-SR*SY, -CR*SY*SP+SR*CY, CP*CR);
    }

    /**
     * Transforms the coordinates of the target to local coordinates of the object
     * @param target the object or coordinates to be transformed
     * @param our the object that the cordinates get localized to
     * @param <T> type of the target object (Vec3 or Obj)
     * @return
     */
    public static <T> Vector3 to_local(T target, Obj our)
    {
        double x = toLocation(target).minus(our.location).dotProduct(our.matrix[0]);
        double y = toLocation(target).minus(our.location).dotProduct(our.matrix[1]);
        double z = toLocation(target).minus(our.location).dotProduct(our.matrix[2]);

        return new Vector3(x,y,z);
    }

    /**
     * Transforms the coordinates of the target to local coordinates of the object
     * @param target the object or coordinates to be transformed
     * @param ourObject the object that the cordinates get localized to
     * @param <T> type of the target object (Vec3 or Obj)
     * @return
     */
    public static <T> Vector3 toLocal(T target, Obj ourObject)
    {
        if(target instanceof Obj)
        {
            return ((Obj) target).local_location;
        }else
        {
            return to_local(target,ourObject);
        }
    }

    /**
     * Transforms an Object or Array to a Vector3
     * @param target object with location
     * @param <T> the type of the input data
     * @return
     */
    public static <T> Vector3 toLocation(T target)
    {
        if(target instanceof Obj)
        {
            return ((Obj) target).location;
        }else
        {
            if(target instanceof double[])
            {
                return new Vector3(((double[])target)[0],((double[])target)[1],((double[])target)[2]);
            }else if(target instanceof Vector3)
            {
                return (Vector3)target;
            }
        }
        System.err.println("Error: Can't convert from " + target.getClass().toString() + " to Vector 3");
        return new Vector3(0,0,0);
    }

    public static <T extends Number>int sign(T x)
    {
        if(x.doubleValue() > 0)
        {
            return 1;
        }else
        {
            return -1;
        }
    }

    public static boolean ballReady(MyBot bot){
        Obj ball = bot.ball;
        if(Math.abs(ball.velocity.z)<100 && ball.location.z < 250)
            if(Math.abs(ball.location.y)<5000)
                return true;
        return false;
    }

    public static double ballProject(MyBot bot)
    {
        Vector3 goal = new Vector3(0,-sign(bot.car.team)*FIELD_LENGTH/2,100);
        Vector3 goalToBall = bot.ball.location.minus(goal).normalized();
        Vector3 difference = bot.car.location.minus(bot.ball.location);
        return difference.dotProduct(goalToBall);
    }

    public static double cap (double val,double l,double h)
    {
        if(val < l)
            return l;
        else if (val > h )
            return h;
        else
            return val;
    }

    public static double steer(double angle)
    {
        double finalv = Math.pow(10 * angle+sign(angle),3)/20;
        return cap(finalv,-1,1);
    }
}
