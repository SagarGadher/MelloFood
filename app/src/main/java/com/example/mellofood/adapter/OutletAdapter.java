package com.example.mellofood.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mellofood.R;
import com.example.mellofood.database.TableContent;
import com.example.mellofood.database.UserDBHelper;
import com.example.mellofood.model.OutletList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OutletAdapter extends RecyclerView.Adapter<OutletAdapter.OutletItemViewHolder> {
    private Context mContext;
    private ArrayList<OutletList> mOutletList;
    private OutletAdapter.OnItemClickListener mListener;

    public OutletAdapter(Context mContext, ArrayList<OutletList> mOutletList) {
        this.mContext = mContext;
        this.mOutletList = mOutletList;
    }

    @NonNull
    @Override
    public OutletItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_list_outlet, parent, false);
        return new OutletItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OutletItemViewHolder holder, int position) {
        OutletList mCurrentItem = mOutletList.get(position);
        holder.ivStoreImage.setImageResource(mCurrentItem.getIvStoreImage());
        holder.tvStoreName.setText(mCurrentItem.getTvStoreName());
        if (mCurrentItem.getIsLike().equals("Like")) {
            holder.ibtnStoreLike.setImageResource(R.drawable.ic_like);
        }
        if (mCurrentItem.getIsLike().equals("UnLike")) {
            holder.ibtnStoreLike.setImageResource(R.drawable.ic_unlike);
        }
    }

    @Override
    public int getItemCount() {
        return mOutletList.size();
    }

    public void setOnItemClickListener(OutletAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int id);
    }

    public class OutletItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivStoreImage, ibtnStoreLike;
        public TextView tvStoreName;

        public OutletItemViewHolder(final View itemView) {
            super(itemView);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            ivStoreImage = itemView.findViewById(R.id.ivStoreImage);
            ibtnStoreLike = itemView.findViewById(R.id.ibtnStoreLike);
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
            itemView.findViewById(R.id.ibtnStoreLocation).setOnClickListener(new View.OnClickListener() {
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
            itemView.findViewById(R.id.ibtnStoreLike).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            List<String> newArrayList = new ArrayList<>();
                            String likes = "";
                            UserDBHelper userDBHelper = new UserDBHelper(mContext);
                            SQLiteDatabase db = userDBHelper.getWritableDatabase();
                            Cursor cursor = userDBHelper.viewUser(db);
                            while (cursor.moveToNext()) {
                                likes = cursor.getString(cursor.getColumnIndex(TableContent.TableEntry.Likes));
                                if (likes != null)
                                    newArrayList.addAll(Arrays.asList(likes.split(",")));
                                Log.d("StringMessage4", newArrayList + " before");

                            }
                            ibtnStoreLike = itemView.findViewById(R.id.ibtnStoreLike);

                            if (!mOutletList.get(getAdapterPosition()).getIsLike().equals("Like")) {
                                String position1 = String.valueOf(mOutletList.get(getAdapterPosition()).getId());
                                newArrayList.add(position1);
                                ibtnStoreLike.setImageResource(R.drawable.ic_like);
                                mOutletList.get(getAdapterPosition()).setIsLike("Like");
                                likes = android.text.TextUtils.join(",", newArrayList);
                                userDBHelper.update(likes, db);
                                Log.d("LoginMessage", likes + "Add");
                            } else if (mOutletList.get(getAdapterPosition()).getIsLike().equals("Like")) {
                                String position1 = String.valueOf(mOutletList.get(getAdapterPosition()).getId());
                                for (int i = 0; i < newArrayList.size(); i++) {
                                    if (newArrayList.get(i).equals(position1)) {
                                        Log.d("LoginMessageRemove", newArrayList.get(i));
                                        newArrayList.remove(i);
                                    }
                                }
                                likes = android.text.TextUtils.join(",", newArrayList);
                                userDBHelper.update(likes, db);
                                userDBHelper.close();
                                ibtnStoreLike.setImageResource(R.drawable.ic_unlike);
                                mOutletList.get(getAdapterPosition()).setIsLike("UnLike");
                            }
                            mListener.onItemClick(position, v.getId());

                            Collections.sort(mOutletList, new Comparator<OutletList>() {
                                @Override
                                public int compare(OutletList o1, OutletList o2) {
                                    return o1.getIsLike().compareTo(o2.getIsLike());
                                }
                            });
                            notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }
}
