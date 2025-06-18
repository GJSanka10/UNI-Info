package com.example.info.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.info.R;
import com.example.info.adapters.NewsAdapter;
import com.example.info.models.News;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private ImageView profileImageView;
    private TextView academicButton, sportButton, eventButton;
    private TextView viewAllAcademic, viewAllTrending; // Add these
    private RecyclerView breakingNewsRecyclerView, trendingNewsRecyclerView;
    private NewsAdapter breakingNewsAdapter, trendingNewsAdapter;
    private List<News> breakingNewsList, trendingNewsList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ImageView menuIcon;

    private TextView headlineTextView;  // Added this
    private TextView academicTab, sportTab, eventTab;

    // Current selected category
    private String currentCategory = "Academic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initializeViews();
        setupRecyclerViews();
        setupClickListeners();
        loadNewsData();

        // Set initial headline text
        updateHeadlineText(currentCategory);
    }

    private void initializeViews() {
        menuIcon = findViewById(R.id.menuIcon);
        profileImageView = findViewById(R.id.profileIcon);
        academicButton = findViewById(R.id.academicTab);
        sportButton = findViewById(R.id.sportTab);
        eventButton = findViewById(R.id.eventTab);
        breakingNewsRecyclerView = findViewById(R.id.academicRecyclerView);
        trendingNewsRecyclerView = findViewById(R.id.trendingRecyclerView);

        // Initialize View All buttons
        viewAllAcademic = findViewById(R.id.viewAllAcademic);
        viewAllTrending = findViewById(R.id.viewAllTrending);

        // Initialize headline TextView for "Academic Headlines"
        headlineTextView = findViewById(R.id.academicHeadlinesTextView);  // Make sure this ID exists in your XML
    }

    private void setupRecyclerViews() {
        breakingNewsList = new ArrayList<>();
        trendingNewsList = new ArrayList<>();

        // Regular adapter for breaking news (horizontal)
        breakingNewsAdapter = new NewsAdapter(breakingNewsList, this, true);

        // Compact adapter for trending news (vertical, compact)
        trendingNewsAdapter = new NewsAdapter(trendingNewsList, this, false, true);

        // Setup horizontal RecyclerView for breaking news with spacing
        breakingNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        breakingNewsRecyclerView.setAdapter(breakingNewsAdapter);
        breakingNewsRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(16)); // 16dp spacing

        // Setup vertical RecyclerView for trending news with spacing
        trendingNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trendingNewsRecyclerView.setAdapter(trendingNewsAdapter);
        trendingNewsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(8)); // 8dp spacing for compact items
    }

    private void setupClickListeners() {
        menuIcon.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        });

        profileImageView.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        });

        // Category tab clicks with visual feedback
        academicButton.setOnClickListener(v -> {
            selectCategory("Academic", academicButton);
            loadCategoryNews("Academic");
        });

        sportButton.setOnClickListener(v -> {
            selectCategory("Sport", sportButton);
            loadCategoryNews("Sport");
        });

        eventButton.setOnClickListener(v -> {
            selectCategory("Event", eventButton);
            loadCategoryNews("Event");
        });

        // View All click listeners
        viewAllAcademic.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ViewAllNewsActivity.class);
            intent.putExtra(ViewAllNewsActivity.EXTRA_NEWS_TYPE, ViewAllNewsActivity.TYPE_CATEGORY);
            intent.putExtra(ViewAllNewsActivity.EXTRA_CATEGORY, currentCategory);
            startActivity(intent);
        });

        viewAllTrending.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ViewAllNewsActivity.class);
            intent.putExtra(ViewAllNewsActivity.EXTRA_NEWS_TYPE, ViewAllNewsActivity.TYPE_TRENDING);
            startActivity(intent);
        });
    }

    private void selectCategory(String category, TextView selectedTab) {
        // Reset all tabs to unselected state
        resetTabAppearance(academicButton);
        resetTabAppearance(sportButton);
        resetTabAppearance(eventButton);

        // Highlight selected tab
        setSelectedTabAppearance(selectedTab);

        currentCategory = category;

        // Update the "View All" text to reflect current category
        updateViewAllText();

        // Update headline text
        updateHeadlineText(category);
    }

    private void resetTabAppearance(TextView tab) {
        tab.setBackgroundResource(R.drawable.tab_unselected_background_light);
        tab.setTextColor(getResources().getColor(android.R.color.black, null));

        // Tint the compound drawable to black
        Drawable[] drawables = tab.getCompoundDrawables();
        if (drawables[0] != null) { // Left drawable
            drawables[0].setTint(getResources().getColor(android.R.color.black, null));
        }
    }

    private void setSelectedTabAppearance(TextView tab) {
        tab.setBackgroundResource(R.drawable.tab_selected_background_light);
        tab.setTextColor(getResources().getColor(android.R.color.white, null));

        // Tint the compound drawable to white
        Drawable[] drawables = tab.getCompoundDrawables();
        if (drawables[0] != null) { // Left drawable
            drawables[0].setTint(getResources().getColor(android.R.color.white, null));
        }
    }
    private void updateHeadlineText(String category) {
        if (headlineTextView != null) {
            String heading = category + " Headlines";
            headlineTextView.setText(heading);
        }
    }

    private void updateViewAllText() {
        // Optional: update "View All" text if you want to show current category there as well
        // viewAllAcademic.setText("View All " + currentCategory);
    }

    private void loadNewsData() {
        loadCategoryNews(currentCategory);
        loadTrendingNews();
    }

    private void loadCategoryNews(String category) {
        Log.d(TAG, "Loading news for category: " + category);

        db.collection("news")
                .whereEqualTo("category", category)
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .limit(5) // Keep limit small for home screen preview
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    breakingNewsList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            News news = document.toObject(News.class);
                            if (news != null) {
                                news.setId(document.getId()); // Set document ID
                                breakingNewsList.add(news);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing news document: " + document.getId(), e);
                        }
                    }
                    breakingNewsAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Loaded " + breakingNewsList.size() + " category news items");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading category news", e);
                    Toast.makeText(this, "Failed to load news", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadTrendingNews() {
        Log.d(TAG, "Loading trending news");

        db.collection("news")
                .whereEqualTo("isTrending", true)
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .limit(5) // Keep limit small for home screen preview
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    trendingNewsList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            News news = document.toObject(News.class);
                            if (news != null) {
                                news.setId(document.getId()); // Set document ID
                                trendingNewsList.add(news);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing trending news document: " + document.getId(), e);
                        }
                    }
                    trendingNewsAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Loaded " + trendingNewsList.size() + " trending news items");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading trending news", e);
                    Toast.makeText(this, "Failed to load trending news", Toast.LENGTH_SHORT).show();
                });
    }

    // Method to refresh data (can be called from swipe refresh)
    public void refreshData() {
        loadNewsData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        refreshData();
    }

    // ItemDecoration for horizontal spacing
    public static class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {
        private final int horizontalSpaceWidth;

        public HorizontalSpaceItemDecoration(int horizontalSpaceWidth) {
            this.horizontalSpaceWidth = horizontalSpaceWidth;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            // Add right margin to all items except the last one
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.right = horizontalSpaceWidth;
            }
        }
    }

    // ItemDecoration for vertical spacing
    public static class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            // Add bottom margin to all items except the last one
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = verticalSpaceHeight;
            }
        }
    }
}