package com.example.poly.alab1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.util.Xml;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherForecastActivity extends Activity {

    TextView    temperatureCurrent;
    TextView    temperatureMin;
    TextView    temperatureMax;
    TextView    windSpeed;
    ImageView   CurrentWeatherImage;
    ProgressBar ProgressBar;

    String weatherURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);


        temperatureCurrent = findViewById(R.id.current_textView);
        temperatureMin = findViewById(R.id.temp_min_textView);
        temperatureMax = findViewById(R.id.temp_max_textView);
        windSpeed = findViewById(R.id.wind_speed_textView);
        CurrentWeatherImage = findViewById(R.id.weather_imageView);
        ProgressBar = findViewById(R.id.ProgressBar);
        ForecastQuery forecast_Query = new ForecastQuery();
        forecast_Query.execute(weatherURL);

    }

    public static Bitmap getImage(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else
                return null;
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        String strTemperatureCurrent;
        String strTemperatureMin;
        String strTemperatureMax;
        String strWindSpeed;
        String iconName;
        String fileName;
        Bitmap imageCurrentWeather;


        @Override
        protected String doInBackground(String... args)
        {
           String weather_URL = args[0];
            //class URL represents a Uniform Resource Locator,
            // a pointer to a "resource" on the World Wide Web

//Get Bitmap from Url with HttpURLConnection
            try {
                // Given a string representation of a URL, sets up a connection and gets
                // an input stream.

                URL url = new URL(weather_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(conn.getInputStream(), null);
                    parser.nextTag();

                    while (parser.next() != XmlPullParser.END_DOCUMENT) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        String name = parser.getName();

                        if (name.equals("temperature")) {
                            strTemperatureCurrent = parser.getAttributeValue(null, "value");
                            SystemClock.sleep(1000);
                            publishProgress(25);
                            strTemperatureMin = parser.getAttributeValue(null, "min");
                            SystemClock.sleep(1000);
                            publishProgress(50);
                            strTemperatureMax = parser.getAttributeValue(null, "max");
                            SystemClock.sleep(1000);
                            publishProgress(75);
                        }
                        if (name.equals("speed")) {
                            strWindSpeed = parser.getAttributeValue(null, "value");
                            SystemClock.sleep(1000);
                           // publishProgress(75);
                        }
                        if (name.equals("weather")) {
                            iconName = parser.getAttributeValue(null, "icon");
                            String imageURL_Str = "http://openweathermap.org/img/w/" + iconName + ".png";
                            fileName = iconName + ".png";

                            File file = getBaseContext().getFileStreamPath(fileName);
                            if (file.exists()) {
                                //when this file exists
                                FileInputStream fis = null;

                                try {
                                    fis = openFileInput(iconName);   ;
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                imageCurrentWeather = BitmapFactory.decodeStream(fis);
                                Log.i("WeatherForecastActivity", "Found the image locally");

                            } else {
                                //downloading and saving  image
                                imageCurrentWeather = getImage(new URL(imageURL_Str));
                                Log.i("WeatherForecastActivity", "Can't find image locally, download it");
                                FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                                imageCurrentWeather.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                            }
                            if (imageCurrentWeather != null) {
                                publishProgress(100);
                            }
                        }
                    }
                } catch (XmlPullParserException ex) {
                    return null;
                }
                //object returned by doInBackground - incoming parameter for onPostExecute()
                //represents the result
                return (strTemperatureCurrent + " " + strTemperatureMin + " " + strTemperatureMax + " " + strWindSpeed);

            } catch (IOException ioe) {
                Log.i("IOException", " Problem appear");
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            ProgressBar.setVisibility(View.VISIBLE);
            //super.onProgressUpdate(value);
            ProgressBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            temperatureCurrent.setText("Current temperature: " + strTemperatureCurrent);
            temperatureMin.setText("Minimum temperature: " + strTemperatureMin);
            temperatureMax.setText("Maximum temperature: " + strTemperatureMax);
            windSpeed.setText("Wind speed: " + strWindSpeed);
            CurrentWeatherImage.setImageBitmap(imageCurrentWeather);
            ProgressBar.setVisibility(View.INVISIBLE);
        }

    }

}
