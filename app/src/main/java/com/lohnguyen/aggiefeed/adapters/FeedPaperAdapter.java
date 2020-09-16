package com.lohnguyen.aggiefeed.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lohnguyen.aggiefeed.fragments.FeedListFragment;

import java.util.List;

public class FeedPaperAdapter extends FragmentStateAdapter {
    private List<FeedListFragment> feedListFragments;

    public FeedPaperAdapter(@NonNull FragmentActivity fragmentActivity, List<FeedListFragment> feedListFragments) {
        super(fragmentActivity);

        this.feedListFragments = feedListFragments;
    }

    @NonNull
    @Override
    public FeedListFragment createFragment(int position) {
        Log.e("feedPagerAdapter", "createFragment: ");
        return feedListFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return feedListFragments.size();
    }
}
