package com.example.victory1908.maptest;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,EasyPermissions.PermissionCallbacks {

    private GoogleMap mMap;
    final int RC_LOCATION = 100;
    final int RC_POSITIVE = 100;
    final int RC_NEGATIVE = 100;
    String[] perms = {android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );

        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            mMap.setMyLocationEnabled(true);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "getString(R.string.camera_and_wifi_rationale)",
                    RC_LOCATION, perms);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }


    @AfterPermissionGranted(RC_LOCATION)
    private void showMyLocationButton() {

        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            mMap.setMyLocationEnabled(true);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "getString(R.string.camera_and_wifi_rationale)",
                    RC_LOCATION, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {


        Log.d("denied", "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // Handle negative button on click listener. Pass null if you don't want to handle it.
        DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Let's show a toast
                Toast.makeText(getApplicationContext(), "we need location permission", Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        };

        // (Optional) Check whether the user denied permissions and checked NEVER ASK AGAIN.
        // This will display a dialog directing them to enable the permission in app settings.
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(
                this,
                "Location permission required",
                android.R.string.ok,
                android.R.string.cancel,
                cancelButtonListener,
                perms);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Do something after user returned from app settings screen. User may be
        // changed/updated the permissions. Let's check whether the user has some permissions or not
        // after returned from settings screen
        if (requestCode == EasyPermissions.SETTINGS_REQ_CODE) {
            boolean locationPermission = EasyPermissions.hasPermissions(
                    this,perms
            );

            if (locationPermission) {
                mMap.setMyLocationEnabled(true);
            }else {
                Toast.makeText(getApplicationContext(), "Please enable location permission", Toast.LENGTH_SHORT)
                        .show();
                this.finish();
            }

            // Do something with the updated permissions
        }
    }

}
