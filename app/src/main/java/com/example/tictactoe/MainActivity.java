package com.example.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    boolean checkGame = false;
    int winner = 2;
    // 0 = yellow, 1 = red
    int activePlayer = 0;
    // 2 means unplayed
    int[] gameState = {2,2,2,2,2,2,2,2,2};
    int[][] winningPositions = {{0,1,2},
                                {3,4,5},
                                {6,7,8},
                                {0,3,6},
                                {1,4,7},
                                {2,5,8},
                                {0,4,8},
                                {2,4,6}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            activePlayer = savedInstanceState.getInt("activePlayer");
            int[] savedGameState = savedInstanceState.getIntArray("gameState");
            checkGame(savedGameState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntArray("gameState", gameState);
        outState.putInt("activePlayer", activePlayer);
    }

    public void dropIn(View view){
        if(winner < 2){
            return;
        }

        ImageView counter = (ImageView)view;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] < 2){
            return;
        }

        counter.setTranslationY(-1000f);
        gameState[tappedCounter] = activePlayer;

        if (activePlayer == 0){
            counter.setImageResource(R.drawable.yellow);
            activePlayer = 1;
        } else {
            counter.setImageResource(R.drawable.red);
            activePlayer = 0;
        }

        counter.animate().translationYBy(1000f).setDuration(250);

        checkWinner();
    }

    private void checkWinner() {
        TextView winnerMessage = findViewById(R.id.main_tvWinner);
        LinearLayout resultPanel = findViewById(R.id.main_resultPanel);

        for (int[] winningPosition : winningPositions){
            if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                gameState[winningPosition[0]] != 2){
                winner = gameState[winningPosition[0]];

                winnerMessage.setText(winner == 1? R.string.red_winner_msg : R.string.yellow_winner_msg);

                resultPanel.setVisibility(View.VISIBLE);
            } else {
                boolean gameIsOver = true;
                for(int state : gameState){
                    if(state == 2){
                        gameIsOver = false;
                        break;
                    }
                }

                if(gameIsOver){
                    winnerMessage.setText(R.string.game_is_over);
                    resultPanel.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void playAgain(View view){
        GridLayout gridLayout = findViewById(R.id.main_grid);
        LinearLayout resultPanel = findViewById(R.id.main_resultPanel);
        resultPanel.setVisibility(View.INVISIBLE);

        winner = 2;
        activePlayer = 0;

        for(int i = 0; i < gameState.length; i++){
            gameState[i] = 2;
            ((ImageView)gridLayout.getChildAt(i)).setImageResource(0);
        }
    }

    public void checkGame(int[] savedGameState){
        GridLayout gridLayout = findViewById(R.id.main_grid);

        for(int i = 0; i < gridLayout.getChildCount(); i++){
            if(savedGameState[i] == 0){
                ((ImageView)gridLayout.getChildAt(i)).setImageResource(R.drawable.yellow);
                gameState[i] = 0;
            } else if (savedGameState[i] == 1) {
                ((ImageView)gridLayout.getChildAt(i)).setImageResource(R.drawable.red);
                gameState[i] = 1;
            }
        }

        checkWinner();
    }
}
