package com.example.newapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class exercise extends AppCompatActivity {

    String username;
    int activityId;
    float rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise);

        String activity_name = getIntent().getExtras().getString("activity_name");
        username = MySingletonClass.getInstance().getUsername();
        System.out.println("exercise:"+username );
        try{
            String activity_json = loadJSONFromAsset();
            ArrayList<JSONObject> activity_list = new ArrayList<JSONObject>();
            JSONObject jsonObject = new JSONObject(activity_json);
            JSONArray ja1 = (JSONArray) jsonObject.get("monring_activities");
            JSONArray ja2 = (JSONArray) jsonObject.get("night_activities");
            JSONArray ja3 = (JSONArray) jsonObject.get("daytime_activities");
            for (int i = 0; i < ja1.length(); i++) {
                JSONObject temp = ja1.getJSONObject(i);
                activity_list.add(temp);
            }
            for (int i = 0; i < ja2.length(); i++) {
                JSONObject temp = ja1.getJSONObject(i);
                activity_list.add(temp);
            }
            for (int i = 0; i < ja3.length(); i++) {
                JSONObject temp = ja1.getJSONObject(i);
                activity_list.add(temp);
        }
            String intro = "";
            for( JSONObject jo : activity_list){
                if(activity_name.contains(jo.getString("name"))){
                    intro = jo.getString("intro");
                    activityId = jo.getInt("id");
                    break;
                }
            }

            TextView title = (TextView)findViewById(R.id.title);
            title.setText(activity_name);
            TextView description = (TextView)findViewById(R.id.description);
            description.setText(intro);

            Button submit_button = (Button)findViewById(R.id.submit_button);
            final RatingBar ratingBar = findViewById(R.id.ratingBar);
            submit_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    rating = Float.valueOf(ratingBar.getRating());
                    connect();

//                    Intent intent = new Intent(exercise.this, main_menu.class);
//                    startActivity(intent);
                }
            });
        }catch (JSONException err){
            Log.d("Error", err.toString());
        }

    }

    public ArrayList<JSONObject> getRandomElement(ArrayList<JSONObject> list, int totalItems){
        Random rand = new Random();

        // create a temporary list for storing
        // selected element
        ArrayList<JSONObject> newList = new ArrayList<>();
        for (int i = 0; i < totalItems; i++) {

            // take a raundom index between 0 to size
            // of given List
            int randomIndex = rand.nextInt(list.size());

            // add element in temporary list
            newList.add(list.get(randomIndex));

            // Remove selected element from orginal list
            list.remove(randomIndex);
        }
        return newList;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("json.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String ReadBufferedHTML(BufferedReader reader, char [] htmlBuffer, int bufSz) throws java.io.IOException
    {
        htmlBuffer[0] = '\0';
        int offset = 0;
        do {
            int cnt = reader.read(htmlBuffer, offset, bufSz - offset);
            if (cnt > 0) {
                offset += cnt;
            } else {
                break;
            }
        } while (true);
        return new String(htmlBuffer);
    }

    public void connect(){
        final ProgressDialog pdialog = new ProgressDialog(this);

        pdialog.setCancelable(false);
        int userid = MySingletonClass.getInstance().getUserid();
        System.out.println("userid "+ Integer.toString(userid) + " " + Integer.toString(activityId) + " " + Float.toString(rating));
        //final String url = "https://i.cs.hku.hk/~sekulski/userRating.php?action=insert&user_id=" + android.net.Uri.encode(Integer.toString(userid), "UTF-8" + "&activities_id=" + android.net.Uri.encode(Integer.toString(activityId), "UTF-8") + "&rating=" + android.net.Uri.encode(Float.toString(rating), "UTF-8"));
        //final String url = "https://i.cs.hku.hk/~sekulski/userRating.php?action=insert&user_id=88&activities_id=5&rating=7.5";
        final String url = "https://i.cs.hku.hk/~sekulski/userRating.php?action=insert&user_id=" + userid + "&activities_id=" + activityId+ "&rating=" + rating;

        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            boolean success;
            String jsonString;

            @Override
            public String doInBackground(String... arg0) {
                // TODO Auto-generated method stub
                success = true;
                //pdialog.setMessage("Before ...");
                //pdialog.show();
                jsonString = getJsonPage(url);
                System.out.println("jsonString: " + jsonString);
                if (jsonString.equals("Fail to login"))
                    success = false;
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (success){
                    System.out.println("Succesfully add rating");
                    startActivity(new Intent(exercise.this, main_menu.class));
                } else {
                    alert( "Error", "Fail to connect" );
                }
                pdialog.hide();
            }
        }.execute("");
    }

    public String getJsonPage(String url) {
        HttpURLConnection conn_object = null;
        final int HTML_BUFFER_SIZE = 2*1024*1024;
        char htmlBuffer[] = new char[HTML_BUFFER_SIZE];

        try {
            URL url_object = new URL(url);
            conn_object = (HttpURLConnection) url_object.openConnection();
            conn_object.setInstanceFollowRedirects(true);

            BufferedReader reader_list = new BufferedReader(new InputStreamReader(conn_object.getInputStream()));
            String HTMLSource = ReadBufferedHTML(reader_list, htmlBuffer, HTML_BUFFER_SIZE);
            reader_list.close();
            return HTMLSource;
        } catch (Exception e) {
            return "Fail to login";
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            if (conn_object != null) {
                conn_object.disconnect();
            }
        }
    }

    protected void alert(String title, String mymessage){
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton){}
                        }
                )
                .show();
    }
}