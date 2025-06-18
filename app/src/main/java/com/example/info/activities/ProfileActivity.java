//
//package com.example.info.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.example.info.R;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class ProfileActivity extends AppCompatActivity {
//
//    // UI
//    private CircleImageView profileImageView;
//    private TextView userNameTextView, userEmailTextView;
//    private Button editProfileButton;
//    private LinearLayout settingLayout, deleteAccountLayout, informationLayout,
//            privacyLayout, helpLayout, signOutLayout, developerOption;
//    private ImageView backButton;
//
//    // Firebase
//    private FirebaseAuth mAuth;
//    private FirebaseFirestore db;
//    private FirebaseUser currentUser;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//        mAuth = FirebaseAuth.getInstance();
//        db   = FirebaseFirestore.getInstance();
//        currentUser = mAuth.getCurrentUser();
//
//        initializeViews();
//        setupClickListeners();
//        loadUserData();
//    }
//
//    private void initializeViews() {
//        profileImageView   = findViewById(R.id.profileImage);
//        userNameTextView   = findViewById(R.id.userName);
//        userEmailTextView  = findViewById(R.id.userEmail);
//        editProfileButton  = findViewById(R.id.editProfileButton);
//
//        settingLayout      = findViewById(R.id.settingOption);
//        deleteAccountLayout= findViewById(R.id.deleteAccountOption);
//        informationLayout  = findViewById(R.id.informationOption);
//        privacyLayout      = findViewById(R.id.privacyOption);
//        helpLayout         = findViewById(R.id.helpOption);
//        signOutLayout      = findViewById(R.id.signOutOption);
//        developerOption    = findViewById(R.id.developerOption);
//        backButton         = findViewById(R.id.backIcon);
//    }
//
//    private void setupClickListeners() {
//
//        backButton.setOnClickListener(v -> finish());
//
//        developerOption.setOnClickListener(v ->
//                startActivity(new Intent(this, DeveloperInfoActivity.class)));
//
//        editProfileButton.setOnClickListener(v ->
//                startActivity(new Intent(this, EditProfileActivity.class)));
//
//        informationLayout.setOnClickListener(v ->
//                startActivity(new Intent(this, DeveloperInfoActivity.class)));
//
//        deleteAccountLayout.setOnClickListener(v -> showDeleteAccountDialog());
//
//        signOutLayout.setOnClickListener(v -> showSignOutDialog());
//    }
//
//    private void loadUserData() {
//        if (currentUser == null) return;
//
//        userEmailTextView.setText(currentUser.getEmail());
//
//        db.collection("users").document(currentUser.getUid())
//                .get()
//                .addOnSuccessListener(document -> {
//                    if (document.exists()) {
//                        String fullName = document.getString("fullName");
//                        String profileImageUrl = document.getString("profileImageUrl");
//
//                        userNameTextView.setText(fullName != null ? fullName : "User");
//
//                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
//                            Glide.with(this)
//                                    .load(profileImageUrl)
//                                    .placeholder(R.drawable.default_profile)
//                                    .into(profileImageView);
//                        }
//                    }
//                });
//    }
//
//    // ──────────────────────────────────────────────────────────────────────────
//    //   Account actions
//    // ──────────────────────────────────────────────────────────────────────────
//
//    private void showSignOutDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("Sign Out")
//                .setMessage("Are you sure you want to sign out?")
//                .setPositiveButton("Yes", (dialog, which) -> {
//                    mAuth.signOut();
//                    startActivity(new Intent(this, LoginActivity.class)
//                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                    finish();
//                })
//                .setNegativeButton("No", null)
//                .show();
//    }
//
//    private void showDeleteAccountDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("Delete Account")
//                .setMessage("This action is permanent. Do you really want to delete your account?")
//                .setPositiveButton("Delete", (dialog, which) -> deleteAccount())
//                .setNegativeButton("Cancel", null)
//                .show();
//    }
//
//    /** Permanently removes the user’s auth record and (optionally) their Firestore doc */
//    private void deleteAccount() {
//        if (currentUser == null) {
//            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // 1) Delete Firestore profile doc (optional but recommended)
//        db.collection("users").document(currentUser.getUid()).delete();
//
//        // 2) Delete Firebase‑Auth user
//        currentUser.delete()
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(this, LoginActivity.class)
//                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                    finish();
//                })
//                .addOnFailureListener(e -> {
//                    // Most common failure: requires recent login
//                    Toast.makeText(this,
//                            "Failed to delete account: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                });
//    }
//
//
//}


