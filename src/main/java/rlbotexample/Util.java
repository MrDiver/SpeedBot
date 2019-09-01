package rlbotexample;

import rlbotexample.Objects.Ball;
import rlbotexample.Objects.GameObject;
import rlbotexample.input.Information;
import rlbotexample.vector.Vector3;

public class Util {

    public final static int GOAL_WIDTH = 1786;
    public final static int FIELD_LENGTH = 10240;
    public final static int FIELD_BREADTH = 8192;
    public final static int CEILING = 2044;

    /**
     * creates a rotation matrix from the rotation components of the input object
     * @param obj
     */
    public static Vector3[] rotator_to_matrix(GameObject obj)
    {
        Vector3 r = obj.rotation();
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
        Vector3 matrix[] = new Vector3[3];
        matrix[0] = new Vector3(CP*CY, CP*SY, SP);
        matrix[1] = new Vector3(CY*SP*SR-CR*SY, SY*SP*SR+CR*CY, -CP * SR);
        matrix[2] = new Vector3(-CR*CY*SP-SR*SY, -CR*SY*SP+SR*CY, CP*CR);
        return matrix;
    }

    /**
     * Transforms the coordinates of the target to local coordinates of the object
     * @param target the object or coordinates to be transformed
     * @param our the object that the cordinates get localized to
     * @param <T> type of the target object (Vec3 or Obj)
     * @return
     */
    public static <T> Vector3 to_local(T target, GameObject our)
    {
        double x = toLocation(target).minus(our.location()).dotProduct(our.getMatrix()[0]);
        double y = toLocation(target).minus(our.location()).dotProduct(our.getMatrix()[1]);
        double z = toLocation(target).minus(our.location()).dotProduct(our.getMatrix()[2]);

        return new Vector3(x,y,z);
    }

    /**
     * Transforms the coordinates of the target to local coordinates of the object
     * @param target the object or coordinates to be transformed
     * @param ourObject the object that the cordinates get localized to
     * @param <T> type of the target object (Vec3 or Obj)
     * @return
     */
    public static <T> Vector3 toLocal(T target, GameObject ourObject)
    {
        if(target instanceof GameObject)
        {
            return ((GameObject) target).transformToLocal(ourObject);
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
        if(target instanceof GameObject)
        {
            return ((GameObject) target).location();
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

    public static Vector3 toLocation(double...target)
    {
        return new Vector3(target[0],target[1],target[2]);
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

    public static boolean ballReady(Information information){
        Ball ball = information.ball;
        if(Math.abs(ball.velocity().z)<100 && ball.location().z < 250)
            if(Math.abs(ball.location().y)<5000)
                return true;
        return false;
    }

    public static double ballProject(Information information)
    {
        Vector3 goal = new Vector3(0,-sign(information.me.team().ordinal())*FIELD_LENGTH/2,100);
        Vector3 goalToBall = information.ball.location().minus(goal).normalized();
        Vector3 difference = information.me.location().minus(information.ball.location());
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

    public static double dpp(Vector3 targetloc, Vector3 targetvel,Vector3 ourloc, Vector3 ourvel)
    {
        double distance = targetloc.flatten().distance(ourloc.flatten());
        if(distance != 0)
        {
            return (((targetloc.x - ourloc.x) * (targetvel.x-ourvel.x))+ ((targetloc.y-ourloc.y) * (targetvel.y - ourvel.y)))/distance;
        }
        return 0;
    }

    public static Vector3 future(Ball ball,double time)
    {
        if(ball.velocity().isZero())
            return ball.location();
        return new Vector3(ball.location().x +ball.velocity().x*time,ball.location().y+ ball.velocity().y*time,0);
    }

    public static double timeZ(Ball ball)
    {
        double rate = 0.97;
        return quad(-325,ball.velocity().z*rate,ball.location().z-92.75);
    }

    public static double quad(double a, double b, double c) {
        double inside = b*b - 4*a*c;
        if (inside < 0 || a==0)
        {
            return 0.1;
        }else
        {
            double n = ((-b -Math.sqrt(inside))/(2*a));
            double p = ((-b + Math.sqrt(inside))/(2*a));
            if(p > n)
                return p;
            return n;
        }
    }
}
