package com.example.mezereon.bookexchange.Adapter;

/**
 * Created by Mezereon on 2017/2/25.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mezereon.bookexchange.ExchangeBookActivity;
import com.example.mezereon.bookexchange.HomeActivity;
import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.MyApp;
import com.example.mezereon.bookexchange.R;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
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
import com.example.mezereon.bookexchange.R;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

/**
 * Created by Mezereon on 2017/2/16.
 */

public class BookListRecycleViewAdapter extends RecyclerView.Adapter<BookListRecycleViewAdapter.BookViewHolder>{

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    private List<Book> books;

    public BookListRecycleViewAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public List<Book> getBooks() { return books; }

    public void setBooks(List<Book> books) { this.books = books; }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookViewHolder(mLayoutInflater.inflate(R.layout.item_book_of_list, parent, false));
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        holder.bookname.setText(books.get(position).getBookname());
        setTheClickEvent(holder,position);
    }

    private void setTheClickEvent(final BookViewHolder holder, final int position) {
        RxView.clicks(holder.cardView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDialog(position);
                    }
                });
    }

    private void showDialog(final int position) {
        new AlertDialog.Builder(mContext).setTitle("系统提示")//设置对话框标题
                .setMessage("确认使用您的《"+ books.get(position).getBookname()+"》和对方进行交换吗？")//设置显示的内5容
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        ExchangeBookActivity activity= (ExchangeBookActivity) mContext;
                        activity.exchange(books.get(position));
                    }
                }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        }).show();//在按键响应事件中显示此对话框
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

        @BindView(R.id.cardViewInBookList)
        CardView cardView;
        @BindView(R.id.bookNameInBookList)
        TextView bookname;

        BookViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
