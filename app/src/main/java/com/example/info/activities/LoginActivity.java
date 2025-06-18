//package com.example.info.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Patterns;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.example.info.R;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private EditText emailEditText, passwordEditText, fullNameEditText, confirmPasswordEditText;
//    private Button signInButton, logInToggle, signInToggle;
//    private TextView forgotPasswordText, createAccountText, titleText, signInText;
//    private LinearLayout loginModeTextContainer, registerModeTextContainer;
//    private CheckBox rememberMeCheckBox;
//    private ProgressBar progressBar;
//    private FirebaseAuth mAuth;
//    private FirebaseFirestore db;
//
//    // Track current mode
//    private boolean isLoginMode = true; // Start with login mode
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login); // Use the combined layout
//
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//        initializeViews();
//        attachTextWatchers();
//        setupClickListeners();
//
//        // Set initial state to login mode
//        setLoginMode();
//    }
//
//    private void initializeViews() {
//        // Common elements
//        emailEditText = findViewById(R.id.emailEditText);
//        passwordEditText = findViewById(R.id.passwordEditText);
//        progressBar = findViewById(R.id.progressBar);
//        titleText = findViewById(R.id.titleText);
//
//        // Toggle buttons
//        logInToggle = findViewById(R.id.logInToggle);
//        signInToggle = findViewById(R.id.signInToggle);
//
//        // Login mode elements
//        forgotPasswordText = findViewById(R.id.forgotPasswordText);
//        createAccountText = findViewById(R.id.createAccountText);
//        loginModeTextContainer = findViewById(R.id.loginModeTextContainer);
//        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
//
//        // Registration mode elements
//        fullNameEditText = findViewById(R.id.fullNameEditText);
//        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
//        signInText = findViewById(R.id.signInText);
//        registerModeTextContainer = findViewById(R.id.registerModeTextContainer);
//
//        // Main action button
//        signInButton = findViewById(R.id.signInButton);
//    }
//
//    private void setupClickListeners() {
//        // Toggle buttons
//        logInToggle.setOnClickListener(v -> {
//            if (!isLoginMode) {
//                isLoginMode = true;
//                setLoginMode();
//                clearFields();
//            }
//        });
//
//        signInToggle.setOnClickListener(v -> {
//            if (isLoginMode) {
//                isLoginMode = false;
//                setRegistrationMode();
//                clearFields();
//            }
//        });
//
//        // Main action button
//        signInButton.setOnClickListener(v -> {
//            if (isLoginMode) {
//                signInUser();
//            } else {
//                registerUser();
//            }
//        });
//
//        // Navigation links
//        if (forgotPasswordText != null) {
//            forgotPasswordText.setOnClickListener(
//                    v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
//        }
//
//        if (createAccountText != null) {
//            createAccountText.setOnClickListener(v -> {
//                isLoginMode = false;
//                setRegistrationMode();
//                clearFields();
//            });
//        }
//
//        if (signInText != null) {
//            signInText.setOnClickListener(v -> {
//                isLoginMode = true;
//                setLoginMode();
//                clearFields();
//            });
//        }
//    }
//
//    private void setLoginMode() {
//        isLoginMode = true;
//
//        // Update title
//        if (titleText != null) {
//            titleText.setText("Login");
//        }
//
//        // Update toggle buttons appearance
//        updateToggleButtons();
//
//        // Show login-specific elements
//        showView(forgotPasswordText);
//        showView(loginModeTextContainer);
//
//        // Hide registration-specific elements
//        hideView(fullNameEditText);
//        hideView(confirmPasswordEditText);
//        hideView(registerModeTextContainer);
//
//        // Update main button text
//        signInButton.setText("Login");
//
//        // Update password field margin
//        if (passwordEditText != null) {
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) passwordEditText.getLayoutParams();
//            params.bottomMargin = (int) (8 * getResources().getDisplayMetrics().density); // 8dp
//            passwordEditText.setLayoutParams(params);
//        }
//    }
//
//    private void setRegistrationMode() {
//        isLoginMode = false;
//
//        // Update title
//        if (titleText != null) {
//            titleText.setText("Sign in");
//        }
//
//        // Update toggle buttons appearance
//        updateToggleButtons();
//
//        // Hide login-specific elements
//        hideView(forgotPasswordText);
//        hideView(loginModeTextContainer);
//
//        // Show registration-specific elements
//        showView(fullNameEditText);
//        showView(confirmPasswordEditText);
//        showView(registerModeTextContainer);
//
//        // Update main button text
//        signInButton.setText("Create an Account");
//
//        // Update password field margin
//        if (passwordEditText != null) {
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) passwordEditText.getLayoutParams();
//            params.bottomMargin = (int) (16 * getResources().getDisplayMetrics().density); // 16dp
//            passwordEditText.setLayoutParams(params);
//        }
//    }
//
//    private void updateToggleButtons() {
//        if (isLoginMode) {
//            // Login button active
//            logInToggle.setBackgroundResource(R.drawable.toggle_active_background);
//            logInToggle.setTextColor(getResources().getColor(R.color.white));
//
//            // Sign in button inactive
//            signInToggle.setBackgroundResource(android.R.color.transparent);
//            signInToggle.setTextColor(getResources().getColor(R.color.text_secondary));
//        } else {
//            // Login button inactive
//            logInToggle.setBackgroundResource(android.R.color.transparent);
//            logInToggle.setTextColor(getResources().getColor(R.color.text_secondary));
//
//            // Sign in button active
//            signInToggle.setBackgroundResource(R.drawable.toggle_active_background);
//            signInToggle.setTextColor(getResources().getColor(R.color.white));
//        }
//    }
//
//    private void showView(View view) {
//        if (view != null) {
//            view.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void hideView(View view) {
//        if (view != null) {
//            view.setVisibility(View.GONE);
//        }
//    }
//
//    private void clearFields() {
//        emailEditText.setText("");
//        passwordEditText.setText("");
//        if (fullNameEditText != null) fullNameEditText.setText("");
//        if (confirmPasswordEditText != null) confirmPasswordEditText.setText("");
//
//        // Clear any error messages
//        emailEditText.setError(null);
//        passwordEditText.setError(null);
//        if (fullNameEditText != null) fullNameEditText.setError(null);
//        if (confirmPasswordEditText != null) confirmPasswordEditText.setError(null);
//    }
//
//    private void attachTextWatchers() {
//        TextWatcher clearErrorWatcher = new TextWatcher() {
//            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (getCurrentFocus() instanceof EditText) {
//                    ((EditText) getCurrentFocus()).setError(null);
//                }
//            }
//            @Override public void afterTextChanged(Editable s) {}
//        };
//
//        emailEditText.addTextChangedListener(clearErrorWatcher);
//        passwordEditText.addTextChangedListener(clearErrorWatcher);
//        if (fullNameEditText != null) {
//            fullNameEditText.addTextChangedListener(clearErrorWatcher);
//        }
//        if (confirmPasswordEditText != null) {
//            confirmPasswordEditText.addTextChangedListener(clearErrorWatcher);
//        }
//    }
//
//    // LOGIN FUNCTIONALITY
//    private void signInUser() {
//        String email = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//
//        if (!validateEmail(email) | !validatePassword(password)) {
//            return;
//        }
//
//        progressBar.setVisibility(View.VISIBLE);
//        signInButton.setEnabled(false);
//
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, task -> {
//                    progressBar.setVisibility(View.GONE);
//                    signInButton.setEnabled(true);
//
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
//
//                        Intent intent = new Intent(this, HomeActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(this,
//                                "Authentication failed: " + task.getException().getMessage(),
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//    }
//
//    // REGISTRATION FUNCTIONALITY
//    private void registerUser() {
//        String fullName = fullNameEditText.getText().toString().trim();
//        String email = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
//
//        if (!validateRegistration(fullName, email, password, confirmPassword)) {
//            return;
//        }
//
//        progressBar.setVisibility(View.VISIBLE);
//        signInButton.setEnabled(false);
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        saveUserToFirestore(user, fullName, email);
//                    } else {
//                        progressBar.setVisibility(View.GONE);
//                        signInButton.setEnabled(true);
//                        Toast.makeText(this, "Registration failed: " +
//                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//    }
//
//    private void saveUserToFirestore(FirebaseUser firebaseUser, String fullName, String email) {
//        Map<String, Object> user = new HashMap<>();
//        user.put("userId", firebaseUser.getUid());
//        user.put("fullName", fullName);
//        user.put("email", email);
//        user.put("profileImageUrl", "");
//        user.put("createdAt", System.currentTimeMillis());
//
//        db.collection("users").document(firebaseUser.getUid())
//                .set(user)
//                .addOnSuccessListener(aVoid -> {
//                    progressBar.setVisibility(View.GONE);
//                    signInButton.setEnabled(true);
//                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(this, HomeActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
//                })
//                .addOnFailureListener(e -> {
//                    progressBar.setVisibility(View.GONE);
//                    signInButton.setEnabled(true);
//                    Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show();
//                });
//    }
//
//    // VALIDATION METHODS
//    private boolean validateEmail(String email) {
//        if (TextUtils.isEmpty(email)) {
//            emailEditText.setError("Email is required");
//            emailEditText.requestFocus();
//            return false;
//        }
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            emailEditText.setError("Enter a valid email");
//            emailEditText.requestFocus();
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validatePassword(String password) {
//        if (TextUtils.isEmpty(password)) {
//            passwordEditText.setError("Password is required");
//            passwordEditText.requestFocus();
//            return false;
//        }
//        if (password.length() < 6) {
//            passwordEditText.setError("Minimum 6 characters");
//            passwordEditText.requestFocus();
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateRegistration(String fullName, String email, String password, String confirmPassword) {
//        // Full Name validation
//        if (TextUtils.isEmpty(fullName)) {
//            fullNameEditText.setError("Full name is required");
//            fullNameEditText.requestFocus();
//            return false;
//        }
//
//        // Email validation
//        if (!validateEmail(email)) {
//            return false;
//        }
//
//        // Password validation
//        if (!validatePassword(password)) {
//            return false;
//        }
//
//        // Confirm Password validation
//        if (TextUtils.isEmpty(confirmPassword)) {
//            confirmPasswordEditText.setError("Please confirm your password");
//            confirmPasswordEditText.requestFocus();
//            return false;
//        } else if (!password.equals(confirmPassword)) {
//            confirmPasswordEditText.setError("Passwords do not match");
//            confirmPasswordEditText.requestFocus();
//            return false;
//        }
//
//        return true;
//    }
//}

package com.example.info.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.info.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, fullNameEditText, confirmPasswordEditText;
    private Button signInButton, logInToggle, signInToggle;
    private TextView forgotPasswordText, createAccountText, titleText, signInText;
    private LinearLayout loginModeTextContainer, registerModeTextContainer;
    private CheckBox rememberMeCheckBox;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Track current mode
    private boolean isLoginMode = true; // Start with login mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Use the combined layout

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initializeViews();
        attachTextWatchers();
        setupClickListeners();

        // Check if we should start in registration mode
        String mode = getIntent().getStringExtra("mode");
        if ("register".equals(mode)) {
            isLoginMode = false;
            setRegistrationMode();
        } else {
            // Set initial state to login mode
            setLoginMode();
        }
    }

    private void initializeViews() {
        // Common elements
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        progressBar = findViewById(R.id.progressBar);
        titleText = findViewById(R.id.titleText);

        // Toggle buttons
        logInToggle = findViewById(R.id.logInToggle);
        signInToggle = findViewById(R.id.signInToggle);

        // Login mode elements
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        createAccountText = findViewById(R.id.createAccountText);
        loginModeTextContainer = findViewById(R.id.loginModeTextContainer);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);

        // Registration mode elements
        fullNameEditText = findViewById(R.id.fullNameEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signInText = findViewById(R.id.signInText);
        registerModeTextContainer = findViewById(R.id.registerModeTextContainer);

        // Main action button
        signInButton = findViewById(R.id.signInButton);
    }

    private void setupClickListeners() {
        // Toggle buttons
        logInToggle.setOnClickListener(v -> {
            if (!isLoginMode) {
                isLoginMode = true;
                setLoginMode();
                clearFields();
            }
        });

        signInToggle.setOnClickListener(v -> {
            if (isLoginMode) {
                isLoginMode = false;
                setRegistrationMode();
                clearFields();
            }
        });

        // Main action button
        signInButton.setOnClickListener(v -> {
            if (isLoginMode) {
                signInUser();
            } else {
                registerUser();
            }
        });

        // Navigation links
        if (forgotPasswordText != null) {
            forgotPasswordText.setOnClickListener(
                    v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
        }

        if (createAccountText != null) {
            createAccountText.setOnClickListener(v -> {
                isLoginMode = false;
                setRegistrationMode();
                clearFields();
            });
        }

        if (signInText != null) {
            signInText.setOnClickListener(v -> {
                isLoginMode = true;
                setLoginMode();
                clearFields();
            });
        }
    }

    private void setLoginMode() {
        isLoginMode = true;

        // Update title
        if (titleText != null) {
            titleText.setText("Login");
        }

        // Update toggle buttons appearance
        updateToggleButtons();

        // Show login-specific elements
        showView(forgotPasswordText);
        showView(loginModeTextContainer);

        // Hide registration-specific elements
        hideView(fullNameEditText);
        hideView(confirmPasswordEditText);
        hideView(registerModeTextContainer);

        // Update main button text
        signInButton.setText("Login");

        // Update password field margin
        if (passwordEditText != null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) passwordEditText.getLayoutParams();
            params.bottomMargin = (int) (8 * getResources().getDisplayMetrics().density); // 8dp
            passwordEditText.setLayoutParams(params);
        }
    }

    private void setRegistrationMode() {
        isLoginMode = false;

        // Update title
        if (titleText != null) {
            titleText.setText("Sign up");
        }

        // Update toggle buttons appearance
        updateToggleButtons();

        // Hide login-specific elements
        hideView(forgotPasswordText);
        hideView(loginModeTextContainer);

        // Show registration-specific elements
        showView(fullNameEditText);
        showView(confirmPasswordEditText);
        showView(registerModeTextContainer);

        // Update main button text
        signInButton.setText("Create an Account");

        // Update password field margin
        if (passwordEditText != null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) passwordEditText.getLayoutParams();
            params.bottomMargin = (int) (16 * getResources().getDisplayMetrics().density); // 16dp
            passwordEditText.setLayoutParams(params);
        }
    }

    private void updateToggleButtons() {
        if (isLoginMode) {
            // Login button active
            logInToggle.setBackgroundResource(R.drawable.toggle_active_background);
            logInToggle.setTextColor(getResources().getColor(R.color.white));

            // Sign in button inactive
            signInToggle.setBackgroundResource(android.R.color.transparent);
            signInToggle.setTextColor(getResources().getColor(R.color.text_secondary));
        } else {
            // Login button inactive
            logInToggle.setBackgroundResource(android.R.color.transparent);
            logInToggle.setTextColor(getResources().getColor(R.color.text_secondary));

            // Sign in button active
            signInToggle.setBackgroundResource(R.drawable.toggle_active_background);
            signInToggle.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void showView(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void hideView(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    private void clearFields() {
        emailEditText.setText("");
        passwordEditText.setText("");
        if (fullNameEditText != null) fullNameEditText.setText("");
        if (confirmPasswordEditText != null) confirmPasswordEditText.setText("");

        // Clear any error messages
        emailEditText.setError(null);
        passwordEditText.setError(null);
        if (fullNameEditText != null) fullNameEditText.setError(null);
        if (confirmPasswordEditText != null) confirmPasswordEditText.setError(null);
    }

    private void attachTextWatchers() {
        TextWatcher clearErrorWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() instanceof EditText) {
                    ((EditText) getCurrentFocus()).setError(null);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        };

        emailEditText.addTextChangedListener(clearErrorWatcher);
        passwordEditText.addTextChangedListener(clearErrorWatcher);
        if (fullNameEditText != null) {
            fullNameEditText.addTextChangedListener(clearErrorWatcher);
        }
        if (confirmPasswordEditText != null) {
            confirmPasswordEditText.addTextChangedListener(clearErrorWatcher);
        }
    }

    // LOGIN FUNCTIONALITY
    private void signInUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!validateEmail(email) | !validatePassword(password)) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        signInButton.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    signInButton.setEnabled(true);

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this,
                                "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    // REGISTRATION FUNCTIONALITY
    private void registerUser() {
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (!validateRegistration(fullName, email, password, confirmPassword)) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        signInButton.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        saveUserToFirestore(user, fullName, email);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        signInButton.setEnabled(true);
                        Toast.makeText(this, "Registration failed: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserToFirestore(FirebaseUser firebaseUser, String fullName, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("userId", firebaseUser.getUid());
        user.put("fullName", fullName);
        user.put("email", email);
        user.put("profileImageUrl", "");
        user.put("createdAt", System.currentTimeMillis());

        db.collection("users").document(firebaseUser.getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    signInButton.setEnabled(true);
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    signInButton.setEnabled(true);
                    Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                });
    }
    // VALIDATION METHODS
    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email");
            emailEditText.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Minimum 6 characters");
            passwordEditText.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateRegistration(String fullName, String email, String password, String confirmPassword) {
        // Full Name validation
        if (TextUtils.isEmpty(fullName)) {
            fullNameEditText.setError("Full name is required");
            fullNameEditText.requestFocus();
            return false;
        }

        // Email validation
        if (!validateEmail(email)) {
            return false;
        }

        // Password validation
        if (!validatePassword(password)) {
            return false;
        }

        // Confirm Password validation
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.setError("Please confirm your password");
            confirmPasswordEditText.requestFocus();
            return false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            confirmPasswordEditText.requestFocus();
            return false;
        }

        return true;
    }
}