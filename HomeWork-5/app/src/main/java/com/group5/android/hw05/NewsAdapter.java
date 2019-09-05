package com.group5.android.hw05;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        News news = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.headline, parent, false);
        TextView textView = convertView.findViewById(R.id.sourceName);
        TextView sourceTitle = convertView.findViewById(R.id.sourceTitle);
        TextView publishedAt = convertView.findViewById(R.id.publishedAt);
        textView.setText(news.getAuthor());
        Log.d("GetAuthor", news.getAuthor());
        ImageView imageView = convertView.findViewById(R.id.imageView);
        if (news.utlToImage != null)
            Picasso.get().load(news.utlToImage).into(imageView);
        sourceTitle.setText(news.getSourcetitle());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = format.parse(news.getPublishedAt().split("T")[0]);
            format = new SimpleDateFormat("dd MMM yyyy");
            publishedAt.setText(format.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
