package com.example.mellofood.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mellofood.JsonPlaceHolderApi;
import com.example.mellofood.adapter.OutletAdapter;
import com.example.mellofood.database.TableContent;
import com.example.mellofood.database.UserDBHelper;
import com.example.mellofood.model.OutletList;
import com.example.mellofood.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.builder.api.TwitterApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import oauth.signpost.OAuthProvider;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OutletActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OutletAdapter.OnItemClickListener {
    private DrawerLayout drawer;
    Toolbar toolbar;
    Intent logOutIntent;

    JsonPlaceHolderApi jsonPlaceHolderApi;

    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String ENC = "UTF-8";
    private Base64 base64 = new Base64();

    private RecyclerView mRecyclerView;
    private OutletAdapter mOutletAdapter;
    private ArrayList<OutletList> mOutletLists;
    GridLayoutManager gridLayoutManager;

    static Activity activity;
    SharedPreferences sharedPreferences;

    String URL, login_token, login_token_secret;
    final static String CONSUMER_KEY = "121a5d77c4bd07477baab083686e9519";
    final static String CONSUMER_SECRET = "c7a926823d14be0f95f53b9ca122396d";

    String nonce;
    String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_outlet);
        logOutIntent = new Intent(OutletActivity.this, LoginActivity.class);
        logOutIntent.putExtra("ActivityName", "OutletActivity");
        activity = OutletActivity.this;

        nonce = UUID.randomUUID().toString();
        Long tsLong = System.currentTimeMillis()/1000;
        timeStamp = tsLong.toString();

        ///Retrofit Code
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.0.10/mello/api/rest/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        getOutlet();
        ///

        URL = "http://10.0.0.10/mello/api/rest/v2/catalog/outlet?lang_code=en_US";
        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        login_token = sharedPreferences.getString(getString(R.string.LOGIN_TOKEN), "");
        login_token_secret = sharedPreferences.getString(getString(R.string.LOGIN_TOKEN_SECRET), "");
        Log.d("login_token", login_token);
        Log.d("login_token_secret", login_token_secret);

        //new AsyncTaskOperation().execute();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("OUTLETS");
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setItemIconTintList(null);

        mRecyclerView = findViewById(R.id.rv_outlet);
        mRecyclerView.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(this, 1);

        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int viewWidth = mRecyclerView.getMeasuredWidth();
                        int newSpanCount = (int) Math.floor(viewWidth / 500);
                        gridLayoutManager.setSpanCount(newSpanCount);
                        gridLayoutManager.requestLayout();
                    }
                });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mOutletLists = new ArrayList<>();
        mOutletLists.add(new OutletList(0, "Ahmedabad One", "UnLike", R.drawable.p3));
        mOutletLists.add(new OutletList(1, "himalaya", "UnLike", R.drawable.p6));
        mOutletLists.add(new OutletList(2, "Food On Way", "UnLike", R.drawable.p3));
        mOutletLists.add(new OutletList(3, "Honest", "UnLike", R.drawable.p6));
        mOutletLists.add(new OutletList(4, "Havmor", "UnLike", R.drawable.p3));
        mOutletLists.add(new OutletList(5, "William Johnes", "UnLike", R.drawable.p6));
        mOutletLists.add(new OutletList(6, "Real Paprika", "UnLike", R.drawable.p3));
        mOutletLists.add(new OutletList(7, "La Pino'z", "UnLike", R.drawable.p6));
        UserDBHelper userDBHelper = new UserDBHelper(this);
        SQLiteDatabase db = userDBHelper.getWritableDatabase();
        Cursor cursor = userDBHelper.viewUser(db);
        while (cursor.moveToNext()) {
            String position = cursor.getString(cursor.getColumnIndex(TableContent.TableEntry.Likes));
            /*mOutletLists.get(position).setIsLike("Like");*/
            Log.d("LoginMassage", "Data Base has already this email " + position);
            if (position != null && !position.isEmpty()) {
                String[] arrSplit = position.split(",");
                for (int i = 0; i < arrSplit.length; i++) {
                    if (!arrSplit[i].isEmpty())
                        mOutletLists.get(Integer.parseInt(arrSplit[i])).setIsLike("Like");
                }
                Log.d("StringMessage", arrSplit + "");
            }

        }
        userDBHelper.close();
        Collections.sort(mOutletLists, new Comparator<OutletList>() {
            @Override
            public int compare(OutletList o1, OutletList o2) {
                return o1.getIsLike().compareTo(o2.getIsLike());
            }
        });

        mOutletAdapter = new OutletAdapter(this, mOutletLists);
        mRecyclerView.setAdapter(mOutletAdapter);
        mOutletAdapter.setOnItemClickListener(this);

        getOutlet();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logOut:
                disconnectFromFacebook();
                break;
            case R.id.nav_outlets:
                Toast.makeText(activity, "You are already in Outlet Activity", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.nav_order:
                Toast.makeText(activity, "Order Clicked", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.nav_payment_methods:
                Toast.makeText(activity, "Payment Method Clicked", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.nav_promotions:
                Toast.makeText(activity, "Promotion Clicked", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.nav_help:
                Toast.makeText(activity, "Help Clicked", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.nav_contact_us:
                Toast.makeText(activity, "Contact us Clicked", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.nav_privacy_policy:
                Toast.makeText(activity, "Privacy Policy Clicked", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.nav_tc:
                Toast.makeText(activity, "Terms and Condition Clicked", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.nav_about_us:
                Toast.makeText(activity, "About us Clicked", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(int position, int id) {
        String storeName = mOutletLists.get(position).getTvStoreName();
        switch (id) {
            case R.id.ibtnStoreLocation:
                Toast.makeText(this, "Location Clicked from " + storeName, Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibtnStoreLike:
                Toast.makeText(this, "Like Clicked from " + storeName, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, storeName + "", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(OutletActivity.this, LandmarkActivity.class);
                i.putExtra("storeName", storeName);
                startActivity(i);
                break;
        }
    }

    public void disconnectFromFacebook() {
        String LoginType;
        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        LoginType = sharedPreferences.getString(getString(R.string.LOGIN_TYPE), "");

        switch (LoginType) {
            case "FACEBOOK":
                if (AccessToken.getCurrentAccessToken() == null) {
                    Toast.makeText(this, "you are already logged Out !!!", Toast.LENGTH_SHORT).show();
                    return; // already logged out
                }

                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                        startActivity(logOutIntent);

                    }
                }).executeAsync();
                break;
            case "CUSTOM":
                SharedPreferences.Editor ed = sharedPreferences.edit();
                ed.putString(getString(R.string.LOGIN_TOKEN), null);
                ed.commit();
                startActivity(logOutIntent);
                break;
        }
    }

    public class AsyncTaskOperation extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String nonce = String.valueOf((int) (Math.random() * 100000000));
        String timestam = String.valueOf(Math.round((new Date()).getTime() / 1000.0));
        /* String parameters =URLEncoder.encode("oauth_consumer_key=" + CONSUMER_KEY+"&" ) +
                 URLEncoder.encode("oauth_token=" + login_token+"&") +
                 URLEncoder.encode("oauth_signature_method=HMAC-SHA1&") +
                 URLEncoder.encode("oauth_timestamp=" + timestam+"&") +
                 URLEncoder.encode("oauth_nonce=" + nonce+"&") +
                 URLEncoder.encode("oauth_version=1.0");*/
        String parameters = "oauth_consumer_key=" + CONSUMER_KEY +
                "&oauth_token=" + login_token +
                "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + timestam +
                "&oauth_nonce=" + nonce +
                "&oauth_version=1.0";
        String signatureBaseString = "GET&" + URLEncoder.encode(URL) + "&" + URLEncoder.encode(parameters);
        //String signatureBaseString = "GET&" + encode(URL) + "&" + encode(parameters);
        String signature = generateSignature(signatureBaseString, CONSUMER_SECRET, login_token_secret);

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("ERRORMAP0", parameters);
            Log.e("ERRORMAP1", signatureBaseString);
            Log.e("ERRORMAP2", signature);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            try {
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // display response
                                jsonObject = response;
                                Log.d("LoginResponse", response.toString());
                                try {
                                    JSONObject j1 = response.getJSONObject("data");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_VOLLEY", error.toString());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        //String authString = "OAuth oauth_consumer_key=\"121a5d77c4bd07477baab083686e9519\",oauth_token=\"fhuv3ftmxgytedrkv4eead3zfsqetrc5\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"1554892731\",oauth_nonce=\"Uy4K3s3GuFL\",oauth_version=\"1.0\",oauth_signature=\"y3A5FPWzJ%2BYRaxa9p312eBKN1Lc%3D\"";
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", "OAuth oauth_consumer_key=\"" + CONSUMER_KEY + "\",oauth_token=\"" + login_token + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"" + timestam + "\",oauth_nonce=\"" + nonce + "\",oauth_version=\"1.0\",oauth_signature=\"" + signature + "\"");
                        //params.put("Authorization", authString);
                        params.put("Content-Type", "application/json");
                        params.put("Device-Name", "android");

                        Log.e("ERRORMAP", params.toString());

                        return params;
                    }
                };
                getRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(getRequest);

            } catch (Exception e) {
                Log.d("LOG_TAG", e.getMessage());
            }
            return null;
        }
    }

    private String encode(String value) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sb = "";
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                sb += "%2A";
            } else if (focus == '+') {
                sb += "%20";
            } else if (focus == '%' && i + 1 < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                sb += '~';
                i += 2;
            } else {
                sb += focus;
            }
        }
        return sb;
    }

    private String generateSignature(String signatueBaseStr, String oAuthConsumerSecret, String oAuthTokenSecret) {
        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec;
            if (null == oAuthTokenSecret) {
                String signingKey = encode(oAuthConsumerSecret) + '&';
                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            } else {
                String signingKey = encode(oAuthConsumerSecret) + '&' + encode(oAuthTokenSecret);
                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            }
            mac.init(spec);
            byteHMAC = mac.doFinal(signatueBaseStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(base64.encode(byteHMAC));
    }

    private void getOutlet(){
        Map<String,String> header = new HashMap<>();
        header.put("Content-Type","application/json");
        header.put("Device-Name","android");
        header.put("Authorization","OAuth oauth_consumer_key=\"121a5d77c4bd07477baab083686e9519\",oauth_token=\""+login_token+"\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\""+timeStamp+"\",oauth_nonce=\""+nonce+"\",oauth_version=\"1.0\",oauth_signature=\"BgyNiyeNDUpmR%2FQabx68InaOoYs%3D\"");
        Call<JSONObject> call = jsonPlaceHolderApi.getOutlet(header);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, retrofit2.Response<JSONObject> response) {
                if (!response.isSuccessful()) {
                    Log.e("RetrofitResponse","Code: " + response.code());
                    return;
                }
                JSONObject j1 = response.body();
                Log.e("RetrofitResponse","Object: " + j1.toString());
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("RetrofitResponseFailure","Code: " + t.getMessage());
            }
        });
    }
}

/*{ ////this code inside this on create
            try {
                signature = getSignature(URLEncoder.encode(
                        URL, ENC),
                        URLEncoder.encode(signatureBaseString, ENC));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
////this code outside this on create
    private String getSignature(String url, String params)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {

        StringBuilder base = new StringBuilder();
        base.append("GET&");
        base.append(url);
        base.append("&");
        base.append(params);

        byte[] keyBytes = (CONSUMER_SECRET + "&").getBytes(ENC);

        SecretKey key = new SecretKeySpec(keyBytes, HMAC_SHA1);

        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(key);

        // encode it, base64 it, change it to string and return.
        return new String(base64.encode(mac.doFinal(base.toString().getBytes(
                ENC))), ENC).trim();
    }
    */