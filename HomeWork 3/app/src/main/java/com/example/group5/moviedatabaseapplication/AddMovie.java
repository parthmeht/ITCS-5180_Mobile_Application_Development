package com.example.group5.moviedatabaseapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.net.URL;

public class AddMovie extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("bc", "mc");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        final EditText name = findViewById(R.id.movieName);
        final EditText description = findViewById(R.id.movieDescription);
        final EditText imdb = findViewById(R.id.imdbEdit);
        final EditText year = findViewById(R.id.yearEdit);
        final SeekBar rating = findViewById(R.id.ratingSeekbar);
        final Spinner genreSpinner = findViewById(R.id.genreSpinner);
        final TextView seekbarValue = findViewById(R.id.seekbarValueTextView);
        seekbarValue.setText("0");
        Boolean flag = false;
        int p = 0;
        Button addMovie = findViewById(R.id.saveAddMovieButton);
        if (getIntent().getExtras() != null) {
            this.setTitle(R.string.editMovie);
            Movie m = (Movie) getIntent().getExtras().get("MOVIES");
            int position = (int) getIntent().getExtras().get("Position");
            name.setText(m.getName());
            description.setText(m.getDescription());
            imdb.setText(m.getMovieImdb());
            addMovie.setText("Save");
            year.setText(m.getMovieYear().toString());
            genreSpinner.setSelection(m.getMovieGenre());
            rating.setProgress(m.getMovieRating());
            Log.d("Position", String.valueOf(position) + m.getName());
            seekbarValue.setText(String.valueOf(rating.getProgress()));
            flag = true;
            p = position;
        }
        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarValue.setText(String.valueOf(rating.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final int finalP = p;
        final Boolean finalFlag = flag;
        addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().matches("") | name.getText().toString().length() > 50 | description.getText().toString().length() > 1000 | description.getText().toString().matches("") | imdb.getText().toString().matches("") | year.getText().toString().matches("") | Integer.parseInt(year.getText().toString()) < 1889 | Integer.parseInt(year.getText().toString()) > 2019 | !URLUtil.isValidUrl(imdb.getText().toString())) {
                    new EditTextValidation(name, 50, "Exceeded 50 character Limit", "Movie name can only have upto 50 characters", getApplicationContext());
                    new EditTextValidation(description, 1000, "Exceeded 1000 character Limit", "Movie Description can only have upto 1000 characters", getApplicationContext());
                    new EditTextValidation(name, "Please Enter Name");
                    new EditTextValidation(description, "Please Enter Description");
                    new EditTextValidation(imdb, "Please Enter Imdb");
                    new EditTextValidation(year, "Please Enter Year");
                    new EditTextValidation(year, "First movie was made in 1889", "You have taken Welcome to the future literally", 1889, 2019, getApplicationContext());
                    new EditTextValidation("Enter valid Url", imdb, "The IMDB Url is invalid", getApplicationContext());
                } else {
                    Log.d("Spinner", String.valueOf(genreSpinner.getSelectedItemId()));
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    Movie m = new Movie(name.getText().toString(), description.getText().toString(), imdb.getText().toString(), (int) genreSpinner.getSelectedItemId(), Integer.parseInt(year.getText().toString()), rating.getProgress());
                    i.putExtra("MOVIES", (Serializable) m);
                    if (finalFlag) {
                        i.putExtra("Position", finalP);
                        setResult(3, i);
                    } else
                        setResult(2, i);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
