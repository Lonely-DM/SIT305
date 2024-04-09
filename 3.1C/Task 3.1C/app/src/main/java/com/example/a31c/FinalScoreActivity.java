package com.example.a31c;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FinalScoreActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private Button takeNewQuizButton, finishButton;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_score);

        TextView congratulationsTextView = findViewById(R.id.congratulationsTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        takeNewQuizButton = findViewById(R.id.takeNewQuizButton);
        finishButton = findViewById(R.id.finishButton);

        preferences = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);
        SharedPreferences preferences = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);
        String userName = preferences.getString(getString(R.string.pref_key_user_name), "User");

        String congratulationsMessage = getString(R.string.congratulations) + ", " + userName + "!";
        congratulationsTextView.setText(congratulationsMessage);

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreTextView.setText(getString(R.string.your_score) + " " + score);

        takeNewQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);
                String userName = preferences.getString(getString(R.string.pref_key_user_name), null);

                if (userName != null) {
                    Intent intent = new Intent(FinalScoreActivity.this, QuestionActivity.class);
                    intent.putExtra("USER_NAME", userName);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(FinalScoreActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the app
                finishAffinity();
            }
        });
    }
}

