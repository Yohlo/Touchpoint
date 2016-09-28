package me.yohlo.touchpoint.Handlers;

/**
 * Created by kyle on 9/28/16.
 */

public class msCalculator {

    public msCalculator() { }
    public int getMs(int score) {

        //return (250*(int) Math.pow(1-0.05, score/10)); // this formula for some reason makes the app lag to shit
                                                         // after 10 score so im just gonna have to do it manually fuck
        if(score <= 9) {return 250; }
        else if(score > 9 && score <= 19) { return 240; }
        else if(score > 19 && score <= 29) { return 226; }
        else if(score > 29 && score <= 39) { return 215; }
        else if(score > 39 && score <= 49) { return 205; }
        else if(score > 49 && score <= 59) { return 194; }
        else { return 185; }

    }

}
