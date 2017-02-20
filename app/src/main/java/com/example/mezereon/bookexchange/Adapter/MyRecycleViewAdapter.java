package com.example.mezereon.bookexchange.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mezereon.bookexchange.MyApp;
import com.example.mezereon.bookexchange.R;
import com.example.mezereon.bookexchange.UserInfoActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mezereon on 2017/2/9.
 */

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.MyRecycleViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;


    public MyRecycleViewAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyRecycleViewHolder(mLayoutInflater.inflate(R.layout.item_setting, parent, false));
    }

    @Override
    public void onBindViewHolder(MyRecycleViewHolder holder, final int position) {
            switch (position){
                case 0:
                    setTheSelfInfo(holder,position);
                    break;
                case 1:
                    setTheExChangeInfo(holder,position);
                    break;
                case 2:
                    setMyBook(holder,position);
                    break;
                case 3:
                    setTheLog(holder,position);
                    break;
                default:
                    break;
            }
    }

    private void setTheLog(MyRecycleViewHolder holder, int position) {
        holder.pic.setImageResource(R.drawable.ic_email_grey600_36dp);
        holder.name.setText("通知消息");
    }

    private void setMyBook(MyRecycleViewHolder holder, int position) {
        holder.pic.setImageResource(R.drawable.ic_my_library_books_grey600_36dp);
        holder.name.setText("我的书籍");
    }

    private void setTheExChangeInfo(MyRecycleViewHolder holder, int position) {
        holder.pic.setImageResource(R.drawable.ic_storage_grey600_36dp);
        holder.name.setText("交换信息");
        holder.settingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void setTheSelfInfo(MyRecycleViewHolder holder, final int position) {
        holder.pic.setImageResource(R.drawable.ic_person_grey600_24dp);
        holder.name.setText("个人信息");
        holder.settingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnToUserInfo();
            }
        });
    }

    private void turnToUserInfo() {
        Intent intent=new Intent();
        intent.setClass(mContext, UserInfoActivity.class);
        mContext.startActivity(intent);
        intent=null;
    }

    @Override
    public int getItemCount() {
        return MyApp.COUNT_OF_SETTING_ITEMS;
    }

    public static class MyRecycleViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageView5)
        ImageView pic;
        @Bind(R.id.textView6)
        TextView name;
        @Bind(R.id.settingCard)
        CardView settingCard;

        MyRecycleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }
}