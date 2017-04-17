package com.example.nick.scarnedice;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class DiceActivity extends AppCompatActivity implements View.OnClickListener {
    private int player_total, player_current, comp_total, comp_current;
    private final String stringScore = "Your turn score: ";
    private final String scoreCompTurn = "Computer turn score: ";
    private final String playerStringScore = "Your score: ";
    private final String compStringScore = "Computer score: ";
    private Random r = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);
        Button roll = (Button) findViewById(R.id.roll);
        Button hold = (Button) findViewById(R.id.hold);
        Button reset = (Button) findViewById(R.id.reset);
        roll.setOnClickListener((View.OnClickListener) this);
        hold.setOnClickListener((View.OnClickListener) this);
        reset.setOnClickListener((View.OnClickListener) this);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.roll):
                ((TextView) findViewById(R.id.comp_turn)).setText("");
                rollDice(r.nextInt(5)+1, true);
                if (player_total + player_current >= 100) {
                    winner(true);
                }
                break;
            case(R.id.hold):
                player_total += player_current;
                ((TextView) findViewById(R.id.your_score)).setText(playerStringScore + player_total);
                player_current = 0;
                compTurnHelper();
                break;
            case(R.id.reset):
                reset();
                break;
        }
    }

    private void rollDice (int roll , boolean playerTurn) {
        ImageView dice= (ImageView) findViewById(R.id.dice);
        if (playerTurn) {
            player_current += roll;
        } else  {
            comp_current += roll;
        }

        switch (roll) {
            case(1):
                if (playerTurn) {
                    player_current = 0;
                    compTurnHelper();
                } else  {
                    comp_current = 1;
                }
                dice.setImageResource(R.drawable.dice1);
                break;
            case(2):
                dice.setImageResource(R.drawable.dice2);
                break;
            case(3):
                dice.setImageResource(R.drawable.dice3);
                break;
            case(4):
                dice.setImageResource(R.drawable.dice4);
                break;
            case(5):
                dice.setImageResource(R.drawable.dice5);
                break;
            case(6):
                dice.setImageResource(R.drawable.dice6);
                break;
        }
        if (playerTurn) {
            ((TextView) findViewById(R.id.turn_score)).setText(stringScore + player_current);
        }
    }

    private void computerTurn () {
        int roll;
        ((TextView) findViewById(R.id.turn_score)).setText(scoreCompTurn + 0);
        roll = r.nextInt(5)+1;
        ((TextView) findViewById(R.id.comp_turn)).setText("Rolling");
        rollDice(roll, false);
        ((TextView) findViewById(R.id.turn_score)).setText(compStringScore + comp_current);
        ((TextView) findViewById(R.id.comp_turn)).setText("Computer rolled a " + roll);
    }

    private void compTurnClean () {
        if (comp_current == 1) {
            comp_current = 0;
        } else {
            ((TextView) findViewById(R.id.comp_turn)).setText("Computer holds.");
        }
        comp_total += comp_current;
        comp_current = 0;
        ((TextView) findViewById(R.id.turn_score)).setText(stringScore + player_current);
        ((TextView) findViewById(R.id.comp_score)).setText(compStringScore + comp_total);
        if (comp_total >= 100) {
            winner(false);
        }
        findViewById(R.id.roll).setClickable(true);
        findViewById(R.id.hold).setClickable(true);
    }

    private  void compTurnHelper () {
        final Handler handler = new Handler();
        findViewById(R.id.roll).setClickable(false);
        findViewById(R.id.hold).setClickable(false);
        handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (comp_current < 20 && comp_current != 1) {
                        computerTurn();
                    }
                    if (comp_current < 20 && comp_current != 1) {
                        handler.postDelayed(this,1000);
                    } else {
                        compTurnClean();
                    }
                }
        }, 100);
    }

    private void winner (boolean player) {
        if (player) {
            ((TextView) findViewById(R.id.comp_turn)).setText("You win!");
        } else {
            ((TextView) findViewById(R.id.comp_turn)).setText("Computer wins.");
        }
        reset();
    }

    private void reset () {
        player_total = player_current = comp_current = comp_total = 0;
        ((TextView) findViewById(R.id.your_score)).setText(R.string.your_score);
        ((TextView) findViewById(R.id.comp_score)).setText(R.string.comp_score);
        ((TextView) findViewById(R.id.turn_score)).setText(R.string.turn_score);
    }
}
