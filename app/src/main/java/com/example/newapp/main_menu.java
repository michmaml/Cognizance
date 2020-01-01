package com.example.newapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class main_menu extends AppCompatActivity {
    Boolean DEBUG;
    Button account;
    Button logOut;
    ArrayList<JSONObject> random_list=new ArrayList<JSONObject>();
    ArrayList<JSONObject> random_list1=new ArrayList<JSONObject>();
    ArrayList<JSONObject> random_list2=new ArrayList<JSONObject>();
    ArrayList<JSONObject> random_list3=new ArrayList<JSONObject>();
    ArrayList<String> ordered_top4;
    String username;
    int userId;
    private   Map<Item, Map<Item, Double>> diff = new HashMap<>();
    private   Map<Item, Map<Item, Integer>> freq = new HashMap<>();
    private   Map<User, HashMap<Item, Double>> inputData;
    public   Map<User, HashMap<Item, Double>> outputData = new HashMap<>();
    List<Item> items;
    Map<User, HashMap<Item, Double>> data = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.main_menu);
        username = MySingletonClass.getInstance().getUsername();
        userId = MySingletonClass.getInstance().getUserid();
        DEBUG = MySingletonClass.getInstance().getDebug();
        TextView welcomeTextView = (TextView)findViewById(R.id.welcome);
        welcomeTextView.setText("Welcome " + username);

        prepareData();
        //GetRecomActivityAndSetbutton();
        GetRandomActivityAndSetbutton();
        GetMonringActivitiesAndSetbutton();
        GetDaytimeActivitiesAndSetbutton();
        GetNightActivitiesAndSetbutton();
    }

    public void prepareData(){
        String activity_json = loadJSONFromAsset();
        try {
            JSONObject jsonObject = new JSONObject(activity_json);
            setItems(jsonObject);
            System.out.println("items.size()" + items.size());
            contact();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void GetRecomActivity(){
            System.out.println("data size:" + data.size());
            slopeOne(data);
            System.out.println("outputdata size:" + outputData.size());
            ArrayList<String> top4 = new ArrayList<>();
            for(Map.Entry<User, HashMap<Item, Double>> user: outputData.entrySet()){
                if(userId==user.getKey().getUserId()) {
                    Map<Item, Double> sorted_user = sortByValue(user.getValue());
                    int counter=0;
                    for (Item j : sorted_user.keySet()) {
                        if(counter>sorted_user.size()-5){
                            System.out.println(" " + j.getItemName() + " --> " + sorted_user.get(j).doubleValue());
                            top4.add(j.getItemName());
                        }
                        counter++;
                    }
                    break;
                }
            }
            ordered_top4 = reverseArrayList(top4);
    }
    public void SetRecommendButtons(){
            Button recom_button1 = (Button)findViewById(R.id.recom_button1);
            recom_button1.setText(ordered_top4.get(0));
            recom_button1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Bundle extras = new Bundle();
                    extras.putString("activity_name",ordered_top4.get(0));

                    Intent intent = new Intent(main_menu.this, exercise.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

            Button recom_button2 = (Button)findViewById(R.id.recom_button2);
            recom_button2.setText(ordered_top4.get(1));
            recom_button2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Bundle extras = new Bundle();
                    extras.putString("activity_name",ordered_top4.get(1));

                    Intent intent = new Intent(main_menu.this, exercise.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

            Button recom_button3 = (Button)findViewById(R.id.recom_button3);
            recom_button3.setText(ordered_top4.get(2));
            recom_button3.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Bundle extras = new Bundle();
                    extras.putString("activity_name",ordered_top4.get(2));

                    Intent intent = new Intent(main_menu.this, exercise.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

            Button recom_button4 = (Button)findViewById(R.id.recom_button4);
            recom_button4.setText(ordered_top4.get(3));
            recom_button4.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Bundle extras = new Bundle();
                    extras.putString("activity_name",ordered_top4.get(3));

                    Intent intent = new Intent(main_menu.this, exercise.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
    }
    public void GetRandomActivityAndSetbutton(){
        String activity_json = loadJSONFromAsset();
        try{
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
            random_list = getRandomElement(activity_list,1);

            Button start_button = (Button)findViewById(R.id.start);
            start_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                     
                    try {
                        extras.putString("activity_name",random_list.get(0).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
    }
    public void GetMonringActivitiesAndSetbutton(){
        String activity_json = loadJSONFromAsset();
        try{
            ArrayList<JSONObject> activity_list = new ArrayList<JSONObject>();
            JSONObject jsonObject = new JSONObject(activity_json);
            JSONArray ja = (JSONArray) jsonObject.get("monring_activities");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject temp = ja.getJSONObject(i);
                activity_list.add(temp);
            }

            random_list1 = getRandomElement(activity_list,4);

            Button morning_button1 = (Button)findViewById(R.id.morning_button1);
            morning_button1.setText(random_list1.get(0).getString("name"));
            morning_button1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                     
                    try {
                        extras.putString("activity_name",random_list1.get(0).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button morning_button2 = (Button)findViewById(R.id.morning_button2);
            morning_button2.setText(random_list1.get(1).getString("name"));
            morning_button2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                     
                    try {
                        extras.putString("activity_name",random_list1.get(1).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button morning_button3 = (Button)findViewById(R.id.morning_button3);
            morning_button3.setText(random_list1.get(2).getString("name"));
            morning_button3.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                     
                    try {
                        extras.putString("activity_name",random_list1.get(2).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button morning_button4 = (Button)findViewById(R.id.morning_button4);
            morning_button4.setText(random_list1.get(3).getString("name"));
            morning_button4.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                     
                    try {
                        extras.putString("activity_name",random_list1.get(3).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
    }
    public void GetDaytimeActivitiesAndSetbutton(){
        String activity_json = loadJSONFromAsset();
        try{
            ArrayList<JSONObject> activity_list = new ArrayList<JSONObject>();
            JSONObject jsonObject = new JSONObject(activity_json);
            JSONArray ja = (JSONArray) jsonObject.get("daytime_activities");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject temp = ja.getJSONObject(i);
                activity_list.add(temp);
            }

            random_list2 = getRandomElement(activity_list,4);

            Button day_button1 = (Button)findViewById(R.id.day_button1);

            day_button1.setText(random_list2.get(02).getString("name"));
            day_button1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                     
                    try {
                        extras.putString("activity_name",random_list2.get(0).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button day_button2 = (Button)findViewById(R.id.day_button2);
            day_button2.setText(random_list2.get(1).getString("name"));
            day_button2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                     
                    try {
                        extras.putString("activity_name",random_list2.get(1).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button day_button3 = (Button)findViewById(R.id.day_button3);
            day_button3.setText(random_list2.get(2).getString("name"));
            day_button3.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                     
                    try {
                        extras.putString("activity_name",random_list2.get(2).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button day_button4 = (Button)findViewById(R.id.day_button4);
            day_button4.setText(random_list2.get(3).getString("name"));
            day_button4.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                     
                    try {
                        extras.putString("activity_name",random_list2.get(3).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
    }
    public void GetNightActivitiesAndSetbutton(){
        String activity_json = loadJSONFromAsset();
        try{
            ArrayList<JSONObject> activity_list = new ArrayList<JSONObject>();
            JSONObject jsonObject = new JSONObject(activity_json);
            JSONArray ja = (JSONArray) jsonObject.get("night_activities");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject temp = ja.getJSONObject(i);
                activity_list.add(temp);
            }

            random_list3 = getRandomElement(activity_list,4);

            Button night_button1 = (Button)findViewById(R.id.night_button1);
            night_button1.setText(random_list3.get(0).getString("name"));
            night_button1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                     
                    try {
                        extras.putString("activity_name",random_list3.get(0).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button night_button2 = (Button)findViewById(R.id.night_button2);
            night_button2.setText(random_list3.get(1).getString("name"));
            night_button2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                     
                    try {
                        extras.putString("activity_name",random_list3.get(1).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button night_button3 = (Button)findViewById(R.id.night_button3);
            night_button3.setText(random_list3.get(2).getString("name"));
            night_button3.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                     
                    try {
                        extras.putString("activity_name",random_list3.get(2).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button night_button4 = (Button)findViewById(R.id.night_button4);
            night_button4.setText(random_list3.get(3).getString("name"));
            night_button4.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                     
                    try {
                        extras.putString("activity_name",random_list3.get(3).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
    }

    public HashMap<Item, Double> sortByValue(HashMap<Item, Double> hm)    {
        // Create a list from elements of HashMap
        List<Map.Entry<Item, Double> > list =  new LinkedList<Map.Entry<Item, Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Item, Double> >() {
            public int compare(Map.Entry<Item, Double> o1,
                               Map.Entry<Item, Double> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Item, Double> temp = new LinkedHashMap<Item, Double>();
        for (Map.Entry<Item, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
    public ArrayList<String> reverseArrayList(ArrayList<String> alist){
        // Arraylist for storing reversed elements
        ArrayList<String> revArrayList = new ArrayList<String>();
        for (int i = alist.size() - 1; i >= 0; i--) {

            // Append the elements in reverse order
            revArrayList.add(alist.get(i));
        }

        // Return the reversed arraylist
        return revArrayList;
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
//    public Map<User, HashMap<Item, Double>> initializeData() throws JSONException {
//        contact();
//        System.out.println("data.size()" + data.size());
//        return data;
//    }
    private void setItems(JSONObject jo) throws JSONException{
        items = new ArrayList();
        items.clear();
        ArrayList<String> activity_list = new ArrayList<String>();
        JSONArray ja1 = (JSONArray) jo.get("monring_activities");
        JSONArray ja2 = (JSONArray) jo.get("night_activities");
        JSONArray ja3 = (JSONArray) jo.get("daytime_activities");
        for (int i = 0; i < ja1.length(); i++) {
            String temp = ja1.getJSONObject(i).getString("name");
            activity_list.add(temp);
        }
        for (int i = 0; i < ja2.length(); i++) {
            String temp = ja1.getJSONObject(i).getString("name");
            activity_list.add(temp);
        }
        for (int i = 0; i < ja3.length(); i++) {
            String temp = ja1.getJSONObject(i).getString("name");
            activity_list.add(temp);
        }
        for(String n : activity_list){
            //System.out.println("n: "+n);
            items.add(new Item(n));
        }
    }
    private void parse_JSON_String(String JSONString) {
        HashMap<Item, Double> records;
        Set<Item> newRecommendationSet;
        String currentUsername = MySingletonClass.getInstance().getUsername();
        int currentUserId = MySingletonClass.getInstance().getUserid();

        List<Integer> UserList = new ArrayList<Integer>();
        try{
            JSONObject rootJSONObj = new JSONObject(JSONString);
            JSONArray jsonArray = rootJSONObj.optJSONArray("userRating");
            if(jsonArray != null){
                for (int i=0; i<jsonArray.length(); i+=3) {
                    if(!UserList.contains(jsonArray.getInt(i))){
                        UserList.add(jsonArray.getInt(i));
                    };
                }
            }else{
                System.out.println("Ugh something's still wrong");
            }

            for(int userId : UserList){
                records = new HashMap<Item, Double>();
                if(jsonArray != null){
                    for (int i=0; i<jsonArray.length(); i+=4) {
                        if(jsonArray.getInt(i+1) == userId){
                            int activityId = jsonArray.getInt(i+2);
                            int rating = jsonArray.getInt(i+3);
                            if(records.containsKey(items.get(activityId))){
                                double old_rating = records.get(items.get(activityId));
                                records.put(items.get(activityId), (old_rating+rating)/2);
                            }else{
                                records.put(items.get(activityId), Double.valueOf(rating));
                            }
                        }
                    }
                }else{
                    System.out.println("Ugh something's still wrong");
                }
                data.put(new User(userId), records);
            }

            if(!UserList.contains(currentUserId)){
                records = new HashMap<Item, Double>();
                records.put(items.get((int) (Math.random() * items.size())), Math.random());
                data.put(new User(currentUserId), records);
            }

            int num_of_user = UserList.size();
            for (int i = 0; i < 10; i++) {
                records = new HashMap<Item, Double>();
                newRecommendationSet = new HashSet<>();
                for (int j = 0; j < 5; j++) {
                    newRecommendationSet.add(items.get((int) (Math.random() * items.size())));
                }
                for (Item item : newRecommendationSet) {
                    records.put(item, Math.random());
                }
                data.put(new User(num_of_user+i), records);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String ReadBufferedHTML(BufferedReader reader, char[] htmlBuffer, int bufSz) throws java.io.IOException{
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
    private String getJsonPage(String url) {
        HttpURLConnection conn_object = null;
        final int HTML_BUFFER_SIZE = 2*1024*1024;
        char htmlBuffer[] = new char[HTML_BUFFER_SIZE];

        try {
            System.out.println("Hi3");
            URL url_object = new URL(url);
            conn_object = (HttpURLConnection) url_object.openConnection();
            conn_object.setInstanceFollowRedirects(true);

            BufferedReader reader_list = new BufferedReader(new InputStreamReader(conn_object.getInputStream()));
            String HTMLSource = ReadBufferedHTML(reader_list, htmlBuffer, HTML_BUFFER_SIZE);
            reader_list.close();
            return HTMLSource;
        } catch (Exception e) {
            System.out.println("Hi4");
            return "Fail to login";
        } finally {
            System.out.println("Hi5");
            if (conn_object != null) {
                conn_object.disconnect();
            }
        }
    }
    private void contact() {
        final String url = "https://i.cs.hku.hk/~sekulski/userRating.php";
        System.out.println("Hi1");
        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            boolean success;
            String jsonString;

            @Override
            public String doInBackground(String... arg0) {
                // TODO Auto-generated method stub
                success = true;
                jsonString = getJsonPage(url);
                if (jsonString.equals("Fail to login")){
                    System.out.println("Fail to login");
                    success = false;
                }else{
                    System.out.println("Can login");
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                System.out.println("result: " + result);
                if (success){
                    System.out.println("on Post execute");
                    parse_JSON_String(jsonString);
                    GetRecomActivity();
                    SetRecommendButtons();
                }else{
                    System.out.println("error on contact");
                }
            }
        }.execute("");
    }

    public  void slopeOne(Map<User, HashMap<Item, Double>> inputData) {
        outputData.clear();
        System.out.println("Slope One - Before the Prediction\n");
        buildDifferencesMatrix(inputData);
        System.out.println("\nSlope One - With Predictions\n");
        predict(inputData);
    }
    private void buildDifferencesMatrix(Map<User, HashMap<Item, Double>> data) {
        for (HashMap<Item, Double> user : data.values()) {
            for (Entry<Item, Double> e : user.entrySet()) {
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<Item, Double>());
                    freq.put(e.getKey(), new HashMap<Item, Integer>());
                }
                for (Entry<Item, Double> e2 : user.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey()).intValue();
                    }
                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey()).doubleValue();
                    }
                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }
            }
        }
        for (Item j : diff.keySet()) {
            for (Item i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i).doubleValue();
                int count = freq.get(j).get(i).intValue();
                diff.get(j).put(i, oldValue / count);
            }
        }
        printData(data);
    }
    private void predict(Map<User, HashMap<Item, Double>> data) {
        HashMap<Item, Double> uPred = new HashMap<Item, Double>();
        HashMap<Item, Integer> uFreq = new HashMap<Item, Integer>();
        for (Item j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }
        for (Entry<User, HashMap<Item, Double>> e : data.entrySet()) {
            for (Item j : e.getValue().keySet()) {
                for (Item k : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(k).get(j).doubleValue() + e.getValue().get(j).doubleValue();
                        double finalValue = predictedValue * freq.get(k).get(j).intValue();
                        uPred.put(k, uPred.get(k) + finalValue);
                        uFreq.put(k, uFreq.get(k) + freq.get(k).get(j).intValue());
                    } catch (NullPointerException e1) {
                    }
                }
            }
            HashMap<Item, Double> clean = new HashMap<Item, Double>();
            for (Item j : uPred.keySet()) {
                if (uFreq.get(j) > 0) {
                    clean.put(j, uPred.get(j).doubleValue() / uFreq.get(j).intValue());
                }
            }
            for (Item j : items) {
                if (e.getValue().containsKey(j)) {
                    clean.put(j, e.getValue().get(j));
                } else {
                    clean.put(j, -1.0);
                }
            }
            outputData.put(e.getKey(), clean);
        }
        printData(outputData);
    }
    private void printData(Map<User, HashMap<Item, Double>> data) {
        for (User user : data.keySet()) {
            System.out.println(user.getUserId() + ":");
            print(data.get(user));
        }
    }
    private void print(HashMap<Item, Double> hashMap) {
        NumberFormat formatter = new DecimalFormat("#0.000");
        for (Item j : hashMap.keySet()) {
            System.out.println(" " + j.getItemName() + " --> " + formatter.format(hashMap.get(j).doubleValue()));
        }
    }

}