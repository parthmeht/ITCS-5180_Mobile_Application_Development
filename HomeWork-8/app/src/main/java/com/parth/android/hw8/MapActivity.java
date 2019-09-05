package com.parth.android.hw8;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);
        if (getIntent().getExtras() != null) {
            trip = (Trip) getIntent().getExtras().getSerializable(TripActivity.TRIP);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (int i = 0; i < trip.restaurants.size() - 1; i++) {
            Restaurant restaurant1 = trip.restaurants.get(i);
            Restaurant restaurant2 = trip.restaurants.get(i + 1);
            LatLng src = new LatLng(restaurant1.lat, restaurant1.lng);
            LatLng dest = new LatLng(restaurant2.lat, restaurant2.lng);
            mMap.addMarker(new MarkerOptions().position(src).title(restaurant1.name));
            mMap.addMarker(new MarkerOptions().position(dest).title(restaurant2.name));
            boundsBuilder.include(src);
            boundsBuilder.include(dest);
            mMap.addPolyline(
                    new PolylineOptions().add(
                            new LatLng(src.latitude, src.longitude),
                            new LatLng(dest.latitude, dest.longitude)
                    ).width(2).color(Color.BLUE).geodesic(true)
            );
        }
        final LatLngBounds latLngBounds = boundsBuilder.build();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 150));
            }
        });

    }
}
