package com.example.mezereon.bookexchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.Module.Book;
import com.github.ybq.android.spinkit.SpinKitView;
import com.jakewharton.rxbinding.view.RxView;

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
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class UserInfoActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText name;
    @BindView(R.id.textView16)
    TextView nameFrontText;
    @BindView(R.id.editText2)
    EditText sex;
    @BindView(R.id.textView17)
    TextView sexFrontText;
    @BindView(R.id.editText3)
    EditText email;
    @BindView(R.id.textView18)
    TextView emailFrontText;
    @BindView(R.id.editText4)
    EditText phone;
    @BindView(R.id.textView19)
    TextView phoneFrontText;
    @BindView(R.id.button4)
    Button changeInfo;

    @Inject
    Retrofit retrofit;

    public interface ChangeInfoService {
        @GET("change.php")
        Observable<Void> changeInfo(@Query("id") int id,
                                      @Query("username") String username,
                                      @Query("password") String password,
                                      @Query("sex") String sex,
                                      @Query("email") String email,
                                      @Query("phone") String phone);
    }

    private SharedPreferences sharePreferenceInUserInfo;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        bindAllTheView();
        initTheDialog();
        injectByDagger();
        getTheSharePreference();
        setTheView();
        setTheClickListener();
        showTheAnimator1();
    }

    private void showTheAnimator1() {
        name.setVisibility(View.VISIBLE);
        nameFrontText.setVisibility(View.VISIBLE);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(name, "translationX", 500f,0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(nameFrontText, "translationX", -500f,0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1).with(animator2);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setDuration(MyApp.SHORT_DURATION);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                showTheAnimator2();
            }
        });
        animSet.start();
    }

    private void showTheAnimator2() {
        sex.setVisibility(View.VISIBLE);
        sexFrontText.setVisibility(View.VISIBLE);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(sex, "translationX", 500f,0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(sexFrontText, "translationX", -500f,0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1).with(animator2);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setDuration(MyApp.SHORT_DURATION);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                showTheAnimator3();
            }
        });
        animSet.start();
    }

    private void showTheAnimator3() {
        email.setVisibility(View.VISIBLE);
        emailFrontText.setVisibility(View.VISIBLE);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(email, "translationX", 500f,0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(emailFrontText, "translationX", -500f,0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1).with(animator2);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setDuration(MyApp.SHORT_DURATION);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                showTheAnimator4();
            }
        });
        animSet.start();
    }

    private void showTheAnimator4() {
        phone.setVisibility(View.VISIBLE);
        phoneFrontText.setVisibility(View.VISIBLE);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(phone, "translationX", 500f,0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(phoneFrontText, "translationX", -500f,0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1).with(animator2);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setDuration(MyApp.SHORT_DURATION);
        animSet.start();
    }

    private void initTheDialog() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Updating....!");
        progressDialog.setTitle("Info Changing");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void setTheClickListener() {
        RxView.clicks(changeInfo).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                progressDialog.show();
                changeUserInfo();
            }
        });
    }

    private void changeUserInfo() {
        ChangeInfoService changeInfoService=retrofit.create(ChangeInfoService.class);
        Subscription subscription=changeInfoService.changeInfo(Integer.parseInt(sharePreferenceInUserInfo.getString("USERID","none")),
                sharePreferenceInUserInfo.getString("USERNAME","NONE"),sharePreferenceInUserInfo.getString("USERPASSWORD","NONE"),
                sharePreferenceInUserInfo.getString("USERSEX","NONE"),email.getText().toString(),
                phone.getText().toString())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Void>() {
                                    @Override
                                    public void onCompleted() {}
                                    @Override
                                    public void onError(Throwable e) { Log.d("error",e.toString());}
                                    @Override
                                    public void onNext(Void v) {
                                        progressDialog.dismiss();
                                        changeLocalData();
                                        trunBack();
                                    }
                                });
    }

    private void trunBack() {
        Toast.makeText(UserInfoActivity.this,"Changed!",Toast.LENGTH_SHORT).show();
        finish();
    }

    private void changeLocalData() {
        sharePreferenceInUserInfo.edit().remove("USEREMAIL").commit();
        sharePreferenceInUserInfo.edit().putString("USEREMAIL",email.getText().toString()).commit();
        sharePreferenceInUserInfo.edit().putString("USERPHONE",phone.getText().toString()).commit();
    }

    private void setTheView() {
        name.setText(sharePreferenceInUserInfo.getString("USERNAME","NONE"));
        sex.setText(sharePreferenceInUserInfo.getString("USERSEX","NONE"));
        email.setText(sharePreferenceInUserInfo.getString("USEREMAIL","NONE"));
        phone.setText(sharePreferenceInUserInfo.getString("USERPHONE","NONE"));
        changeViewToInvisible();
    }

    private void changeViewToInvisible() {
        name.setVisibility(View.INVISIBLE);
        nameFrontText.setVisibility(View.INVISIBLE);
        sex.setVisibility(View.INVISIBLE);
        sexFrontText.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);
        emailFrontText.setVisibility(View.INVISIBLE);
        phone.setVisibility(View.INVISIBLE);
        phoneFrontText.setVisibility(View.INVISIBLE);

    }

    private void getTheSharePreference() {
        sharePreferenceInUserInfo=this.getSharedPreferences("USERINFO",MODE_PRIVATE);
    }

    private void bindAllTheView() {
        ButterKnife.bind(this);
    }
}
