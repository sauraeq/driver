package com.abc.taxidriver.Services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.abc.taxidriver.Utils.ConstantUtils;
import com.abc.taxidriver.Utils.SharedPreferenceUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class ServiceTaskNew extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private FusedLocationProviderClient mFusedLocationClient;
    private com.google.android.gms.location.LocationRequest locationRequest;
    private LocationCallback locationCallback;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    double currlat = 0.0, currlog = 0.0;
    private FusedLocationProviderClient fusedLocationClient;
  //  FirebaseDatabase database;

    public int counter = 0;
    Context context;

    public ServiceTaskNew(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public ServiceTaskNew() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("onBind", "onBind");
        // startScanning();
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
       

        Log.d("onCreate", "onCreate");
        
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

   /* public void getLocationPermission() {
        getLocation();
    }*/
/*

    public void getLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
    }
*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Log.d("service", "is running");
            startTimer();
            //   startScanning();
        } catch (Exception e) {
            Log.d("excep", e.toString());
        }

        //we have some options for service
        //start sticky means service will be explicity started and stopped
        return START_STICKY;
    }

    public void startScanning() {
        try {
            settingsrequest();

           // updateLocation(lat, lng, address, date);

        } catch (Exception e) {
            Log.d("excep", e.toString());

        }

    }


    public void deleteFiles(String path) {
        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
                Log.d("IOException", e.toString());

            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy", "onDestroy");

        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 5000, 60000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (counter++));
                startScanning();
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }




    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {

    }

    public void settingsrequest() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();
        com.google.android.gms.location.LocationRequest locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //  setLoginScreen();
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                       /* try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(ServiceTaskNew.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }*/
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    public void getLocation() {
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            Log.d("fused_location", location + "");
                            if (location != null) {

                                currlat = location.getLatitude();
                                currlog = location.getLongitude();
                                Log.d("currlatservice", currlat + "...");
                                Log.d("currlogservice", currlog + "...");
                             /*   database = FirebaseDatabase.getInstance();
                                database.getReference().child("Driver_Current_Location").child(
                                        SharedPreferenceUtils.Companion.getInstance(context).getStringValue(ConstantUtils.USER_ID,"")
                                ).setValue(currlat+","+currlog);*/
                                SharedPreferenceUtils.Companion.getInstance(context).setStringValue(ConstantUtils.CURENT_LAT, currlat+"");
                                SharedPreferenceUtils.Companion.getInstance(context).setStringValue(ConstantUtils.CURENT_LNG, currlog+"");

                                getCurrentAddress(currlat, currlog);
                            } else {
                                getLocationfromClient();
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    public void getLocationfromClient() {
        try {
            Log.d("location_clientstart", "..........");
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            locationRequest = com.google.android.gms.location.LocationRequest.create();
            locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(20 * 1000);

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    try {
                        Log.d("location_client", locationResult + "");

                        if (locationResult == null) {
                            // return;
                        }
                        for (Location location : locationResult.getLocations()) {
                            if (location != null) {
                                if (mFusedLocationClient != null) {
                                    mFusedLocationClient.removeLocationUpdates(locationCallback);
                                }
                                currlat = location.getLatitude();
                                currlog = location.getLongitude();
                                Log.d("location_from_client", location + "");
                                Log.d("currlatservice", currlat + "...");
                                Log.d("currlogservice", currlog + "...");
                              /*  database = FirebaseDatabase.getInstance();
                                database.getReference().child("Driver_Current_Location").child(
                                       SharedPreferenceUtils.Companion.getInstance(context).getStringValue(ConstantUtils.USER_ID,"")
                                ).setValue(currlat+","+currlog);*/
                                SharedPreferenceUtils.Companion.getInstance(context).setStringValue(ConstantUtils.CURENT_LAT, currlat+"");
                                SharedPreferenceUtils.Companion.getInstance(context).setStringValue(ConstantUtils.CURENT_LNG, currlog+"");

                                 getCurrentAddress(currlat, currlog);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } catch (Exception e) {

        }
    }

    public void getCurrentAddress(double latitude, double longitude) {


        Geocoder gc = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                // for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                //    sb.append(address.getAddressLine(i)).append("\n");
                sb.append(address.getSubLocality()).append(", ");
                sb.append(address.getLocality()).append(", ");
                sb.append(address.getAdminArea()).append(", ");
                sb.append(address.getCountryName()).append(", ");
                sb.append(address.getPostalCode());

            }
            Log.d("currentAddress", sb.toString());
            SharedPreferenceUtils.Companion.getInstance(context).setStringValue(ConstantUtils.CURRENTLOCATION, sb.toString());

        } catch (Exception e) {

        }
    }


    public void updateLocation(String lat, String lng, String address, String date) {

    }

}

