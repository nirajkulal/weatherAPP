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
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weather.R;
import com.example.weather.databinding.ActivityMainBinding;
import com.example.weather.model.localDatabase.data.ViewData;
import com.example.weather.utils.UIHelper;
import com.example.weather.viewModel.WeatherViewModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    int requestCodeLoc = 123;
    WeatherViewModel viewModel;

    /**
     * Activity life cycle callback
     */
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
            } else if (workInfo.getState() == WorkInfo.State.RUNNING) {
                updateUIRunning();
            }
        });
    }

    /**
     * Update Job status - Tqast message
     */
    private void updateUIRunning() {
        Toast.makeText(this,
                viewModel.getRunningMessage(this),
                Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * To setup workmanger if not schduled before
     */
    private void setUp() {
        if (UIHelper.isEnabled(MainActivity.this)) {
            viewModel.setupJOB(MainActivity.this);
            observe();
            handleErrorMessage();
            handleDialog();
        } else {
            updateUIPermmisonError();
        }
    }

    /**
     * Hanlde alert window
     */
    private void handleDialog() {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                if (!isFinishing())
                    alertDialog.cancel();
            }
        }
    }

    /**
     * Handle error message
     */
    private void handleErrorMessage() {
        if (viewModel.isErrorDisplayed(this, binding)) {
            binding.locText.setText(viewModel.getRunningMessage(this));
        }
    }

    /**
     * UI updated from local database.
     */
    private void updateUI() {
        ViewData viewData = viewModel.getData();
        if (viewData != null) {
            binding.current.setText(viewData.getCurrentTemp());
            if (viewData.getLocation() != null)
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
            updateUIPermmisonError();

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    requestCodeLoc);
        } else {
            requestLocation();
        }
    }

    /**
     * Update error message
     */
    private void updateUIPermmisonError() {
        binding.locText.setText(viewModel.getPermissionError(this));
    }

    /**
     * Location enable
     */
    private void requestLocation() {
        checkLocationenabled();
    }

    /**
     * Check permission
     */
    public void checkLocationenabled() {
        if (!UIHelper.isLocationEnabled(MainActivity.this)) {
            updateUIPermmisonError();
            buildAlertMessageNoGps();
        } else {
            setUp();
        }
    }

    /**
     * Alert dialog is used to prompt request dialog for location enable
     */
    private void buildAlertMessageNoGps() {
        handleDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert_title)
                .setCancelable(true)
                .setMessage(R.string.alert_desc);
        builder.setPositiveButton(
                android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();

    }

    AlertDialog alertDialog;

    /**
     * Permisson callbacks
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCodeLoc == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation();
            }
        }
    }

    /**
     * Activity life cycle callback
     */
    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
        setUp();
        updateUI();
    }
}