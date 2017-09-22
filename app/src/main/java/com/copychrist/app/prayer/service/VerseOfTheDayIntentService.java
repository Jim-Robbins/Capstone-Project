package com.copychrist.app.prayer.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.copychrist.app.prayer.util.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import timber.log.Timber;

/**
 * Created by jim.robbins on 9/22/17.
 */

public class VerseOfTheDayIntentService extends IntentService {
    public static final String VERSE_IN = "votd_in";
    public static final String VERSE_OUT = "votd_out";
    public static final String BASE_URL = "www.something.com";
    public static final String STATUS_RUNNING = "running";
    public static final String STATUS_FINISHED = "finished";
    public static final String STATUS_ERROR = "error";

    public VerseOfTheDayIntentService() {
        super(VerseOfTheDayIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Timber.v("Verse Of the Day Service Started");

        String url = intent.getStringExtra("url");
        Timber.d(url);
        getVerseFromApi(url);

        Timber.v("Service Stopping!");
        this.stopSelf();
    }

    private void getVerseFromApi(String url) {
        // Make the call to the API
        String dataJsonStr = getJSONDataFromApiCall(url);

        // Parse results
        String verse = parseResultsJSON(dataJsonStr);

        // Notify the UI data is ready
        sendLocalBroadcastResult(STATUS_FINISHED, verse);
    }

    private String getJSONDataFromApiCall(String verseUrl) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String dataJsonStr = null;

        try {
            // Create the request to API, and open the connection
            URL url = new URL(verseUrl);

            // Create the request, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            dataJsonStr = buffer.toString();
        } catch (IOException e) {
            Timber.e("Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Timber.e("Error closing stream", e);
                }
            }
        }
        return dataJsonStr;
    }

    private String parseResultsJSON(String dataJsonStr) {
        String result;
        try {
            Gson gson = new Gson();
            VerseOfTheDay[] verses = gson.fromJson(dataJsonStr, VerseOfTheDay[].class);
            int verseNumber = Utils.getRandomIndex(verses.length-1);
            VerseOfTheDay verseOfTheDay = verses[verseNumber];
            result = verseOfTheDay.getVerse();
        } catch (Error e) {
            result = e.getMessage();
        }


        return result;
    }
    /**
     * Send local broadcast to notify listeners the background task is complete
     */
    private void sendLocalBroadcastResult(String resultKey, String verse) {
        Intent intent = new Intent(resultKey);
        intent.putExtra("verse",verse);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}

class VerseOfTheDay {
    public String book_name;
    public String book_id;
    public String book_order;
    public String chapter_id;
    public String chapter_title;
    public String verse_id;
    public String verse_text;
    public String paragraph_number;

    public String getVerse(){
       return book_name + " " + chapter_id + ":" + verse_id + "\n" + verse_text.replaceAll("\t","");
    }
}