package com.pa1.locationapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    TextView tvLoc,tvTime;
    Button btnShare, btnDistance;

    GoogleApiClient mLocationClient;
    Location mLastLoc;

    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to Exit.?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }
        );
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        builder.setTitle("Exit Confirm");
        builder.show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLoc= findViewById(R.id.tvLocation);

        btnShare=findViewById(R.id.btnShare);
        btnDistance= findViewById(R.id.btnDistance);

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this).addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        mLocationClient=builder.build();

        btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),Distance.class);
                startActivity(i);

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent shr= new Intent(Intent.ACTION_SEND);
            shr.setType("text/plain");
            String shareBody = tvLoc.getText().toString();
            shr.putExtra(Intent.EXTRA_SUBJECT,"My address");
            shr.putExtra(Intent.EXTRA_TEXT,shareBody);
            startActivity(Intent.createChooser(shr,"Share Via"));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mLocationClient!=null)
        {
            mLocationClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLoc = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
        if(mLastLoc!=null)
        {
            double lat=mLastLoc.getLatitude();
            double longt= mLastLoc.getLongitude();

            Geocoder geocoder= new Geocoder(getApplicationContext(), Locale.ENGLISH);
            try{
                List<Address> adresss=geocoder.getFromLocation(lat,longt,1);
                if(adresss!=null)
                {
                    Address fa = adresss.get(0);
                    tvLoc.setText(fa.getAddressLine(0));
                }
                else
                {
                    tvLoc.setText("UMMMM Not foound");
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "ConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "ConnectionFailed", Toast.LENGTH_SHORT).show();
    }}
