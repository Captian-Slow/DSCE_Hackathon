package com.hackathon.dsce.amit.dosomething;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

public class SignupActivity extends Activity {

    private EditText emailEditText, phoneEditText, passwordEditText, userNameEditText;
    private TextView loginTextView, signUpTextView;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonLoginRequest;
    private String loginURL = UrlStrings.registerUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpTextView = (TextView) findViewById(R.id.sup);
        loginTextView = (TextView) findViewById(R.id.lin);
        userNameEditText = (EditText) findViewById(R.id.usrusr);
        passwordEditText = (EditText) findViewById(R.id.pswrdd);
        emailEditText = (EditText) findViewById(R.id.mail);
        phoneEditText = (EditText) findViewById(R.id.mobphone);

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(phoneEditText.getText().toString().length() != 10){

                    showWrongPhone();
                    return;
                }
                sendAndPrintResponse();
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent it = new Intent(SignupActivity.this, LoginActivity.class);
                //startActivity(it);
                finish();
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

            loginJsonObject = new JSONObject().put("Email", emailEditText.getText()).put("Password", passwordEditText.getText()).put("Phone", phoneEditText.getText()).put("Name", userNameEditText.getText());
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

    private void parseJsonResponse(JSONObject response)
    {
        String responseString = "";
        try{

            responseString = response.getString("Login");
        }catch (Exception e) {
            e.printStackTrace();}

        if(responseString.equals("Unsuccessful")){
            Toast.makeText(this, "Email ID already exists !", Toast.LENGTH_SHORT).show();
            return;
        }
        try {

            showEmailSent();
            User.setEmail(response.getString("Email"));
            User.setPassword(response.getString("Password"));
            User.setPhoneNum(response.getString("Phone"));
            User.setUserName(response.getString("Name"));
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            User.setIsLoggedin(true);
            startActivity(intent);
            finish();

        }catch (Exception e){e.printStackTrace();}
    }

    private void showWrongPhone(){
        Toast.makeText(this, "Please enter a valid Phone Number.", Toast.LENGTH_SHORT).show();
    }

    private void showEmailSent(){

        Toast.makeText(this, "A conformation link has been sent to your email ID. Please Log In after conforming your identity.", Toast.LENGTH_LONG).show();
    }
}