package com.example.info.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.info.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    // UI
    private CircleImageView profileImageView;
    private TextView userNameTextView, userEmailTextView;
    private Button editProfileButton;
    private LinearLayout settingLayout, deleteAccountLayout, informationLayout,
            privacyLayout, helpLayout, signOutLayout, developerOption;
    private ImageView backButton;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        initializeViews();
        setupClickListeners();
        loadUserData();
    }

    private void initializeViews() {
        profileImageView   = findViewById(R.id.profileImage);
        userNameTextView   = findViewById(R.id.userName);
        userEmailTextView  = findViewById(R.id.userEmail);
        editProfileButton  = findViewById(R.id.editProfileButton);

        settingLayout      = findViewById(R.id.settingOption);
        deleteAccountLayout= findViewById(R.id.deleteAccountOption);
        informationLayout  = findViewById(R.id.informationOption);
        privacyLayout      = findViewById(R.id.privacyOption);
        helpLayout         = findViewById(R.id.helpOption);
        signOutLayout      = findViewById(R.id.signOutOption);
        developerOption    = findViewById(R.id.developerOption);
        backButton         = findViewById(R.id.backIcon);
    }

    private void setupClickListeners() {

        backButton.setOnClickListener(v -> finish());

        developerOption.setOnClickListener(v ->
                startActivity(new Intent(this, DeveloperInfoActivity.class)));

        editProfileButton.setOnClickListener(v ->
                startActivity(new Intent(this, EditProfileActivity.class)));

        informationLayout.setOnClickListener(v ->
                startActivity(new Intent(this, DeveloperInfoActivity.class)));

        deleteAccountLayout.setOnClickListener(v -> showDeleteAccountDialog());

        signOutLayout.setOnClickListener(v -> showSignOutDialog());
    }

    private void loadUserData() {
        if (currentUser == null) return;

        userEmailTextView.setText(currentUser.getEmail());

        db.collection("users").document(currentUser.getUid())
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String fullName = document.getString("fullName");
                        String profileImageUrl = document.getString("profileImageUrl");

                        userNameTextView.setText(fullName != null ? fullName : "User");

                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(profileImageUrl)
                                    .placeholder(R.drawable.default_profile)
                                    .into(profileImageView);
                        }
                    }
                });
    }

    // ──────────────────────────────────────────────────────────────────────────
    //   Account actions
    // ──────────────────────────────────────────────────────────────────────────

    private void showSignOutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    mAuth.signOut();
                    startActivity(new Intent(this, LoginActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("This action is permanent. Do you really want to delete your account?")
                .setPositiveButton("Delete", (dialog, which) -> deleteAccount())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAccount() {
        if (currentUser == null) {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user signed in recently (within last 5 minutes)
        long timeSinceLastSignIn = System.currentTimeMillis() - currentUser.getMetadata().getLastSignInTimestamp();
        long fiveMinutesInMillis = 5 * 60 * 1000; // 5 minutes

        if (timeSinceLastSignIn > fiveMinutesInMillis) {
            // User needs to re-authenticate
            showReAuthenticationDialog();
        } else {
            // Proceed with account deletion
            performAccountDeletion();
        }
    }

    private void showReAuthenticationDialog() {
        // Create input field for password
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setHint("Enter your password");

        new AlertDialog.Builder(this)
                .setTitle("Re-authentication Required")
                .setMessage("Please enter your password to delete your account")
                .setView(input)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    String password = input.getText().toString().trim();
                    if (!password.isEmpty()) {
                        reAuthenticateUser(password);
                    } else {
                        Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void reAuthenticateUser(String password) {
        String email = currentUser.getEmail();
        if (email == null) {
            Toast.makeText(this, "Unable to get user email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading dialog
        AlertDialog loadingDialog = new AlertDialog.Builder(this)
                .setTitle("Authenticating...")
                .setMessage("Please wait")
                .setCancelable(false)
                .create();
        loadingDialog.show();

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        currentUser.reauthenticate(credential)
                .addOnSuccessListener(aVoid -> {
                    loadingDialog.dismiss();
                    Toast.makeText(this, "Authentication successful", Toast.LENGTH_SHORT).show();
                    performAccountDeletion();
                })
                .addOnFailureListener(e -> {
                    loadingDialog.dismiss();
                    String errorMessage = e.getMessage();
                    if (errorMessage != null && errorMessage.contains("password")) {
                        Toast.makeText(this, "Incorrect password. Please try again.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Authentication failed: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void performAccountDeletion() {
        // Show progress dialog
        AlertDialog progressDialog = new AlertDialog.Builder(this)
                .setTitle("Deleting Account")
                .setMessage("Please wait...")
                .setCancelable(false)
                .create();
        progressDialog.show();

        // 1) Delete Firestore profile doc first
        db.collection("users").document(currentUser.getUid()).delete()
                .addOnCompleteListener(firestoreTask -> {
                    // 2) Delete Firebase Auth user (whether Firestore deletion succeeded or not)
                    currentUser.delete()
                            .addOnSuccessListener(aVoid -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();

                                // Navigate to login
                                Intent intent = new Intent(this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                String errorMessage = e.getMessage();

                                // Handle specific error cases
                                if (errorMessage != null) {
                                    if (errorMessage.contains("requires-recent-login")) {
                                        Toast.makeText(this, "Session expired. Please try again.", Toast.LENGTH_LONG).show();
                                        showReAuthenticationDialog();
                                    } else if (errorMessage.contains("network")) {
                                        Toast.makeText(this, "Network error. Please check your connection.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(this, "Failed to delete account: " + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(this, "Unknown error occurred", Toast.LENGTH_LONG).show();
                                }
                            });
                });
    }
}