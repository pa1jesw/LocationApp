package com.pa1.locationapp;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

public class Distance extends AppCompatActivity{

    EditText etFrom,etTo;
    Button btnCal;
    TextView tvRes;
    Double srcLat,srcLongt,destLat,destLongt;
    GoogleApiClient mLocation;
    Location mLastLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);
        tvRes=findViewById(R.id.tvResult);
        btnCal= findViewById(R.id.btnCal);
        etFrom=findViewById(R.id.etStart);
        etTo=findViewById(R.id.etDest);



        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = etFrom.getText().toString().trim();
                String to = etTo.getText().toString().trim();
                if(from == "")
                {
                    etFrom.setError("Please Enter Location");
                }
                if(to == "")
                {
                    etTo.setError("Please Enter Location");
                }
                else
                {
                    Geocoder geocoder= new Geocoder(getApplicationContext(), Locale.ENGLISH);


                    try{
                        List<Address> srcAd = geocoder.getFromLocationName(from,1);
                        List<Address>  destAd = geocoder.getFromLocationName(to,1);

                        Address srcAda = srcAd.get(0);
                        Address Dest = destAd.get(0);

                        srcLat = srcAda.getLatitude();
                        destLat = Dest.getLatitude();
                        srcLongt = srcAda.getLongitude();
                        destLongt= Dest.getLongitude();

                        tvRes.setText(from + " & "+ to+" is \n"+getDistancce());

                    }catch (Exception e){
                        Toast.makeText(Distance.this, ""+e, Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    private String getDistancce() {
        Location loc1= new Location("Source");
        loc1.setLongitude(srcLongt);
        loc1.setLatitude(srcLat);
        Location loc2= new Location("Destni");
        loc2.setLongitude(destLongt);
        loc2.setLatitude(destLat);

        float dist = loc1.distanceTo(loc2);
        float km = dist/1000f;
        String sm = String.format("%.3f",km);
        return sm+"KM ";
    }

}
