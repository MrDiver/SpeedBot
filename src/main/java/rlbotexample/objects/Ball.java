package rlbotexample.objects;

import rlbot.flat.BallInfo;
import rlbotexample.vector.Vector3;

public class Ball extends GenericObject{
    BallInfo b;

    public void update(BallInfo b)
    {
        this.b = b;
        setLocation(new Vector3(b.physics().location()));
        setVelocity(new Vector3(b.physics().velocity()));
        setRotation(new Vector3(b.physics().rotation().pitch(),b.physics().rotation().roll(),b.physics().rotation().yaw()));
    }

    public Vector3 angularVelocity()
    {
        return new Vector3(b.physics().angularVelocity());
    }
}
