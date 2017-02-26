package com.example.mezereon.bookexchange.Adapter;

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

public class BeforeExchangeRecycleViewAdapter extends RecyclerView.Adapter<BeforeExchangeRecycleViewAdapter.BeforeExchangeViewHolder>{

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private final  SharedPreferences sharedPreferences;

    private List<Exchange> exchanges;
    private String username;

    public BeforeExchangeRecycleViewAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
       sharedPreferences=mContext.getSharedPreferences("USERINFO",mContext.MODE_PRIVATE);
        username=sharedPreferences.getString("USERNAME","NONE");
    }

    public List<Exchange> getExchanges() { return exchanges; }

    public void setExchanges(List<Exchange> exchanges) { this.exchanges = exchanges; }

    @Override
    public BeforeExchangeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BeforeExchangeViewHolder(mLayoutInflater.inflate(R.layout.item_before_exchange, parent, false));
    }

    @Override
    public void onBindViewHolder(BeforeExchangeViewHolder holder, int position) {
        Exchange exchange=exchanges.get(position);
        if(username.equals(exchange.getUsernamea())){
            holder.bookname.setText(exchange.getBooknameb());
            Picasso.with(mContext).load(exchange.getBooksrcb()).into(holder.bookPic);
            holder.date.setText(exchange.getDate());
            holder.userName.setText(exchange.getUsernameb());
            holder.check.setVisibility(View.INVISIBLE);
            holder.waitText.setVisibility(View.VISIBLE);
        }else{
            holder.bookname.setText(exchange.getBooknamea());
            Picasso.with(mContext).load(exchange.getBooksrca()).into(holder.bookPic);
            holder.date.setText(exchange.getDate());
            holder.userName.setText(exchange.getUsernamea());
            setTheClickEvent(holder,position);
        }

    }


    private void setTheClickEvent(final BeforeExchangeViewHolder holder, final int position) {
        RxView.clicks(holder.check)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                       MyExchangeActivity myActivity=(MyExchangeActivity)mContext;
                        myActivity.showCover(exchanges.get(position).getBooksrcb(),exchanges.get(position).getBooknameb());
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

    public static class BeforeExchangeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardViewInBeforeExchange)
        CardView cardView;
        @BindView(R.id.booknameInBeforeExchange)
        TextView bookname;
        @BindView(R.id.userNameInBeforeExchange)
        TextView userName;
        @BindView(R.id.bookPicInBeforeExchange)
        ImageView bookPic;
        @BindView(R.id.dateInBeforeExchange)
        TextView date;
        @BindView(R.id.itemLayout)
        RelativeLayout layout;
        @BindView(R.id.button11)
        Button check;
        @BindView(R.id.textView25)
        TextView waitText;

        BeforeExchangeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
