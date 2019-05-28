package com.example.mellofood.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.mellofood.adapter.MenuAdapter;
import com.example.mellofood.adapter.PagerViewAdapter;
import com.example.mellofood.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuActivity extends AppCompatActivity implements MenuAdapter.OnItemClickListener {
    Toolbar toolbar;

    private ViewPager mMainPager;
    ImageButton ibtnMoreList;
    private PagerViewAdapter mPagerViewAdapter;
    String selectedStore;
    TextView tvList,tvTotalAmount,tvItemCount,btnViewCart;

    private final String CUSTOM_ADAPTER_TEXT = "text";
    final String[] Store = {"Ahmedabad One", "himalaya", "Food On Way", "Honest", "Havmor", "William Johnes", "Real Paprika", "La Pino'z"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_menu);
        Log.d("StartActivity", "start");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("storeName"));

        btnViewCart = findViewById(R.id.btnViewCart);
        btnViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this,CartActivity.class);
                startActivity(i);
            }
        });
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvItemCount = findViewById(R.id.tvItemCount);
        tvList = findViewById(R.id.tvList);
        tvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListDialog();
            }
        });
        ibtnMoreList = findViewById(R.id.ibtnMoreList);
        ibtnMoreList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListDialog();
            }
        });
        mMainPager = findViewById(R.id.mainPager);

        mPagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());
        mMainPager.setAdapter(mPagerViewAdapter);
        mMainPager.setCurrentItem(getIntent().getIntExtra("position", 0));

        selectedStore = getIntent().getStringExtra("storeName");
        tvList.setText(selectedStore);

        mMainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvList.setText(Store[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void openListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
        // Create SimpleAdapter list data.
        List<Map<String, Object>> dialogItemList = new ArrayList<Map<String, Object>>();
        int listItemLen = Store.length;
        for (int i = 0; i < listItemLen; i++) {
            Map<String, Object> itemMap = new HashMap<String, Object>();
            itemMap.put(CUSTOM_ADAPTER_TEXT, Store[i]);

            dialogItemList.add(itemMap);
        }

        // Create SimpleAdapter object.
        SimpleAdapter simpleAdapter = new SimpleAdapter(MenuActivity.this, dialogItemList,
                R.layout.layout_list_alert_dialog,
                new String[]{CUSTOM_ADAPTER_TEXT},
                new int[]{R.id.tvListName});

        // Set the data adapter.
        builder.setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int itemIndex) {
                tvList.setText(Store[itemIndex]);
                mMainPager.setCurrentItem(itemIndex);
            }
        });
        builder.create();
        builder.show();
    }
    @Override
    public void onItemClick(int position, int id) {

    }
}
