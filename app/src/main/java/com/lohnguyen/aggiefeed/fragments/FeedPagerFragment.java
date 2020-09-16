package com.lohnguyen.aggiefeed.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.lohnguyen.aggiefeed.R;
import com.lohnguyen.aggiefeed.adapters.FeedPaperAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeedPagerFragment extends Fragment {

    ViewPager2 viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("feedPager", "pager created");

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<FeedListFragment> feedListFragments = new ArrayList<>();
        feedListFragments.add(FeedListFragment.newInstance());
        feedListFragments.add(FeedListFragment.newInstance(true));

        viewPager = view.findViewById(R.id.feed_view_pager);
        viewPager.setAdapter(new FeedPaperAdapter(getActivity(), feedListFragments));
    }
}
