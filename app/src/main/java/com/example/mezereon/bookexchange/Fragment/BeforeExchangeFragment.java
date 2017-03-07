package com.example.mezereon.bookexchange.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.mezereon.bookexchange.Adapter.BeforeExchangeRecycleViewAdapter;
import com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter;
import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.Module.Exchange;
import com.example.mezereon.bookexchange.MyApp;
import com.example.mezereon.bookexchange.R;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BeforeExchangeFragment extends Fragment {

    @BindView(R.id.recycleViewOfBooksInBeforeExchange)
    RecyclerView beforeExchangeRecycleView;
    @BindView(R.id.spin_kitInBeforeExchange)
    SpinKitView spinKitViewInBeforeExchange;
    @BindView(R.id.pullRefreshLayoutInBeforeExchange)
    PullRefreshLayout pullRefreshLayout;

    @Inject
    Retrofit retrofit;


    private View view;

    public interface GetExchangeService {
        @GET("getExchange.php")
        Observable<List<Exchange>> getExchanges(@Query("username") String username);
    }

    public BeforeExchangeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_before_exchange, container, false);
        bindAllTheView();
        injectByDagger();
        setTheRecycleView();
        showTheSpinKitView();
        getExchangeFromNetWork();
        setViewOverScroll();
        return view;
    }

    private void setViewOverScroll() {
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        pullRefreshLayout.setColorSchemeColors(R.color.swipe_color_1,
                R.color.swipe_color_2, R.color.swipe_color_3, R.color.swipe_color_4);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getExchangeFromNetWork();
            }
        });
    }

    private void getExchangeFromNetWork() {
        GetExchangeService getExchangeService=retrofit.create(GetExchangeService.class);
        Subscription subscription=getExchangeService.getExchanges(getActivity()
                .getSharedPreferences("USERINFO",getActivity().MODE_PRIVATE).getString("USERNAME","NONE"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Exchange>>() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(getActivity(),"网络出错", Toast.LENGTH_SHORT).show(); }
                    @Override
                    public void onNext(List<Exchange> exchanges) {
                        MyApp.getInstance().setExchanges(reveseList(exchanges));
                        setTheAdapterForRecycleView(reveseList(exchanges));
                        hideTheSpinKitView();
                        pullRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void hideTheSpinKitView() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(spinKitViewInBeforeExchange, "alpha", 1f,0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.play(animator1);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                showTheBeforeExchange();
            }
        });
        animSet.start();
    }

    private void showTheBeforeExchange() {
        beforeExchangeRecycleView.setVisibility(View.VISIBLE);
        spinKitViewInBeforeExchange.setVisibility(View.GONE);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(beforeExchangeRecycleView, "alpha", 0f,1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator2);
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.start();
    }

    private void setTheAdapterForRecycleView(List<Exchange> exchanges) {
        BeforeExchangeRecycleViewAdapter beforeExchangeRecycleViewAdapter=new BeforeExchangeRecycleViewAdapter(view.getContext());
        beforeExchangeRecycleViewAdapter.setExchanges(exchanges);
        beforeExchangeRecycleView.setAdapter(beforeExchangeRecycleViewAdapter);
    }

    private List<Exchange> reveseList(List<Exchange> exchanges) {
        List<Exchange> newList=new ArrayList<Exchange>();
        int tempCount=0;
        for(int i=0;i<exchanges.size();i++){
            newList.add(exchanges.get(exchanges.size()-1-i));
        }
        return newList;
    }

    private void showTheSpinKitView() {
        spinKitViewInBeforeExchange.setVisibility(View.VISIBLE);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(spinKitViewInBeforeExchange, "alpha", 0f,1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1);
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.start();
    }

    private void setTheRecycleView() {
        beforeExchangeRecycleView.setLayoutManager(new LinearLayoutManager(view.getContext()));//这里用线性显示 类似于listview
        beforeExchangeRecycleView.setAdapter(new NormalRecycleViewAdapter(view.getContext()));
        beforeExchangeRecycleView.setVisibility(View.GONE);
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void bindAllTheView() {
        ButterKnife.bind(this,view);
    }


}
