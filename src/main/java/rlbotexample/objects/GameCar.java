package rlbotexample.objects;

import rlbot.flat.PlayerInfo;
import rlbot.flat.Rotator;
import rlbot.flat.ScoreInfo;
import rlbotexample.Util;
import rlbotexample.input.Team;
import rlbotexample.vector.Vector3;

public class GameCar extends GenericObject{
    PlayerInfo p;

    public void update(PlayerInfo p)
    {
        this.p = p;
        this.update(new Vector3(p.physics().location()),new Vector3(p.physics().velocity()),new Vector3(p.physics().rotation().pitch(),p.physics().rotation().yaw(),p.physics().rotation().roll()));
    }

    public Vector3 getAngularVelocity() {
        return new Vector3(p.physics().angularVelocity());
    }

    public int boost()
    {
        return p.boost();
    }

    public boolean jumped()
    {
        return p.jumped();
    }

    public boolean doubleJumped()
    {
        return p.doubleJumped();
    }

    public boolean hasWheelContact()
    {
        return p.hasWheelContact();
    }

    public boolean isDemolished()
    {
        return p.isDemolished();
    }

    public boolean isSupersonic()
    {
        return p.isSupersonic();
    }

    public Team team()
    {
        return Team.values()[p.team()];
    }

    public int teamSign()
    {
        return Util.sign(Team.values()[p.team()].ordinal());
    }

    public String name()
    {
        return p.name();
    }

    public ScoreInfo scoreInfo()
    {
        return p.scoreInfo();
    }

    public Rotator getRotator()
    {
        return p.physics().rotation();
    }

    public float speed()
    {
        return (float)new Vector3(p.physics().velocity()).magnitude();
    }

}
