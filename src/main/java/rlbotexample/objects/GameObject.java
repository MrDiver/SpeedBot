package rlbotexample.objects;

import rlbotexample.Util;
import rlbotexample.vector.Vector3;

import static rlbotexample.Util.toLocation;

public abstract class GameObject {

    private Vector3 location = new Vector3(0,0,0);
    private Vector3 velocity = new Vector3(0,0,0);
    private Vector3 rotation = new Vector3(0,0,0);

    public GameObject()
    {

    }

    public GameObject(Vector3 location, Vector3 velocity, Vector3 rotation)
    {
        this.location = location;
        this.velocity = velocity;
        this.rotation = rotation;
    }

    public Vector3[] getMatrix()
    {
        return Util.rotator_to_matrix(this);
    }

    /**
     * Returns the given target coordinates as local coordinates from the view of this GameObject
     * @param target the target coordinates (Vector3, double[])
     * @param <T> type of the target coordinates
     * @return returns the target coordinates tranformed into local coordinates of this GameObject
     */
    public <T>Vector3 transformToLocal(T target)
    {
        double x = toLocation(target).minus(this.location()).dotProduct(this.getMatrix()[0]);
        double y = toLocation(target).minus(this.location()).dotProduct(this.getMatrix()[1]);
        double z = toLocation(target).minus(this.location()).dotProduct(this.getMatrix()[2]);
        return new Vector3(x,y,z);
    }

    /**
     * Returns the given target coordinates as local coordinates from the view of this GameObject
     * @param target the target coordinates (double x, double y, double z)
     * @return returns the target coordinates tranformed into local coordinates of this GameObject
     */
    public Vector3 transformToLocal(double... target)
    {
        double x = toLocation(target).minus(this.location()).dotProduct(this.getMatrix()[0]);
        double y = toLocation(target).minus(this.location()).dotProduct(this.getMatrix()[1]);
        double z = toLocation(target).minus(this.location()).dotProduct(this.getMatrix()[2]);
        return new Vector3(x,y,z);
    }

    public Vector3 location() {
        return location;
    }

    public Vector3 velocity() {
        return velocity;
    }

    public Vector3 rotation() {
        return rotation;
    }


    public void setLocation(Vector3 location) {
        this.location = location;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }

    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
    }

    public abstract void update(Vector3 location, Vector3 velocity,Vector3 rotation);

    public boolean inLocation(Vector3 loc)
    {
        return this.location.distance(loc) < 2;
    }

    public boolean inLocation(Vector3 loc,int range)
    {
        return this.location.distance(loc) < range;
    }

    public boolean inLocation2D(Vector3 loc)
    {
        return this.location.flatten().distance(loc.flatten()) < 2;
    }

    public boolean inLocation2D(Vector3 loc,int range)
    {
        return this.location.flatten().distance(loc.flatten()) < range;
    }

}
