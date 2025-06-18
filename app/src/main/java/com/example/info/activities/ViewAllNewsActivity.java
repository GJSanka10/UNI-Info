package com.example.info.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.info.adapters.NewsAdapter;
import com.example.info.models.News;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.info.R;

import java.util.ArrayList;
import java.util.List;

public class ViewAllNewsActivity extends AppCompatActivity {
    private static final String TAG = "ViewAllNewsActivity";
    public static final String EXTRA_NEWS_TYPE = "news_type";
    public static final String EXTRA_CATEGORY = "category";
    public static final String TYPE_CATEGORY = "category";
    public static final String TYPE_TRENDING = "trending";
    private TextView titleTextView;
    private ImageView backButton;
    private RecyclerView newsRecyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyStateTextView;
    private NewsAdapter newsAdapter;
    private List<News> newsList;
    private FirebaseFirestore db;

    private String newsType;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_news);

        db = FirebaseFirestore.getInstance();

        // Get intent extras
        newsType = getIntent().getStringExtra(EXTRA_NEWS_TYPE);
        category = getIntent().getStringExtra(EXTRA_CATEGORY);

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        setupSwipeRefresh();
        loadNews();
    }

    private void initializeViews() {
        titleTextView = findViewById(R.id.titleTextView);
        backButton = findViewById(R.id.backButton);
        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        emptyStateTextView = findViewById(R.id.emptyStateTextView);

        // Set title based on news type
        if (TYPE_TRENDING.equals(newsType)) {
            titleTextView.setText("All Trending News");
        } else if (TYPE_CATEGORY.equals(newsType) && category != null) {
            titleTextView.setText("All " + category + " News");
        } else {
            titleTextView.setText("All News");
        }
    }

    private void setupRecyclerView() {
        newsList = new ArrayList<>();
        // Use vertical layout with full-width cards
        newsAdapter = new NewsAdapter(newsList, this, false, false);

        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setAdapter(newsAdapter);
        newsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(12));
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary, null));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadNews();
        });
    }

    private void loadNews() {
        showLoading(true);

        Query query;

        if (TYPE_TRENDING.equals(newsType)) {
            // Load all trending news
            query = db.collection("news")
                    .whereEqualTo("isTrending", true)
                    .orderBy("publishedAt", Query.Direction.DESCENDING)
                    .limit(50); // Load more items for "view all"
        } else if (TYPE_CATEGORY.equals(newsType) && category != null) {
            // Load all news for specific category
            query = db.collection("news")
                    .whereEqualTo("category", category)
                    .orderBy("publishedAt", Query.Direction.DESCENDING)
                    .limit(100); // Load more items for "view all"
        } else {
            // Fallback - load all news
            query = db.collection("news")
                    .orderBy("publishedAt", Query.Direction.DESCENDING)
                    .limit(100);
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    newsList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            News news = document.toObject(News.class);
                            if (news != null) {
                                news.setId(document.getId());
                                newsList.add(news);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing news document: " + document.getId(), e);
                        }
                    }

                    showLoading(false);
                    newsAdapter.notifyDataSetChanged();

                    // Show/hide empty state
                    if (newsList.isEmpty()) {
                        showEmptyState();
                    } else {
                        hideEmptyState();
                    }

                    Log.d(TAG, "Loaded " + newsList.size() + " news items");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading news", e);
                    showLoading(false);
                    Toast.makeText(this, "Failed to load news", Toast.LENGTH_SHORT).show();
                    showEmptyState();
                });
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            newsRecyclerView.setVisibility(View.GONE);
            emptyStateTextView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void showEmptyState() {
        newsRecyclerView.setVisibility(View.GONE);
        emptyStateTextView.setVisibility(View.VISIBLE);

        if (TYPE_TRENDING.equals(newsType)) {
            emptyStateTextView.setText("No trending news available");
        } else if (TYPE_CATEGORY.equals(newsType) && category != null) {
            emptyStateTextView.setText("No " + category.toLowerCase() + " news available");
        } else {
            emptyStateTextView.setText("No news available");
        }
    }

    private void hideEmptyState() {
        newsRecyclerView.setVisibility(View.VISIBLE);
        emptyStateTextView.setVisibility(View.GONE);
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