package com.lohnguyen.aggiefeed.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.lohnguyen.aggiefeed.entities.FeedItem;
import com.lohnguyen.aggiefeed.R;
import com.lohnguyen.aggiefeed.adapters.FeedItemAdapter;
import com.lohnguyen.aggiefeed.viewmodels.MainViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity implements FeedItemAdapter.OnFeedItemListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String EXTRA_TITLE = "com.lohnguyen.aggiefeed.TITLE";
    public static final String EXTRA_DISPLAY_NAME = "com.lohnguyen.aggiefeed.DISPLAY_NAME";
    public static final String EXTRA_OBJECT_TYPE = "com.lohnguyen.aggiefeed.OBJECT_TYPE";
    public static final String EXTRA_PUBLISHED = "com.lohnguyen.aggiefeed.PUBLISHED";
    public static final String EXTRA_LOCATION = "com.lohnguyen.aggiefeed.LOCATION";
    public static final String EXTRA_START_DATE = "com.lohnguyen.aggiefeed.START_DATE";
    public static final String EXTRA_END_DATE = "com.lohnguyen.aggiefeed.END_DATE";

    private MainViewModel mainViewModel;
    private List<FeedItem> allFeedItems;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FeedItemAdapter adapter = new FeedItemAdapter(MainActivity.this);

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout = findViewById(R.id.swiperefresh_feeditems);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = findViewById(R.id.recyclerview_feeditems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getAll().observe(this, feedItems -> {
            allFeedItems = feedItems;
            adapter.setSearchFeedItems(allFeedItems);
            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        setUpSearchItem(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            mainViewModel.fetchAll();
            return true;
        } else if (item.getItemId() == R.id.menu_add) {
            mainViewModel.insertTest();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        mainViewModel.refresh(swipeRefreshLayout);
    }

    @Override
    public void onFeedItemClick(int position) {
        FeedItem feedItem = allFeedItems.get(position);
        Intent intent = new Intent(this, DetailActivity.class);

        assert feedItem != null;
        intent.putExtra(EXTRA_TITLE, feedItem.getTitle());
        intent.putExtra(EXTRA_DISPLAY_NAME, feedItem.getDisplayName());
        intent.putExtra(EXTRA_OBJECT_TYPE, feedItem.getObjectType());
        intent.putExtra(EXTRA_PUBLISHED, feedItem.getPublished());
        intent.putExtra(EXTRA_LOCATION, feedItem.getLocation());
        intent.putExtra(EXTRA_START_DATE, feedItem.getStartDate());
        intent.putExtra(EXTRA_END_DATE, feedItem.getEndDate());

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    private void setUpSearchItem(Menu menu) {
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(ContextCompat.getColor(this.getApplicationContext(), R.color.accent));
        searchAutoComplete.setTextColor(ContextCompat.getColor(this.getApplicationContext(), R.color.accent));

        ImageView searchCloseIcon = (ImageView)searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchCloseIcon.setImageResource(R.drawable.ic_clear_24);

        ImageView searchIcon = (ImageView)searchView.findViewById(androidx.appcompat.R.id.search_button);
        searchIcon.setImageResource(R.drawable.ic_search_24);

        ImageView searchMagIcon = (ImageView)searchView.findViewById(androidx.appcompat.R.id.search_voice_btn);
        searchMagIcon.setImageResource(R.drawable.ic_keyboard_voice_24);

        RxSearchObservable.fromView(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .map(text -> text.toLowerCase().trim())
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static class RxSearchObservable {

        public static Observable<String> fromView(SearchView searchView) {
            final PublishSubject<String> subject = PublishSubject.create();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    subject.onComplete();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String text) {
                    subject.onNext(text);
                    return true;
                }
            });

            return subject;
        }
    }
}