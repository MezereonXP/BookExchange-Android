package com.example.mezereon.bookexchange.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.Module.Comment;
import com.example.mezereon.bookexchange.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mezereon on 2017/2/17.
 */

public class CommentRecycleViewAdapter extends RecyclerView.Adapter<CommentRecycleViewAdapter.CommentViewHolder>{

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    private List<Comment> comments;

    public CommentRecycleViewAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public List<Comment> getComments() { return comments; }

    public void setComments(List<Comment> comments) { this.comments = comments; }

    @Override
    public CommentRecycleViewAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentRecycleViewAdapter.CommentViewHolder(mLayoutInflater.inflate(R.layout.item_article_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentRecycleViewAdapter.CommentViewHolder holder, int position) {
        Picasso.with(mContext).load(comments.get(position).getSrc()).into(holder.circleImageView);
        holder.userName.setText(comments.get(position).getUsername());
        holder.time.setText(comments.get(position).getTime());
        holder.content.setText(comments.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        if(comments!=null){
            return comments.size();
        }else{
            return 0;
        }
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cardViewInComment)
        CardView cardView;
        @Bind(R.id.circleImageViewInComment)
        CircleImageView circleImageView;
        @Bind(R.id.userNameInComment)
        TextView userName;
        @Bind(R.id.timeInComment)
        TextView time;
        @Bind(R.id.textViewInComment)
        TextView content;

        CommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
