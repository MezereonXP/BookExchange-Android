package com.example.mezereon.bookexchange;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.Module.Book;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
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
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddForumActivity extends AppCompatActivity {

    @Bind(R.id.button5)
    Button sendForum;
    @Bind(R.id.editText5)
    EditText name;
    @Bind(R.id.editText6)
    EditText content;

    @Inject
    Retrofit retrofit;

    private SharedPreferences sharePreference;
    private ProgressDialog progressDialog;
    public interface SendForumService {
        @FormUrlEncoded
        @POST("upForum.php")
        Observable<Void> send(@Field("id") String id,
                              @Field("username") String username,
                              @Field("date") String time,
                              @Field("introduction") String introduction,
                              @Field("authorpic") String src,
                              @Field("title") String title);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_forum);
        bindAllTheView();
        injectByDagger();
        initSharePreference();
        initProgressDialog();
        setTheClickEvent();
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void initProgressDialog() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("上传帖子中....");
        progressDialog.setTitle("System");
    }

    private void initSharePreference() {
        sharePreference=getSharedPreferences("USERINFO",MODE_PRIVATE);
    }

    private void setTheClickEvent() {
        sendForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                send();
            }
        });
    }

    private void send() {
        SendForumService sendForumService=retrofit.create(SendForumService.class);
        Subscription subscrition=sendForumService.send(sharePreference.getString("USERID","none"),
                sharePreference.getString("USERNAME","none"),returnTime(),content.getText().toString()
                ,sharePreference.getString("USERSRC","none"),name.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) { Log.e("error",e.toString()); }
                    @Override
                    public void onNext(Void aVoid) {
                        progressDialog.dismiss();
                        finish();
                    }
                });
    }

    private void bindAllTheView() {
        ButterKnife.bind(this);
    }

    private String returnTime(){
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        String s = year+"-"+month+"-"+date+"-  "+hour+":"+minute+":"+second;
        return s;
    }
}
