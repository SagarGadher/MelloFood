package com.example.mellofood.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mellofood.R;
import com.example.mellofood.adapter.RestaurantAdapter;
import com.example.mellofood.model.RestaurantList;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import java.util.ArrayList;

public class LandmarkActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RestaurantAdapter.OnItemClickListener {
    static Activity activity;
    Toolbar toolbar;
    Intent logOutIntent;
    private DrawerLayout drawer;
    private RecyclerView mRecyclerView;
    private RestaurantAdapter mRestaurantAdapter;
    private ArrayList<RestaurantList> mRestaurantsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_landmark);

        logOutIntent = new Intent(LandmarkActivity.this, LoginActivity.class);
        logOutIntent.putExtra("ActivityName", "LandmarkActivity");

        activity = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("storeName"));
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);

        mRecyclerView = findViewById(R.id.rv_outlet);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRestaurantsList = new ArrayList<>();
        mRestaurantsList.add(new RestaurantList("Ahmedabad One", "Ahmedabad One", R.drawable.p3));
        mRestaurantsList.add(new RestaurantList("himalaya", "himalaya", R.drawable.p6));
        mRestaurantsList.add(new RestaurantList("Food On Way", "Food On Way", R.drawable.p3));
        mRestaurantsList.add(new RestaurantList("Honest", "Honest", R.drawable.p6));
        mRestaurantsList.add(new RestaurantList("Havmor", "Havmor", R.drawable.p3));
        mRestaurantsList.add(new RestaurantList("William Johnes", "William Johnes", R.drawable.p6));
        mRestaurantsList.add(new RestaurantList("Real Paprika", "Real Paprika", R.drawable.p3));
        mRestaurantsList.add(new RestaurantList("La Pino'z", "La Pino'z", R.drawable.p6));

        mRestaurantAdapter = new RestaurantAdapter(this, mRestaurantsList);
        mRecyclerView.setAdapter(mRestaurantAdapter);
        mRestaurantAdapter.setOnItemClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logOut:
                disconnectFromFacebook();
                break;
            case R.id.nav_outlets:
                onBackPressed();
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
        String storeName = mRestaurantsList.get(position).getTvStoreName();
        Toast.makeText(this, storeName + "", Toast.LENGTH_SHORT).show();
        switch (id) {
            case R.id.btnNew:
                Toast.makeText(this, "New Clicked from " + storeName, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnPromotion:
                Toast.makeText(this, "Promotion Clicked from " + storeName, Toast.LENGTH_SHORT).show();
                break;
            default:
                Intent i = new Intent(LandmarkActivity.this, MenuActivity.class);
                i.putExtra("storeName", storeName);
                i.putExtra("position", position);
                startActivity(i);
                break;
        }
    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            Toast.makeText(this, "you are already logged Out !!!", Toast.LENGTH_SHORT).show();
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                try {
                    LoginManager.getInstance().logOut();
                } catch (Exception e) {
                    Log.d("FacebookLogOutError", e.getMessage());
                } finally {
                    startActivity(logOutIntent);
                }
            }
        }).executeAsync();
    }
}
