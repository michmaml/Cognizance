package com.example.newapp;

import android.content.Context;
import android.os.AsyncTask;

public class BackgroundWorker extends AsyncTask<String,Void,Void> {
    Context context;
    BackgroundWorker (Context ctx){
        context = ctx;
    }


    @Override
    protected Void doInBackground(String... params) {
        String type = params[0];
        if(type.equals("login")) {

        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }
}
