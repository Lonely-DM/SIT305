//MainActivity.java

package com.example.a31c;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText editTextUserName;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUserName = findViewById(R.id.editText_userName);
        Button buttonStart = findViewById(R.id.button_start);
        preferences = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);

        String savedName = preferences.getString(getString(R.string.pref_key_user_name), "");
        if (!savedName.isEmpty()) {
            editTextUserName.setText(savedName);
        }

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz();
            }
        });
    }

    private void startQuiz() {
        String userName = editTextUserName.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(MainActivity.this, getString(R.string.error_name_required), Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(getString(R.string.pref_key_user_name), userName);
            editor.apply();

            Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
            startActivity(intent);
        }
    }
}

//QuestionActivity.java

package com.example.a31c;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class QuestionActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView questionTextView;
    private Button[] answerButtons = new Button[4];
    private Button submitButton;
    private String[] questions;
    private String[][] answers;
    private String[] correctAnswers;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String selectedAnswer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        String userName = getIntent().getStringExtra("USER_NAME");
        progressBar = findViewById(R.id.progressBar);
        questionTextView = findViewById(R.id.questionTextView);
        answerButtons[0] = findViewById(R.id.answerButton1);
        answerButtons[1] = findViewById(R.id.answerButton2);
        answerButtons[2] = findViewById(R.id.answerButton3);
        answerButtons[3] = findViewById(R.id.answerButton4);
        submitButton = findViewById(R.id.submitButton);

        // Merged questions and answers initialization
        questions = new String[]{
                getString(R.string.question_1),
                getString(R.string.question_2),
                getString(R.string.question_3),
                getString(R.string.question_4),
                getString(R.string.question_5)
        };

        answers = new String[][]{
                {getString(R.string.option1_question1), getString(R.string.option2_question1), getString(R.string.option3_question1), getString(R.string.option4_question1)},
                {getString(R.string.option1_question2), getString(R.string.option2_question2), getString(R.string.option3_question2), getString(R.string.option4_question2)},
                {getString(R.string.option1_question3), getString(R.string.option2_question3), getString(R.string.option3_question3), getString(R.string.option4_question3)},
                {getString(R.string.option1_question4), getString(R.string.option2_question4), getString(R.string.option3_question4), getString(R.string.option4_question4)},
                {getString(R.string.option1_question5), getString(R.string.option2_question5), getString(R.string.option3_question5), getString(R.string.option4_question5)}
        };

        correctAnswers = new String[]{
                getString(R.string.option1_question1), // Correct answer for Question 1
                getString(R.string.option2_question2), // Correct answer for Question 2
                getString(R.string.option1_question3), // Correct answer for Question 3
                getString(R.string.option1_question4), // Correct answer for Question 4
                getString(R.string.option1_question5)  // Correct answer for Question 5
        };

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedAnswer.isEmpty()) {
                    checkAnswer();
                }
            }
        });

        progressBar.setMax(questions.length);
        loadQuestion();
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.length) {
            String[] currentAnswers = answers[currentQuestionIndex];
            questionTextView.setText(questions[currentQuestionIndex]);

            for (int i = 0; i < answerButtons.length; i++) {
                answerButtons[i].setText(currentAnswers[i]);
                answerButtons[i].setBackgroundColor(Color.LTGRAY);

                int finalI = i;
                answerButtons[i].setOnClickListener(v -> {
                    for (Button btn : answerButtons) {
                        btn.setBackgroundColor(Color.LTGRAY);
                    }
                    selectedAnswer = currentAnswers[finalI];
                    answerButtons[finalI].setBackgroundColor(Color.CYAN);
                });
            }
        }
    }

    private void checkAnswer() {
        int correctAnswerIndex = findAnswerIndex(correctAnswers[currentQuestionIndex]);
        boolean isCorrect = selectedAnswer.equals(correctAnswers[currentQuestionIndex]);

        if (isCorrect) {
            // 如果答案正确，只将正确答案标注为绿色
            answerButtons[correctAnswerIndex].setBackgroundColor(Color.GREEN);
            score++;
        } else {
            // 如果答案错误，将用户选择的答案标注为红色
            int userAnswerIndex = findAnswerIndex(selectedAnswer);
            answerButtons[userAnswerIndex].setBackgroundColor(Color.RED);
            // 并将正确答案标注为绿色
            answerButtons[correctAnswerIndex].setBackgroundColor(Color.GREEN);
        }

        // 禁用所有答案按钮，防止用户更改选择
        for (Button btn : answerButtons) {
            btn.setClickable(false);
        }

        submitButton.setEnabled(false); // 禁用提交按钮

        if (currentQuestionIndex < questions.length - 1) {
            // 等待2秒后加载下一题
            submitButton.postDelayed(() -> {
                currentQuestionIndex++;
                loadQuestion();
                resetAnswerButtons();
                submitButton.setEnabled(true); // 启用提交按钮
                updateProgressBar();
            }, 2000);
        } else {
            // 最后一题答案检查后跳转到分数显示页面
            submitButton.postDelayed(this::finishQuiz, 2000);
        }
    }

    private int findAnswerIndex(String answer) {
        for (int i = 0; i < answers[currentQuestionIndex].length; i++) {
            if (answers[currentQuestionIndex][i].equals(answer)) {
                return i;
            }
        }
        return -1; // 未找到答案索引
    }

    private void resetAnswerButtons() {
        for (Button btn : answerButtons) {
            btn.setBackgroundColor(Color.LTGRAY); // 重置按钮背景色
            btn.setClickable(true); // 启用按钮
        }
        selectedAnswer = ""; // 重置选中的答案
    }


    private void updateProgressBar() {
        progressBar.setProgress(currentQuestionIndex + 1);
    }

    private void finishQuiz() {
        Intent intent = new Intent(QuestionActivity.this, FinalScoreActivity.class);
        intent.putExtra("SCORE", score);
        startActivity(intent);
        finish();
    }
}


//FinalScoreActivity.java

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

