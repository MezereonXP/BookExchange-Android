package com.example.mezereon.bookexchange.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mezereon.bookexchange.Adapter.MyRecycleViewAdapter;
import com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter;
import com.example.mezereon.bookexchange.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelfFragment extends Fragment {


    @Bind(R.id.setting)
    RecyclerView setting;
    @Bind(R.id.imageView3)
    ImageView bg;
    @Bind(R.id.userpic)
    CircleImageView pic;
    @Bind(R.id.textView11)
    TextView name;
    @Bind(R.id.textView12)
    TextView sign;

    private boolean hasLazyLoad = false;
    private View viewOnSelfFragment;
    public void setHasLazyLoad(boolean hasLazyLoad) {
        this.hasLazyLoad = hasLazyLoad;
    }

    /**
     * 懒加载,防止ViewPager重复创建
     */
    protected void onLazyLoad() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && !hasLazyLoad) {
            onLazyLoad();
            hasLazyLoad = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hasLazyLoad = false;
    }
    public SelfFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewOnSelfFragment= inflater.inflate(R.layout.fragment_self, container, false);
        bindAllTheViews();
        setTheRecycleView();
        setViewOverScroll();
        setTheUserInfo();
        return viewOnSelfFragment;
    }

    private void bindAllTheViews() {
        ButterKnife.bind(this,viewOnSelfFragment);
    }

    private void setTheRecycleView() {
        setting.setLayoutManager(new LinearLayoutManager(viewOnSelfFragment.getContext()));//这里用线性显示 类似于listview
        setting.setAdapter(new MyRecycleViewAdapter(viewOnSelfFragment.getContext()));
    }

    private void setViewOverScroll() {
        OverScrollDecoratorHelper.setUpOverScroll(setting,OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
    }

    private void setTheUserInfo() {
        SharedPreferences sp=getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        Picasso.with(viewOnSelfFragment.getContext()).load(sp.getString("USERSRC","NONE")).into(pic);
        name.setText(sp.getString("USERNAME","none"));
        sign.setText(sp.getString("USERSIGNATRUE","NONE"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
