package com.example.mezereon.bookexchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.example.mezereon.bookexchange.Adapter.BookListRecycleViewAdapter;
import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.Module.Book;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Mezereon on 2017/2/25.
 */

public class ExchangeBookActivity extends AppCompatActivity{


    @BindView(R.id.textView22)
    TextView bookName;
    @BindView(R.id.textView23)
    TextView introduction;
    @BindView(R.id.imageView8)
    ImageView bookPic;
    @BindView(R.id.textView24)
    TextView wantKind;
    @BindView(R.id.button7)
    Button checkMyBook;
    @BindView(R.id.blackMask)
    ImageView blackMask;
    @BindView(R.id.titleOfYourBooks)
    TextView title;
    @BindView(R.id.scrollViewOfYourBooks)
    ScrollView scrollViewOfYourBooks;
    @BindView(R.id.yourBookList)
    RecyclerView bookList;

    @Inject
    Retrofit retrofit;

    private List<Book> books,yourBooks;
    private int positionOfBooks;
    private SharedPreferences sharePreference;
    private ProgressDialog progressDialog;

    public interface GetBooksService {
        @GET("getUserBook.php")
        Observable<List<Book>> getBook(@Query("username") String username);
    }

    public interface ExchangeService {
        @GET("exchange.php")
        Observable<Void> getBook(@Query("usernamea") String usernamea,
                                       @Query("usernameb") String usernameb,
                                       @Query("ida") String ida,
                                       @Query("idb") String idb,
                                       @Query("date") String date);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheSatusBar();
        setContentView(R.layout.activity_exchange_book);
        getThePositionFromIntent();
        initTheBooks();
        bindAllTheView();
        injectByDagger();
        initViewResource();
        initProgressDialog();
        initSharePreference();
        animateTheInfoOfBook();
        setTheClickEvent();
    }

    private void animateTheInfoOfBook() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                Glider.glide(Skill.ElasticEaseOut, 1200, ObjectAnimator.ofFloat(introduction, "translationX", -1200, 0)),
                Glider.glide(Skill.ElasticEaseOut, 1200, ObjectAnimator.ofFloat(bookName, "translationX", -1200, 0)),
                Glider.glide(Skill.ElasticEaseOut, 1200, ObjectAnimator.ofFloat(bookPic, "translationX", -1200, 0)),
                Glider.glide(Skill.ElasticEaseOut, 1200, ObjectAnimator.ofFloat(wantKind, "translationX", -1200, 0))
        );
        set.setDuration(MyApp.MIDDLE_DURATION);
        set.start();
    }

    private void setTheSatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setStatusBarColor(Color.parseColor(MyApp.COLOR_STATUSBAR));
        }
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void initSharePreference() {
        sharePreference=getSharedPreferences("USERINFO",MODE_PRIVATE);
    }

    private void initProgressDialog() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("获取您的书籍中....");
        progressDialog.setTitle("System");
    }

    private void setTheClickEvent() {
        RxView.clicks(checkMyBook)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showTheBlackMask();
                        showTheProgressDialog();
                        getYourBooksFromNetwork();
                    }
                });
    }

    private void getYourBooksFromNetwork() {
        GetBooksService getBookService=retrofit.create(GetBooksService.class);
        Subscription subscription = getBookService.getBook(sharePreference.getString("USERNAME","NONE"))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<List<Book>>() {
                                            @Override
                                            public void onCompleted() { }
                                            @Override
                                            public void onError(Throwable e) {
                                                Log.e("error",e.toString());
                                            }
                                            @Override
                                            public void onNext(List<Book> books) {
                                                Log.d("books",books.size()+"");
                                                yourBooks=books;
                                                progressDialog.dismiss();
                                                showTheTitle();
                                            }
                                        });
    }

    private void showTheList() {
        BookListRecycleViewAdapter adapter=new BookListRecycleViewAdapter(this);
        adapter.setBooks(yourBooks);
        bookList.setLayoutManager(new LinearLayoutManager(this));
        bookList.setAdapter(adapter);
        scrollViewOfYourBooks.setVisibility(View.VISIBLE);
        scrollViewOfYourBooks.setZ(100);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(scrollViewOfYourBooks, "alpha", 0f,1f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(scrollViewOfYourBooks,"translationY",500,0);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1).with(animator2);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.start();
    }

    private void showTheTitle() {
        title.setVisibility(View.VISIBLE);
        title.setZ(100);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(title, "alpha", 0f,1f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(title,"translationY",500,0);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1).with(animator2);
        animSet.setDuration(MyApp.SHORT_DURATION);
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                showTheList();
            }
        });
        animSet.start();
    }

    private void showTheProgressDialog() {
        progressDialog.show();
    }

    private void showTheBlackMask() {
        blackMask.setVisibility(View.VISIBLE);
        blackMask.setZ(100);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(blackMask, "alpha", 0f,0.7f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1);
        animSet.setDuration(MyApp.SHORT_DURATION);
        animSet.start();
    }

    private void initTheBooks() {
        books=MyApp.getInstance().getBooks();
    }

    private void initViewResource() {
        Picasso.with(this).load(books.get(positionOfBooks).getSrc()).into(bookPic);
        bookName.setText("书名 : "+books.get(positionOfBooks).getBookname());
        introduction.setText("介绍 : "+books.get(positionOfBooks).getIntroduction());
        wantKind.setText("想要的类型 : "+books.get(positionOfBooks).getWantkind());
    }

    private void bindAllTheView() {
        ButterKnife.bind(this);
    }

    private void getThePositionFromIntent() {
        positionOfBooks=getIntent().getIntExtra("positionOfBooks",-1);
        if(positionOfBooks==-1){
            throw new NullPointerException();
        }
    }

    @Override
    public void onBackPressed() {
        if(blackMask.getVisibility()==View.VISIBLE){
            hideTheListAndTitle();
        }else{
            finish();
        }
    }

    private void hideTheListAndTitle() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(blackMask, "alpha", 0.7f,0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(title, "alpha", 1f,0f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(scrollViewOfYourBooks, "alpha", 1f,0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1).with(animator2).with(animator3);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                blackMask.setVisibility(View.INVISIBLE);
                title.setVisibility(View.INVISIBLE);
                scrollViewOfYourBooks.setVisibility(View.INVISIBLE);
            }
        });
        animSet.setDuration(MyApp.SHORT_DURATION);
        animSet.start();
    }

    public void exchange(Book book) {
        setTheProgressBar();
        ExchangeService exchangeService=retrofit.create(ExchangeService.class);
        Subscription subscription=exchangeService.getBook(sharePreference.getString("USERNAME","NONE")
                                ,books.get(positionOfBooks).getUsername(),book.getId()+""
                                ,books.get(positionOfBooks).getId()+"",MyApp.getInstance().returnTime())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {}
                            @Override
                            public void onError(Throwable e) { Log.e("error",e.toString());}
                            @Override
                            public void onNext(Void v) {
                                progressDialog.dismiss();
                                Toasty.success(ExchangeBookActivity.this,"交换成功",Toast.LENGTH_SHORT,true).show();
                                finish();
                            }
                        });
    }

    private void setTheProgressBar() {
        progressDialog.setMessage("正在交换中....");
        progressDialog.show();
    }
}
