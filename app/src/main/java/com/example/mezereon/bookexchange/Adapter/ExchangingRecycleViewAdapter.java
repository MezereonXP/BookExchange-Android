package com.example.mezereon.bookexchange.Adapter;

/**
 * Created by Mezereon on 2017/3/11.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mezereon.bookexchange.Module.Exchange;
import com.example.mezereon.bookexchange.MyExchangeActivity;
import com.example.mezereon.bookexchange.R;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mezereon.bookexchange.ExchangeBookActivity;
import com.example.mezereon.bookexchange.Fragment.BeforeExchangeFragment;
import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.Module.Exchange;
import com.example.mezereon.bookexchange.MyApp;
import com.example.mezereon.bookexchange.MyExchangeActivity;
import com.example.mezereon.bookexchange.R;
import com.example.mezereon.bookexchange.UserBookActivity;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import rx.functions.Action1;

/**
 * Created by Mezereon on 2017/2/26.
 */

public class ExchangingRecycleViewAdapter extends RecyclerView.Adapter<ExchangingRecycleViewAdapter.ExchangingRecycleViewHolder>{

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private final SharedPreferences sharedPreferences;

    private List<Exchange> exchanges;
    private String username;

    public ExchangingRecycleViewAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        sharedPreferences=mContext.getSharedPreferences("USERINFO",mContext.MODE_PRIVATE);
        username=sharedPreferences.getString("USERNAME","NONE");
    }

    public List<Exchange> getExchanges() { return exchanges; }

    public void setExchanges(List<Exchange> exchanges) { this.exchanges = exchanges; }

    @Override
    public ExchangingRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExchangingRecycleViewHolder(mLayoutInflater.inflate(R.layout.item_exchanging, parent, false));
    }

    @Override
    public void onBindViewHolder(ExchangingRecycleViewHolder holder, int position) {
        Exchange exchange=exchanges.get(position);
        if (username.equals(exchange.getUsernamea())) {
            holder.bookname.setText(exchange.getBooknameb());
            Picasso.with(mContext).load(exchange.getBooksrcb()).into(holder.bookPic);
            holder.date.setText(exchange.getDate());
            holder.userName.setText(exchange.getUsernameb());
            setTheClickEvent(holder, position);
        } else {
            holder.bookname.setText(exchange.getBooknamea());
            Picasso.with(mContext).load(exchange.getBooksrca()).into(holder.bookPic);
            holder.date.setText(exchange.getDate());
            holder.userName.setText(exchange.getUsernamea());
            setTheClickEvent2(holder, position);
        }

    }

    private void setTheClickEvent2(ExchangingRecycleViewHolder holder, final int position) {
        final MyExchangeActivity myActivity=(MyExchangeActivity)mContext;
        RxView.clicks(holder.check)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        myActivity.showDialog(exchanges.get(position).getNumberb());
                    };
                });
        RxView.clicks(holder.send)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        myActivity.showInputDialog(2,exchanges.get(position).getId());
                    };
                });
    }


    private void setTheClickEvent(final ExchangingRecycleViewHolder holder, final int position) {
        final MyExchangeActivity myActivity=(MyExchangeActivity)mContext;
        RxView.clicks(holder.check)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        myActivity.showDialog(exchanges.get(position).getNumbera());
                    };
                });
        RxView.clicks(holder.send)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        myActivity.showInputDialog(1,exchanges.get(position).getId());
                    };
                });
    }



    @Override
    public int getItemCount() {
        if(exchanges!=null){
            return exchanges.size();
        }else{
            return 0;
        }
    }

    public static class ExchangingRecycleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardViewInExchanging)
        CardView cardView;
        @BindView(R.id.booknameInExchanging)
        TextView bookname;
        @BindView(R.id.userNameInExchanging)
        TextView userName;
        @BindView(R.id.bookPicInExchanging)
        ImageView bookPic;
        @BindView(R.id.dateInExchanging)
        TextView date;
        @BindView(R.id.itemLayout)
        RelativeLayout layout;
        @BindView(R.id.checkNumberInExchanging)
        TextView check;
        @BindView(R.id.sendNumber)
        TextView send;

        ExchangingRecycleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

