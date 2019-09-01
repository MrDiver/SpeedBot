package rlbotexample.input;

import rlbot.cppinterop.RLBotDll;
import rlbot.flat.*;
import rlbotexample.Objects.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Holds all Information about the current game
 */
public class Information {
    public int playerIndex;
    public GameCar me;
    public ArrayList<GameCar> cars;
    public Ball ball;
    public BoostPadManager boostPadManager;
    private GameInfo gameInfo;
    private boolean notInitialized = true;
    public Goal ownGoal;
    public Goal eneGoal;

    private float kickoffLast;

    public Information(int playerIndex)
    {
        this.playerIndex = playerIndex;
    }
    public void loadGameTickPacket(GameTickPacket packet) {
        gameInfo = packet.gameInfo();
        process(packet);
    }
    private void init(GameTickPacket game)
    {
        cars = new ArrayList<>();
        ball = new Ball();
        boostPadManager = new BoostPadManager();
        notInitialized = false;

        for(int i = 0; i < game.playersLength(); i++)
        {
            cars.add(null);
            GameCar tmp = new GameCar();
            tmp.update(game.players(i));
            cars.set(i,tmp);
            if(i == playerIndex)
                me = cars.get(playerIndex);
        }

        ownGoal = new Goal(me.team());
        eneGoal = new Goal(Team.values()[(me.team().ordinal()+1)%2]);
    }
    private void process(GameTickPacket game)
    {
        gameInfo=game.gameInfo();
        if(notInitialized)
            init(game);

        if(game.playersLength()> cars.size())
            init(game);

        for(int i = 0; i < game.playersLength(); i++)
        {
           cars.get(i).update(game.players(i));
        }
        ball.update(game.ball());
        kickoffLast = isKickoffPause()?this.secondsElapsed():kickoffLast;
    }

    public float gameSpeed()
    {
        return gameInfo.gameSpeed();
    }

    public float gameTimeRemaining()
    {
        return gameInfo.gameTimeRemaining();
    }

    public float secondsElapsed()
    {
        return gameInfo.secondsElapsed();
    }

    public boolean isKickoffPause()
    {
        return gameInfo.isKickoffPause();
    }
    public boolean isAfterKickoff()
    {
        return this.secondsElapsed()-kickoffLast<5;
    }

    public float timeAfterKickoff()
    {
        return this.secondsElapsed()-kickoffLast;
    }

    public boolean isMatchEnded()
    {
        return gameInfo.isMatchEnded();
    }

    public boolean isOvertime()
    {
        return gameInfo.isOvertime();
    }

    public boolean isRoundActive()
    {
        return gameInfo.isRoundActive();
    }

    public boolean isUnlimitedTime()
    {
        return gameInfo.isUnlimitedTime();
    }
}
