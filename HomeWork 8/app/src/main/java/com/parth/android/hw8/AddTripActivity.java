package com.parth.android.hw8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddTripActivity extends AppCompatActivity implements RestaurantAdapter.RestaurantAdapterListener {
    private static final String TAG = "demo";
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    RestaurantAdapter restaurantAdapter;
    ListView listView;
    ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
    ArrayList<Restaurant> selectedRestaurant = new ArrayList<>();
    EditText trip_name_editText;
    Place place;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setTitle("Add Trip");
        if (getIntent().getExtras()!=null){
            user = getIntent().getExtras().getParcelable(MainActivity.USER);
            Log.d(TAG,user.toString());
        }

        listView = findViewById(R.id.restaurant_list_view);
        restaurantAdapter = new RestaurantAdapter(AddTripActivity.this, R.layout.restaurant_item, restaurantArrayList, this);
        listView.setAdapter(restaurantAdapter);
        trip_name_editText = findViewById(R.id.trip_name_editText);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Trips");
        findViewById(R.id.destination_city_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AutocompleteFilter filter = new AutocompleteFilter.Builder().setCountry("US").setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(filter).build(AddTripActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.add_trips_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trip_name_editText.getText().toString()==null || trip_name_editText.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(view.getContext(),"Enter Trip Name",Toast.LENGTH_LONG).show();
                } else if (place == null){
                    Toast.makeText(view.getContext(),"Enter a Destination city",Toast.LENGTH_LONG).show();
                } else if(selectedRestaurant.size() == 0){
                    Toast.makeText(view.getContext(), "Select Restaurants.", Toast.LENGTH_SHORT).show();
                }else if(selectedRestaurant.size() > 15) {
                    Toast.makeText(view.getContext(), "Select upto 15 Restaurants.", Toast.LENGTH_SHORT).show();
                }else{
                    String id = myRef.push().getKey();
                    Trip trip = new Trip(id, trip_name_editText.getText().toString(),user.getUid(), (String) place.getName(), place.getLatLng().latitude, place.getLatLng().longitude, selectedRestaurant);
                    myRef.child(id).setValue(trip);
                    Log.d("id",id);
                    Intent intent = new Intent(AddTripActivity.this, TripActivity.class);
                    intent.putExtra(MainActivity.USER, user);
                    startActivity(intent);
                    finish();
                }
            }
        });


        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                getRestaurantsList(place.getLatLng());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void viewList(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                restaurantAdapter.notifyDataSetChanged();
            }
        });

    }

    void getRestaurantsList(LatLng latLng) {
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latLng.latitude + "," + latLng.longitude + "&radius=1600&type=restaurant&keyword=cruise&key=" + getString(R.string.KEY))
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try  {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    restaurantArrayList.clear();
                    JSONObject root = new JSONObject(response.body().string());
                    JSONArray results = root.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);
                        Restaurant restaurant = new Restaurant();
                        restaurant.id = obj.getString("id");
                        restaurant.name = obj.getString("name");
                        restaurant.place_id = obj.getString("place_id");
                        restaurant.vicinity = obj.getString("vicinity");
                        restaurant.rating = obj.getInt("rating");
                        JSONObject geometry = obj.getJSONObject("geometry");
                        JSONObject loc = geometry.getJSONObject("location");
                        LatLng location = new LatLng(loc.getDouble("lat"),loc.getDouble("lng"));
                        restaurant.lat = location.latitude;
                        restaurant.lng = location.longitude;
                        restaurantArrayList.add(restaurant);
                        Log.d(TAG, restaurant.toString());
                    }
                    viewList();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void addRestaurant(Restaurant restaurant) {
        selectedRestaurant.add(restaurant);
    }

    @Override
    public void deleteRestaurant(Restaurant restaurant) {
        selectedRestaurant.remove(restaurant);
    }
}
