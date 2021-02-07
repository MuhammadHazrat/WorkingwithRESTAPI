package com.example.android.workingwithrestapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button jokeButton, weatherButton, catButton;
    private TextView jokeTV, weatherTV;
    LinearLayout Layoutlinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        jokeButton = findViewById(R.id.btnJoke);
        weatherButton = findViewById(R.id.btnWeather);
        catButton = findViewById(R.id.btnCat);
        jokeTV = findViewById(R.id.tvJoke);
        weatherTV = findViewById(R.id.tvWeather);
        Layoutlinear = findViewById(R.id.linearLayout);

//        Joke Button
        jokeButton.setOnClickListener(v -> {

            Ion.with(this)
                    .load("http://api.icndb.com/jokes/random?limitTo=[nerdy]")
                    .asString()
                    .setCallback((e, result) -> {
                        Log.e("tag", result);
                        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                        try {
                            processChuckData(result);
                        }
                        catch (JSONException jsonException) {
                                Toast.makeText(this, "Data not fetched", Toast.LENGTH_SHORT).show();
                        }
                    });
            });

//        Weather Button
        weatherButton.setOnClickListener(v -> {
            Ion.with(this)
                    .load("http://api.openweathermap.org/data/2.5/weather?q=Lahore,pk&units=metric&appid=a23c33aebd49d457fa1169fd3c89f77d")
                    .asString()
                    .setCallback((e, result) -> {
                        Log.e("tag", result);
                        try {
                            processWeatherData(result);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    });
        });

        catButton.setOnClickListener(v -> {
            Ion.with(this)
                    .load("https://api.thecatapi.com/v1/images/search?limit=6")
                    .asString()
                    .setCallback((e, result) -> {
                        result = "{ images: " + result + " }";
                        Log.e("tag", result);
                        try {
                            processCatData(result);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    });
        });

    }

    private void processWeatherData(String result) throws JSONException {
        JSONObject mainTemperature = new JSONObject(result).getJSONObject("main");
        String temp = mainTemperature.getString("temp");
        String nameTemperature = new JSONObject(result).getString("name");
        weatherTV.setText(nameTemperature + ": Temperature = " + temp + "*C");

    }

    private void processCatData(String result) throws JSONException {

        Layoutlinear.removeAllViews();

        JSONArray images = new JSONObject(result).getJSONArray("images");

        for (int i=0; i<images.length(); i++) {
            String url = images.getJSONObject(i).getString("url");
            Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

            ImageView iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Layoutlinear.addView(iv);

            Picasso.get()
                    .load(url)
                    .into(iv);
        }
    }

    private void processChuckData(String result) throws JSONException {
        String joke = new JSONObject(result).getJSONObject("value").getString("joke");
        jokeTV.setText(joke);
    }
}