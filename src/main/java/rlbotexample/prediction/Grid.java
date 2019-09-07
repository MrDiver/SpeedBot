package rlbotexample.prediction;

import rlbot.Bot;
import rlbot.cppinterop.RLBotDll;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.NamedRenderer;
import rlbot.render.RenderPacket;
import rlbot.render.Renderer;
import rlbotexample.Util;
import rlbotexample.objects.Path;
import rlbotexample.vector.Vector3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Grid {

    int xNodes,yNodes;
    float xSpacing,ySpacing;
    Node [][] nodes;
    Node nil;
    public Grid(int xNodes, int yNodes)
    {
        this.xNodes = xNodes;
        this.yNodes = yNodes;
        xSpacing = 8192/xNodes;
        ySpacing = 10240/yNodes;
        nodes = new Node[yNodes][xNodes];
        nil = new Node(new Vector3(0,100000,0),null);
        for(int y = 0; y < yNodes; y++)
        {
            for(int x = 0; x < xNodes; x++)
            {
                nodes[y][x] = new Node(new Vector3(x*xSpacing-4096,y*ySpacing-5120,0),nil);
            }
        }
    }

    public Node getNodeAt(Vector3 position)
    {
        int y = (int)Util.cap((position.y+5120) / ySpacing,0,yNodes-1);
        int x = (int)Util.cap((position.x+4096) / xSpacing,0,xNodes-1);

        return nodes[y][x];
    }

    public void initialize(Vector3 target)
    {
        Node destination = getNodeAt(target);
        for(int y = 0; y < yNodes; y++)
        {
            for(int x = 0; x < xNodes; x++)
            {
                nodes[y][x].setDestination(destination);
                nodes[y][x].setClosed(false);
                nodes[y][x].setVisited(false);
                nodes[y][x].setParent(nil);
                nodes[y][x].setgCost(-1);
            }
        }
    }


    int drawingY = 0;
    int slicesY =5;
    int drawingX = 0;
    int slicesX = 5;
    public void draw()
    {
        ArrayList<RenderPacket> renderPackets = new ArrayList<>();
        NamedRenderer namedRenderer = new NamedRenderer("G"+drawingY+""+drawingX);
        namedRenderer.startPacket();
        for(int y = 0; y < yNodes; y++)
        {


                for (int x = 0; x < xNodes; x += 1) {
                    if(y%slicesY == 0/*||nodes[y][x].isClosed()*/)
                    {
                    if(x%slicesX == 0/*||nodes[y][x].isClosed()*/)
                        nodes[y][x].draw(namedRenderer);
                    }
                }


        }
        namedRenderer.finishAndSend();

        /*drawingX++;
        if(drawingX>=slicesX) {
            drawingY++;
            drawingX =0;
        }
        if(drawingY >=slicesY)
            drawingY=0;*/
    }

    public void AStar(Vector3 start, Vector3 destination,int range)
    {
        initialize(destination);
        PriorityQueue<Node> openList = new PriorityQueue<>();
        openList.add(getNodeAt(start));

        while (!openList.isEmpty())
        {
            draw();
            Node current = openList.poll();
            current.setVisited(true);
            if(current == getNodeAt(destination))
            {
                //TODO: return path
                Path path = new Path();
                while(current != nil) {
                    path.add(current.getPosition());
                    current = current.getParent();
                }
                path.draw();
                return;
            }
            current.close();

            //Expand Node
            for(int y = -range; y < range; y++)
            {
                for(int x = -range; x < range; x++)
                {
                    Node successor = getNodeAt(current.getPosition().plus(new Vector3(x*xSpacing,y*ySpacing,0)));
                    if(successor.isClosed())
                        continue;
                    boolean tmp = successor.relax(current);
                    successor.setVisited(true);
                    if(tmp)
                    {
                        openList.add(successor);
                    }
                }
            }
        }
    }
}
