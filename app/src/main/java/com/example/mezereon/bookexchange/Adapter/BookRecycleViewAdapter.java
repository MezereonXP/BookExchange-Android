package com.example.mezereon.bookexchange.Adapter;

import android.content.Context;
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

import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

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

        @Bind(R.id.cardViewInBookItem)
                CardView cardView;
        @Bind(R.id.bookname)
                TextView bookname;
        @Bind(R.id.bookIntroduce)
                TextView bookIntroduce;
        @Bind(R.id.bookPic)
                ImageView bookPic;

        BookViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
