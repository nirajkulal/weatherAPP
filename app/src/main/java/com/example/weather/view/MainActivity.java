package com.example.weather.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.WorkInfo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.weather.databinding.ActivityMainBinding;
import com.example.weather.model.localDatabase.data.ViewData;
import com.example.weather.utils.UIHelper;
import com.example.weather.viewModel.WeatherViewModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    int requestCode = 123;

    WeatherViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
    }

    /**
     * Observer added to workinfo and whenever job completed and new job enques UI updated.
     */
    private void observe() {
        viewModel.getInfo().observe(this, listOfWorkInfo -> {
            if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                return;
            }
            WorkInfo workInfo = listOfWorkInfo.get(0);
            if (workInfo.getState() == WorkInfo.State.ENQUEUED) {
                updateUI();
            }
        });
    }

    /**
     * To setup workmanger if not schduled before
     */
    private void setUp() {
        if (UIHelper.isEnabled(MainActivity.this)) {
            viewModel.setupJOB(MainActivity.this);
            observe();
        }
    }

    /**
     * UI updated from local database.
     */
    private void updateUI() {
        ViewData viewData = viewModel.getData();
        if (viewData != null) {
            binding.current.setText(viewData.getCurrentTemp());
            binding.locText.setText(viewData.getLocation());
            binding.feelsLike.setText(viewData.getFeelsLike());
            binding.type.setText(viewData.getStatus());
            binding.sunrise.setText(viewData.getSunRise());
            binding.lastUpdated.setText(viewData.getLastUpdated());
            binding.sunset.setText(viewData.getSunSet());
            binding.minMax.setText(viewData.getMinMaxTemp());
            Glide.with(this).load(viewData.getIcon()).into(binding.statusIcon);
        }
    }

    /**
     * Permisson handling
     */
    private void checkPermission() {
        if (UIHelper.isLocationPermission(MainActivity.this)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    requestCode);
        } else {
            requestLocation();
        }
    }

    /**
     * Location enable
     */
    private void requestLocation() {
        checkLocationenabled();
    }

    public void checkLocationenabled() {
        if (!UIHelper.isLocationEnabled(MainActivity.this)) {
            buildAlertMessageNoGps();
        } else {
            setUp();
        }
    }

    /**
     * Alert dialog is used to prompt request dialog for location enable
     */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
        setUp();
        updateUI();
    }
}