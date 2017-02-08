package com.example.mezereon.bookexchange.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mezereon.bookexchange.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mezereon on 2017/2/8.
 */

public class NormalRecycleViewAdapter extends RecyclerView.Adapter<NormalRecycleViewAdapter.NormalTextViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;


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
        holder.name.setText("Saber"+position);
        holder.pic.setImageResource(R.drawable.ic_person_black_24dp);
        holder.content.setText("SaberSaberSaberSaberSaberSaberSaberSaberSaberSaberSaberSaberSaberSaberSaberSaberSaberSaberSaberSaberSaber");
        holder.good.setText(position+"11 点赞数");
        holder.title.setText("Title"+position);
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
        return 10;
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