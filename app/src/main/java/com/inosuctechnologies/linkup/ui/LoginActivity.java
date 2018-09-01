package com.inosuctechnologies.linkup.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class LoginActivity extends AppCompatActivity {
    EditText mobileNo, password;
    TextView signupHere, forgetPass;
    Button loginBtn;
    private static String LOGIN_URL = "http://linkup.ist.co.zw/mobile/signup.php";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.log_in);
        getSupportActionBar().hide();
        mobileNo = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        signupHere = findViewById(R.id.tv_signuphere);
        loginBtn = findViewById(R.id.btn_login);
        signupHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMobile = mobileNo.getText().toString();
                String userPass = password.getText().toString();

                if (userMobile.equalsIgnoreCase("") ||
                        userPass.equalsIgnoreCase("") || userPass.length() >= 5) {
                    if (userMobile.equalsIgnoreCase("")) {
                        mobileNo.setError("Enter your mobile No ");
                    } else if (userPass.equalsIgnoreCase("")) {
                        password.setError("Enter your password");
                    } else {
                        if (Constants.isOnline(getApplicationContext())) {
                            login(LOGIN_URL, userMobile, userPass);
                        } else {
                            Toast.makeText(getApplicationContext(), "No internet Connection",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Entries are Wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void login(String loginUrl, final String login_userMob, final String login_userPass) {
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getInt("success") == 1) {

                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"),
                                Toast.LENGTH_LONG).show();

//                        JSONObject jsonObjectInfo = jsonObject.getJSONObject("User");
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        intent.putExtra("name", jsonObjectInfo.getString("name"));
//                        intent.putExtra("email", jsonObjectInfo.getString("email"));
//                        intent.putExtra("mobile", jsonObjectInfo.getString("mobile"));
//                        startActivity(intent);
                    } else
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"),
                                Toast.LENGTH_LONG).show();
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
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("mobile", login_userMob);
                param.put("password", login_userPass);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.
                DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }
}