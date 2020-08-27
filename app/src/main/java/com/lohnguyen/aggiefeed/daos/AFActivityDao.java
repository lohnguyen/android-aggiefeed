package com.lohnguyen.aggiefeed.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.lohnguyen.aggiefeed.entities.AFActivity;

import java.util.List;

@Dao
public abstract class AFActivityDao {

    @Insert
    public abstract void insert(AFActivity activity);

    @Insert
    public abstract void insert(List<AFActivity> activities);

    @Update
    public abstract void update(AFActivity activity);

    @Delete
    public abstract void delete(AFActivity activity);

    @Query("DELETE FROM activity_table")
    public abstract void deleteAll();

    @Query("SELECT * FROM activity_table")
    public abstract LiveData<List<AFActivity>> getAll();

    @Transaction
    public void deleteAndInsert(List<AFActivity> activities) {
        deleteAll();
        insert(activities);
    }
}
