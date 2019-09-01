package rlbotexample.Controller;

import rlbot.flat.Rotator;
import rlbotexample.Objects.GameCar;
import rlbotexample.Util;
import rlbotexample.input.Information;
import rlbotexample.input.Predictions;
import rlbotexample.vector.Vector3;

public class ActionLibrary {
    protected Information information;

    public ActionLibrary(Information information)
    {
        this.information = information;
    }

    /**
     * Standard action for dodge
     * @param time length of the dodge (not delay between jumps)
     * @return
     */
    public Action dodge(float time,Information information)
    {
        Action a = dodge(time,0,true);
        return a;
    }

    public Action dodge(float time,double angle,boolean tillGround)
    {
        Action a = dodge(time,100,50,50,angle,true);
        if(tillGround)
            a.addCondition(()->information.me.hasWheelContact());
        return a;
    }

    public Action dodge(float time,float first,float wait,float second,double angle,boolean withThrottle)
    {
        Action a = new Action(time,information).add(new ActionPart(0,first).withJump().withPitch((float)-Math.cos(angle)).withYaw((float)-Math.sin(angle)))
                .add(new ActionPart(first,first+wait).withJump(false).withPitch((float)-Math.cos(angle)).withYaw((float)-Math.sin(angle)))
                .add(new ActionPart(first+wait,first+wait+second).withJump().withPitch((float)-Math.cos(angle)).withYaw((float)-Math.sin(angle)))
                .add(new ActionPart(0,time).withThrottle(withThrottle?1:0));
        return a;
    }

    /**
     * Standard action for delayed dodge.
     * time between 800 and 1300 works best
     * @param time length of the dodge (not delay between jumps)
     * @param delay delay between first and second jump
     * @return
     */
    public Action delayeddodge(float time,long delay,Information information)
    {
        Action a = new Action(time,information).add(new ActionPart(0,delay-100).withJump().withPitch(0))
                .add(new ActionPart(delay-100,delay).withJump(false).withPitch(-1))
                .add(new ActionPart(delay,delay+100).withJump().withPitch(-1));
        return a;

    }

    /**
     * Standard wavedash action
     * @param time
     * @return
     */
    public Action wavedash(long time,float roll,Information information)
    {
        /*Action a = new Action(time)
                .add(new ActionPart(0,time).withThrottle(1))
                .add(new ActionPart(50,60).withPitch(1).withJump())
                .add(new ActionPart(60,280).withJump(false).withPitch(1))
                .add(new ActionPart(850,950).withJump().withPitch(-1))
                .add(new ActionPart(950,1000).withJump(false).withPitch(0)); /*
                .add(new ActionPart(20,500).withJump(false).withPitch(1))
                .add(new ActionPart(500,800).withJump().withPitch(-1));*/
        Action a = new Action(time,information)
                .add(new ActionPart(0,2000).withSlide().withThrottle(1))
                .add(new ActionPart(0,2).withJump())
                .add(new ActionPart(10,200).withPitch(1).withYaw(0).withRoll(roll))
                .add(new ActionPart(850,900).withPitch(-1).withYaw(0).withRoll(-roll))
                .add(new ActionPart(850,890).withJump());
        return a;
    }

    /**
     * standard drive action
     * @param steer
     * @param throttle
     * @param boost
     * @return
     */
    public Action drive(float steer,float throttle,boolean boost)
    {
        Action a = new Action(1,information).add(new ActionPart(0,100).withSteer(steer).withThrottle(throttle).withBoost(boost));
        return a;
    }

    public Action sonicflip(float time,float offset)
    {
        Action a = new Action(time,information)
                .add(new ActionPart(0+offset,2600+offset).withThrottle(1))
                .add(new ActionPart(100+offset,500+offset).withJump().withPitch(-0.3f))
                .add(new ActionPart(600+offset,700+offset).withJump().withPitch(-1))
                .add(new ActionPart(1800+offset,2600+offset).withSlide())
                .add(new ActionPart(2050+offset,2100+offset).withJump().withPitch(1))
                .add(new ActionPart(2450+offset,2600+offset).withJump().withPitch(-1));
        return a;
    }

    public ActionChain diagonalFlick(float angle,boolean boost)
    {
        ActionChain c = chain(2000)
                /*.addAction(action(500).add(part(0,500).withThrottle(1)).addCondition(()->information.me.velocity().magnitude()>500))*/
                .addAction(action((2300-information.me.velocity().y)/90+70).add(part(0,500).withThrottle(1).withSteer(-angle)))
                .addAction(action(150).add(part(0,100).withThrottle(1).withPitch(1).withJump()).add(part(0,150).withBoost(boost)))
                .addAction(action(25).add(part(0,50).withThrottle(1).withPitch(-1).withRoll(angle).withJump().withBoost(boost)))
                .addAction(action(800).add(part(0,1000).withThrottle(1).withPitch(1).withBoost(boost).withRoll(angle).withYaw(angle)));

        return c;
    }

