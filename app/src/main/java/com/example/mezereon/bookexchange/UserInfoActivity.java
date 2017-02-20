package com.example.mezereon.bookexchange;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.Module.Book;
import com.github.ybq.android.spinkit.SpinKitView;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
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

    @Bind(R.id.editText)
    EditText name;
    @Bind(R.id.editText2)
    EditText sex;
    @Bind(R.id.editText3)
    EditText email;
    @Bind(R.id.editText4)
    EditText phone;
    @Bind(R.id.button4)
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
    }

    private void initTheDialog() {
        progressDialog=new ProgressDialog(this);
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
    }

    private void getTheSharePreference() {
        sharePreferenceInUserInfo=this.getSharedPreferences("USERINFO",MODE_PRIVATE);
    }

    private void bindAllTheView() {
        ButterKnife.bind(this);
    }
}
