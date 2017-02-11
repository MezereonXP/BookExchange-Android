package com.example.mezereon.bookexchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.Fragment.CommentFragment;
import com.example.mezereon.bookexchange.Module.Article;
import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.Module.User;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.stream.JsonReader;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {


    @Bind(R.id.textView)
    TextView title;
    @Bind(R.id.textView2)
    TextView signUp;
    @Bind(R.id.textView3)
    TextView signIn;
    @Bind(R.id.imageView)
    ImageView logo;
    @Bind(R.id.layout1)
    LinearLayout layout1;
    @Bind(R.id.textInputLayout)
    TextInputLayout name;
    @Bind(R.id.textInputLayout2)
    TextInputLayout pwd;
    @Bind(R.id.textView4)
    TextView haveAccount;
    @Bind(R.id.button)
    Button btn_signIn;
    @Bind(R.id.white)
    CircleImageView white;
    @Bind(R.id.button2)
    Button btn_logIn;
    @Bind(R.id.spin_kit3)
    SpinKitView spinKitView;


    @Inject
    Retrofit retrofit;

    private  ObjectAnimator animator=new ObjectAnimator();
    private AnimatorSet animSet=new AnimatorSet();


    public interface LoginService {
        @GET("getInfo.php")
        Observable<List<User>> login(@Query("username") String username);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        DaggerAppComponent.builder().build().inject(this);

        SharedPreferences sp=this.getSharedPreferences("USERINFO",MODE_PRIVATE);
        if(!sp.getString("USERNAME","none").equals("none")){
            Intent intent=new Intent();
            intent.setClass(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator moveIn = ObjectAnimator.ofFloat(title, "translationY", 0f,350f);
                animator = ObjectAnimator.ofFloat(title, "alpha", 1f, 0f);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(logo, "scaleY", 1f,0.4f);
                ObjectAnimator animator3 = ObjectAnimator.ofFloat(logo, "scaleX", 1f,0.4f);
                ObjectAnimator animator4 = ObjectAnimator.ofFloat(logo, "translationY", 0f,-300f);
                ObjectAnimator animator5 = ObjectAnimator.ofFloat(layout1, "translationY", 0f,300f);
                animSet = new AnimatorSet();
                animSet.play(moveIn).with(animator).with(animator2).with(animator3).with(animator4).with(animator5);
                animSet.setDuration(500);
                animSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        //Toast.makeText(LoginActivity.this,"start",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //Toast.makeText(LoginActivity.this,"end!",Toast.LENGTH_SHORT).show();
                        layout1.setVisibility(View.INVISIBLE);
                        title.setVisibility(View.INVISIBLE);
                        name.setVisibility(View.VISIBLE);
                        pwd.setVisibility(View.VISIBLE);
                        haveAccount.setAlpha(0f);
                        haveAccount.setVisibility(View.VISIBLE);
                        btn_signIn.setAlpha(0f);
                        btn_signIn.setVisibility(View.VISIBLE);
                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(name, "translationY", 100f,-200f);
                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(name, "alpha", 0f,1f);
                        ObjectAnimator animator3 = ObjectAnimator.ofFloat(pwd, "translationY", 200f,-170f);
                        ObjectAnimator animator4 = ObjectAnimator.ofFloat(pwd, "alpha", 0f,1f);
                        ObjectAnimator animator5 = ObjectAnimator.ofFloat(haveAccount, "alpha", 0f,1f);
                        ObjectAnimator animator6 = ObjectAnimator.ofFloat(haveAccount, "translationX", -500f,0f);
                        ObjectAnimator animator7 = ObjectAnimator.ofFloat(btn_signIn, "translationX", 1600f,0f);
                        ObjectAnimator animator8 = ObjectAnimator.ofFloat(btn_signIn, "alpha", 0f,1f);
                        AnimatorSet animSet = new AnimatorSet();
                        animSet.play(animator1).with(animator2).with(animator3).with(animator4)
                                    .with(animator5).with(animator6).with(animator7).with(animator8);
                        animSet.setDuration(500);
                        animSet.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                       // Toast.makeText(LoginActivity.this,"cancel!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        //Toast.makeText(LoginActivity.this,"repeat!",Toast.LENGTH_SHORT).show();
                    }
                });
                animSet.start();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                white.setVisibility(View.VISIBLE);
                btn_signIn.setVisibility(View.INVISIBLE);
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(white, "scaleY", 1f,100f);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(white, "scaleX", 1f,100f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(animator1).with(animator2);
                animSet.setDuration(500);
                animSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        spinKitView.setVisibility(View.VISIBLE);
                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(spinKitView, "alpha", 0f,1f);
                        AnimatorSet animSet = new AnimatorSet();
                        animSet.play(animator1);
                        animSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                final LoginService loginService=retrofit.create(LoginService.class);
                                Subscription subscription=loginService.login(name.getEditText().getText().toString())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<List<User>>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                Log.d("error",e.toString());
                                            }

                                            @Override
                                            public void onNext(List<User> s) {
                                                spinKitView.setVisibility(View.INVISIBLE);
                                                if(!s.get(0).getPassword().equals(pwd.getEditText().getText().toString())){
                                                    Toast.makeText(LoginActivity.this,"Failed!",Toast.LENGTH_SHORT).show();
                                                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(white, "alpha", 1f,0f);
                                                    AnimatorSet animSet = new AnimatorSet();
                                                    animSet.addListener(new AnimatorListenerAdapter() {
                                                        @Override
                                                        public void onAnimationEnd(Animator animation) {
                                                            white.setVisibility(View.INVISIBLE);
                                                            btn_signIn.setVisibility(View.VISIBLE);
                                                            pwd.setErrorEnabled(true);
                                                            pwd.setError("请输入正确的密码");
                                                        }
                                                    });
                                                    animSet.play(animator1);
                                                    animSet.start();
                                                }else{
                                                    Toast.makeText(LoginActivity.this,"Success!",Toast.LENGTH_SHORT).show();
                                                    SharedPreferences sp=LoginActivity.this.getSharedPreferences("USERINFO",MODE_PRIVATE);
                                                    SharedPreferences.Editor editor=sp.edit();
                                                    editor.putString("USERNAME",s.get(0).getUsername());
                                                    editor.putString("USERID",s.get(0).getId()+"");
                                                    editor.putString("USERSRC",s.get(0).getSrc());
                                                    editor.putString("USERSIGNATRUE",s.get(0).getSignatrue());
                                                    editor.putString("USERSEX",s.get(0).getSex());
                                                    editor.commit();
                                                    Intent intent=new Intent();
                                                    intent.setClass(LoginActivity.this,HomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                            }
                        });
                        animSet.setDuration(1000);
                        animSet.start();


                    }
                });
                animSet.start();


            }
        });
        btn_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                white.setVisibility(View.VISIBLE);
                btn_logIn.setVisibility(View.INVISIBLE);
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(white, "scaleY", 1f,100f);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(white, "scaleX", 1f,100f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(animator1).with(animator2);
                animSet.setDuration(500);
                animSet.start();
            }
        });

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_logIn.setAlpha(0f);
                btn_logIn.setVisibility(View.VISIBLE);
                ObjectAnimator animator7 = ObjectAnimator.ofFloat(btn_signIn, "translationX", 0f,1600f);
                ObjectAnimator animator8 = ObjectAnimator.ofFloat(btn_signIn, "alpha", 1f,0f);
                ObjectAnimator animator3 = ObjectAnimator.ofFloat(haveAccount, "alpha", 1f,0f);
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(btn_logIn, "translationX", 1600f,0f);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(btn_logIn, "alpha", 0f,1f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(animator7).with(animator8).with(animator3).before(animator1).before(animator2);
                animSet.setDuration(500);
                animSet.start();
            }
        });


    }
}
