
package com.example.info.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.info.models.News;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.info.R;

public class NewsDetailActivity extends AppCompatActivity {

    private static final String TAG = "NewsDetailActivity";
    private FirebaseFirestore db;

    // UI elements
    private TextView titleTextView, descriptionTextView, contentTextView, categoryTextView, publishedAtTextView;
    private ImageView newsImageView, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail); // Make sure this layout exists and has the right views

        db = FirebaseFirestore.getInstance();

        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        contentTextView = findViewById(R.id.contentTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        publishedAtTextView = findViewById(R.id.publishedAtTextView);
        newsImageView = findViewById(R.id.newsImageView);
        backButton = findViewById(R.id.backButton);

        // Back button closes the detail
        backButton.setOnClickListener(v -> finish());

        String newsId = getIntent().getStringExtra("newsId");
        if (newsId != null) {
            loadNewsById(newsId);
        } else {
            Toast.makeText(this, "No news ID provided", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadNewsById(String newsId) {
        db.collection("news")
                .document(newsId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        News news = documentSnapshot.toObject(News.class);
                        if (news != null) {
                            displayNews(news);
                        }
                    } else {
                        Toast.makeText(this, "News not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading news detail", e);
                    Toast.makeText(this, "Failed to load news", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void displayNews(News news) {
        titleTextView.setText(news.getTitle());
        descriptionTextView.setText(news.getDescription());
        contentTextView.setText(news.getContent());
        categoryTextView.setText(news.getCategory());
        // If you have a date formatter method, use it:
        publishedAtTextView.setText(news.getFormattedTime());

        Glide.with(this)
                .load(news.getImageUrl())
                .placeholder(R.drawable.card_bg)
                .error(R.drawable.card_bg)
                .into(newsImageView);
    }
}
