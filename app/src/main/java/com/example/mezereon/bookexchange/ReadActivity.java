package com.example.mezereon.bookexchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mezereon.bookexchange.Adapter.BookRecycleViewAdapter;
import com.example.mezereon.bookexchange.Adapter.CommentRecycleViewAdapter;
import com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter;
import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.Fragment.BookShowFragment;
import com.example.mezereon.bookexchange.Module.Article;
import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.Module.Comment;
import com.example.mezereon.bookexchange.Module.SimpleArticle;
import com.github.ybq.android.spinkit.SpinKitView;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ReadActivity extends AppCompatActivity {

    @BindView(R.id.textView14)
    TextView title;
    @BindView(R.id.textView15)
    TextView content;
    @BindView(R.id.readtitle)
    ImageView back;
    @BindView(R.id.spin_kitInRead)
    SpinKitView spinKitViewInRead;
    @BindView(R.id.scrollViewInRead)
    ScrollView scrollViewInRead;
    @BindView(R.id.userNameInRead)
    TextView userName;
    @BindView(R.id.userPicInRead)
    CircleImageView userPic;
    @BindView(R.id.commentRecycleView)
    RecyclerView comments;
    @BindView(R.id.addComment)
    ImageView addComment;


    private int positionFromCommentFragment;
    private int positionFromTalkFragment;

    @Inject
    Retrofit retrofit;
    public interface GetBookService {
        @GET("getArticleContent.php")
        Observable<List<SimpleArticle>> getAllBooks(@Query("id")int id);
    }

    public interface GetCommentForArticlesService {
        @GET("getArticleComment.php")
        Observable<List<Comment>> getCommentForArticles(@Query("id")int id);
    }

    public interface GetCommentForForumsService {
        @GET("getForumComment.php")
        Observable<List<Comment>> getCommentForForums(@Query("id")int id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheStatusBar();
        setContentView(R.layout.activity_read);
        getThePosition();
        bindAllTheViews();
        injectByDagger();
        showTheSpinKitView();
        setTheUserPicAndName();
        setTheTitleAndContent();
        setTheBackButton();
        if(positionFromCommentFragment!=-1){
            getTheContentFromNetWork();
        }else{
            showTheTalk();
        }
        loadTheComment();
        setTheScrollView();
        setTheAddCommentTextView();
    }

    private void setTheBackButton() {
        RxView.clicks(back)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
    }

    private void setTheAddCommentTextView() {
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnToAddCommentActivity(positionFromCommentFragment,positionFromTalkFragment);
            }
        });
    }

    private void turnToAddCommentActivity(int positionFromCommentFragment, int positionFromTalkFragment) {
        Intent intent =new Intent();
        intent.putExtra("positionFromCommentFragment",positionFromCommentFragment);
        intent.putExtra("positionFromTalkFragment",positionFromTalkFragment);
        intent.setClass(this,AddCommentActivity.class);
        startActivity(intent);
    }

    private void setTheScrollView() {
        scrollViewInRead.post(new Runnable() {
            @Override
            public void run() {
                scrollViewInRead.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void loadTheComment() {
        initTheRecycleView();
        if(positionFromCommentFragment!=-1){
            loadTheCommentForArticles();
        }else{
            loadTheCommentForTalks();
        }
    }

    private void initTheRecycleView() {
        comments.setLayoutManager(new LinearLayoutManager(this));
        comments.setAdapter(new CommentRecycleViewAdapter(this));
    }

    private void loadTheCommentForTalks() {
        GetCommentForForumsService getCommentForForumsService=retrofit.create(GetCommentForForumsService.class);
        Subscription subscription=getCommentForForumsService.getCommentForForums(MyApp.getInstance().getForums().get(positionFromTalkFragment).getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Comment>>() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) { }
                    @Override
                    public void onNext(List<Comment> comments) {
                        CommentRecycleViewAdapter commentRecycleViewAdapter=new CommentRecycleViewAdapter(ReadActivity.this);
                        commentRecycleViewAdapter.setComments(reveseList(comments));
                        ReadActivity.this.comments.setAdapter(commentRecycleViewAdapter);
                    }
                });
    }

    private void loadTheCommentForArticles() {
        GetCommentForArticlesService getCommentForArticleService=retrofit.create(GetCommentForArticlesService.class);
        Subscription subscription=getCommentForArticleService.getCommentForArticles(MyApp.getInstance().getArticles().get(positionFromCommentFragment).getId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<List<Comment>>() {
                                            @Override
                                            public void onCompleted() { }
                                            @Override
                                            public void onError(Throwable e) { }
                                            @Override
                                            public void onNext(List<Comment> comments) {
                                                CommentRecycleViewAdapter commentRecycleViewAdapter=new CommentRecycleViewAdapter(ReadActivity.this);
                                                commentRecycleViewAdapter.setComments(reveseList(comments));
                                                ReadActivity.this.comments.setAdapter(commentRecycleViewAdapter);
                                            }
                                        });
    }

    private List<Comment> reveseList(List<Comment> comments) {
        List<Comment> newList=new ArrayList<Comment>();
        for(int i=0;i<comments.size();i++){
            newList.add(comments.get(comments.size()-1-i));
        }
        return newList;
    }

    private void setTheUserPicAndName() {
        if(positionFromCommentFragment!=-1){
            Picasso.with(this).load(MyApp.getInstance().getArticles().get(positionFromCommentFragment).getAuthorpic()).into(userPic);
            userName.setText(MyApp.getInstance().getArticles().get(positionFromCommentFragment).getUsername());
        }else{
            Picasso.with(this).load(MyApp.getInstance().getForums().get(positionFromTalkFragment).getSrc()).into(userPic);
            userName.setText(MyApp.getInstance().getForums().get(positionFromTalkFragment).getUsername());
        }

    }

    private void showTheTalk() {
        hideTheSpinKitView();
        showTheArticle();
    }

    private void getThePosition() {
         positionFromCommentFragment=getIntent().getIntExtra("position",-1);
         positionFromTalkFragment=getIntent().getIntExtra("position2",-1);
    }

    private void hideTheSpinKitView() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(spinKitViewInRead, "alpha", 1f,0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.play(animator1);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                spinKitViewInRead.setVisibility(View.GONE);
            }
        });
        animSet.start();
    }

    private void setTheStatusBar() {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setStatusBarColor(Color.parseColor(MyApp.COLOR_TOOLBAR));
    }

    private void showTheSpinKitView() {
        spinKitViewInRead.setVisibility(View.VISIBLE);
        AnimatorSet animSet = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(spinKitViewInRead, "alpha", 0f,1f);
        animSet.play(animator1);
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.start();
    }

    private void showTheArticle() {
        scrollViewInRead.setVisibility(View.VISIBLE);
        AnimatorSet animSet = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(scrollViewInRead, "alpha", 0f,1f);
        animSet.play(animator1);
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.start();
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void getTheContentFromNetWork() {
        GetBookService getBookService=retrofit.create(GetBookService.class);
        Subscription subscription=getBookService.getAllBooks(MyApp.getInstance().getArticles().get(positionFromCommentFragment!=-1?positionFromCommentFragment:positionFromTalkFragment).getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SimpleArticle>>() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) {
                        Log.d("error",e.toString());
                    }
                    @Override
                    public void onNext(List<SimpleArticle> simpleArticle) {
                        content.setText(Html.fromHtml(simpleArticle.get(0).getContent()));
                        hideTheSpinKitView();
                        showTheArticle();
                    }
                });
    }

    private void setTheTitleAndContent() {
        if(positionFromCommentFragment!=-1){
            title.setText(MyApp.getInstance().getArticles().get(positionFromCommentFragment).getTitle());
            //content.setText(Html.fromHtml(MyApp.getInstance().getArticles().get(positionFromCommentFragment).getIntroduction()));
        }
        if(positionFromTalkFragment!=-1){
            title.setText(MyApp.getInstance().getForums().get(positionFromTalkFragment).getTitle());
            content.setText(Html.fromHtml(MyApp.getInstance().getForums().get(positionFromTalkFragment).getIntroduction()));
        }
    }

    private void bindAllTheViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
