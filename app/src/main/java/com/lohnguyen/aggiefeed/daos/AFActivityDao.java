package com.lohnguyen.aggiefeed.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lohnguyen.aggiefeed.entities.AFActivity;

import java.util.List;

@Dao
public interface AFActivityDao {

    @Insert
    void insert(AFActivity activity);

    @Insert
    void insert(List<AFActivity> activities);

    @Update
    void update(AFActivity activity);

    @Delete
    void delete(AFActivity activity);

    @Query("DELETE FROM activity_table")
    void deleteAll();

    @Query("SELECT * FROM activity_table")
    LiveData<List<AFActivity>> getAll();
}
