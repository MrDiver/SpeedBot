package rlbotexample.Objects;

import rlbotexample.vector.Vector3;

public class GenericObject extends GameObject{
    @Override
    public void update(Vector3 location, Vector3 velocity, Vector3 rotation) {
        setLocation(location);
        setVelocity(velocity);
        setRotation(rotation);
    }
}
