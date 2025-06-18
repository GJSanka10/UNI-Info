package com.example.info.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.info.R;

public class DeveloperInfoActivity extends AppCompatActivity {

    private ImageView backButton;
    private TextView developerNameTextView, studentIdTextView, personalStatementTextView,
            releaseVersionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info);

        initializeViews();
        setupClickListeners();
        setupDeveloperInfo();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        developerNameTextView = findViewById(R.id.developerNameTextView);
        studentIdTextView = findViewById(R.id.studentIdTextView);
        personalStatementTextView = findViewById(R.id.personalStatementTextView);
        releaseVersionTextView = findViewById(R.id.releaseVersionTextView);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
    }

    private void setupDeveloperInfo() {
        developerNameTextView.setText("Your Name Here");
        studentIdTextView.setText("Student ID: 2021xxxxxx");
        personalStatementTextView.setText("Occupation");
        releaseVersionTextView.setText(getString(R.string.release_version_text));
    }
}