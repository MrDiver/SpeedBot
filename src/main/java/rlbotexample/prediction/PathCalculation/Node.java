package rlbotexample.prediction.PathCalculation;

import rlbot.render.Renderer;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Node implements Comparable<Node> {
    public final Vector3 position;
    public final Vector3 destination;
    public final NodeWeight weight;
    Node pred;
    final Node nil;

    float gCost; //DistanceFromStartingNode
    float hCost; //DistanceFromEndNode
    float fCost; //h + g

    boolean closed;
    public Node(NodeWeight weight,Vector3 position,Vector3 destination,Node nil)
    {
        this.position = position;
        this.destination = destination;
        this.weight = weight;
        pred = nil;
        this.nil = nil;
        closed = false;
        hCost = position.distance(destination);
    }
    public boolean relax(Node other)
    {
        if(pred == nil)
        {
            this.pred = other;
            this.gCost = other.gCost + weight.weight(this);
            this.fCost = gCost + hCost;
            return true;
        }else
        {
            if(gCost >other.gCost + weight.weight(this))
            {
                this.pred = other;
                this.gCost = other.gCost + weight.weight(this);
                this.fCost = gCost + hCost;
                return true;
            }
        }
        return false;
    }

    public void draw(Renderer r) {
        r.drawCenteredRectangle3d(closed ? Color.red : Color.green ,position,5,5,true);
        r.drawLine3d(Color.yellow,position,position.plus(pred.position.minus(position).scaledToMagnitude(50)));
        //r.drawString3d("F: "+this.fCost,Color.white,position.plus(new Vector3(0,0,0)),1,1);
        //r.drawString3d("G: "+this.gCost,Color.white,position.plus(new Vector3(0,0,100)),1,1);
        //r.drawString3d("H: "+this.hCost,Color.white,position.plus(new Vector3(0,0,200)),1,1);
    }

    @Override
    public int compareTo(Node o) {
        return fCost - o.fCost<0?-1:1;
    }
}