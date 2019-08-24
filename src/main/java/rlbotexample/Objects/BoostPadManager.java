package rlbotexample.Objects;

import rlbotexample.Util;
import rlbotexample.boost.BoostManager;
import rlbotexample.boost.BoostPad;

public class BoostPadManager extends BoostManager{

    public static <T> BoostPad getNearestSmall(T target)
    {
        double nearestDistance;
        BoostPad nearest;
        nearest = getSmallBoosts().get(0);
        nearestDistance = Util.FIELD_LENGTH;
        double distance;
        for(BoostPad bp : getSmallBoosts())
        {
            if(bp.isActive())
            {
                distance = bp.getLocation().distance(Util.toLocation(target));
                if(distance < nearestDistance)
                {
                    nearestDistance = distance;
                    nearest = bp;
                }
            }
        }
        return nearest;
    }

    public static BoostPad getNearestSmall(double... target)
    {
        double nearestDistance;
        BoostPad nearest;
        nearest = getSmallBoosts().get(0);
        nearestDistance = Util.FIELD_LENGTH;
        double distance;
        for(BoostPad bp : getSmallBoosts())
        {
            if(bp.isActive())
            {
                distance = bp.getLocation().distance(Util.toLocation(target));
                if(distance < nearestDistance)
                {
                    nearestDistance = distance;
                    nearest = bp;
                }
            }
        }
        return nearest;
    }

    public static <T> BoostPad getNearestFull(T target)
    {
        double nearestDistance;
        BoostPad nearest;
        nearest = getFullBoosts().get(0);
        nearestDistance = Util.FIELD_LENGTH;
        double distance;
        for(BoostPad bp : getFullBoosts())
        {
            if(bp.isActive())
            {
                distance = bp.getLocation().distance(Util.toLocation(target));
                if(distance < nearestDistance)
                {
                    nearestDistance = distance;
                    nearest = bp;
                }
            }
        }
        return nearest;
    }

    public static BoostPad getNearestFull(double... target)
    {
        double nearestDistance;
        BoostPad nearest;
        nearest = getFullBoosts().get(0);
        nearestDistance = Util.FIELD_LENGTH;
        double distance;
        for(BoostPad bp : getFullBoosts())
        {
            if(bp.isActive())
            {
                distance = bp.getLocation().distance(Util.toLocation(target));
                if(distance < nearestDistance)
                {
                    nearestDistance = distance;
                    nearest = bp;
                }
            }
        }
        return nearest;
    }
}
