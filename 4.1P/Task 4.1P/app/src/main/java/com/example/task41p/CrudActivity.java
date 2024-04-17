package com.example.task41p;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CrudActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextDueDate;
    private boolean isEditMode = false;
    private int taskId = -1;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_crud_activity);
        initViews();
        dbHelper = new DBHelper(this);
        checkModeAndSetData();
        setupSaveButton();
    }

    private void initViews() {
        editTextTitle = findViewById(R.id.editTextTaskTitle);
        editTextDescription = findViewById(R.id.editTextTaskDescription);
        editTextDueDate = findViewById(R.id.editTextTaskDueDate);
    }

    private void checkModeAndSetData() {
        isEditMode = getIntent().getBooleanExtra("editMode", false);
        taskId = getIntent().getIntExtra("taskId", -1);

        if (isEditMode && taskId != -1) {
            String title = getIntent().getStringExtra("title");
            String description = getIntent().getStringExtra("description");
            String dueDate = getIntent().getStringExtra("dueDate");

            editTextTitle.setText(title);
            editTextDescription.setText(description);
            editTextDueDate.setText(dueDate);
        }
    }

    private void setupSaveButton() {
        Button buttonSave = findViewById(R.id.buttonSaveTask);
        buttonSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveTask();
            }
        });
    }

    private boolean validateInputs() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String dueDate = editTextDueDate.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidDate(dueDate)) {
            Toast.makeText(this, "Please enter a valid date in the format dd/MM/yyyy.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String dueDate = editTextDueDate.getText().toString().trim();

        if (isEditMode) {
            dbHelper.updateTask(new Task(taskId, title, description, dueDate));
            Toast.makeText(this, "Task updated successfully.", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.insertTask(title, description, dueDate);
            Toast.makeText(this, "Task added successfully.", Toast.LENGTH_SHORT).show();
        }
        finish(); // Return to MainActivity
    }

    private boolean isValidDate(String dueDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        sdf.setLenient(false);
        try {
            sdf.parse(dueDate);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
