package com.example.mezereon.bookexchange.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mezereon.bookexchange.Adapter.BookRecycleViewAdapter;
import com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter;
import com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter2;
import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.Module.Forum;
import com.example.mezereon.bookexchange.MyApp;
import com.example.mezereon.bookexchange.R;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Mezereon on 2017/2/15.
 */

public class BookShowFragment extends Fragment {

    //Constructor of the BookShowFragment
    public BookShowFragment(){ }

    private View viewOnBookShowFragment;

    @Bind(R.id.recycleViewOfBooks)
    RecyclerView bookRecycleView;
    @Bind(R.id.spin_kitInBookShow)
    SpinKitView spinKitViewInBookShow;

    @Inject
    Retrofit retrofit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewOnBookShowFragment= inflater.inflate(R.layout.fragment_bookshow, container, false);
        bindAllViews();
        injectByDagger();
        setTheRecycleView();
        showTheSpinKitView();
        getTalksFromNetWork();
        return viewOnBookShowFragment;
    }

    public interface GetBookService {
        @GET("getAllBook.php")
        Observable<List<Book>> getAllBooks();
    }

    private void getTalksFromNetWork() {
        GetBookService getBookService=retrofit.create(GetBookService.class);
        Subscription subscription=getBookService.getAllBooks()
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
                        MyApp.getInstance().setBooks(book);
                        setTheAdapterForRecycleView(book);
                        hideTheSpinKitView();
                    }
                });
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
        BookRecycleViewAdapter bookRecycleViewAdapter=new BookRecycleViewAdapter(viewOnBookShowFragment.getContext());
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
        bookRecycleView.setLayoutManager(new LinearLayoutManager(viewOnBookShowFragment.getContext()));//这里用线性显示 类似于listview
        bookRecycleView.setAdapter(new NormalRecycleViewAdapter(viewOnBookShowFragment.getContext()));
        bookRecycleView.setVisibility(View.GONE);
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void bindAllViews() {
        ButterKnife.bind(this,viewOnBookShowFragment);
    }

}
