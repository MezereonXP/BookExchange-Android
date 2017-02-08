package com.example.mezereon.bookexchange.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter;
import com.example.mezereon.bookexchange.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TalkFragment extends Fragment {


    @Bind(R.id.talk)
    RecyclerView talk;
    public TalkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_talk, container, false);
        ButterKnife.bind(this,v);
        talk.setLayoutManager(new LinearLayoutManager(v.getContext()));//这里用线性显示 类似于listview
        talk.setAdapter(new NormalRecycleViewAdapter(v.getContext()));
        return v;
    }

}
