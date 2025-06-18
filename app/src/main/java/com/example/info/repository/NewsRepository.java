// File: app/src/main/java/com/example/pulse/repository/NewsRepository.java
package com.example.info.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.info.models.News;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NewsRepository {
    private static final String TAG = "NewsRepository";
    private static NewsRepository instance;
    private FirebaseFirestore db;

    // LiveData for observing data changes
    private MutableLiveData<List<News>> breakingNewsLiveData;
    private MutableLiveData<List<News>> trendingNewsLiveData;
    private MutableLiveData<List<News>> categoryNewsLiveData;
    private MutableLiveData<Boolean> loadingLiveData;
    private MutableLiveData<String> errorLiveData;

    private NewsRepository() {
        db = FirebaseFirestore.getInstance();
        initializeLiveData();
    }

    public static NewsRepository getInstance() {
        if (instance == null) {
            instance = new NewsRepository();
        }
        return instance;
    }

    private void initializeLiveData() {
        breakingNewsLiveData = new MutableLiveData<>();
        trendingNewsLiveData = new MutableLiveData<>();
        categoryNewsLiveData = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    // Getters for LiveData
    public MutableLiveData<List<News>> getBreakingNewsLiveData() {
        return breakingNewsLiveData;
    }

    public MutableLiveData<List<News>> getTrendingNewsLiveData() {
        return trendingNewsLiveData;
    }

    public MutableLiveData<List<News>> getCategoryNewsLiveData() {
        return categoryNewsLiveData;
    }

    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    // Fetch breaking news
    public void fetchBreakingNews() {
        loadingLiveData.setValue(true);

        db.collection("news")
                .whereEqualTo("isBreaking", true)
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<News> newsList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            News news = document.toObject(News.class);
                            news.setId(document.getId()); // Set document ID
                            newsList.add(news);
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing news document: " + document.getId(), e);
                        }
                    }
                    breakingNewsLiveData.setValue(newsList);
                    loadingLiveData.setValue(false);
                    Log.d(TAG, "Fetched " + newsList.size() + " breaking news items");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching breaking news", e);
                    errorLiveData.setValue("Failed to load breaking news: " + e.getMessage());
                    loadingLiveData.setValue(false);
                });
    }

    // Fetch trending news
    public void fetchTrendingNews() {
        loadingLiveData.setValue(true);

        db.collection("news")
                .whereEqualTo("isTrending", true)
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .limit(15)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<News> newsList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            News news = document.toObject(News.class);
                            news.setId(document.getId());
                            newsList.add(news);
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing trending news document: " + document.getId(), e);
                        }
                    }
                    trendingNewsLiveData.setValue(newsList);
                    loadingLiveData.setValue(false);
                    Log.d(TAG, "Fetched " + newsList.size() + " trending news items");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching trending news", e);
                    errorLiveData.setValue("Failed to load trending news: " + e.getMessage());
                    loadingLiveData.setValue(false);
                });
    }

    // Fetch news by category
    public void fetchNewsByCategory(String category) {
        loadingLiveData.setValue(true);

        db.collection("news")
                .whereEqualTo("category", category)
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<News> newsList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            News news = document.toObject(News.class);
                            news.setId(document.getId());
                            newsList.add(news);
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing category news document: " + document.getId(), e);
                        }
                    }
                    categoryNewsLiveData.setValue(newsList);
                    loadingLiveData.setValue(false);
                    Log.d(TAG, "Fetched " + newsList.size() + " news items for category: " + category);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching news by category: " + category, e);
                    errorLiveData.setValue("Failed to load " + category + " news: " + e.getMessage());
                    loadingLiveData.setValue(false);
                });
    }

    // Search news
    public void searchNews(String query, SearchCallback callback) {
        if (query == null || query.trim().isEmpty()) {
            callback.onSearchResult(new ArrayList<>());
            return;
        }

        loadingLiveData.setValue(true);

        // Note: Firestore doesn't support full-text search natively
        // This is a basic implementation - consider using Algolia or Elasticsearch for better search
        db.collection("news")
                .orderBy("title")
                .startAt(query.toLowerCase())
                .endAt(query.toLowerCase() + '\uf8ff')
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<News> searchResults = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            News news = document.toObject(News.class);
                            news.setId(document.getId());
                            // Additional filtering can be done here
                            if (news.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                    news.getContent().toLowerCase().contains(query.toLowerCase())) {
                                searchResults.add(news);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing search result: " + document.getId(), e);
                        }
                    }
                    callback.onSearchResult(searchResults);
                    loadingLiveData.setValue(false);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error searching news", e);
                    callback.onSearchError(e.getMessage());
                    loadingLiveData.setValue(false);
                });
    }

    // Refresh all data
    public void refreshAllData() {
        fetchBreakingNews();
        fetchTrendingNews();
    }

    // Interface for search callback
    public interface SearchCallback {
        void onSearchResult(List<News> results);
        void onSearchError(String error);
    }
}