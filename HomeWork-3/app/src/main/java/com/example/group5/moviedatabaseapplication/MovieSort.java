package com.example.group5.moviedatabaseapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MovieSort extends AppCompatActivity {

    private TextView sortHeaderTextView, titleValueTextView, genreValueTextView, ratingValueTextView, yearValueTextView, imdbValueTextView;
    private EditText descriptionViewText;
    private String[] movie_genre;
    private ImageButton first, last, previous, next;
    private Button finish;
    private ArrayList<Movie> movieArrayList;
    private static int i = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_movie);
        sortHeaderTextView = findViewById(R.id.sortHeaderTextView);
        titleValueTextView = findViewById(R.id.titleValueTextView);
        genreValueTextView = findViewById(R.id.genreValueTextView);
        ratingValueTextView = findViewById(R.id.ratingValueTextView);
        yearValueTextView = findViewById(R.id.yearValueTextView);
        imdbValueTextView = findViewById(R.id.imdbValueTextView);
        descriptionViewText = findViewById(R.id.descriptionViewText);
        first = findViewById(R.id.firstImageButton);
        last = findViewById(R.id.lastImageButton);
        previous = findViewById(R.id.previousImageButton);
        next = findViewById(R.id.nextImageButton);
        finish = findViewById(R.id.finishButton);
        descriptionViewText.setKeyListener(null);
        descriptionViewText.setFocusable(false);
        descriptionViewText.setCursorVisible(false);
        movie_genre = getResources().getStringArray(R.array.movie_genre);
        if (getIntent() != null && getIntent().getExtras() != null) {
            String tag = getIntent().getExtras().getString(MainActivity.SHOW_LIST_TAG);
            movieArrayList = (ArrayList<Movie>) getIntent().getSerializableExtra(MainActivity.MOVIES_KEY);
            Log.d("moviesSort", movieArrayList.toString());
            Log.d("moviesSort", "tag: " + tag);
            Movie movie = null;
            if ("YEAR".equalsIgnoreCase(tag)) {
                this.setTitle(R.string.moviesByYear);
                sortHeaderTextView.setText(R.string.moviesByYear);
                Collections.sort(movieArrayList, new CompareYear());
            } else if ("RATING".equalsIgnoreCase(tag)) {
                this.setTitle(R.string.moviesByRating);
                sortHeaderTextView.setText(R.string.moviesByRating);
                Collections.sort(movieArrayList, new CompareRating());
            } else {
                Toast.makeText(sortHeaderTextView.getContext(), "Invalid Input", Toast.LENGTH_LONG).show();
            }
            Display();
        }

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;
                Display();
            }
        });

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = movieArrayList.size() - 1;
                Display();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i < movieArrayList.size() - 1) {
                    ++i;
                    Display();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 0) {
                    --i;
                    Display();
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("moviesSort", "Inside OnPause");
        i = 0;
    }

    public void Display() {
        Movie movie = movieArrayList.get(i);
        if (movie != null) {
            titleValueTextView.setText(movie.getName());
            genreValueTextView.setText(movie_genre[movie.getMovieGenre()]);
            ratingValueTextView.setText(movie.getMovieRating().toString() + "/5");
            yearValueTextView.setText(movie.getMovieYear().toString());
            imdbValueTextView.setText(movie.getMovieImdb());
            descriptionViewText.setText(movie.getDescription());
        }
    }
}

class CompareYear implements Comparator<Movie> {

    @Override
    public int compare(Movie o1, Movie o2) {
        return o1.getMovieYear() - o2.getMovieYear();
    }
}

class CompareRating implements Comparator<Movie> {

    @Override
    public int compare(Movie o1, Movie o2) {
        return o2.getMovieRating() - o1.getMovieRating();
    }
}
