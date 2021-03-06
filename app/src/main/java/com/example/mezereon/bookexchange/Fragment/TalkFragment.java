package com.example.mezereon.bookexchange.Fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter;
import com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter2;
import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.Module.Article;
import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.Module.Forum;
import com.example.mezereon.bookexchange.MyApp;
import com.example.mezereon.bookexchange.R;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollStateListener;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class TalkFragment extends Fragment {


    @BindView(R.id.talk)
    RecyclerView talk;
    @BindView(R.id.spin_kit2)
    SpinKitView spinKitView;
    @BindView(R.id.swipeInTalk)
    PullRefreshLayout swipeRefreshLayout;

    @Inject
    Retrofit retrofit;

    private View v;
    public TalkFragment() {
        // Required empty public constructor
    }

    public interface GetForumService {
        @GET("getForumInfo.php")
        Observable<List<Forum>> getAllForums();
    }

    private void setTheRefreshEvent() {
        swipeRefreshLayout.setColorSchemeColors(R.color.swipe_color_1,
                R.color.swipe_color_2, R.color.swipe_color_3, R.color.swipe_color_4);
        swipeRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_talk, container, false);
        bindAllViews();
        injectByDagger();
        setTheRecycleView();
        showTheSpinKitView();
        getTalkFromNetWork();
        setTheRefreshEvent();
        setViewOverScroll();
        return v;
    }

    private void setViewOverScroll() {
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTalkFromNetWork();
            }
        });
    }

    public void getForumsByName(List<Forum> forums) {
        MyApp.getInstance().setForums(reveseList(forums));
        setTheAdapterForRecycleView(reveseList(forums));
    }

    private void bindAllViews() {
        ButterKnife.bind(this,v);
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }


    private void showTheSpinKitView() {
        spinKitView.setVisibility(View.VISIBLE);
    }


    private void setTheRecycleView() {
        talk.setLayoutManager(new LinearLayoutManager(v.getContext()));//这里用线性显示 类似于listview
        talk.setAdapter(new NormalRecycleViewAdapter(v.getContext()));
        talk.setVisibility(View.GONE);
    }

    private void getTalkFromNetWork() {
        GetForumService getBookService=retrofit.create(GetForumService.class);
        Subscription subscription=getBookService.getAllForums()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Forum>>() {
                    @Override
                    public void onCompleted() {
                        //Toasty.success(getActivity(),"加载完成", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(getActivity(),"网络出错", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(List<Forum> forums) {
                        MyApp.getInstance().setForums(reveseList(forums));
                        setTheAdapterForRecycleView(reveseList(forums));
                        hideTheSpinKitView();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void setTheAdapterForRecycleView(List<Forum> forums) {
        NormalRecycleViewAdapter2 normalRecycleViewAdapter2=new NormalRecycleViewAdapter2(v.getContext());
        normalRecycleViewAdapter2.setArticles(forums);
        talk.setAdapter(normalRecycleViewAdapter2);
    }

    private List<Forum> reveseList(List<Forum> forums) {
        List<Forum> newList=new ArrayList<Forum>();
        int tempCount=0;
        for(int i=0;i<forums.size();i++){
            newList.add(forums.get(forums.size()-1-i));
        }
        return newList;
    }

    private void hideTheSpinKitView() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(spinKitView, "alpha", 1f,0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                showTheTalk();
            }
        });
        animSet.start();
    }


    private void showTheTalk() {
        talk.setVisibility(View.VISIBLE);
        spinKitView.setVisibility(View.GONE);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(talk, "alpha", 0f,1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator2);
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
