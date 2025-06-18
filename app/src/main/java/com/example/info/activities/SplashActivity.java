////package com.example.info.activities;
////
////import android.content.Intent;
////import android.os.Bundle;
////import android.os.Handler;
////import android.view.animation.Animation;
////import android.view.animation.AnimationUtils;
////import android.widget.ImageView;
////
////import androidx.appcompat.app.AppCompatActivity;
////
////import com.google.firebase.auth.FirebaseAuth;
////import com.google.firebase.auth.FirebaseUser;
////import com.example.info.R;
////
////public class SplashActivity extends AppCompatActivity {
////
////    private static final int SPLASH_DURATION = 3000; // 3 seconds
////    private ImageView logoImageView;
////    private FirebaseAuth mAuth;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_splash);
////
////        mAuth = FirebaseAuth.getInstance();
////        logoImageView = findViewById(R.id.logoImageView);
////
////        // Start logo animation
////        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
////        logoImageView.startAnimation(fadeIn);
////
////        // Delay and check authentication
////        new Handler().postDelayed(() -> {
////            checkAuthenticationState();
////        }, SPLASH_DURATION);
////    }
////
////    private void checkAuthenticationState() {
////        FirebaseUser currentUser = mAuth.getCurrentUser();
////        Intent intent;
////
////        if (currentUser != null) {
////            // User is logged in, go to home
////           // intent = new Intent(SplashActivity.this, HomeActivity.class);
////            intent = new Intent(SplashActivity.this, HomeActivity.class);
////        } else {
////            // User is not logged in, go to login
////            intent = new Intent(SplashActivity.this, LoginActivity.class);
////        }
////
////        startActivity(intent);
////        finish();
////    }
////}
//
//package com.example.info.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.View;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.example.info.R;
//
//public class SplashActivity extends AppCompatActivity {
//
//    private static final int SPLASH_DURATION = 3000; // 3 seconds
//    private ImageView logoImageView;
//    private LinearLayout logoContainer;
//    private LinearLayout buttonsContainer;
//    private ProgressBar progressBar;
//    private Button loginButton;
//    private Button createAccountButton;
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//
//        initializeViews();
//        mAuth = FirebaseAuth.getInstance();
//
//        // Start with splash screen
//        showSplashScreen();
//    }
//
//    private void initializeViews() {
//        logoImageView = findViewById(R.id.logoImageView);
//        logoContainer = findViewById(R.id.logoContainer);
//        buttonsContainer = findViewById(R.id.buttonsContainer);
//        progressBar = findViewById(R.id.progressBar);
//        loginButton = findViewById(R.id.loginButton);
//        createAccountButton = findViewById(R.id.createAccountButton);
//
//        // Set button click listeners
//        loginButton.setOnClickListener(v -> {
//            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        });
//
//        createAccountButton.setOnClickListener(v -> {
//            // Navigate to registration activity
//            // Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
//            // startActivity(intent);
//            // finish();
//        });
//    }
//
//    private void showSplashScreen() {
//        // Start logo animation
//        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
//        logoContainer.startAnimation(fadeIn);
//
//        // Show progress bar during loading
//        progressBar.setVisibility(View.VISIBLE);
//        buttonsContainer.setVisibility(View.GONE);
//
//        // After splash duration, check authentication or show login options
//        new Handler().postDelayed(() -> {
//            checkAuthenticationState();
//        }, SPLASH_DURATION);
//    }
//
//    private void checkAuthenticationState() {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null) {
//            // User is logged in, go directly to home
//            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            // User not logged in, show login/register options
//            showLoginOptions();
//        }
//    }
//
//    private void showLoginOptions() {
//        // Hide progress bar
//        progressBar.setVisibility(View.GONE);
//
//        // Show buttons with animation
//        buttonsContainer.setVisibility(View.VISIBLE);
//        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
//        buttonsContainer.startAnimation(slideUp);
//    }
//}

package com.example.info.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.info.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000; // 3 seconds
    private ImageView logoImageView;
    private LinearLayout logoContainer;
    private LinearLayout buttonsContainer;
    private ProgressBar progressBar;
    private Button loginButton;
    private Button createAccountButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initializeViews();
        mAuth = FirebaseAuth.getInstance();

        // Start with splash screen
        showSplashScreen();
    }

    private void initializeViews() {
        logoImageView = findViewById(R.id.logoImageView);
        logoContainer = findViewById(R.id.logoContainer);
        buttonsContainer = findViewById(R.id.buttonsContainer);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);

        // Set button click listeners
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        createAccountButton.setOnClickListener(v -> {
            // Navigate to LoginActivity in registration mode
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.putExtra("mode", "register"); // Pass extra to indicate registration mode
            startActivity(intent);
            finish();
        });
    }

    private void showSplashScreen() {
        // Start logo animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logoContainer.startAnimation(fadeIn);

        // Show progress bar during loading
        progressBar.setVisibility(View.VISIBLE);
        buttonsContainer.setVisibility(View.GONE);

        // After splash duration, check authentication or show login options
        new Handler().postDelayed(() -> {
            checkAuthenticationState();
        }, SPLASH_DURATION);
    }

    private void checkAuthenticationState() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // User is logged in, go directly to home
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // User not logged in, show login/register options
            showLoginOptions();
        }
    }

    private void showLoginOptions() {
        // Hide progress bar
        progressBar.setVisibility(View.GONE);

        // Show buttons with animation
        buttonsContainer.setVisibility(View.VISIBLE);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        buttonsContainer.startAnimation(slideUp);
    }
}