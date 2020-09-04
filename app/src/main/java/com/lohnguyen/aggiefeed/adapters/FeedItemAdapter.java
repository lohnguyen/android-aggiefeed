package com.lohnguyen.aggiefeed.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lohnguyen.aggiefeed.R;
import com.lohnguyen.aggiefeed.entities.FeedItem;

import java.util.ArrayList;
import java.util.List;

public class FeedItemAdapter extends RecyclerView.Adapter<FeedItemAdapter.ViewHolder> implements Filterable {

    private List<FeedItem> allFeedItems;
    private List<FeedItem> searchFeedItems;
    private OnFeedItemListener onFeedItemListener;

    public FeedItemAdapter(OnFeedItemListener onFeedItemListener) {
        this.onFeedItemListener = onFeedItemListener;
    }

    public void setSearchFeedItems(List<FeedItem> feedItems) {
        this.searchFeedItems = feedItems;
        this.allFeedItems = new ArrayList<>(feedItems);
        this.notifyDataSetChanged();
    }

    /**
     * Set up click listener
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
        FeedItem feedItem = searchFeedItems.get(position);
        holder.titleView.setText(feedItem.getTitle());
        holder.displayNameView.setText(feedItem.getDisplayName());
    }

    @Override
    public int getItemCount() {
        if (searchFeedItems != null) return searchFeedItems.size();
        else return 0;
    }

    public interface OnFeedItemListener {
        void onFeedItemClick(int position);
    }

    /**
     * Set up filtering for search
     */
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<FeedItem> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(allFeedItems);
            } else {
                String filteredPattern = charSequence.toString();

                for (FeedItem item : allFeedItems) {
                    if (item.getTitle().toLowerCase().contains(filteredPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            searchFeedItems.clear();
            searchFeedItems.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }
}
