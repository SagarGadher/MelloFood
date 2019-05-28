package com.example.mellofood.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mellofood.R;
import com.example.mellofood.activity.CartActivity;
import com.example.mellofood.model.OrderList;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderItemViewHolder> {
    private Context mContext;
    private ArrayList<OrderList> mOrderItemList;
    private OrderAdapter.OnItemClickListener mListener;

    public OrderAdapter(Context mContext, ArrayList<OrderList> mOrderItemList) {
        this.mContext = mContext;
        this.mOrderItemList = mOrderItemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_list_order, parent, false);
        return new OrderItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderList mCurrentItem = mOrderItemList.get(position);
        holder.tvItemName.setText(mCurrentItem.getItemName());
        holder.tvItemDescription.setText(mCurrentItem.getItemDescription());
        holder.tvItemPrice.setText("$"+mCurrentItem.getItemPrice());
        holder.tvItemAmount.setText(mCurrentItem.getItemAmount());
    }

    @Override
    public int getItemCount() {
        return mOrderItemList.size();
    }

    public class OrderItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName,tvItemDescription,tvItemPrice,tvItemAmount;

        public OrderItemViewHolder(final View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemDescription = itemView.findViewById(R.id.tvItemDescription);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvItemAmount = itemView.findViewById(R.id.tvItemAmount);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position,v.getId());
                        }
                    }
                }
            });
            itemView.findViewById(R.id.ivRemove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position,v.getId());
                        }
                    }
                }
            });
            itemView.findViewById(R.id.ivEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position,v.getId());
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position,int id);
    }

    public void setOnItemClickListener(OrderAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}
