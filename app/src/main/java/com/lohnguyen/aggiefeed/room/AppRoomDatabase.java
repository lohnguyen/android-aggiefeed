package com.lohnguyen.aggiefeed.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lohnguyen.aggiefeed.daos.AFActivityDao;
import com.lohnguyen.aggiefeed.entities.AFActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {AFActivity.class}, version = 1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {

    private static AppRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract AFActivityDao AFActivityDao();

    public static synchronized AppRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppRoomDatabase.class, "activity_database")
                    .fallbackToDestructiveMigration() // delete db upon migration
                    .build();
        }

        return INSTANCE;
    }
}
