package rlbotexample.States;

import rlbot.Bot;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.Controller.AbstractAction;
import rlbotexample.Controller.Action;
import rlbotexample.Controller.ActionChain;
import rlbotexample.Controller.ActionPart;
import rlbotexample.input.Information;
import rlbotexample.vector.Vector3;

import java.awt.*;

public class TestActions extends State {
    public TestActions(Information information) {
        super(information);
    }

    @Override
    public AbstractAction getAction() {
        /*return Action.dodge(1500,1,true,information).delay(200).add(new ActionPart(0,200).withSteer(1));*/
        ActionChain c = chain(5000)
                .addAction(action(300)
                    .add(part(0,300).withSteer(1).withThrottle(1)))
                .addAction(Action.dodge(1000,information))
                .addAction(action(600)
                        .add(part(0,600).withSteer(-1).withThrottle(1)))
                .addAction(action(300)
                        .add(part(0,300).withSteer(1).withThrottle(1)));
        return c;
    }

    @Override
    public void draw(Bot bot) {
        Renderer r = BotLoopRenderer.forBotLoop(bot);
        r.drawString3d("Test",Color.white,information.me.location().plus(new Vector3(0,0,300)),1,1);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public double getRating() {
        return 10;
    }
}
