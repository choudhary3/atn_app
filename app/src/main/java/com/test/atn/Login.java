package com.test.atn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {
    public static final String LOGIN_CHECK = "LOGGED_IN";
    public static final String NAME = "NAME";
    private static final String LOGIN_URL = "http://pcpradeep22.16mb.com/login.php";
    String usernameS;
    String passwordS;
    private EditText username;
    private EditText password;
    private Intent intentLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        Button login = (Button) findViewById(R.id.login);
        intentLogin = new Intent(this, WelcomeUser.class);

        username.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        username.setCursorVisible(true);
                    }
                }
        );

        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isNetworkAvailable()){
                            usernameS = username.getText().toString().trim().toLowerCase();
                            passwordS = password.getText().toString();

                            for(int i = 0; i < usernameS.length(); i++){
                                if(usernameS.charAt(i) == ' '){
                                    Toast.makeText(getBaseContext(), "Username cannot contain any space!", Toast.LENGTH_SHORT).show();
                                    return ;
                                }
                            }

                            for(int i = 0; i < passwordS.length(); i++){
                                if(passwordS.charAt(i) == ' '){
                                    Toast.makeText(getBaseContext(), "Password cannot contain any space!", Toast.LENGTH_SHORT).show();
                                    return ;
                                }
                            }

                            loginUser(usernameS, passwordS);
                        }else{
                            Toast.makeText(getBaseContext(), "No internet Bitch!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        TextView register = (TextView) findViewById(R.id.register);
        final Intent intentRegister = new Intent(this, Register.class);

        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(intentRegister);
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

    private void loginUser(String username, String password) {
        String urlSuffix = "?username="+username+"&password="+password;
        class LoginUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.startsWith("W")){
                    Toast.makeText(getBaseContext(),"Success!", Toast.LENGTH_SHORT).show();
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean(LOGIN_CHECK, true);
                    editor.putString(NAME, s.substring(1,2).toUpperCase() + s.substring(2));
                    editor.commit();
                    startActivity(intentLogin);
                }else{
                    Toast.makeText(getBaseContext(),s, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(LOGIN_URL+s);
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

        LoginUser lu = new LoginUser();
        lu.execute(urlSuffix);
    }

}
