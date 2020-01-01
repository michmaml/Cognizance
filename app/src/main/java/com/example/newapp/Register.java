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

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    EditText usernameV;
    EditText emailV;
    EditText passwordV;
    EditText passwordCV;
    Button createAccount;
    ArrayList<String> usernameA = new ArrayList<String>();
    ArrayList<String> emailA = new ArrayList<String>();
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=\\S+$)" +           //no white spaces
            ".{6,}" +               //at least 6 characters
            "$");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameV = (EditText) findViewById(R.id.insertname);
        emailV = (EditText) findViewById(R.id.insertemail);
        passwordV = (EditText) findViewById(R.id.insertpassword);
        passwordCV = (EditText) findViewById(R.id.insertcpass);
        createAccount = (Button) findViewById(R.id.createaccount);

        createAccount.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                contact();
                if(isUsernameValidReg(usernameV) && isEmailValidReg(emailV)&& isPasswordValidReg(passwordV,passwordCV)) {
                    if (usernameAlreadyUsed())
                        startActivity(new Intent(Register.this, PopupLoginAlreadyExistsRegister.class));
                    else if (emailAlreadyUsed())
                        startActivity(new Intent(Register.this, PopupEmailAlreadyExistsRegister.class));
                    else if (userExistsReg(usernameV, emailV)) {
                        connect(usernameV, emailV, passwordV);
                        startActivity(new Intent(Register.this, MainActivity.class));
                    }
                }
            }
        });
    }

    //These three methods check if the input is valid from the application's side
    public Boolean isUsernameValidReg(EditText name) {
        if(TextUtils.isEmpty(name.getText())) {
            name.setError("Field cannot be empty");
            return false;
        }else{
            return true;
        }
    }

    public Boolean isEmailValidReg(EditText input){
        if(TextUtils.isEmpty(input.getText().toString())) {
            emailV.setError("Field cannot be empty");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(input.getText()).matches()){
            emailV.setError("Please provide a valid email");
            return false;
        }else{
            emailV.setError(null);
            return true;
        }
    }

    public Boolean isPasswordValidReg(EditText input, EditText verifyInput){
        if(TextUtils.isEmpty(input.getText())){
            passwordV.setError("Field cannot be empty");
            return false;
        }else if(!PASSWORD_PATTERN.matcher(input.getText().toString()).matches()){
            Toast.makeText(Register.this, "Password must be minimum of 6 characters and contain at least:" +
                            "\nONE lower case letter" +
                            "\nONE upper case letter" +
                            "\nONE digit",
                    Toast.LENGTH_LONG).show();
            passwordV.setError("Password too weak");
            return false;
        }else if(input.getText().toString().equals(verifyInput.getText().toString())){
            return true;
        }else
            passwordCV.setError("Passwords do not match");
            return false;
    }



    /*These methods fetch data from db; first four check if the credentials are unique
    userExistsReg for registration so that there are not 2 accounts with the same username/email,
    userExistsLog checks if the db contains the login/email and password*/

    private Boolean usernameAlreadyUsed(){
        parse_JSON_String_and_Switch_Activity("https://i.cs.hku.hk/~sekulski/tutorialMy2.php");
        Boolean b = false;
        if (usernameA.contains(usernameV.getText().toString()))
            b = true;
        return b;
    }

    private Boolean emailAlreadyUsed(){
        parse_JSON_String_and_Switch_Activity("https://i.cs.hku.hk/~sekulski/tutorialMy2.php");
        Boolean b = false;
        if (emailA.contains(emailV.getText().toString()))
            b = true;
        return b;
    }

    private Boolean userExistsReg(EditText username, EditText email){
        parse_JSON_String_and_Switch_Activity("https://i.cs.hku.hk/~sekulski/tutorialMy2.php");
        Boolean b = true;
        if (usernameA.contains(username.getText().toString()) && emailA.contains(email.getText().toString()))
            b = false;
        return b;
    }

    /*public Boolean userExistsLog(EditText input1, EditText input2){
        parse_JSON_String_and_Switch_Activity("https://i.cs.hku.hk/~sekulski/tutorialMy2.php");
        Boolean b = true;
        if (isUsernameValidReg(input1) && !TextUtils.isEmpty(input2.getText())){
            if(!usernameA.contains(input1.getText().toString()) && !passwordA.contains(input2.getText().toString()))
                b = false;
        }
        return b;
    }*/


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

    public void parse_JSON_String_and_Switch_Activity(String JSONString) {
        try {
            JSONObject rootJSONObj = new JSONObject(JSONString);
            JSONArray jsonArray = rootJSONObj.optJSONArray("cognizance");
            if(jsonArray != null){
                for (int i=0; i<jsonArray.length(); i+=3) {
                    String usernames = jsonArray.getString(i);
                    usernameA.add(usernames);
                }
                for (int i=1; i<jsonArray.length(); i+=3) {
                    String emails = jsonArray.getString(i);
                    emailA.add(emails);
                }
            }else{
                System.out.println("Ugh something's still wrong");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void connect(EditText u, EditText e, EditText p){
        final ProgressDialog pdialog = new ProgressDialog(this);

        pdialog.setCancelable(false);
        String name = u.getText().toString();
        String email = e.getText().toString();
        String password = p.getText().toString();

        final String url = "https://i.cs.hku.hk/~sekulski/tutorialMy2.php" + (name.isEmpty() ? "" : "?action=insert&username=" + android.net.Uri.encode(name, "UTF-8") +
                "&email=" + android.net.Uri.encode(email, "UTF-8") + "&password=" + android.net.Uri.encode(password, "UTF-8"));

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
                if (jsonString.equals("Fail to login"))
                    success = false;
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (success){
                    parse_JSON_String_and_Switch_Activity(jsonString);
                    if(userExistsReg(usernameV,emailV))
                        startActivity(new Intent(Register.this, MainActivity.class));
                } else {
                    alert( "Error", "Fail to connect" );
                }
                pdialog.hide();
            }
        }.execute("");
    }

    private void contact() {
        final String url = "https://i.cs.hku.hk/~sekulski/tutorialMy2.php";

        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
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