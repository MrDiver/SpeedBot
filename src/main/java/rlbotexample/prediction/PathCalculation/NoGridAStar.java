package rlbotexample.prediction.PathCalculation;

import rlbot.render.NamedRenderer;
import rlbot.render.RenderPacket;
import rlbotexample.input.Information;
import rlbotexample.objects.Path;
import rlbotexample.vector.Vector3;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class NoGridAStar{
    Information information;
    Node nil;



    public NoGridAStar(Information information)
    {
        this.information = information;
        nil = new Node(null,new Vector3(0,0,0),new Vector3(0,0,0),null);
    }

    RenderPacket renderPacket;
    Path path= new Path();
    public void calculate(Vector3 start,Vector3 destination,float radius,float divisions,NodeWeight weight)
    {
        start = start.make2D();
        destination = destination.make2D();

        //Set up the queue and closed list
        PriorityQueue<Node> openList = new PriorityQueue<>();
        ArrayList<Node> closedList = new ArrayList<>();
        openList.add(new Node(weight,start,destination,nil));

        NamedRenderer r = new NamedRenderer("AStarRender");

        while (!openList.isEmpty() && openList.size()<1000) {

            //Start rendering stuff
            r.startPacket();
            for(Node open:openList)
            {
                open.draw(r);
            }
            for(Node closed:closedList)
            {
                closed.draw(r);
            }
            renderPacket = r.finishPacket();

            //Remove current node from queue and close it
            Node current = openList.poll();
            current.closed = true;
            closedList.add(current);
            openList.clear();
            //Check if we reached the destination in the search radius
            if(current.position.distance(destination) < radius)
            {
                //TODO: Return Path
                Path path = new Path();
                while(current != nil) {
                    path.add(current.position);
                    current = current.pred;
                }
                this.path = path;
                return;
            }


            //Go trough all neighbours in radius
            for(float i = 0; i < 2*Math.PI; i+= Math.PI/divisions)
            {

                //Create a new neighbour
                Node successor = new Node(weight,current.position.plus(new Vector3(Math.cos(i),Math.sin(i),0).scaledToMagnitude(radius)),destination,nil);
                //successor.pred = current;
                boolean already=false;

                //Check if a neighbour like that already exists
                /*for(Node open:openList)
                {
                    if (successor.position.distance(open.position)<radius/divisions)
                    {
                        already = true;
                        successor = open;
                    }
                }*/
                for(Node closed:closedList)
                {
                    if (successor.position.distance(closed.position)<radius/2)
                    {
                        already = true;
                        successor = closed;
                    }
                }
                if(!successor.closed)
                    successor.relax(current);
                if(!already)
                    openList.add(successor);
            }
        }
    }

    public void draw()
    {
       //RLBotDll.sendRenderPacket(renderPacket);
        path.draw();
     /*   NamedRenderer r = new NamedRenderer("AStarRender"+start);
        r.startPacket();
        float radius = 100;
        Vector3 point = new Vector3(0,0,0);
        r.startPacket();
        r.drawCenteredRectangle3d(Color.red,point,10,10,true);
        Vector3 offset = new Vector3(Math.cos(start),Math.sin(start),0).scaledToMagnitude(radius);
        r.drawCenteredRectangle3d(Color.yellow,point.plus(offset),10,10,true);
        r.finishAndSend();
        start += Math.PI/4;
        if(start > Math.PI*2)
            start = 0;*/
    }
}
