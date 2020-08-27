package com.lohnguyen.aggiefeed.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lohnguyen.aggiefeed.R;
import com.lohnguyen.aggiefeed.entities.FeedItem;

import java.util.List;

public class FeedItemAdapter extends RecyclerView.Adapter<FeedItemAdapter.ViewHolder> {

    private List<FeedItem> feedItems;
    private OnFeedItemListener onFeedItemListener;

    public FeedItemAdapter(List<FeedItem> feedItems, OnFeedItemListener onFeedItemListener) {
        this.feedItems = feedItems;
        this.onFeedItemListener = onFeedItemListener;
    }

    public interface OnFeedItemListener {
        void onFeedItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleView;
        TextView displayNameView;
        OnFeedItemListener onFeedItemListener;

        public ViewHolder(@NonNull View itemView, OnFeedItemListener onFeedItemListener) {
            super(itemView);

            titleView = itemView.findViewById(R.id.title);
            displayNameView = itemView.findViewById(R.id.display_name);

            this.onFeedItemListener = onFeedItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onFeedItemListener.onFeedItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public FeedItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view, onFeedItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedItemAdapter.ViewHolder holder, int position) {
        FeedItem feedItem = feedItems.get(position);
        holder.titleView.setText(feedItem.getTitle());
        holder.displayNameView.setText(feedItem.getDisplayName());
    }

    @Override
    public int getItemCount() {
        if (feedItems != null) return feedItems.size();
        else return 0;
    }
}
