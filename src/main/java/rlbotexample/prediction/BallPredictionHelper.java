
package rlbotexample.prediction;

import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.BallPrediction;
import rlbot.flat.PredictionSlice;
import rlbot.render.Renderer;
import rlbotexample.Objects.Impact;
import rlbotexample.Objects.Zone;
import rlbotexample.Util;
import rlbotexample.input.Information;
import rlbotexample.vector.Vector3;

import java.awt.*;

/**
 * This class can help you get started with ball prediction. Feel free to change it as much as you want,
 * this is part of your bot, not part of the framework!
 */
public class BallPredictionHelper {

    public static void drawTillMoment(BallPrediction ballPrediction, float gameSeconds, Color color, Renderer renderer) {
        Vector3 previousLocation = null;
        for (int i = 0; i < ballPrediction.slicesLength(); i += 8) {
            PredictionSlice slice = ballPrediction.slices(i);
            if (slice.gameSeconds() > gameSeconds) {
                break;
            }
            Vector3 location = new Vector3(slice.physics().location());
            if (previousLocation != null) {
                renderer.drawLine3d(color, previousLocation, location);
            }
            previousLocation = location;
        }
    }

    public static Vector3 predict(float gameSeconds, Information info) {
        gameSeconds = info.secondsElapsed() + gameSeconds/1000;
        try {
            BallPrediction ballPrediction = RLBotDll.getBallPrediction();
            Vector3 previousLocation = null;
            for (int i = 0; i < ballPrediction.slicesLength(); i += 4) {
                PredictionSlice slice = ballPrediction.slices(i);
                if (slice.gameSeconds() > gameSeconds) {
                    return new Vector3(slice.physics().location());
                }
            }
        }catch(RLBotInterfaceException e)
        {
            e.printStackTrace();
        }
        return new Vector3(0,0,0);
    }

    public static Impact reachingZone(float gameSeconds, Zone zone, Information info) {
        gameSeconds = info.secondsElapsed() + gameSeconds/1000;
        try {
            BallPrediction ballPrediction = RLBotDll.getBallPrediction();
            for (int i = 0; i < ballPrediction.slicesLength(); i += 1) {
                PredictionSlice slice = ballPrediction.slices(i);
                if(slice.gameSeconds()>gameSeconds)
                    break;
                Vector3 pos = new Vector3(slice.physics().location());

                //if(i%10==0)
                //renderer.drawRectangle3d(Color.yellow,pos,10,10,true);
                if(zone.inSide(pos))
                {
                    return new Impact(pos,slice.gameSeconds()-info.secondsElapsed(),true);
                }
            }
        }catch(RLBotInterfaceException e)
        {
            e.printStackTrace();
        }
        return new Impact(new Vector3(),0,false);
    }

    public static Vector3 predictFirstTouch(Information info) {
        try {
            BallPrediction ballPrediction = RLBotDll.getBallPrediction();
            for (int i = 0; i < ballPrediction.slicesLength(); i += 1) {
                PredictionSlice slice = ballPrediction.slices(i);
                Vector3 location = new Vector3(slice.physics().location());
                if(location.z < 100)
                {
                    return location;
                }
            }
        }catch(RLBotInterfaceException e)
        {
            e.printStackTrace();
        }
        return new Vector3(0,0,0);
        /*float gameSeconds = (float)Util.timeZ(info.ball);
        gameSeconds = info.secondsElapsed() + gameSeconds;
        try {
            BallPrediction ballPrediction = RLBotDll.getBallPrediction();
            Vector3 previousLocation = null;
            for (int i = 0; i < ballPrediction.slicesLength(); i += 4) {
                PredictionSlice slice = ballPrediction.slices(i);
                if (slice.gameSeconds() > gameSeconds) {
                    break;
                }
                Vector3 location = new Vector3(slice.physics().location());
                previousLocation = location;
            }
            if(previousLocation != null)
                return new Vector3(previousLocation.x,previousLocation.y,0);
            return new Vector3(0,0,0);
        }catch(RLBotInterfaceException e)
        {
            e.printStackTrace();
        }
        return new Vector3(0,0,0);*/
    }


}