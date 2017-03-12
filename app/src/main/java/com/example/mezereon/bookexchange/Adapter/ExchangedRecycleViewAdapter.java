package com.example.mezereon.bookexchange.Adapter;

/**
 * Created by Mezereon on 2017/3/12.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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


public class ExchangedRecycleViewAdapter extends RecyclerView.Adapter<ExchangedRecycleViewAdapter.ExchangedViewHolder>{

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private final SharedPreferences sharedPreferences;

    private List<Exchange> exchanges;
    private String username;

    public ExchangedRecycleViewAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        sharedPreferences=mContext.getSharedPreferences("USERINFO",mContext.MODE_PRIVATE);
        username=sharedPreferences.getString("USERNAME","NONE");
    }

    public List<Exchange> getExchanges() { return exchanges; }

    public void setExchanges(List<Exchange> exchanges) { this.exchanges = exchanges; }

    @Override
    public ExchangedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExchangedViewHolder(mLayoutInflater.inflate(R.layout.item_exchanged, parent, false));
    }

    @Override
    public void onBindViewHolder(ExchangedViewHolder holder, int position) {
        Exchange exchange=exchanges.get(position);
        if (username.equals(exchange.getUsernamea())) {
            setUseraExchange(holder,exchange,position);
        } else {
            setUserbExchange(holder,exchange,position);
        }
    }

    private void setUserbExchange(ExchangedViewHolder holder, Exchange exchange, int position) {
        holder.bookname.setText(exchange.getBooknamea());
        Picasso.with(mContext).load(exchange.getBooksrca()).into(holder.bookPic);
        holder.date.setText(exchange.getDate());
        holder.userName.setText(exchange.getUsernamea());
        setTheClickEvent2(holder, position);
        if(exchange.getNumberb().equals("1")){
            holder.recevie.setTextColor(Color.parseColor("#656565"));
            holder.recevie.setText("已收货");
            holder.recevie.setEnabled(false);
        }
    }

    private void setUseraExchange(ExchangedViewHolder holder, Exchange exchange, int position) {
        holder.bookname.setText(exchange.getBooknameb());
        Picasso.with(mContext).load(exchange.getBooksrcb()).into(holder.bookPic);
        holder.date.setText(exchange.getDate());
        holder.userName.setText(exchange.getUsernameb());
        setTheClickEvent(holder, position);
        if(exchange.getNumbera().equals("1")){
            holder.recevie.setTextColor(Color.parseColor("#656565"));
            holder.recevie.setText("已收货");
            holder.recevie.setEnabled(false);
        }
    }

    private void setTheClickEvent2(ExchangedViewHolder holder, final int position) {
        RxView.clicks(holder.recevie)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        MyExchangeActivity myActivity=(MyExchangeActivity)mContext;
                        myActivity.recevie(exchanges.get(position).getId(),2);
                    };
                });
    }


    private void setTheClickEvent(final ExchangedViewHolder holder, final int position) {
        RxView.clicks(holder.recevie)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        MyExchangeActivity myActivity=(MyExchangeActivity)mContext;
                        myActivity.recevie(exchanges.get(position).getId(),1);
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

    public static class ExchangedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardViewInExchanged)
        CardView cardView;
        @BindView(R.id.booknameInExchanged)
        TextView bookname;
        @BindView(R.id.userNameInExchanged)
        TextView userName;
        @BindView(R.id.bookPicInExchanged)
        ImageView bookPic;
        @BindView(R.id.dateInExchanged)
        TextView date;
        @BindView(R.id.itemLayout)
        RelativeLayout layout;
        @BindView(R.id.checkNumberInExchanged)
        TextView recevie;

        ExchangedViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
