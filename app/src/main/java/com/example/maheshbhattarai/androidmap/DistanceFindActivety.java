package com.example.maheshbhattarai.androidmap;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class DistanceFindActivety extends AppCompatActivity {

    EditText first_address, second_address;
    TextView showMiless;
    Button bnt_findMiles;
    double mi;
    public Double currentLatitute;
    public Double currentLongtitute;
    public Double destinationLatitute;
    public Double destinationLongtitute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_find);

        first_address = (EditText) findViewById(R.id.first_address);
        second_address = (EditText) findViewById(R.id.second_address);

        showMiless = (TextView) findViewById(R.id.showMiles);

        bnt_findMiles = (Button) findViewById(R.id.show_distance);

        bnt_findMiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (first_address.getText().toString().isEmpty()) {
                    Toast.makeText(DistanceFindActivety.this, "Enter Location Address first", Toast.LENGTH_SHORT).show();
                } else if (second_address.getText().toString().isEmpty()) {
                    Toast.makeText(DistanceFindActivety.this, "Enter Location Address second", Toast.LENGTH_SHORT).show();
                } else {
                    String strAddress = first_address.getText().toString();
                    String strAddress2 = second_address.getText().toString();


                    Geocoder coder2 = new Geocoder(DistanceFindActivety.this);
                    List<Address> latlonh_address2;

                    try {
                        // May throw an IOException
                        latlonh_address2 = coder2.getFromLocationName(strAddress2, 5);
                        if (latlonh_address2 == null) {
                            Toast.makeText(DistanceFindActivety.this, "second addres null", Toast.LENGTH_SHORT).show();
                        } else {
                            Address location = latlonh_address2.get(0);
                            destinationLatitute = location.getLatitude();
                            destinationLongtitute = location.getLongitude();

                            //showMiless.setText("Latitude : " + location.getLatitude() + "\n" + "Longtitude :" + location.getLongitude());

                        }

                    } catch (IOException ex) {

                        ex.printStackTrace();
                    }


                    Geocoder coder = new Geocoder(DistanceFindActivety.this);
                    List<Address> latlonh_address;

                    try {
                        // May throw an IOException
                        latlonh_address = coder.getFromLocationName(strAddress, 5);
                        if (latlonh_address == null) {
                            Toast.makeText(DistanceFindActivety.this, "first addres null", Toast.LENGTH_SHORT).show();
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

                }
            }
        });

    }
}
