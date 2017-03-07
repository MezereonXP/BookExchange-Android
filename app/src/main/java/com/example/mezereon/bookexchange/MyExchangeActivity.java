package com.example.mezereon.bookexchange;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mezereon.bookexchange.Fragment.BeforeExchangeFragment;
import com.example.mezereon.bookexchange.Fragment.ExchangedFragment;
import com.example.mezereon.bookexchange.Fragment.ExchangingFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.squareup.picasso.Picasso;


import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheStatusBar();
        setContentView(R.layout.activity_my_exchange);
        bindAllTheView();
        setTheFragmentAdapter();
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

    public void showCover(String booksrcb, String booknameb) {
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
}
