package me.yohlo.touchpoint;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import me.yohlo.touchpoint.Handlers.Rounder;
import me.yohlo.touchpoint.Handlers.msCalculator;
import me.yohlo.touchpoint.Handlers.userSettings;
import me.yohlo.touchpoint.Objects.MyButton;

//import me.yohlo.touchpoint.objects.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CountDownTimer timer;
    private double time;
    private MyButton button1, button2, button3, button4, button5, button6, button7, button8,
            button9, button10, button11, button12, button13, button14, button15;
    private msCalculator msCalc;
    private boolean game, in;
    private int[] redButtons;
    private int highScore, score, ms, loss, e, maxRed, total;
    private Rounder rounder; // custom Rounder class to round decimals
    private userSettings user;
    private MyButton[] buttons;
    //private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        in = false; // instructions screen is off

        user = new userSettings(this);
        highScore = user.getHighScore();
        total = user.getTotal();

        msCalc = new msCalculator();

        setContentView(R.layout.activity_main);

        final TextView whalecum = (TextView) findViewById(R.id.welcome);
        whalecum.setText("Touchpoint");

        final TextView t = (TextView) findViewById(R.id.highscore); //same thing but with highscore
        t.setText("High Score: " + highScore);

        final TextView hh = (TextView) findViewById(R.id.hiddenhigh);
        hh.setText("Total Score: " + total);

        t.setAnimation(null);

        findViewById(R.id.start_button).setOnClickListener(this);
        findViewById(R.id.instruc).setOnClickListener(this);

        this.createButtons();

    }

    //starts the game!
    public void startGame() {

        game = true;
        score = 0;
        ms = msCalc.getMs(score);
        loss = 0;
        maxRed = user.getMaxRed();

        resetButtons();

        // this is the TextView for the loss reason.
        findViewById(R.id.textView).setVisibility(View.GONE);

        //TextViews for high score and for current score.
        final TextView cur = (TextView) findViewById(R.id.highscore);
        cur.setText("This Round: " + score);
        final TextView hi = (TextView) findViewById(R.id.hiddenhigh);
        hi.setVisibility(View.VISIBLE);
        hi.setText("High Score: " + highScore);

        //makes the ad go away while you play.
        //mAdView.setVisibility(View.GONE);

        //makes buttons visible.
        findViewById(R.id.buttons).setVisibility(View.VISIBLE);

        update();

        try{
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        countdown();

    }

    public void countdown() { //This is the method for the 2 second coundown clock/

        final TextView scoreT = (TextView) findViewById(R.id.welcome); //gets the TextView that originally just said "Touchpoint", Now it's going to have the timer.

        timer = new CountDownTimer(2000, 1) { //2000 as in 2000 milliseconds, or 2 seconds. I *think* the 1 is how often the clock ticks, so every 1 millisecond, onTick will be called.

            public void onTick(long millisUntilFinished) { //onTick, every time the clock ticks. So like, every millisecond.

                time = (double)millisUntilFinished / 1000.00; //millisUntilFinished is straight foward, and I divide it by 1000 to get it in seconds.
                time = rounder.round(time,2); //rounds time to the nearest 2 decimal points
                scoreT.setText(String.valueOf(time)); //sets the TextView to display the time remaining.

            }

            public void onFinish() {
                scoreT.setText("Game Over");
                game = false; //game = false, stops the while loop that the game is in.
                findViewById(R.id.buttons).setVisibility(View.GONE);
            }
        }.start(); //starts the cock

    }

    public void update() { //update method, updates button color.

        new Thread() {
            public void run() {

                while (game){ //while game is true

                    try {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                Random r = new Random();
                                int change = r.nextInt(15); //gets a random number between 0 and 14 (theres 15 buttons and 0-14 is 15 numbers)

                                if(buttons[change].isGreen()) {
                                    setButtonColor(buttons[change], false);
                                    checkButton(buttons[change].getId()); // TODO
                                } else { }
                            }
                        });

                        if(score % 10 == 0);
                        ms = msCalc.getMs(score);

                        Thread.sleep(ms); //sleeps for ms milliseconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //This is outside of the while loop, so this happens when the game is over.

                        //sets the start_button to visible and changes text to restart
                        final Button button = (Button) findViewById(R.id.start_button);
                        button.setVisibility(View.VISIBLE);
                        button.setText("Restart");

                        timer.cancel(); //cancels the timer

                        //changes the textview that had the countdown timer to say "Game Over"
                        final TextView text = (TextView) findViewById(R.id.welcome);
                        text.setText("Game Over");

                        //Tells the reason for losing.
                        final TextView reason = (TextView) findViewById(R.id.textView);
                        reason.setVisibility(View.VISIBLE);
                        if(loss == 1){

                            reason.setText("Try not to press a red button next time.");

                        }
                        else{

                            reason.setText("Try to press the buttons quicker next time.");

                        }

                        if (score > highScore) { highScore = score; } //updates highscore.
                        resetButtons(); // resets the buttons

                        //says high score and score from previous round.
                        final TextView cur = (TextView) findViewById(R.id.highscore);
                        cur.setText("This Round: " + score);
                        final TextView high = (TextView) findViewById(R.id.hiddenhigh);
                        high.setText("High Score: " + highScore);

                        total+=score;

                        final TextView totView = (TextView)findViewById(R.id.total);
                        totView.setVisibility(View.VISIBLE);
                        totView.setText("Total Score: " + (total));
                        user.setTotal(total);

                    }
                });

            }
        }.start();
    }

    public void onClick(View v) { // onClick listener

        switch (v.getId()) { //gets id of the view clicked (stored as v)

            case R.id.start_button: //if it was start_button

                //changes visibility of views
                findViewById(R.id.start_button).setVisibility(View.INVISIBLE);
                findViewById(R.id.instruc).setVisibility(View.GONE);
                findViewById(R.id.howto).setVisibility(View.GONE);
                findViewById(R.id.instructions).setVisibility(View.GONE);
                findViewById(R.id.welcome).setVisibility(View.VISIBLE);
                findViewById(R.id.highscore).setVisibility(View.VISIBLE);
                findViewById(R.id.total).setVisibility(View.GONE);

                startGame(); //starts the game!

                break;

            case R.id.instruc: //if it was the instructions buttons

                if(in == false) { //if the instructions isnt on display

                    //changes visibility of items in the terminal
                    findViewById(R.id.welcome).setVisibility(View.INVISIBLE);
                    findViewById(R.id.highscore).setVisibility(View.INVISIBLE);
                    findViewById(R.id.howto).setVisibility(View.VISIBLE);
                    findViewById(R.id.instructions).setVisibility(View.VISIBLE);
                    findViewById(R.id.total).setVisibility(View.GONE);

                    in = true; //sets in(instructions) to true

                }
                else{

                    //makes instructions disapear and everything else come back
                    findViewById(R.id.howto).setVisibility(View.INVISIBLE);
                    findViewById(R.id.instructions).setVisibility(View.INVISIBLE);
                    findViewById(R.id.welcome).setVisibility(View.VISIBLE);
                    findViewById(R.id.highscore).setVisibility(View.VISIBLE);
                    findViewById(R.id.total).setVisibility(View.VISIBLE);

                    in = false; //sets in to false

                }

                break;

            //default
            default:

                int id = v.getId(); //gets id of button pressed

                MyButton b = null;

                for(MyButton e : buttons) {
                    if(e.getId() == v.getId())
                        b = e;
                }

                if (b.isRed()) { //if the tag is red, the game is over.

                    game = false;
                    loss = 1;
                    findViewById(R.id.buttons).setVisibility(View.GONE);

                } else {

                    score++; //updates score
                    final TextView curs = (TextView) findViewById(R.id.highscore);
                    curs.setText("This Round: " + score);

                    setButtonColor(b, false);

                    checkButton(id); //runs check to update array

                    if (score > highScore) { //updates high score

                        highScore = score;

                        final TextView highS = (TextView) findViewById(R.id.hiddenhigh);
                        highS.setText("High Score: " + highScore);
                        user.setHighScore(highScore);

                    }

                    //cancels the timer and then restarts it.
                    if (timer != null)
                        timer.cancel();
                    countdown();

                }

                break;

        }

    }

    public void checkButton(int id) { //changes button back to green once it was red

        int p; //p is id of button that is already in array

        if (redButtons[maxRed-1] != 0) { //if array is full

            if (e >= maxRed) { e = 0; }

            p = redButtons[e];

            for(MyButton b : buttons) {
                if(b.getId() == p) setButtonColor(b, true);
            }

            redButtons[e++] = id;

        } else { //if array isnt full yet

            if (e <= maxRed) { //sets int at position e to the id of the newly red button
                redButtons[e++] = id;
            }

        }

    }

    private void createButtons() {

        this.button1 = new MyButton(findViewById(R.id.button1));
        this.button2 = new MyButton(findViewById(R.id.button2));
        this.button3 = new MyButton(findViewById(R.id.button3));
        this.button4 = new MyButton(findViewById(R.id.button4));
        this.button5 = new MyButton(findViewById(R.id.button5));
        this.button6 = new MyButton(findViewById(R.id.button6));
        this.button7 = new MyButton(findViewById(R.id.button7));
        this.button8 = new MyButton(findViewById(R.id.button8));
        this.button9 = new MyButton(findViewById(R.id.button9));
        this.button10 = new MyButton(findViewById(R.id.button10));
        this.button11 = new MyButton(findViewById(R.id.button11));
        this.button12 = new MyButton(findViewById(R.id.button12));
        this.button13 = new MyButton(findViewById(R.id.button13));
        this.button14 = new MyButton(findViewById(R.id.button14));
        this.button15 = new MyButton(findViewById(R.id.button15));

        buttons = new MyButton[]
                {button1, button2, button3, button4, button5, button6, button7, button8,
                        button9, button10, button11, button12, button13, button14, button15};

        for(MyButton e : buttons) {
            findViewById(e.getId()).setOnClickListener(this);
        }

    }
    private void resetButtons() {
        for(MyButton b : buttons) { setButtonColor(b, true); }

        if(maxRed == 6) { redButtons = new int[]{0,0,0,0,0,0}; }
        else if(maxRed == 5) { redButtons = new int[]{0,0,0,0,0}; }
        else if(maxRed == 4) { redButtons = new int[]{0,0,0,0}; }
        else { redButtons = new int[]{0,0,0,0,0,0}; }
    }
    private void setButtonColor(MyButton b, boolean col) {
        if(col) {
            findViewById(b.getId()).setBackgroundResource(R.drawable.green);
            b.setGreen();
        } else {
            findViewById(b.getId()).setBackgroundResource(R.drawable.red);
            b.setRed();
        }
    }

}