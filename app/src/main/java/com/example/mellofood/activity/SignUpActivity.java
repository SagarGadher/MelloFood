package com.example.mellofood.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mellofood.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvReturnLogin;
    EditText etUserName, etPassword, etCPassword;
    Button btnSignUp;
    CheckBox cbAgree;
    String UserName, Password, CPassword, md5;
    boolean validation;
    String token;
    SharedPreferences sharedPreferences;
    static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_sign_up);
        activity = SignUpActivity.this;
        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), "");
        Log.v("fromSharedPreferance", token);

        tvReturnLogin = findViewById(R.id.tvReturnLogin);
        tvReturnLogin.setPaintFlags(tvReturnLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvReturnLogin.setOnClickListener(this);
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etCPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        cbAgree = findViewById(R.id.cbAgree);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvReturnLogin:
                onBackPressed();
                break;
            case R.id.btnSignUp:
                if (checkValiation()) {
                    UserName = etUserName.getText().toString();
                    Password = etPassword.getText().toString();
                    md5 = md5(Password);
                    Log.d("LoginDetailsE", UserName);
                    Log.d("LoginDetailsP", md5);
                    Log.d("LoginDetailsT", token + "hello");
                    new AsyncTaskOperation().execute();
                    Toast.makeText(this, "Done !", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean checkValiation() {
        UserName = etUserName.getText().toString();
        Password = etPassword.getText().toString();
        CPassword = etCPassword.getText().toString();
        if (TextUtils.isEmpty(UserName)) {
            etUserName.setError("User Name is Required !");
        } else if (TextUtils.isEmpty(Password)) {
            etPassword.setError("Password is Required !");
        } else if (TextUtils.isEmpty(Password)) {
            etCPassword.setError("Confirm Password is Required !");
        } else if (!Password.equals(CPassword)) {
            etCPassword.setError("Password and Confirm Password must be same !");
            etCPassword.setText("");
        } else {
            validation = true;
        }
        return validation;
    }

    public class AsyncTaskOperation extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            try {
                RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
                String URL = "http://10.0.0.10/mello/api/rest/v2/customer/profile/";
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("username", UserName);
                jsonBody.put("password", md5);
                jsonBody.put("newsSubscription", "subscribed");
                jsonBody.put("device_token", token);
                jsonBody.put("device_type", "Android");

                final String mRequestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_VOLLEY", response);
                        if (response.equals("200")) {
                            Toast.makeText(SignUpActivity.this, "You have successfully registered.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                            i.putExtra("Flag_Activity", true);
                            i.putExtra("UserName", UserName);
                            i.putExtra("Password", md5);
                            i.putExtra("ActivityName", "SignUpActivity");
                            startActivity(i);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Something went wrong please try again latter.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_VOLLEY", error.toString());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        //params.put("Content-Type", "application/x-www-form-urlencoded");
                        params.put("Content-Type", "application/json");
                        return params;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {

                            responseString = String.valueOf(response.statusCode);

                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                mQueue.add(stringRequest);

            } catch (Exception e) {
                Log.d("LOG_TAG", e.getMessage());
            }
            return null;
        }
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
