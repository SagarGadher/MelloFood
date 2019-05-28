package com.example.mellofood.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mellofood.R;
import com.example.mellofood.model.MenuList;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuItemViewHolder> {
    private Context mContext;
    private ArrayList<MenuList> mMenuItemList;
    private MenuAdapter.OnItemClickListener mListener;

    public MenuAdapter(Context mContext, ArrayList<MenuList> mMenuItemList) {
        this.mContext = mContext;
        this.mMenuItemList = mMenuItemList;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_list_menu, parent, false);
        return new MenuItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuList mCurrentItem = mMenuItemList.get(position);
        holder.ivMenuItemImage.setImageResource(mCurrentItem.getIvMenuItemImage());
        holder.tvMenuItemName.setText(mCurrentItem.getTvMenuItemName());
        holder.tvMenuItemDescription.setText(mCurrentItem.getTvMenuItemDescription());
        holder.tvMenuItemPrice.setText(mCurrentItem.getTvMenuItemPrice());
    }

    @Override
    public int getItemCount() {
        return mMenuItemList.size();
    }

    public void setOnItemClickListener(MenuAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int id);
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivMenuItemImage;
        public TextView tvMenuItemName, tvMenuItemDescription, tvMenuItemPrice;

        public MenuItemViewHolder(final View itemView) {
            super(itemView);
            tvMenuItemName = itemView.findViewById(R.id.tvMenuItemName);
            ivMenuItemImage = itemView.findViewById(R.id.ivMenuItemImage);
            tvMenuItemDescription = itemView.findViewById(R.id.tvMenuItemDescription);
            tvMenuItemPrice = itemView.findViewById(R.id.tvMenuItemPrice);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position, v.getId());
                        }
                    }
                }
            });
            itemView.findViewById(R.id.ibtnAddItem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position, v.getId());
                        }
                    }
                }
            });
        }
    }
}
