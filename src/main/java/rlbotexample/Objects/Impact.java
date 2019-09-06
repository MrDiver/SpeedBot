package rlbotexample.Objects;

import rlbot.Bot;
import rlbot.render.Renderer;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Impact {
    boolean impact;

    public Vector3 getLocation() {
        return location;
    }

    Vector3 location;
    final float timeUntilImpact;

    public Impact(Vector3 location,float timeUntilImpact,boolean impact)
    {
        this.impact = impact;
        this.location = location;
        this.timeUntilImpact = timeUntilImpact;
    }
    public float getTimeUntilImpact() {
        return timeUntilImpact;
    }

    public boolean isImpacting() {
        return impact;
    }

    public void draw(Color c, Renderer r) {
        if(impact) {
            r.drawCenteredRectangle3d(c, location, 10, 10, true);
            r.drawString3d("Time: " + timeUntilImpact, Color.white, location.plus(new Vector3(0, 0, 90)), 1, 1);
        }
    }
}
