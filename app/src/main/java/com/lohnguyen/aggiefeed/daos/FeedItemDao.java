package com.lohnguyen.aggiefeed.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.lohnguyen.aggiefeed.entities.FeedItem;

import java.util.List;

@Dao
public abstract class FeedItemDao {

    @Insert
    public abstract void insert(FeedItem feedItem);

    @Insert
    public abstract void insert(List<FeedItem> feedItems);

    @Update
    public abstract void update(FeedItem feedItem);

    @Delete
    public abstract void delete(FeedItem feedItem);

    @Query("DELETE FROM feed_item_table")
    public abstract void deleteAll();

    @Query("SELECT * FROM feed_item_table")
    public abstract LiveData<List<FeedItem>> getAll();

    @Query("SELECT * FROM feed_item_table WHERE displayName LIKE :displayName")
    public abstract LiveData<List<FeedItem>> getHappeningTodayFeedItems(String displayName);

    @Transaction
    public void deleteAllAndInsert(List<FeedItem> feedItems) {
        deleteAll();
        insert(feedItems);
    }
}
