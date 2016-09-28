package me.yohlo.touchpoint.Objects;


import android.view.View;


/**
 * Created by kyle on 10/19/15.
 */

public class MyButton {

    private boolean green;
    private View buttonView;
    public int buttonId;

    public MyButton(View buttonView) {

        this.buttonView = buttonView;
        buttonId = buttonView.getId();
        this.green = true;

    }

    public boolean isRed() { return !green; }
    public boolean isGreen() { return green; }
    public int getId() { return buttonId; }

    public void setRed() { green = false; }
    public void setGreen() { green = true; }

}