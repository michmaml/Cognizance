package com.example.newapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText usernameView;
    EditText passwordView;
    Button loginbutton;
    Button register_btn;
    List<String> dbUsername = new ArrayList<String>();
    List<String> dbPassword = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contact();
        loginbutton = findViewById(R.id.login);
        register_btn = findViewById(R.id.register);
        usernameView = (EditText) findViewById(R.id.editText);
        passwordView = (EditText) findViewById(R.id.editText2);

        loginbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(MySingletonClass.getInstance().getDebug()){
                    MySingletonClass.getInstance().setUsername("Debug");
                    MySingletonClass.getInstance().setUserid(999);
                    Intent intent = new Intent(MainActivity.this, main_menu.class);
                    startActivity(intent);
                }else{
                    if (TextUtils.isEmpty(usernameView.getText()) || TextUtils.isEmpty(passwordView.getText())){
                        startActivity(new Intent(MainActivity.this, PopUpNoLoginInfoProvided.class));
                    }
                    else if (userExistsLog(usernameView, passwordView)){
                        Intent intent = new Intent(MainActivity.this, main_menu.class);
                        startActivity(intent);
                    }else{
                        startActivity(new Intent(MainActivity.this, PopupIfLoginOrPasswordIsInvalid.class));
                    }
                }

            }
        });

        register_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });
    }

    public Boolean userExistsLog(EditText input1, EditText input2){
        String json = "https://i.cs.hku.hk/~sekulski/tutorialMy2.php";
        parse_JSON_String_and_Switch_Activity(json);
        for(int i=0; i<dbUsername.size(); i++){
            System.out.println("check name: " + dbUsername.get(i));
            if(dbUsername.get(i).equals(input1.getText().toString())){
                System.out.println("check name true ");
                System.out.println("check password: " + dbPassword.get(i));
                if(dbPassword.get(i).equals(input2.getText().toString())){
                    System.out.println("check password true ");
                    MySingletonClass.getInstance().setUsername(dbUsername.get(i));
                    MySingletonClass.getInstance().setUserid(i);
                    return true;
                }
            }
        }

        return false;
    }


    /*Methods below could I guess be referenced from Register.java(apart from contact!!) but due to looooong hours of debugging
    I realised that too many variables would have to be adapted as login does not require sending data to db(as register does for instance).
    So I copied and modified them so that they work.
    */
    private String ReadBufferedHTML(BufferedReader reader, char [] htmlBuffer, int bufSz) throws java.io.IOException
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

    private void parse_JSON_String_and_Switch_Activity(String JSONString) {
        try {
            JSONObject rootJSONObj = new JSONObject(JSONString);
            JSONArray jsonArray = rootJSONObj.optJSONArray("cognizance");
            if(jsonArray != null){
                for (int i=0; i<jsonArray.length(); i+=3) {
                    String usernames = jsonArray.getString(i);
                    dbUsername.add(usernames);
                }
                for (int i=2; i<jsonArray.length(); i+=3) {
                    String passwords = jsonArray.getString(i);
                    dbPassword.add(passwords);
                }
            }else{
                System.out.println("Ugh something's still wrong");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getJsonPage(String url) {
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
            if (conn_object != null) {
                conn_object.disconnect();
            }
        }
    }

    private void contact() {
        final String url = "https://i.cs.hku.hk/~sekulski/tutorialMy2.php";

        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            boolean success;
            String jsonString;

            @Override
            public String doInBackground(String... arg0) {
                // TODO Auto-generated method stub
                success = true;
                jsonString = getJsonPage(url);
                if (jsonString.equals("Fail to login"))
                    success = false;
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (success)
                    parse_JSON_String_and_Switch_Activity(jsonString);
            }
        }.execute("");
    }
}
