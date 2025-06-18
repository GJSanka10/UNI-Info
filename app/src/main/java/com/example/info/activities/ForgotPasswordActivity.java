package com.example.info.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.example.info.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button sendResetLinkButton;
    private TextView errorTextView;
    private TextView backToLoginTextView;
    private ImageView backButton;
    private ProgressBar progressBar;
    private LinearLayout successLayout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        sendResetLinkButton = findViewById(R.id.sendResetLinkButton);
        errorTextView = findViewById(R.id.errorTextView);
        backToLoginTextView = findViewById(R.id.backToLoginTextView);
        backButton = findViewById(R.id.backButton);
        progressBar = findViewById(R.id.progressBar);
        successLayout = findViewById(R.id.successLayout);
    }

    private void setupClickListeners() {
        // Back button click
        backButton.setOnClickListener(v -> finish());

        // Send reset link button click
        sendResetLinkButton.setOnClickListener(v -> sendPasswordResetEmail());

        // Back to login click
        backToLoginTextView.setOnClickListener(v -> {
            finish(); // Just go back for now
        });
    }

    private void sendPasswordResetEmail() {
        String email = emailEditText.getText().toString().trim();

        // Validate email
        if (!validateEmail(email)) {
            return;
        }

        // Show loading state
        showLoadingState();

        // Send password reset email using Firebase
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    hideLoadingState();

                    if (task.isSuccessful()) {
                        // Success
                        showSuccessMessage();
                        hideError();
                    } else {
                        // Error
                        String errorMessage = "Failed to send reset email. Please try again.";
                        showError(errorMessage);
                    }
                });
    }

    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            showError("Please enter your email address");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Please enter a valid email address");
            return false;
        }

        hideError();
        return true;
    }

    private void showLoadingState() {
        progressBar.setVisibility(View.VISIBLE);
        sendResetLinkButton.setEnabled(false);
        sendResetLinkButton.setText("Sending...");
    }

    private void hideLoadingState() {
        progressBar.setVisibility(View.GONE);
        sendResetLinkButton.setEnabled(true);
        sendResetLinkButton.setText("Send Reset Link");
    }

    private void showSuccessMessage() {
        if (successLayout != null) {
            successLayout.setVisibility(View.VISIBLE);
        }
        Toast.makeText(this, "Reset link sent to your email!", Toast.LENGTH_LONG).show();
    }

    private void showError(String message) {
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        errorTextView.setVisibility(View.GONE);
    }
}