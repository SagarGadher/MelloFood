package com.example.mellofood.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mellofood.R;
import com.example.mellofood.adapter.MenuAdapter;
import com.example.mellofood.model.MenuList;

import java.util.ArrayList;

public class MenuFragment extends Fragment implements MenuAdapter.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
    private RecyclerView mRecyclerView;
    private MenuAdapter mMenuAdapter;
    private ArrayList<MenuList> mMenuItemList;
    int position,total = 1;

    Dialog mOrderDialog;
    ImageView ivCloseDialog, ivMenuItemImage,ivSubtract,ivAdd;
    TextView tvMenuItemName, tvMenuItemDescription,tvItemAmount,tvTotal,btnOrderPlace;
    RadioButton rbSizeSmall, rbSizeMedium, rbSizeLarge, rbCheeseLess, rbCheeseMore;
CheckBox cbBreadGarlic, cbBreadJain;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.menu_item_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mMenuItemList = new ArrayList<>();
        mOrderDialog = new Dialog(getActivity());
        position = getArguments().getInt("position");

        mMenuItemList.add(new MenuList("Ahmedabad One" + position, "Ahmedabad One", "$80.00", R.drawable.p3));
        mMenuItemList.add(new MenuList("himalaya" + position, "himalaya", "$180.00", R.drawable.p6));
        mMenuItemList.add(new MenuList("Food On Way" + position, "Food On Way", "$90.00", R.drawable.p3));
        mMenuItemList.add(new MenuList("Honest" + position, "Honest", "$50.00", R.drawable.p6));
        mMenuItemList.add(new MenuList("Havmor" + position, "Havmor", "$80.00", R.drawable.p3));
        mMenuItemList.add(new MenuList("William Johnes" + position, "William Johnes", "$40.00", R.drawable.p6));
        mMenuItemList.add(new MenuList("Real Paprika" + position, "Real Paprika", "$120.00", R.drawable.p3));
        mMenuItemList.add(new MenuList("La Pino'z" + position, "La Pino'z", "$140.00", R.drawable.p6));
        mMenuItemList.add(new MenuList("William Johnes" + position, "William Johnes", "$40.00", R.drawable.p6));
        mMenuItemList.add(new MenuList("Real Paprika" + position, "Real Paprika", "$120.00", R.drawable.p3));
        mMenuItemList.add(new MenuList("La Pino'z" + position, "La Pino'z", "$140.00", R.drawable.p6));
        mMenuItemList.add(new MenuList("William Johnes" + position, "William Johnes", "$40.00", R.drawable.p6));
        mMenuItemList.add(new MenuList("Real Paprika" + position, "Real Paprika", "$120.00", R.drawable.p3));
        mMenuItemList.add(new MenuList("La Pino'z" + position, "La Pino'z", "$140.00", R.drawable.p6));

        mMenuAdapter = new MenuAdapter(getActivity(), mMenuItemList);
        mRecyclerView.setAdapter(mMenuAdapter);
        mMenuAdapter.setOnItemClickListener(this);
        setHasOptionsMenu(true);
    }

    public void openUserNameDialog(int position) {
        mOrderDialog.setContentView(R.layout.popup_dialog_design);
        ivCloseDialog = mOrderDialog.findViewById(R.id.dialog_close);
        ivMenuItemImage = mOrderDialog.findViewById(R.id.ivMenuItemImage);
        tvMenuItemName = mOrderDialog.findViewById(R.id.tvMenuItemName);
        tvMenuItemDescription = mOrderDialog.findViewById(R.id.tvMenuItemDescription);
        tvMenuItemName.setText(mMenuItemList.get(position).getTvMenuItemName());
        ivMenuItemImage.setImageResource(mMenuItemList.get(position).getIvMenuItemImage());
        tvMenuItemName.setText(mMenuItemList.get(position).getTvMenuItemName());
        tvMenuItemDescription.setText(mMenuItemList.get(position).getTvMenuItemDescription());

        tvItemAmount = mOrderDialog.findViewById(R.id.tvItemAmount);
        tvTotal = mOrderDialog.findViewById(R.id.tvTotal);

        rbSizeSmall = mOrderDialog.findViewById(R.id.rbSizeSmall);
        rbSizeMedium = mOrderDialog.findViewById(R.id.rbSizeMedium);
        rbSizeLarge = mOrderDialog.findViewById(R.id.rbSizeLarge);
        rbCheeseLess = mOrderDialog.findViewById(R.id.rbCheeseLess);
        rbCheeseMore = mOrderDialog.findViewById(R.id.rbCheeseMore);
        cbBreadGarlic = mOrderDialog.findViewById(R.id.cbBreadGarlic);
        cbBreadJain = mOrderDialog.findViewById(R.id.cbBreadJain);
        rbSizeSmall.setOnCheckedChangeListener(this);
        rbSizeMedium.setOnCheckedChangeListener(this);
        rbSizeLarge.setOnCheckedChangeListener(this);
        rbCheeseLess.setOnCheckedChangeListener(this);
        rbCheeseMore.setOnCheckedChangeListener(this);
        cbBreadGarlic.setOnCheckedChangeListener(this);
        cbBreadJain.setOnCheckedChangeListener(this);

        ivAdd = mOrderDialog.findViewById(R.id.ivAdd);
        ivSubtract = mOrderDialog.findViewById(R.id.ivSubtract);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total >=5){
                    Toast.makeText(getActivity(), "Max amount Added !", Toast.LENGTH_SHORT).show();
                    return;
                }
                total +=1;
                tvItemAmount.setText(String.valueOf(total));
                Toast.makeText(getActivity(), "Item Added !", Toast.LENGTH_SHORT).show();
            }
        });
        ivSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(total <= 0){
                    return;
                }
                total -=1;
                tvItemAmount.setText(String.valueOf(total));
                Toast.makeText(getActivity(), "Item removed !", Toast.LENGTH_SHORT).show();
            }
        });
        btnOrderPlace = mOrderDialog.findViewById(R.id.btnOrderPlace);
        btnOrderPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Order Placed !", Toast.LENGTH_SHORT).show();
                mOrderDialog.dismiss();
            }
        });

        ivCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOrderDialog.dismiss();
            }
        });
        mOrderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mOrderDialog.show();
        mOrderDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onItemClick(int position, int id) {
        String itemName = mMenuItemList.get(position).getTvMenuItemName();
        String itemPrice = mMenuItemList.get(position).getTvMenuItemPrice();
        Toast.makeText(getActivity(), itemName + "is selected", Toast.LENGTH_SHORT).show();

        switch (id) {
            case R.id.ibtnAddItem:
                openUserNameDialog(position);
                Toast.makeText(getActivity(), "Item add price is" + itemPrice, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rbSizeSmall:
                if (isChecked) {
                    Toast.makeText(getActivity(), "Small size Selected !", Toast.LENGTH_SHORT).show();
                }
                /*if (!isChecked) {
                    Toast.makeText(getActivity(), "Small size Removed !", Toast.LENGTH_SHORT).show();
                }*/

                break;
            case R.id.rbSizeMedium:
                if (isChecked) {
                    Toast.makeText(getActivity(), "Medium Size Selected !", Toast.LENGTH_SHORT).show();
                }
               /* if (!isChecked) {
                    Toast.makeText(getActivity(), "Medium size Removed !", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.rbSizeLarge:
                if (isChecked) {
                    Toast.makeText(getActivity(), "Large size Selected !", Toast.LENGTH_SHORT).show();
                }
                /*if (!isChecked) {
                    Toast.makeText(getActivity(), "Large size Removed !", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.rbCheeseLess:
                if (isChecked) {
                    Toast.makeText(getActivity(), "Less Cheese Selected !", Toast.LENGTH_SHORT).show();
                }
                /*if (!isChecked) {
                    Toast.makeText(getActivity(), "Less Cheese Removed !", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.rbCheeseMore:
                if (isChecked){
                    Toast.makeText(getActivity(), "More Cheese Selected !", Toast.LENGTH_SHORT).show();
                }
                /*if (!isChecked){
                    Toast.makeText(getActivity(), "More Cheese Removed !", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.cbBreadGarlic:
                if (isChecked) {
                    Toast.makeText(getActivity(), "Garlic Bread Selected !", Toast.LENGTH_SHORT).show();
                }
                /*if (!isChecked) {
                    Toast.makeText(getActivity(), "Less Cheese Removed !", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.cbBreadJain:
                if (isChecked){
                    Toast.makeText(getActivity(), "Jain Bread Selected !", Toast.LENGTH_SHORT).show();
                }
                /*if (!isChecked){
                    Toast.makeText(getActivity(), "More Cheese Removed !", Toast.LENGTH_SHORT).show();
                }*/
                break;
        }
    }
}
