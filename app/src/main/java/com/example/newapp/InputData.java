package com.example.newapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class InputData {

//    protected    List<Item> items = Arrays.asList(new Item("Candy"), new Item("Drink"), new Item("Soda"), new Item("Popcorn"), new Item("Snacks"));
    static List<Item> items;
    static Map<User, HashMap<Item, Double>> data;
    public Map<User, HashMap<Item, Double>> initializeData(JSONObject jo) throws JSONException {

        setItems(jo);
        System.out.println("items.size()" + items.size());
        data = new HashMap<>();
        contact();
        System.out.println("data.size()" + data.size());
        return data;
    }

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
        System.out.println("JSONString " + JSONString);
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
                    for (int i=0; i<jsonArray.length(); i+=3) {
                        if(jsonArray.getInt(i) == userId){
                            int activityId = jsonArray.getInt(i+1);
                            int rating = jsonArray.getInt(i+2);
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
    private String ReadBufferedHTML(BufferedReader reader, char[] htmlBuffer, int bufSz) throws java.io.IOException
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
                    System.out.println("hi2");
                    parse_JSON_String(jsonString);
                }else{
                    System.out.println("error on contact");
                }

            }
        }.execute("");
    }

}
