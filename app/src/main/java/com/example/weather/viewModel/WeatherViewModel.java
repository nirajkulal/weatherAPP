package com.example.weather.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.weather.model.Worker;
import com.example.weather.model.localDatabase.data.ViewData;
import com.example.weather.model.localDatabase.DatabaseFunctions;
import com.example.weather.model.localDatabase.LocalDatabaseFactory;
import com.example.weather.view.MainActivity;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class WeatherViewModel extends ViewModel {

    private static final String TAG_SYNC_DATA = "API_CALL";
    private static final String SYNC_DATA_WORK_NAME = "API_CALL_WEATHER";
    private LiveData<List<WorkInfo>> mSavedWorkInfo;

    /**
     * Check Job is scheduled
     * @param tag
     * @param context
     * @return
     */
    private boolean isWorkScheduled(String tag, Context context) {
        WorkManager instance = WorkManager.getInstance(context);
        ListenableFuture<List<WorkInfo>> statuses = instance.getWorkInfosByTag(tag);
        try {
            boolean running = false;
            List<WorkInfo> workInfoList = statuses.get();
            for (WorkInfo workInfo : workInfoList) {
                WorkInfo.State state = workInfo.getState();
                running = state == WorkInfo.State.RUNNING | state == WorkInfo.State.ENQUEUED;
            }
            return running;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Setup a job if not scheduled
     * @param context
     */
    public void setupJOB(MainActivity context) {
        if (!isWorkScheduled(TAG_SYNC_DATA, context)) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();
            PeriodicWorkRequest periodicSyncDataWork =
                    new PeriodicWorkRequest.Builder(Worker.class, 15, TimeUnit.MINUTES)
                            .addTag(TAG_SYNC_DATA)
                            .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.SECONDS)
                            .setConstraints(constraints)
                            .build();
            WorkManager
                    .getInstance(context).enqueueUniquePeriodicWork(
                    SYNC_DATA_WORK_NAME,
                    ExistingPeriodicWorkPolicy.REPLACE, //Existing Periodic Work policy
                    periodicSyncDataWork //work request
            );
        }
        mSavedWorkInfo = WorkManager
                .getInstance(context).getWorkInfosByTagLiveData(TAG_SYNC_DATA);
    }

    public LiveData<List<WorkInfo>> getInfo() {
        return mSavedWorkInfo;
    }

    /**
     * Get data from local database.
     * @return
     */
    public ViewData getData() {
        DatabaseFunctions localDatabaseHandler = LocalDatabaseFactory.getDatabase();
        return localDatabaseHandler.fetch();
    }
}
