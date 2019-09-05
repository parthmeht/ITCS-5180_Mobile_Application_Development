package com.parth.android.hw8;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TripAdapter extends ArrayAdapter<Trip> {

    FirebaseDatabase database;
    DatabaseReference myRef;
    TripListener listener;
    FirebaseUser user;
    public TripAdapter(Context context, int resource, List<Trip> objects, TripListener listener, FirebaseUser user) {
        super(context, resource, objects);
        this.listener = listener;
        this.user = user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Trip trip = getItem(position);
        final ViewHolder viewHolder;
        if(convertView == null){ //if no view to re-use then inflate a new one
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trip_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = convertView.findViewById(R.id.trip_name_text_view);
            viewHolder.textViewCity = convertView.findViewById(R.id.destination_city_text_view);
            viewHolder.mapButton = convertView.findViewById(R.id.map_image_button);
            viewHolder.deleteButton = convertView.findViewById(R.id.delete_button);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Trips");
        viewHolder.textViewName.setText(trip.name);
        viewHolder.textViewCity.setText(trip.city);
        viewHolder.mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.mapsPath(trip);
            }
        });

        if (user!=null && !trip.userId.equalsIgnoreCase(user.getUid()))
            viewHolder.deleteButton.setVisibility(View.INVISIBLE);

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(trip.id).removeValue();
            }
        });

        return convertView;
    }

    //View Holder to cache the views
    private static class ViewHolder{
        TextView textViewName;
        TextView textViewCity;
        ImageButton mapButton;
        ImageButton deleteButton;
    }

    interface TripListener{
        void mapsPath(Trip trip);
    }
}
