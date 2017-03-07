package com.example.mezereon.bookexchange;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mezereon.bookexchange.Component.DaggerAppComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddCommentActivity extends AppCompatActivity {



    private int positionFromCommentFragment;
    private int positionFromTalkFragment;
    private SharedPreferences sharePreference;
    private ProgressDialog progressDialog;

    @Inject
    Retrofit retrofit;

    public interface SendForumCommentService {
        @GET("addForumComment.php")
        Observable<Void> send(@Query("articleid") String id,
                              @Query("username") String username,
                              @Query("time") String time,
                              @Query("comment") String comment);
    }
    public interface SendArticleCommentService {
        @GET("addComment.php")
        Observable<Void> send(@Query("articleid") String id,
                              @Query("username") String username,
                              @Query("time") String time,
                              @Query("comment") String comment);
    }
    @BindView(R.id.editTextInComment)
    EditText comment;
    @BindView(R.id.button6)
    Button sendComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheStatusBar();
        setContentView(R.layout.activity_add_comment);
        bindAllTheView();
        initSharePreference();
        initTheProgressDialog();
        injectByDagger();
        initThePosition();
        setTheClickEvent();
    }

    //Set the status bar of the system
    private void setTheStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setStatusBarColor(Color.parseColor(MyApp.COLOR_STATUSBAR));
        }
    }

    private void initTheProgressDialog() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("发表评论中....");
        progressDialog.setTitle("System");
    }

    private void initSharePreference() {
        sharePreference=getSharedPreferences("USERINFO",MODE_PRIVATE);
    }

    private void setTheClickEvent() {
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment.getText().length()!=0){
                    progressDialog.show();
                    send();
                }

            }
        });
    }

    private void send() {
        if (positionFromTalkFragment!=-1){
            sendTalkComment();
        }else{
            sendArticleComment();
        }
    }

    private void sendArticleComment() {
        SendArticleCommentService sendArticleCommentService=retrofit.create(SendArticleCommentService.class);
        Subscription subscription=sendArticleCommentService.send(MyApp.getInstance().getArticles().get(positionFromCommentFragment).getId()+"",
                sharePreference.getString("USERNAME","none"),returnTime(),comment.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) { Log.e("error",e.toString());}
                    @Override
                    public void onNext(Void aVoid) {
                        progressDialog.dismiss();
                        Toasty.success(AddCommentActivity.this,"评论成功", Toast.LENGTH_SHORT,true).show();
                        finish();
                    }
                });
    }

    private void sendTalkComment() {
        SendForumCommentService sendForumCommentService=retrofit.create(SendForumCommentService.class);
        Subscription subscription=sendForumCommentService.send(MyApp.getInstance().getForums().get(positionFromTalkFragment).getId()+"",
                sharePreference.getString("USERNAME","none"),returnTime(),comment.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) { Log.e("error",e.toString());}
                    @Override
                    public void onNext(Void aVoid) {
                        progressDialog.dismiss();
                        Toasty.success(AddCommentActivity.this,"评论成功", Toast.LENGTH_SHORT,true).show();
                        finish();
                    }
                });
    }

    private String returnTime() {
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        String s = year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
        return s;
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void bindAllTheView() {
        ButterKnife.bind(this);
    }

    private void initThePosition() {
        positionFromCommentFragment=getIntent().getIntExtra("positionFromCommentFragment",-1);
        positionFromTalkFragment=getIntent().getIntExtra("positionFromTalkFragment",-1);
    }
}
