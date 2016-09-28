package me.yohlo.touchpoint.Handlers;

import java.math.BigDecimal;

/**
 * Created by yohlo on 9/9/16.
 */

public class Rounder {

    public Rounder() {}

    //used to round to decimal places.
    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

}