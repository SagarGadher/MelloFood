package com.example.mellofood.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mellofood.R;
import com.example.mellofood.database.TableContent;
import com.example.mellofood.database.UserDBHelper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rootLayout;
    ViewTreeObserver viewTreeObserver;
    Button btnSignUp, btnLogin;
    EditText etUserName, etPassword;
    TextView tvSkip, tvForgot;
    CheckBox cbAgree, ic_password;
    String UserName, Password,md5,token,login_token;
    Boolean validation = false;
    LoginButton btnFacebookLogin;
    CallbackManager callbackManager;
    JSONObject userObject;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_login);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        FirebaseApp.initializeApp(this);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), "");
        Log.v("fromSharedPreferance", token);
        login_token = sharedPreferences.getString(getString(R.string.LOGIN_TOKEN), "");
        Log.v("fromShared_login", login_token);
        if (!login_token.isEmpty() && login_token != null){
            Toast.makeText(this, "You are already Login", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this,OutletActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        Intent si = getIntent();
        boolean checkActivity = si.getBooleanExtra("Flag_Activity",false);
        if (checkActivity){
            UserName = getIntent().getStringExtra("UserName");
            md5 = getIntent().getStringExtra("Password");
            new AsyncTaskOperation1().execute();
        }
        final Intent i = new Intent(LoginActivity.this, OutletActivity.class);
        userObject = new JSONObject();

        rootLayout = findViewById(R.id.rootLayout);
        if (savedInstanceState == null) {
            doAnimation();
        }
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        tvForgot = findViewById(R.id.tvForgot);
        tvForgot.setOnClickListener(this);
        tvSkip = findViewById(R.id.tvSkip);
        tvSkip.setOnClickListener(this);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnFacebookLogin = findViewById(R.id.btnFacebookLogin);
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        ic_password = findViewById(R.id.ic_password);
        ic_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ic_password.setBackgroundResource(R.drawable.ic_show);
                    Log.d("Somthing", "hide");
                } else if (!isChecked) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ic_password.setBackgroundResource(R.drawable.ic_hide);
                    Log.d("Something", "show");
                }
            }
        });
        cbAgree = findViewById(R.id.cbAgree);
        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

        if (!loggedOut) {
            //Picasso.get().load(Profile.getCurrentProfile().getProfilePictureUri(200, 200)).into(imageView);
            Log.d("TAGUsername", "Username is: " + Profile.getCurrentProfile().getName());

            //Using Graph API
            startActivity(i);
            MainActivity.activity.finish();
            finish();
        }

        btnFacebookLogin.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();

        btnFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                insertUser(AccessToken.getCurrentAccessToken());
                Log.d("API123", loggedIn + " ??");
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedPreferences.edit();
                ed.putString(getString(R.string.LOGIN_TYPE), "FACEBOOK");
                ed.commit();
                startActivity(i);
                finish();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void insertUser(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");

                            new AsyncTaskOperation().execute(first_name,last_name,email);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                Log.d("Log_token_",token + "hello");
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.btnLogin:
                if (checkValiation()) {
                    Log.d("LoginDetailsE", UserName);
                    Log.d("LoginDetailsP", md5);
                    Log.d("LoginDetailsT",token + "hello");
                    new AsyncTaskOperation1().execute();
                    Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvSkip:
                startActivity(new Intent(this, OutletActivity.class));
                finish();
                break;
        }
    }


    private boolean checkValiation() {
        UserName = etUserName.getText().toString();
        Password = etPassword.getText().toString();
        md5 = md5(Password);
        if (TextUtils.isEmpty(UserName)) {
            etUserName.setError("User Name is Required !");
        } else if (TextUtils.isEmpty(Password)) {
            etPassword.setError("Password is Required !");
        } else {
            validation = true;
        }
        return validation;
    }

    private void doAnimation() {
        rootLayout.setVisibility(View.INVISIBLE);
        viewTreeObserver = rootLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int cx = rootLayout.getWidth() / 2;
                    int cy = rootLayout.getHeight() / 2;

                    float finalRadius = Math.max(rootLayout.getWidth(), rootLayout.getHeight());

                    // create the animator for this view (the start radius is zero)
                    Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, cx, cy, 0, finalRadius);
                    circularReveal.setDuration(1500).addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            switch (getIntent().getStringExtra("ActivityName")) {
                                case "SignUpActivity":
                                    SignUpActivity.activity.finish();
                                    break;
                                case "MainActivity":
                                    MainActivity.activity.finish();
                                    break;
                                case "OutletActivity":
                                    OutletActivity.activity.finish();
                                    break;
                                case "LandmarkActivity":
                                    OutletActivity.activity.finish();
                                    LandmarkActivity.activity.finish();
                                    break;
                            }
                        }
                    });

                    // make the view visible and start the animation
                    rootLayout.setVisibility(View.VISIBLE);
                    circularReveal.start();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }

    public class AsyncTaskOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String email = strings[2];
            Log.d("LoginMassage",""+email);
            UserDBHelper userDBHelper = new UserDBHelper(LoginActivity.this);
            SQLiteDatabase db = userDBHelper.getWritableDatabase();
            Cursor cursor = userDBHelper.viewUser(db);
            /*userDBHelper.insertLike(1,db);*/
            userDBHelper.insertUser(strings[0],strings[1],strings[2],"",db);
            while (cursor.moveToNext()) {
                String EMAIL =cursor.getString(cursor.getColumnIndex(TableContent.TableEntry.EMAIL));
                if (email.equals(EMAIL)){
                    Log.d("LoginMassage","Data Base has alrady this email");
                    break;
                }
                else {
                    Log.d("LoginMassage","Data inserted");
                    userDBHelper.insertUser(strings[0],strings[1],strings[2],null,db);
                }
            }
            userDBHelper.close();
            return null;
        }
    }

    public class AsyncTaskOperation1 extends AsyncTask<Void, Void, JSONObject> {
        JSONObject jsonObject = new JSONObject();
        String id,login_token_secret;
        @Override
        protected JSONObject doInBackground(Void... Void) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            try {
                String URL = "http://10.0.0.10/mello/authenticate?group_id=1&lang_code=en_US&email="+UserName+"&password="+md5+"&newsSubscription=subscribed&device_token="+token+"&device_type=Android&country_code=91";
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                // display response
                                jsonObject = response;
                                Log.d("LoginResponse", response.toString());
                                try {
                                    JSONObject j1= response.getJSONObject("data");
                                    id = j1.getString("id");
                                    login_token= j1.getString("oauth_token");
                                    login_token_secret = j1.getString("oauth_token_secret");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                                SharedPreferences.Editor ed = sharedPreferences.edit();
                                ed.putString(getString(R.string.LOGIN_TYPE), "CUSTOM");
                                ed.putString(getString(R.string.LOGIN_ID), id);
                                ed.putString(getString(R.string.LOGIN_TOKEN), login_token);
                                ed.putString(getString(R.string.LOGIN_TOKEN_SECRET), login_token_secret);
                                Log.v("fromShared_login", login_token);
                                ed.commit();

                                Intent i = new Intent(LoginActivity.this,OutletActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_VOLLEY", error.toString());
                            }
                        }
                );

                queue.add(getRequest);

            } catch (Exception e) {
                Log.d("LOG_TAG",e.getMessage());
            }
            return jsonObject;
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
