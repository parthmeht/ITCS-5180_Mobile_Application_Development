package com.parth.android.inclassassignment10;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<LatLng> latLngArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String values = loadJSONFromAsset(MapsActivity.this);
        Log.d("Value", values);
        try {
            latLngArrayList = parse(values);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String loadJSONFromAsset(Activity activity) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open("trip.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public ArrayList<LatLng> parse(String jsonLine) throws JSONException {
        ArrayList<LatLng> arrayList = new ArrayList<>();
        JSONObject rootObj = new JSONObject(jsonLine);
        JSONArray jsonArray = rootObj.getJSONArray("points");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            LatLng latLng = new LatLng(obj.getDouble("latitude"), obj.getDouble("longitude"));
            arrayList.add(latLng);
        }
        return arrayList;
    }

    public void createPolynomial(GoogleMap map, LatLng p1, LatLng p2) {
        Polyline line = map.addPolyline(new PolylineOptions()
                .add(p1, p2)
                .width(5)
                .color(Color.RED));
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
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.addMarker(new MarkerOptions().position(latLngArrayList.get(0)).title("Start"));
        mMap.addMarker(new MarkerOptions().position(latLngArrayList.get(latLngArrayList.size() - 1)).title("End"));
        for (int i = 0; i < latLngArrayList.size() - 1; i++) {
            createPolynomial(mMap, latLngArrayList.get(i), latLngArrayList.get(i + 1));
        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (LatLng latLng : latLngArrayList) {
                    builder.include(latLng);
                }

                final LatLngBounds bounds = builder.build();

                try {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
