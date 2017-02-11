package com.example.mezereon.bookexchange.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mezereon.bookexchange.Module.Article;
import com.example.mezereon.bookexchange.Module.Forum;
import com.example.mezereon.bookexchange.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mezereon on 2017/2/9.
 */

public class NormalRecycleViewAdapter2 extends RecyclerView.Adapter<NormalRecycleViewAdapter2.NormalTextViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public List<Forum> getArticles() {
        return forums;
    }

    public void setArticles(List<Forum> forums) {
        this.forums = forums;
    }

    private List<Forum> forums;

    public NormalRecycleViewAdapter2(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }



    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, final int position) {
        holder.name.setText(forums.get(position).getUsername());
        Picasso.with(mContext).load(forums.get(position).getSrc()).into(holder.pic);
        holder.content.setText(Html.fromHtml(forums.get(position).getIntroduction()));
        holder.good.setText(position+"11 点赞数");
        holder.title.setText(forums.get(position).getTitle());
        holder.numOfComment.setText(position+"22 评论");
        holder.concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"concern "+position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(forums==null){
            return 0;
        }else{
            return forums.size();
        }
    }


    public static class NormalTextViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.circleImageView)
        CircleImageView pic;
        @Bind(R.id.textView5)
        TextView name;
        @Bind(R.id.textView7)
        TextView title;
        @Bind(R.id.textView8)
        TextView content;
        @Bind(R.id.textView9)
        TextView good;
        @Bind(R.id.textView10)
        TextView numOfComment;
        @Bind(R.id.button3)
        Button concern;
        @Bind(R.id.cardView)
        CardView card;

        NormalTextViewHolder(View view) {
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