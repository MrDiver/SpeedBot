package rlbotexample.States.Defense;

import rlbot.Bot;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.ActionLibrary;
import rlbotexample.States.State;
import rlbotexample.input.Information;
import rlbotexample.objects.Path;
import rlbotexample.prediction.Grid;
import rlbotexample.prediction.Predictions;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class Retreat extends State {


    public Retreat(Information information, ActionLibrary actionLibrary, Predictions predictions) {
        super(information, actionLibrary, predictions);
        name = "Retreat";
    }

    AbstractAction tmp;
    Vector3 target;
    Path path = new Path();
    @Override
    public AbstractAction getAction() {
        Grid grid = new Grid(50,50);
        grid.initialize(new Vector3(0,0,0));
        path = grid.AStar(information.me.location(),information.ownGoal.location(),1);
        target = path.getNextOnPath(information);
        /*if(information.me.location().distance(information.ownGoal.location())>1500)
            return actionLibrary.driveTowardsFaster(predictions.nearestBoostSmallOnPath(information.ownGoal.location()),2300,false);*/

        tmp = actionLibrary.driveTowardsFaster(information.ownGoal.location(),2300,true);
        return tmp;
    }

    @Override
    public void draw(Bot bot) {
        path.draw();
        if(information.me.location().distance(information.ownGoal.location())>1000)
            predictions.nearestBoostSmallOnPath(information.ownGoal.location()).draw(Color.yellow,bot);
        target.draw(Color.red,bot);
    }

    @Override
    public boolean isAvailable()
    {
       return predictions.wrongSide()||tmp != null && tmp.isActive();
    }

    @Override
    public double getRating() {
        return predictions.isHittingOwngoal().isImpacting()||predictions.wrongSide()||predictions.enemyCanShoot()||!predictions.possession()||tmp != null && tmp.isActive()?7:0;
    }
}
