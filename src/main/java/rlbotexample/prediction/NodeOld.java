package rlbotexample.prediction;

import rlbot.render.Renderer;
import rlbotexample.objects.BoostPadManager;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class NodeOld implements Comparable<NodeOld>{
    private Vector3 position;
    private NodeOld parent;
    private NodeOld nil;
    private int gCost; //DistanceFromStartingNode
    private int hCost; //DistanceFromEndNode
    private int fCost; //h + g
    private boolean closed;
    private boolean visited;

    public NodeOld(Vector3 position, NodeOld nil)
    {
        this.position = position;
        parent = nil;
        this.nil = nil;
        gCost = -1;
        hCost = -1;
        fCost = -1;

        closed = false;
        visited = false;
    }

    public void setDestination(NodeOld destination)
    {
        hCost = (int)position.distance(destination.getPosition());
    }

    public boolean relax(NodeOld other)
    {
        if(parent == nil)
        {
            parent = other;
            gCost = other.gCost + getWeight();
            fCost = gCost + hCost;
            visited = true;
            return true;
        }else
        {
            if(gCost >other.gCost + getWeight())
            {
                parent = other;
                gCost = other.gCost + getWeight();
                fCost = gCost + hCost;
                visited = true;
                return true;
            }
        }
        return false;
    }

    public void close()
    {
        closed = true;
    }

    public int getWeight()
    {
        return (int)BoostPadManager.getNearestSmall(position).getLocation().distance(position);
    }

    @Override
    public int compareTo(NodeOld o) {
        return this.getfCost() - o.getfCost();
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public NodeOld getParent() {
        return parent;
    }

    public void setParent(NodeOld parent) {
        this.parent = parent;
    }

    public int getgCost() {
        return gCost;
    }

    public void setgCost(int gCost) {
        this.gCost = gCost;
    }

    public int gethCost() {
        return hCost;
    }

    public void sethCost(int hCost) {
        this.hCost = hCost;
    }

    public int getfCost() {
        return fCost;
    }

    public void setfCost(int fCost) {
        this.fCost = fCost;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void draw(Renderer r) {
        r.drawCenteredRectangle3d(visited? closed ? Color.red : Color.green : Color.white,position,5,5,true);
        //r.drawString3d("F: "+fCost,Color.white,position.plus(new Vector3(0,0,0)),1,1);
        //r.drawString3d("G: "+gCost,Color.white,position.plus(new Vector3(0,0,100)),1,1);
        r.drawString3d("H: "+hCost,Color.white,position.plus(new Vector3(0,0,200)),1,1);
    }
}