    public ActionChain diagonalFlickNoCorrection(float angle,boolean boost)
    {
        ActionChain c = chain(2000)
                .addAction(action(150).add(part(0,100).withThrottle(1).withPitch(1).withJump()).add(part(0,150).withBoost(boost)))
                .addAction(action(25).add(part(0,50).withThrottle(1).withPitch(-1).withRoll(angle).withJump().withBoost(boost)))
                .addAction(action(800).add(part(0,1000).withThrottle(1).withPitch(1).withBoost(boost).withRoll(angle).withYaw(angle)));

        return c;
    }


    /**
     * Drive towards a given location with a certain speed and do this until location is reached
     * @param loc
     * @param speed
     * @param tillLocation
     */
    public ActionChain driveTowards(Vector3 loc,float speed,boolean tillLocation)
    {
        GameCar me = information.me;
        Value angle = ()->(float)Util.cap(information.me.transformToLocal(loc).angle2D(),-1,1);
        Value throttle = () -> (me.speed()<speed?1:me.speed()>=speed?0:0.1f);
        Bool boost = () -> speed > 1410 && me.speed() < speed && me.speed() < 2250 && Math.abs(angle.val())<0.2f;


        Action a = action(100)
                .add(part(0,10000).withThrottle(throttle).withSteer(angle).withBoost(boost));
        if(tillLocation)
            a.addCondition(()->me.location().distance(loc)<50);

        ActionChain chain = chain(3000)
                .addAction(a);

        return chain;
    }

    public ActionChain driveTowardsFast(Vector3 loc,float speed,boolean tillLocation)
    {
        GameCar me = information.me;
        Value angle = ()->(float)Util.cap(information.me.transformToLocal(loc).angle2D(),-1,1);
        Value angleHard = ()->angle.val()<-0.1f?-1: angle.val() > 0.1f ? 1: 0;
        Value throttle = () -> (me.speed()<speed?1:me.speed()>=speed?0:0.1f);
        Bool boost = () -> speed > 1410 && me.speed() < speed && me.speed() < 2250 && Math.abs(angle.val())<0.2f;
        ActionLibrary actionLibrary = new ActionLibrary(information);

        Action a = action(100)
                .add(part(0,200000).withThrottle(throttle).withSteer(angle).withBoost(boost));

        Bool slide = ()-> Math.abs(angle.val()) > Math.PI/2;
        Action correctAngle = action(300)
                .add(part(0,3000000).withThrottle(1).withBoost().withSlide(slide).withSteer(angleHard).withBoost()).addCondition(()->angle.val() <0.1f);

        if(tillLocation)
            a.addCondition(()->me.location().distance(loc)<50);

        ActionChain chain = chain(3000)
                .addAction(correctAngle);
                if(Math.abs(angle.val())<0.2f&&me.location().distance(loc)>1500)
                    chain.addAction(actionLibrary.diagonalFlick(angleHard.val(),true));
                chain.addAction(a);

        return chain;
    }

    public ActionChain boostTowards(Vector3 loc,float speed,boolean tillLocation)
    {
        GameCar me = information.me;
        Value angle = ()->(float)Util.cap(information.me.transformToLocal(loc).angle2D(),-1,1);
        Value angleHard = ()->angle.val()<-0.1f?-1: angle.val() > 0.1f ? 1: angle.val()*2;
        Bool boost = () ->me.speed() < 2250;
        ActionLibrary actionLibrary = new ActionLibrary(information);

        Action a = action(100)
                .add(part(0,200000).withThrottle(1).withSteer(angleHard).withBoost(boost));

        if(tillLocation)
            a.addCondition(()->me.location().distance(loc)<50);

        ActionChain chain = chain(100).addAction(a);
        return chain;
    }

    public ActionChain flatToSurface()
    {
        GameCar me = information.me;
        Rotator rotation = me.getRotator();

        ActionChain chain = chain(200);

        chain.addAction(action(1000).add(part(0,1000).withPitch(-rotation.pitch()).withRoll(-rotation.roll())).addCondition(()->Math.abs(rotation.pitch())<0.2f&&Math.abs(rotation.roll())<0.2f));
        return chain;
    }








    protected ActionChain chain(float time)
    {
        return new ActionChain(time,information);
    }

    protected Action action(float time)
    {
        return new Action(time,information);
    }

    protected ActionPart part(float start,float end)
    {
        return new ActionPart(start,end);
    }


}
