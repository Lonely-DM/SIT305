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

