package com.test.atn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends AppCompatActivity {
    private static final String REGISTER_URL = "http://pcpradeep22.16mb.com/register.php";
    private EditText name;
    private EditText username;
    private EditText password;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.registerButton);

        name.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name.setCursorVisible(true);
                    }
                }
        );

        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isNetworkAvailable()){
                            registerUser();
                        }else{
                            Toast.makeText(getBaseContext(), "No internet Bitch!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void registerUser() {
        String nameS = name.getText().toString().trim().toLowerCase();
        String usernameS = username.getText().toString().trim().toLowerCase();
        String passwordS = password.getText().toString();

        for(int i = 0; i < nameS.length(); i++){
            if(nameS.charAt(i) == ' '){
                Toast.makeText(this, "I only require your first name!", Toast.LENGTH_SHORT).show();
                return ;
            }
        }

        for(int i = 0; i < usernameS.length(); i++){
            if(usernameS.charAt(i) == ' '){
                Toast.makeText(this, "Username cannot contain any space!", Toast.LENGTH_SHORT).show();
                return ;
            }
        }

        for(int i = 0; i < passwordS.length(); i++){
            if(passwordS.charAt(i) == ' '){
                Toast.makeText(this, "Password cannot contain any space!", Toast.LENGTH_SHORT).show();
                return ;
            }
        }

        register(nameS,usernameS,passwordS);
    }

    private void register(String name, String username, String password) {
        String urlSuffix = "?name="+name+"&username="+username+"&password="+password;
        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getBaseContext(),s, Toast.LENGTH_SHORT).show();
                if(s.equalsIgnoreCase("successfully registered!!")){
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                }catch(Exception e){
                    return null;
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }

}
