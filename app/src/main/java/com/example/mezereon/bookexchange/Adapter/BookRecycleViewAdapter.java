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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mezereon.bookexchange.ExchangeBookActivity;
import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.Module.User;
import com.example.mezereon.bookexchange.R;
import com.example.mezereon.bookexchange.UserBookActivity;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

/**
 * Created by Mezereon on 2017/2/16.
 */

public class BookRecycleViewAdapter extends RecyclerView.Adapter<BookRecycleViewAdapter.BookViewHolder>{

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    private List<Book> books;

    public BookRecycleViewAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public List<Book> getBooks() { return books; }

    public void setBooks(List<Book> books) { this.books = books; }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookViewHolder(mLayoutInflater.inflate(R.layout.item_book, parent, false));
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Picasso.with(mContext).load(books.get(position).getSrc()).into(holder.bookPic);
        holder.bookname.setText(books.get(position).getBookname());
        holder.bookIntroduce.setText(books.get(position).getIntroduction());
        setTheClickEvent(holder,position);
    }

    private void setTheClickEvent(final BookViewHolder holder, final int position) {
        RxView.clicks(holder.cardView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(!(mContext instanceof UserBookActivity)) {
                            Intent intent = new Intent();
                            intent.setClass(mContext, ExchangeBookActivity.class);
                            intent.putExtra("positionOfBooks", position);
                            mContext.startActivity(intent);
                            intent = null;
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        if(books!=null){
            return books.size();
        }else{
            return 0;
        }
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardViewInBookItem)
                CardView cardView;
        @BindView(R.id.bookname)
                TextView bookname;
        @BindView(R.id.bookIntroduce)
                TextView bookIntroduce;
        @BindView(R.id.bookPic)
                ImageView bookPic;

        BookViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
