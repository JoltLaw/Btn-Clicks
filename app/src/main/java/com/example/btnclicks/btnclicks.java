package com.example.btnclicks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class btnclicks extends AppCompatActivity {

    int score;

    Handler handler;

    Boolean timerRunning;
    TextView currentScore;

    TextView userHighScoreTextView;

    TextView globalHighScoreTextView;

    TextView globalHighScoreHolderTextView;

    SharedPreferences sharedPref;

    Intent intent;


    FirebaseDatabase database;
    DatabaseReference reference;
    int globalHighScore;

    String globalHighScoreHolder;

    int userHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btnclicks);
        score = 0;
        timerRunning = false;
        currentScore = findViewById(R.id.textView2);
        userHighScoreTextView = findViewById(R.id.textView3);
        globalHighScoreTextView = findViewById(R.id.textView5);
        globalHighScoreHolderTextView = findViewById(R.id.textView6);
         sharedPref = this.getSharedPreferences("com.example.btnclicks", Context.MODE_PRIVATE);
        userHighScore = sharedPref.getInt("HIGH_SCORE", 0);
        userHighScoreTextView.setText("Your High Score: " + userHighScore);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("HighScore");
        getHighScore();
        intent = getIntent();
        userHighScoreTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                sharedPref.edit().putInt("HIGH_SCORE", 0).apply();
                userHighScore = sharedPref.getInt("HIGH_SCORE", 0);
                userHighScoreTextView.setText("Your High Score: " + 0);

                return false;
            }
        });
        handler = new Handler();
    }

    public void getHighScore() {
        reference.child("HIGH_SCORE").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    globalHighScore = ((Number) task.getResult().child("score").getValue()).intValue() ;
                    globalHighScoreHolder = task.getResult().child("email").getValue().toString();
                    setHighScore(globalHighScore, globalHighScoreHolder);
                }
                else {
                    Toast.makeText(getApplicationContext(), "failed to get global High Score", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void btnClick (View view) {
        score++;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                score = 0;
                getHighScore();
                currentScore.setText("Current Score: " + 0);
                timerRunning = false;
            }
        };
        currentScore.setText("Current Score: " + score);

        if (score > userHighScore) {
              if (globalHighScore < score) {
            setHighScore( score,  intent.getStringExtra("email"));
            GlobalHighScore newHighScore = new GlobalHighScore(score,intent.getStringExtra("email"));
              reference.child("HIGH_SCORE").setValue(newHighScore);
        }
            sharedPref.edit().putInt("HIGH_SCORE", score).apply();
            userHighScoreTextView.setText("Your High Score: " + score);
        }

        if (timerRunning == false) {
            handler.postDelayed(runnable, 5000);
            timerRunning = true;
        }
    }

    public  void setHighScore(int highScore, String highScorePlayer) {
        globalHighScoreTextView.setText("Global High Score: " + Integer.toString(highScore));
        globalHighScoreHolderTextView.setText("By: " + highScorePlayer);

    }
}