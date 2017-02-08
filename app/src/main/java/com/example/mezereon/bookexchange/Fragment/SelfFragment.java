package com.example.mezereon.bookexchange.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mezereon.bookexchange.Adapter.MyRecycleViewAdapter;
import com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter;
import com.example.mezereon.bookexchange.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelfFragment extends Fragment {


    @Bind(R.id.setting)
    RecyclerView setting;
    @Bind(R.id.imageView3)
    ImageView bg;
    public SelfFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_self, container, false);
        ButterKnife.bind(this,v);
        setting.setLayoutManager(new LinearLayoutManager(v.getContext()));//这里用线性显示 类似于listview
        setting.setAdapter(new MyRecycleViewAdapter(v.getContext()));
        OverScrollDecoratorHelper.setUpOverScroll(setting,OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        return v;
    }

}
