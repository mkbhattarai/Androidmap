package com.example.maheshbhattarai.androidmap;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.Manifest;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
//here mention all interface need implement
{
    GoogleMap mMap;
    LatLng latLng;
    MarkerOptions marker;
    public Double currentLatitude;
    public Double currentLongitude;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this is for version permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //here this condition check for android version
            //before m->marshmallow version , if you are running android version before march melon ,
            //then you don't require permission
            //but if you are running later version after marshmallow
            //then you need to check permssion before loading map.
            checkLocationPermission(); //This is method for getting permission
            //will go to there
        }

        //this is used for loading map in design

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_image); //map_img id for fragment map, already there in design

        //this is loading map
        mapFragment.getMapAsync(this); //this will generate onMapReady method , is below

    }


    //this is map ready method
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;  //creating Map Instance
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //setting normal view , there is different view , but we have normal
        mMap.getUiSettings().setMyLocationButtonEnabled(true); // set false
        mMap.getUiSettings().setMapToolbarEnabled(true); //set false


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //this is for getting permission if version is greater than marshmallow
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {

            //this is for below march melon
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    //This is built in method , just copy paste
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    //This is for finding current location method
    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mLastLocation != null) {
            Log.e("location", "lat " + mLastLocation.getLatitude() + " long " + mLastLocation.getLongitude());
            Toast.makeText(MainActivity.this,"lat " + mLastLocation.getLatitude() + " long " + mLastLocation.getLongitude(),Toast.LENGTH_LONG).show();
            currentLatitude = mLastLocation.getLatitude();
            currentLongitude = mLastLocation.getLongitude();

            if (currentLatitude != null && currentLongitude != null) {
                LatLng origin = new LatLng(currentLatitude, currentLongitude);
                float zoomLevel = 15.0f; //This goes up to 21
                marker = new MarkerOptions()
                        .position(origin)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin));
                mMap.addMarker(marker);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, zoomLevel));
            }

        }

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }


    //this is for connecting with google map
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MainActivity.this);
        }

    }

    //just copy paste
    @Override
    public void onConnectionSuspended(int i) {

    }


    //This is for permission checking for laoding map
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    //Just copy paste.

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Log.e("connection", "Connection Failed");
    }
}
