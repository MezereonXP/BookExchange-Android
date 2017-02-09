package com.example.mezereon.bookexchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.Fragment.CommentFragment;
import com.example.mezereon.bookexchange.Fragment.SelfFragment;
import com.example.mezereon.bookexchange.Fragment.TalkFragment;
import com.example.mezereon.bookexchange.Module.Book;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.tab_layout)
    TabLayout layout_tab;
    @Bind(R.id.viewpager)
    ViewPager vp;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search)
    SearchView searchView;
    @Bind(R.id.imageView2)
    ImageButton add;



    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
            10);
    private FragmentPagerAdapter myAdapter;
    private ArrayList<Fragment> myFragments=new ArrayList<>();
    private SelfFragment selffragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setStatusBarColor(Color.parseColor("#01579b"));
        Fade fade = new Fade();
        fade.setDuration(500);
        getWindow().setEnterTransition(fade);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        init();
        add.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);
        layout_tab.setTabMode(TabLayout.MODE_FIXED);
        layout_tab.setBackgroundColor(Color.parseColor("#FFFFFF"));
        layout_tab.setSelectedTabIndicatorHeight(3);
        layout_tab.setTabTextColors(Color.parseColor("#6B6B6B"),Color.parseColor("#3399cc"));
        layout_tab.addTab(layout_tab.newTab().setText("书评").setIcon(R.drawable.comment));
        layout_tab.addTab(layout_tab.newTab().setText("讨论").setIcon(R.drawable.talk));
        layout_tab.addTab(layout_tab.newTab().setText("个人").setIcon(R.drawable.self));
        layout_tab.setupWithViewPager(vp);
        layout_tab.getTabAt(0).setText("书评").setIcon(R.drawable.comment);
        layout_tab.getTabAt(1).setText("讨论").setIcon(R.drawable.talk);
        layout_tab.getTabAt(2).setText("个人").setIcon(R.drawable.self);

        layout_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String name= (String) tab.getText();
                if(name.equals("书评")){
                    if(toolbar.getVisibility()==View.GONE) {
                        toolbar.setVisibility(View.VISIBLE);
                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(toolbar, "alpha", 0f, 1f);
                        AnimatorSet animSet = new AnimatorSet();
                        animSet.play(animator1);
                        animSet.setDuration(1000);
                        animSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {

                            }
                        });
                        animSet.start();
                    }
                }else if(name.equals("讨论")){
                    if(toolbar.getVisibility()==View.GONE) {
                        toolbar.setVisibility(View.VISIBLE);
                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(toolbar, "alpha", 0f, 1f);
                        AnimatorSet animSet = new AnimatorSet();
                        animSet.play(animator1);
                        animSet.setDuration(1000);
                        animSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {

                            }
                        });
                        animSet.start();
                    }

                }else if(name.equals("个人")){

                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(toolbar, "alpha", 1f,0f);
                    AnimatorSet animSet = new AnimatorSet();
                    animSet.play(animator1);
                    animSet.setDuration(1000);
                    animSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            toolbar.setVisibility(View.GONE);
                        }
                    });
                    animSet.start();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
    private void init() {
        CommentFragment commentfragment=new CommentFragment();
        selffragment=new SelfFragment();
        TalkFragment talkfragment=new TalkFragment();
        myFragments.add(commentfragment);
        myFragments.add(talkfragment);
        myFragments.add(selffragment);
        myAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return myFragments.get(position);
            }

            @Override
            public int getCount() {
                return myFragments.size();
            }
        };
        vp.setAdapter(myAdapter);
        vp.setOffscreenPageLimit(2);
    }

    public void animate(){
        if(layout_tab.getSelectedTabPosition()!=2) {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(toolbar, "alpha", 1f, 0f);
            AnimatorSet animSet = new AnimatorSet();
            animSet.play(animator1);
            animSet.setDuration(1000);
            animSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    toolbar.setVisibility(View.GONE);
                }
            });
            animSet.start();
        }
    }
    public void animate2(){
        if(toolbar.getVisibility()==View.GONE&&layout_tab.getSelectedTabPosition()!=2) {
            toolbar.setVisibility(View.VISIBLE);
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(toolbar, "alpha", 0f, 1f);
            AnimatorSet animSet = new AnimatorSet();
            animSet.play(animator1);
            animSet.setDuration(1000);
            animSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                }
            });
            animSet.start();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }


}
