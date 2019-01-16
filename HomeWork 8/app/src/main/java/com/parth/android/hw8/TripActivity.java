package com.parth.android.hw8;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripActivity extends AppCompatActivity implements TripAdapter.TripListener {
    private static final String TAG = "demo";
    public static final String TRIP = "TRIP";
    FirebaseUser user;
    ListView listView;
    TripAdapter tripAdapter;
    ArrayList<Trip> trips = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        listView = findViewById(R.id.trip_list_view);
        if (getIntent().getExtras() != null) {
            user = getIntent().getExtras().getParcelable(MainActivity.USER);
            Log.d(TAG, user.toString());
        }
        tripAdapter = new TripAdapter(TripActivity.this, R.layout.trip_item, trips, TripActivity.this, user);
        listView.setAdapter(tripAdapter);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Trips");
        findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(TripActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.add_trip_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TripActivity.this, AddTripActivity.class);
                intent.putExtra(MainActivity.USER, user);
                startActivity(intent);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trips.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Trip value = child.getValue(Trip.class);
                    trips.add(value);
                }
                tripAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void mapsPath(Trip trip) {
        Intent intent = new Intent(TripActivity.this, MapActivity.class);
        intent.putExtra(TRIP, trip);
        startActivity(intent);
    }
}
