package com.inosuctechnologies.linkup.ui;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inosuctechnologies.linkup.R;
import com.inosuctechnologies.linkup.ui.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    EditText Name, Email, Mobile, Password;
    Button btnSignup;
    TextView login_here;
    private static String SIGNUP_URL = "";
    Constants constants;
    CheckBox tAndC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle(R.string.sign_up);

        getSupportActionBar().hide();
        Name = findViewById(R.id.userName);
        Email = findViewById(R.id.et_email);
        Mobile = findViewById(R.id.et_mobile);
        Password = findViewById(R.id.et_password);
        login_here = findViewById(R.id.tv_signup_loginhere);
        login_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        tAndC = findViewById(R.id.terms_conditions);

        btnSignup = findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = Name.getText().toString();
                String userEmail = Email.getText().toString();
                String userMobile = Mobile.getText().toString();
                String userPassword = Password.getText().toString();


                // Pattern match for email id
                Pattern p = Pattern.compile(Constants.regEx);
                Matcher m = p.matcher(userEmail);


                //		Pattern match for Mobile No
                Pattern mobi = Pattern.compile(Constants.mobregEx);
                Matcher mob = mobi.matcher(userMobile);


                if (userName.equalsIgnoreCase("") || userName.length() >= 3 || userEmail.equalsIgnoreCase("") || userEmail.length() >= 8 ||
                        userMobile.equalsIgnoreCase("") || userMobile.length() == 10 || userPassword.equalsIgnoreCase("") || userPassword.length() >= 5) {

                    if (userName.equalsIgnoreCase("")) {
                        Name.setError("Please Enter Name ");
                    } else if (userEmail.equalsIgnoreCase("") || !m.find()) {
                        Email.setError("Please Enter Valid Email ");
                    } else if (userMobile.equalsIgnoreCase("") || !mob.find()) {
//                        Mobile.setError("Please Enter Valid Mobile Number ");
                    } else if (userPassword.equalsIgnoreCase("")) {
                        Password.setError("Please Enter Password ");
                    } else if (!tAndC.isChecked()) {
                        Toast.makeText(getApplicationContext(), "Please Check Terms and Conditions", Toast.LENGTH_LONG).show();

                    } else {
                        if (Constants.isOnline(getApplicationContext())) {
                            register(SIGNUP_URL, userName, userEmail, userMobile, userPassword);
                        } else {
                            Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Entries are Wrong", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void register(String signupUrl, final String getuserName, final String getuserEmail, final String getuserMobile, final String getuserPassword) {


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, signupUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.getInt("success") == 0) {

                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    } else if (jsonObject.getInt("success") == 1) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);

                    } else

                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> param = new HashMap<String, String>();

                param.put("name", getuserName);
                param.put("email", getuserEmail);
                param.put("mobile", getuserMobile);
                param.put("password", getuserPassword);
                return param;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
}