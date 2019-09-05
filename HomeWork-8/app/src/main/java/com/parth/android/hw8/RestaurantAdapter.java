package com.parth.android.hw8;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class RestaurantAdapter extends ArrayAdapter<Restaurant> {

    RestaurantAdapterListener listener;

    public RestaurantAdapter(Context context, int resource, List<Restaurant> objects, RestaurantAdapterListener listener) {
        super(context, resource, objects);
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Restaurant restaurant = getItem(position);
        final ViewHolder viewHolder;
        if(convertView == null){ //if no view to re-use then inflate a new one
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.restaurant_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = convertView.findViewById(R.id.name_textView);
            viewHolder.textViewAddress = convertView.findViewById(R.id.address_textView);
            viewHolder.textViewRating = convertView.findViewById(R.id.rating_textView);
            viewHolder.checkBox = convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewName.setText(restaurant.name);
        viewHolder.textViewAddress.setText(restaurant.vicinity);
        viewHolder.textViewRating.setText("Rating: "+restaurant.rating);
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.checkBox.isChecked()){
                    listener.addRestaurant(restaurant);
                }else{
                    listener.deleteRestaurant(restaurant);
                }
            }
        });
        return convertView;
    }

    //View Holder to cache the views
    private static class ViewHolder{
        TextView textViewName;
        TextView textViewAddress;
        TextView textViewRating;
        CheckBox checkBox;
    }

    interface RestaurantAdapterListener{
        void addRestaurant(Restaurant restaurant);
        void deleteRestaurant(Restaurant restaurant);
    }
}
