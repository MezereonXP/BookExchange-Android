package com.example.mezereon.bookexchange;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.Fragment.BeforeExchangeFragment;
import com.example.mezereon.bookexchange.Fragment.ExchangedFragment;
import com.example.mezereon.bookexchange.Fragment.ExchangingFragment;
import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.Module.Exchange;
import com.jakewharton.rxbinding.view.RxView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.squareup.picasso.Picasso;


import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MyExchangeActivity extends AppCompatActivity {

    @BindView(R.id.viewpagertab)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.coverInMyExchange)
    ImageView cover;
    @BindView(R.id.bookPicInMyExchange)
    ImageView bookPic;
    @BindView(R.id.agree)
    Button agree;
    @BindView(R.id.refuse)
    Button refuse;
    @BindView(R.id.titleInMyExchange)
    TextView title;
    @BindView(R.id.bookNameInMyExchange)
    TextView bookName;

    private Exchange myExchange;
    private ProgressDialog progressDialog;

    @Inject
    Retrofit retrofit;

    public interface AgreeService {
        @GET("setExchange.php")
        Observable<Void> agree(@Query("id") String id,
                                       @Query("ida") String ida,
                                       @Query("idb") String idb,
                                       @Query("bookid") String bookid);
    }

    public interface RefuseService {
        @GET("setExchange2.php")
        Observable<Void> refuse(@Query("id") String id);
    }

    public interface SendService {
        @GET("send.php")
        Observable<Void> send(@Query("id") String id,
                              @Query("number") String number,
                              @Query("kind") String kind);
    }

    public interface ReceviceService {
        @GET("recevie.php")
        Observable<Void> recevie(@Query("id") String id,
                              @Query("kind") String kind);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheStatusBar();
        setContentView(R.layout.activity_my_exchange);
        initTheProgressDialog();
        injectByDagger();
        bindAllTheView();
        setTheFragmentAdapter();
        setTheButtonClickEvent();
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void initTheProgressDialog() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在向服务器确认信息....");
        progressDialog.setTitle("System");
    }

    private void setTheButtonClickEvent() {
        RxView.clicks(agree)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        progressDialog.show();
                        agreeTheExchange();
                    }
                });
        RxView.clicks(refuse)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        progressDialog.show();
                        refuseTheExchange();
                    }
                });
    }

    private void refuseTheExchange() {
        RefuseService refuseTheExchange=retrofit.create(RefuseService.class);
        Subscription subscription=refuseTheExchange.refuse(myExchange.getId()+"")
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<Void>() {
                                            @Override
                                            public void onCompleted() {}
                                            @Override
                                            public void onError(Throwable e) {
                                                Toasty.error(MyExchangeActivity.this,"网络出错"
                                                        , Toast.LENGTH_SHORT).show();
                                            }
                                            @Override
                                            public void onNext(Void aVoid) {
                                                progressDialog.dismiss();
                                                setViewVisibility(View.GONE);
                                                Toasty.success(MyExchangeActivity.this,"拒绝成功"
                                                        , Toast.LENGTH_SHORT).show();
                                            }
                                        });
    }

    private void agreeTheExchange() {
        AgreeService agreeService=retrofit.create(AgreeService.class);
        Subscription subcription=agreeService.agree(myExchange.getId()+"",myExchange.getBookida()
                                    ,myExchange.getBookidb(),myExchange.getBookida())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Void>() {
                                    @Override
                                    public void onCompleted() { }
                                    @Override
                                    public void onError(Throwable e) {
                                        Toasty.error(MyExchangeActivity.this,"网络出错"
                                            , Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onNext(Void v) {
                                        progressDialog.dismiss();
                                        setViewVisibility(View.GONE);
                                        Toasty.success(MyExchangeActivity.this,"同意成功"
                                                , Toast.LENGTH_SHORT).show();
                                    }
                                });
    }

    //Set the status bar of the system
    private void setTheStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setStatusBarColor(Color.parseColor(MyApp.COLOR_STATUSBAR));
        }
    }

    private void setTheFragmentAdapter() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("待交换", BeforeExchangeFragment.class)
                .add("交换中", ExchangingFragment.class)
                .add("已交换", ExchangedFragment.class)
                .create());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        smartTabLayout.setViewPager(viewPager);
    }

    private void bindAllTheView() {
        ButterKnife.bind(this);
    }

    public void showCover(String booksrcb, String booknameb, Exchange exchange) {
        this.myExchange=exchange;
        setViewVisibility(View.VISIBLE);
        Picasso.with(this).load(booksrcb).into(bookPic);
        bookName.setText(booknameb);
        setTheZValueOfViews();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(bookPic, "alpha", 0f,1f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(cover,"alpha",0f,0.7f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(title,"translationY",-500f,0f);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(agree, "alpha", 0f,1f);
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(refuse, "alpha", 0f,1f);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(bookName, "alpha", 0f,1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1).with(animator2).with(animator3).with(animator4).with(animator5);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.start();
    }

    private void setTheZValueOfViews() {
        bookName.setZ(121);
        cover.setZ(120);
        title.setZ(121);
        agree.setZ(121);
        refuse.setZ(121);
        bookPic.setZ(121);
    }

    private void setViewVisibility(int visible) {
        bookPic.setVisibility(visible);
        cover.setVisibility(visible);
        title.setVisibility(visible);
        agree.setVisibility(visible);
        refuse.setVisibility(visible);
        bookName.setVisibility(visible);
    }

    @Override
    public void onBackPressed() {
        if (cover.getVisibility()==View.VISIBLE){
            setViewVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }

    public void showDialog(String s){
        new AlertDialog.Builder(this).setTitle("系统提示")//设置对话框标题
                .setMessage((!s.equals(""))?"您的单号为"+s:"对方还未上传物流信息")//设置显示的内5容
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                    }
                }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        }).show();//在按键响应事件中显示此对话框
    }
    public void showInputDialog(final int kind,final int id){
            final EditText editText = new EditText(this);
            AlertDialog.Builder inputDialog =
                    new AlertDialog.Builder(this);
            inputDialog.setTitle("请输入单号").setView(editText);
            inputDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String number= editText.getText().toString();
                            sendNumber(kind,number,id);
                        }
                    }).show();

    }

    private void sendNumber(int kind, String number, int id) {
        progressDialog.show();
        SendService sendService=retrofit.create(SendService.class);
        Subscription subscription=sendService.send(id+"",number,kind+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(MyExchangeActivity.this,"网络出错"
                                , Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onNext(Void aVoid) {
                        Toasty.success(MyExchangeActivity.this,"提交成功"
                                , Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    public void recevie(int id, int kind) {
        progressDialog.show();
        ReceviceService recevieService=retrofit.create(ReceviceService.class);
        Subscription subscription=recevieService.recevie(id+"",kind+"")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {}
                            @Override
                            public void onError(Throwable e) {
                                Toasty.error(MyExchangeActivity.this,"网络出错"
                                        , Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            @Override
                            public void onNext(Void aVoid) {
                                Toasty.success(MyExchangeActivity.this,"确认成功"
                                        , Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
    }

}
