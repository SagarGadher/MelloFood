package com.example.mellofood.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mellofood.R;
import com.example.mellofood.adapter.OrderAdapter;
import com.example.mellofood.model.OrderList;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements OrderAdapter.OnItemClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    TextView tvAddMore, tvApplyPC, tvFinalAmount, tvTotalAmount, tvStoreName, tvLandmarkName,tvCheckOut;
    EditText etPromoCode;
    RadioButton rbPayWithDBS, rbPayWithPOBS;

    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    private ArrayList<OrderList> mOrderLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_cart);

        tvLandmarkName = findViewById(R.id.tvLandmarkName);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvFinalAmount = findViewById(R.id.tvFinalAmount);
        tvApplyPC = findViewById(R.id.tvApplyPC);
        tvAddMore = findViewById(R.id.tvAddMore);
        tvCheckOut = findViewById(R.id.tvCheckOut);
        etPromoCode = findViewById(R.id.etPromoCode);
        rbPayWithDBS = findViewById(R.id.rbPayWithDBS);
        rbPayWithPOBS = findViewById(R.id.rbPayWithPOBS);

        rbPayWithPOBS.setOnCheckedChangeListener(this);
        rbPayWithDBS.setOnCheckedChangeListener(this);

        tvAddMore.setPaintFlags(tvAddMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvApplyPC.setOnClickListener(this);
        tvAddMore.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.rvOrderList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOrderLists = new ArrayList<>();
        mOrderLists.add(new OrderList("Ahmedabad One", "Ahmedabad One", "62", "2"));
        mOrderLists.add(new OrderList("himalaya", "himalaya", "82", "1"));
        mOrderLists.add(new OrderList("Food On Way", "Food On Way", "92", "3"));
        mOrderLists.add(new OrderList("Honest", "Honest", "42", "4"));
        mOrderLists.add(new OrderList("Havmor", "Havmor", "52", "1"));
        mOrderLists.add(new OrderList("William Johnes", "William Johnes", "72", "1"));
        mOrderLists.add(new OrderList("Real Paprika", "Real Paprika", "52", "2"));
        mOrderLists.add(new OrderList("La Pino'z", "La Pino'z", "32", "3"));

        mOrderAdapter = new OrderAdapter(this, mOrderLists);
        mRecyclerView.setAdapter(mOrderAdapter);
        mOrderAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position, int id) {
        String ItemName = mOrderLists.get(position).getItemName();
        switch (id) {
            case R.id.ivEdit:
                Toast.makeText(this, "Edit Clicked from " + ItemName, Toast.LENGTH_SHORT).show();
                break;
            case R.id.ivRemove:

                Toast.makeText(this, "Delete Clicked from " + ItemName, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvAddMore:
                Toast.makeText(CartActivity.this, "Add More", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvApplyPC:
                Toast.makeText(CartActivity.this, "Apply Promo Code", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvCheckOut:
                Toast.makeText(CartActivity.this, "Check Out", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.rbPayWithDBS:
                if (isChecked){
                    Toast.makeText(this, "DBS is selected", Toast.LENGTH_SHORT).show();
                }
                else if (!isChecked){

                }
                break;
            case R.id.rbPayWithPOBS:
                if (isChecked){
                    Toast.makeText(this, "POBS is selected", Toast.LENGTH_SHORT).show();
                }
                else if (!isChecked){

                }
                break;
        }
    }
}
