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
import com.example.mellofood.model.RestaurantList;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantItemViewHolder> {
    private Context mContext;
    private ArrayList<RestaurantList> mRestaurantList;
    private RestaurantAdapter.OnItemClickListener mListener;

    public RestaurantAdapter(Context mContext, ArrayList<RestaurantList> mRestaurantList) {
        this.mContext = mContext;
        this.mRestaurantList = mRestaurantList;
    }

    @NonNull
    @Override
    public RestaurantItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_list_restaurant, parent, false);
        return new RestaurantItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantItemViewHolder holder, int position) {
        RestaurantList mCurrentItem = mRestaurantList.get(position);
        holder.ivStoreImage.setImageResource(mCurrentItem.getIvStoreImage());
        holder.tvStoreName.setText(mCurrentItem.getTvStoreName());
    }

    @Override
    public int getItemCount() {
        return mRestaurantList.size();
    }

    public void setOnItemClickListener(RestaurantAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int id);
    }

    public class RestaurantItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivStoreImage;
        public TextView tvStoreName, tvDescription;

        public RestaurantItemViewHolder(final View itemView) {
            super(itemView);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            ivStoreImage = itemView.findViewById(R.id.ivStoreImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
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
            itemView.findViewById(R.id.btnNew).setOnClickListener(new View.OnClickListener() {
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
            itemView.findViewById(R.id.btnPromotion).setOnClickListener(new View.OnClickListener() {
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
