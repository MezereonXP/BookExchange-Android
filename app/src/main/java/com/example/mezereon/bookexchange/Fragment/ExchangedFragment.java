package com.example.mezereon.bookexchange.Fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.mezereon.bookexchange.Adapter.ExchangedRecycleViewAdapter;
import com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter;
import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
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
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangedFragment extends Fragment {

    @BindView(R.id.recycleViewOfBooksInExchanged)
    RecyclerView recyclerViewInExchanged;
    @BindView(R.id.spin_kitInExchanged)
    SpinKitView spinKitView;
    @BindView(R.id.pullRefreshLayoutInExchanged)
    PullRefreshLayout pullRefreshLayout;

    @Inject
    Retrofit retrofit;
    public interface GetExchangeService {
        @GET("getExchange.php")
        Observable<List<Exchange>> getExchanges(@Query("username") String username,
                                                @Query("status") String status);
    }

    private View view;

    public ExchangedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_exchanged, container, false);
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
                .getSharedPreferences("USERINFO",
                        getActivity().MODE_PRIVATE).getString("USERNAME","NONE"),"2")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Exchange>>() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(getActivity(),"网络出错", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(List<Exchange> exchanges) {
                        setTheAdapterForRecycleView(reveseList(exchanges));
                        hideTheSpinKitView();
                        pullRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void hideTheSpinKitView() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(spinKitView, "alpha", 1f,0f);
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
        recyclerViewInExchanged.setVisibility(View.VISIBLE);
        spinKitView.setVisibility(View.GONE);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(recyclerViewInExchanged, "alpha", 0f,1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator2);
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.start();
    }

    private void setTheAdapterForRecycleView(List<Exchange> exchanges) {
        ExchangedRecycleViewAdapter exchangedRecycleViewAdapter=new ExchangedRecycleViewAdapter(view.getContext());
        exchangedRecycleViewAdapter.setExchanges(exchanges);
        recyclerViewInExchanged.setAdapter(exchangedRecycleViewAdapter);
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
        spinKitView.setVisibility(View.VISIBLE);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(spinKitView, "alpha", 0f,1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1);
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.start();
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void bindAllTheView() {
        ButterKnife.bind(this,view);
    }

    private void setTheRecycleView() {
        recyclerViewInExchanged.setLayoutManager(new LinearLayoutManager(view.getContext()));//这里用线性显示 类似于listview
        recyclerViewInExchanged.setAdapter(new NormalRecycleViewAdapter(view.getContext()));
        recyclerViewInExchanged.setVisibility(View.GONE);
    }

}
