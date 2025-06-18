package com.example.info.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.info.R;
import com.example.info.models.News;
import com.example.info.activities.NewsDetailActivity;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> newsList;
    private Context context;
    private boolean isHorizontal = false;
    private boolean isCompact = false;

    public NewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    public NewsAdapter(List<News> newsList, Context context, boolean isHorizontal) {
        this.newsList = newsList;
        this.context = context;
        this.isHorizontal = isHorizontal;
    }

    public NewsAdapter(List<News> newsList, Context context, boolean isHorizontal, boolean isCompact) {
        this.newsList = newsList;
        this.context = context;
        this.isHorizontal = isHorizontal;
        this.isCompact = isCompact;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId;
        if (isCompact) {
            layoutId = R.layout.item_news_compact;
        } else {
            layoutId = isHorizontal ? R.layout.item_news_horizontal : R.layout.item_news_vertical;
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);

        // Set title
        holder.titleTextView.setText(news.getTitle());

        // Set description if available (not used in compact layout)
        if (holder.descriptionTextView != null) {
            String description = news.getDescription();
            if (description.isEmpty()) {
                // Use first 100 characters of content as description
                String content = news.getContent();
                description = content.length() > 100 ? content.substring(0, 100) + "..." : content;
            }
            holder.descriptionTextView.setText(description);
        }

        // Set category
        if (holder.categoryTextView != null) {
            holder.categoryTextView.setText(news.getCategory().toUpperCase());
        }

        // Set time
        if (holder.timeTextView != null) {
            holder.timeTextView.setText(news.getFormattedTime());
        }

        // Load image using Glide
        if (holder.imageView != null) {
            String imageUrl = news.getImageUrl();
            if (!imageUrl.isEmpty()) {
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.card_bg)
                        .error(R.drawable.card_bg)
                        .transform(new RoundedCorners(isCompact ? 8 : 16))
                        .into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.card_bg);
            }
        }

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            // intent.putExtra("news", news);
            intent.putExtra("newsId", news.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsList != null ? newsList.size() : 0;
    }

    public void updateNewsList(List<News> newNewsList) {
        this.newsList = newNewsList;
        notifyDataSetChanged();
    }

    public void addNews(News news) {
        if (newsList != null) {
            newsList.add(0, news); // Add to beginning
            notifyItemInserted(0);
        }
    }

    public void removeNews(int position) {
        if (newsList != null && position >= 0 && position < newsList.size()) {
            newsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView categoryTextView;
        TextView timeTextView;
        ImageView imageView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            imageView = itemView.findViewById(R.id.newsImageView);
        }
    }
}