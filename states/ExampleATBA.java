package rlbotexample.states;

import rlbot.Bot;
import rlbotexample.Obj;
import rlbotexample.MyBot;
import rlbotexample.output.ControlsOutput;


public class ExampleATBA extends State{

    @Override
    public boolean available(MyBot bot) {
        return false;
    }

    @Override
    public ControlsOutput execute(MyBot bot) {
        Obj target = bot.ball;
        double target_speed = bot.ball.velocity.magnitude() + bot.car.location.distance(target.location)/1.5;
        return exampleController(bot.car,target,target_speed);
    }
}
