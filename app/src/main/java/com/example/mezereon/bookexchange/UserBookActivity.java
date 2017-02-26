package com.example.mezereon.bookexchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.baoyz.widget.PullRefreshLayout;
import com.example.mezereon.bookexchange.Adapter.BookRecycleViewAdapter;
import com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter;
import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.Fragment.BookShowFragment;
import com.example.mezereon.bookexchange.Module.Book;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserBookActivity extends AppCompatActivity {


    @BindView(R.id.recycleViewOfBooksInUserBook)
    RecyclerView bookRecycleView;
    @BindView(R.id.spin_kitInUserBook)
    SpinKitView spinKitViewInBookShow;
    @BindView(R.id.pullRefreshLayoutInUserBook)
    PullRefreshLayout pullRefreshLayout;

    @Inject
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book);
        bindAllViews();
        injectByDagger();
        setTheRecycleView();
        showTheSpinKitView();
        getBooksFromNetWork();
        setViewOverScroll();
    }

    private void setViewOverScroll() {
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        pullRefreshLayout.setColorSchemeColors(R.color.swipe_color_1,
                R.color.swipe_color_2, R.color.swipe_color_3, R.color.swipe_color_4);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBooksFromNetWork();
            }
        });
    }

    public interface GetBookService {
        @GET("getUserBook.php")
        Observable<List<Book>> getAllBooks(@Query("username") String username);
    }

    private void getBooksFromNetWork() {
        GetBookService getBookService=retrofit.create(GetBookService.class);
        Subscription subscription=getBookService.getAllBooks(getSharedPreferences("USERINFO",MODE_PRIVATE).getString("USERNAME","NONE"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Book>>() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) {
                        Log.d("error",e.toString());
                    }
                    @Override
                    public void onNext(List<Book> book) {
                        MyApp.getInstance().setBooks(reveseList(book));
                        setTheAdapterForRecycleView(reveseList(book));
                        hideTheSpinKitView();
                        pullRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private List<Book> reveseList(List<Book> books) {
        List<Book> newList=new ArrayList<Book>();
        int tempCount=0;
        for(int i=0;i<books.size();i++){
            newList.add(books.get(books.size()-1-i));
        }
        return newList;
    }

    private void hideTheSpinKitView() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(spinKitViewInBookShow, "alpha", 1f,0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.play(animator1);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                showTheBook();
            }
        });
        animSet.start();
    }

    private void setTheAdapterForRecycleView(List<Book> book) {
        BookRecycleViewAdapter bookRecycleViewAdapter=new BookRecycleViewAdapter(this);
        bookRecycleViewAdapter.setBooks(book);
        bookRecycleView.setAdapter(bookRecycleViewAdapter);
    }

    private void showTheSpinKitView() {
        spinKitViewInBookShow.setVisibility(View.VISIBLE);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(spinKitViewInBookShow, "alpha", 0f,1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1);
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.start();
    }

    private void showTheBook() {
        bookRecycleView.setVisibility(View.VISIBLE);
        spinKitViewInBookShow.setVisibility(View.GONE);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(bookRecycleView, "alpha", 0f,1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator2);
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.start();
    }


    private void setTheRecycleView() {
        bookRecycleView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
        bookRecycleView.setAdapter(new NormalRecycleViewAdapter(this));
        bookRecycleView.setVisibility(View.GONE);
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void bindAllViews() {
        ButterKnife.bind(this);
    }
}
