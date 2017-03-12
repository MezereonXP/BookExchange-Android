package com.example.mezereon.bookexchange.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter;
import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.HomeActivity;
import com.example.mezereon.bookexchange.LoginActivity;
import com.example.mezereon.bookexchange.Module.Article;
import com.example.mezereon.bookexchange.Module.Book;
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

public class CommentFragment extends Fragment implements
        TabHost.TabContentFactory, GestureDetector.OnGestureListener {

    private GestureDetector detector;
    private View viewOnCommentFragment;
    //避免ViewPager在一开始创建
    private boolean hasLazyLoad = false;
    private HomeActivity homeActivity=(HomeActivity)this.getActivity();

    @BindView(R.id.recycle)
    RecyclerView comment;
    @BindView(R.id.spin_kit)
    SpinKitView spinKitView;
    @BindView(R.id.swipeInComment)
    PullRefreshLayout swipeRefreshLayout;

    @Inject
    Retrofit retrofit;
    public CommentFragment() {
        // Required empty public constructor
    }

    public void setHasLazyLoad(boolean hasLazyLoad) {
        this.hasLazyLoad = hasLazyLoad;
    }

    /**
     * 懒加载,防止ViewPager重复创建
     */
    protected void onLazyLoad() { }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && !hasLazyLoad) {
            onLazyLoad();
            hasLazyLoad = true;
        }
    }

    public void getBooksByName(List<Article> articles) {
        MyApp.getInstance().setArticles(reveseList(articles));
        setTheAdapter(reveseList(articles));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hasLazyLoad = false;
    }

    public interface GetArticleService {
        @GET("getArticleInfo.php")
        Observable<List<Article>> getAllArticles();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewOnCommentFragment=inflater.inflate(R.layout.fragment_comment, container, false);
        DaggerAppComponent.builder().build().inject(this);
        ButterKnife.bind(this,viewOnCommentFragment);
        initTheView();
        getTheArticlesFromNetWork();
        setTheGestureDetector();
        setViewOverScroll();
        setTheRefreshEvent();
        return viewOnCommentFragment;
    }

    private void setTheRefreshEvent() {
        swipeRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        swipeRefreshLayout.setColorSchemeColors(R.color.swipe_color_1,
                R.color.swipe_color_2, R.color.swipe_color_3, R.color.swipe_color_4);
    }

    private void initTheView() {
        comment.setLayoutManager(new LinearLayoutManager(viewOnCommentFragment.getContext()));
        comment.setAdapter(new NormalRecycleViewAdapter(viewOnCommentFragment.getContext()));
        comment.setVisibility(View.GONE);
        spinKitView.setVisibility(View.VISIBLE);
    }

    private void setViewOverScroll() {
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTheArticlesFromNetWork();
            }
        });
    }

    private void setTheGestureDetector() {
        final GestureDetector mGestureDetector = new GestureDetector(
                getActivity(), this);
        HomeActivity.MyOnTouchListener myOnTouchListener = new HomeActivity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                boolean result = mGestureDetector.onTouchEvent(ev);
                return result;
            }
        };
        if(homeActivity==null){
            homeActivity= (HomeActivity) getActivity();
        }
        homeActivity.registerMyOnTouchListener(myOnTouchListener);

    }


    private void getTheArticlesFromNetWork() {
        GetArticleService getBookService=retrofit.create(GetArticleService.class);
        Subscription subscription=getBookService.getAllArticles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Article>>() {
                    @Override
                    public void onCompleted() {
                       //Toasty.success(getActivity(),"加载完成", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(getActivity(),"网络出错", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(List<Article> articles) {
                        MyApp.getInstance().setArticles(reveseList(articles));
                        hideTheSpinKitView();
                        setTheAdapter(reveseList(articles));
                        swipeRefreshLayout.setRefreshing(false);

                    }
                });
    }

    private void setTheAdapter(List<Article> articles) {
        NormalRecycleViewAdapter normalRecycleViewAdapter=new NormalRecycleViewAdapter(viewOnCommentFragment.getContext());
        normalRecycleViewAdapter.setArticles(articles);
        comment.setAdapter(normalRecycleViewAdapter);
    }

    private List<Article> reveseList(List<Article> articles) {
        List<Article> newList=new ArrayList<Article>();
        int tempCount=0;
        for(int i=0;i<articles.size();i++){
            newList.add(articles.get(articles.size()-1-i));
        }
        return newList;
    }


    private void hideTheSpinKitView() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(spinKitView, "alpha", 1f,0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) { showTheComment(); }
        });
        animSet.start();
    }


    private void showTheComment() {
        comment.setVisibility(View.VISIBLE);
        spinKitView.setVisibility(View.GONE);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(comment, "alpha", 0f,1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator2);
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) { return false; }

    @Override
    public void onLongPress(MotionEvent e) { }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        if(distanceY>0){
            homeActivity.hideTheToolbarByCommentFragment();
        }else if(distanceY<0){
            homeActivity.showTheToolbarByCommentFragment();
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


    @Override
    public View createTabContent(String tag) {
        return null;
    }
}
