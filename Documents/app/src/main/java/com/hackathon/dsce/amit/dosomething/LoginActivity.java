package com.hackathon.dsce.amit.dosomething;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    private EditText email, password;
    private TextView login, signUp;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonLoginRequest;
    private String loginURL = UrlStrings.loginUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sp=getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putBoolean("isFirstRun", false).commit();

       // Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        //Log.d("ALERT", isFirstRun.toString());

        signUp = (TextView) findViewById(R.id.sup);
        email = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.pswrdd);
        login = (TextView) findViewById(R.id.lin);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(it);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAndPrintResponse();
            }
        });
    }

    private void sendAndPrintResponse()
    {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue(this);
        JSONObject loginJsonObject = null;
        try {

            loginJsonObject = new JSONObject().put("Email", email.getText()).put("Password", password.getText());
        }catch (Exception e){e.printStackTrace();}

        jsonLoginRequest = new JsonObjectRequest(Request.Method.POST, loginURL, loginJsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ALERT !!", response.toString());
                loading.dismiss();
                parseJsonResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                error.printStackTrace();
                showCannotConnectDialog();
                Log.i("ALERT ERROR!!", error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        requestQueue.add(jsonLoginRequest);
    }

    private void showCannotConnectDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Cannot connect")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Returns back to previous page
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void parseJsonResponse(JSONObject response)
    {
        String responseStringLogin = "", responseStringActivated = "";
        try{

            //responseStringActivated = response.getString("Activated");
            responseStringLogin = response.getString("Login");
        }catch (Exception e){e.printStackTrace();}

        if(responseStringLogin.equals("Unsuccessful")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Incorrect Credentials")
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Returns back to previous page
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return;
        }

        try {

            User.setEmail(response.getString("Email"));
            User.setPassword(response.getString("Password"));
            User.setPhoneNum(response.getString("Phone"));
            User.setUserName(response.getString("Name"));
            Toast.makeText(this,"Logged in as" + response.getString("Email"), Toast.LENGTH_LONG).show();
            User.setIsLoggedin(true);
            //Storing data locally
            SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putString("Name",User.getUserName());
            Ed.putString("Password",User.getPassword());

            Ed.putString("Email", User.getEmail());
            Ed.putString("PhoneNum", User.getPhoneNum());
            Ed.putBoolean("isLoggedIn", User.getIsLoggedin());

            Ed.commit();

            Intent intent = new Intent(LoginActivity.this, TaskActivity.class);
            startActivity(intent);
            finish();

        }catch (Exception e){e.printStackTrace();}
    }
}
