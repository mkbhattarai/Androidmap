package com.example.maheshbhattarai.androidmap;

import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadrosid.lib.drawroutemap.DrawMarker;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

// my first comment
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
//here mention all interface need implement
{


    double mi;
    GoogleMap mMap;
    LatLng latLng;
    MarkerOptions marker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;


    EditText first_address, second_address;
    TextView showMiless;
    Button bnt_findMiles;

    public Double currentLatitute;
    public Double currentLongtitute;
    public Double destinationLatitute;
    public Double destinationLongtitute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this is for version permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //here this condition check for android version
            //before m->march melo version , if you are running android version before march melon ,
            //then you don't require permission
            //but if you are running later version after march melo
            //then you need to check permssion before loading map.
            checkLocationPermission(); //This is method for getting permission
            //will go to there
        }

        first_address = (EditText) findViewById(R.id.first_address);
        second_address = (EditText) findViewById(R.id.second_address);

        showMiless = (TextView) findViewById(R.id.showMiles);

        bnt_findMiles = (Button) findViewById(R.id.show_distance);


        //this is used for loading map in desing

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_image); //map_img id for fragment map, laready there in desing

        //this is loading map
        mapFragment.getMapAsync(this); //this will generate onMapReady method , is below


        bnt_findMiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (first_address.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter Location Address first", Toast.LENGTH_SHORT).show();
                } else if (second_address.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter Location Address second", Toast.LENGTH_SHORT).show();
                } else {
                    String strAddress = first_address.getText().toString();
                    String strAddress2 = second_address.getText().toString();


                    Geocoder coder2 = new Geocoder(MainActivity.this);
                    List<Address> latlonh_address2;

                    try {
                        // May throw an IOException
                        latlonh_address2 = coder2.getFromLocationName(strAddress2, 5);
                        if (latlonh_address2 == null) {
                            Toast.makeText(MainActivity.this, "second addres null", Toast.LENGTH_SHORT).show();
                        } else {
                            Address location = latlonh_address2.get(0);
                            destinationLatitute = location.getLatitude();
                            destinationLongtitute = location.getLongitude();

                            //showMiless.setText("Latitude : " + location.getLatitude() + "\n" + "Longtitude :" + location.getLongitude());

                        }

                    } catch (IOException ex) {

                        ex.printStackTrace();
                    }


                    Geocoder coder = new Geocoder(MainActivity.this);
                    List<Address> latlonh_address;

                    try {
                        // May throw an IOException
                        latlonh_address = coder.getFromLocationName(strAddress, 5);
                        if (latlonh_address == null) {
                            Toast.makeText(MainActivity.this, "first addres null", Toast.LENGTH_SHORT).show();
                        } else {
                            Address location = latlonh_address.get(0);
                            currentLatitute = location.getLatitude();
                            currentLongtitute = location.getLongitude();

                            //showMiless.setText("Latitude : " + location.getLatitude() + "\n" + "Longtitude :" + location.getLongitude());

                        }

                    } catch (IOException ex) {

                        ex.printStackTrace();
                    }


                    int Radius = 6371;// radius of earth in Km
                    double lat1 = currentLatitute;
                    double lat2 = destinationLatitute;
                    double lon1 = currentLongtitute;
                    double lon2 = destinationLongtitute;
                    double dLat = Math.toRadians(lat2 - lat1);
                    double dLon = Math.toRadians(lon2 - lon1);
                    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                            + Math.cos(Math.toRadians(lat1))
                            * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                            * Math.sin(dLon / 2);
                    double c = 2 * Math.asin(Math.sqrt(a));
                    double valueResult = Radius * c;
                    double km = valueResult / 1;
                    DecimalFormat newFormat = new DecimalFormat("####");
                    int kmInDec = Integer.valueOf(newFormat.format(km));
                    double meter = valueResult % 1000;
                    int meterInDec = Integer.valueOf(newFormat.format(meter));

                    //... Computation
                    mi = km * 0.621;
                    showMiless.setText("Miles is : " + mi);
                    Log.e("Distanc in miles : ", mi + " Miles");
                    Log.e("Radius Value", "" + valueResult + "   KM  " + kmInDec
                            + " Meter   " + meterInDec);



                    LatLng origin = new LatLng(currentLatitute, currentLongtitute);
                    LatLng destination = new LatLng(destinationLatitute, destinationLongtitute);
                    DrawRouteMaps.getInstance(MainActivity.this)
                            .draw(origin, destination, mMap);
                    DrawMarker.getInstance(MainActivity.this).draw(mMap, origin, R.drawable.map_pin, "");
                    DrawMarker.getInstance(MainActivity.this).draw(mMap, destination, R.drawable.map_pin, "");

                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(origin)
                            .include(destination).build();
                    Point displaySize = new Point();
                    getWindowManager().getDefaultDisplay().getSize(displaySize);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 180, 15));

                }

            }
        });


    }


    //this is map ready method
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;  //creating Map Instance
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //setting normal view , there is diferent view , but we have normal
        mMap.getUiSettings().setMyLocationButtonEnabled(false); // set false
        mMap.getUiSettings().setMapToolbarEnabled(false); //set false



        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //this is for getting permission if version is greater than march melo
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


    //This is for finding current locatin method
    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mLastLocation != null) {
            Log.e("location", "lat " + mLastLocation.getLatitude() + " long " + mLastLocation.getLongitude());
            Toast.makeText(MainActivity.this,"lat " + mLastLocation.getLatitude() + " long " + mLastLocation.getLongitude(),Toast.LENGTH_LONG).show();
            currentLatitute = mLastLocation.getLatitude();
            currentLongtitute = mLastLocation.getLongitude();

            if (currentLatitute != null && currentLongtitute != null) {
               
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

    //Just copy paster.

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
