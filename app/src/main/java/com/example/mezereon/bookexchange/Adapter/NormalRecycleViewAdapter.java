package com.example.mezereon.bookexchange.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mezereon.bookexchange.Module.Article;
import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.R;
import com.example.mezereon.bookexchange.ReadActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;


import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mezereon on 2017/2/8.
 */

public class NormalRecycleViewAdapter extends RecyclerView.Adapter<NormalRecycleViewAdapter.NormalTextViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    private List<Article> articles;

    public NormalRecycleViewAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }



    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, final int position) {
        holder.name.setText(articles.get(position).getUsername());
        Picasso.with(mContext).load(articles.get(position).getAuthorpic()).into(holder.pic);
        holder.content.setText(Html.fromHtml(articles.get(position).getIntroduction()));
        holder.good.setText(position+"11 点赞数");
        holder.title.setText(articles.get(position).getTitle());
        holder.numOfComment.setText(position+"22 评论");
        holder.concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"concern "+position,Toast.LENGTH_SHORT).show();
            }
        });
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnToRead(position);
            }
        });

    }

    private void turnToRead(int position) {
        Intent intent=new Intent();
        intent.putExtra("position",position);
        intent.setClass(mContext, ReadActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(articles==null){
            return 0;
        }else{
            return articles.size();
        }
    }


    public static class NormalTextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.circleImageView)
        CircleImageView pic;
        @BindView(R.id.textView5)
        TextView name;
        @BindView(R.id.textView7)
        TextView title;
        @BindView(R.id.textView8)
        TextView content;
        @BindView(R.id.textView9)
        TextView good;
        @BindView(R.id.textView10)
        TextView numOfComment;
        @BindView(R.id.button3)
        Button concern;
        @BindView(R.id.cardView)
        CardView card;
        @BindView(R.id.commentLayout)
        RelativeLayout relativeLayout;

        NormalTextViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}