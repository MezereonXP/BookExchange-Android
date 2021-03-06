package com.example.mezereon.bookexchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import com.example.mezereon.bookexchange.Fragment.BookShowFragment;
import com.example.mezereon.bookexchange.Fragment.CommentFragment;
import com.example.mezereon.bookexchange.Fragment.SelfFragment;
import com.example.mezereon.bookexchange.Fragment.TalkFragment;
import com.example.mezereon.bookexchange.Module.Article;
import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.Module.Forum;

import java.util.ArrayList;
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
import rx.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout layout_tab;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search)
    SearchView searchView;
    @BindView(R.id.imageView2)
    ImageView add;

    @Inject
    Retrofit retrofit;

    public interface GetBookByNameService {
        @GET("getBookByName.php")
        Observable<List<Book>> getBooks(@Query("bookname") String bookName);
    }

    public interface GetArticleByNameService {
        @GET("getArticleByName.php")
        Observable<List<Article>> getBooks(@Query("bookname") String bookName);
    }

    public interface GetForumByNameService {
        @GET("getForumByName.php")
        Observable<List<Forum>> getForums(@Query("bookname") String bookName);
    }

    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
            10);
    private FragmentPagerAdapter myAdapter;
    private ArrayList<Fragment> myFragments=new ArrayList<>();
    private SelfFragment selffragment=new SelfFragment();
    private CommentFragment commentfragment=new CommentFragment();
    private TalkFragment talkfragment=new TalkFragment();
    private BookShowFragment bookShowFragment=new BookShowFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheStatusBar();
        setContentView(R.layout.activity_home);
        //judgeTheIntentValue();
        initView();
        injectByDagger();
        manageTheView();
        setTheAddEvent();
        setTheSearchView();
        layout_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String name= (String) tab.getText();
                if(name.equals("书评")&&toolbar.getVisibility()==View.GONE){
                    showTheToolbar();
                }else if(name.equals("讨论")&&toolbar.getVisibility()==View.GONE){
                    showTheToolbar();
                }else if(name.equals("个人")){
                    hideTheToolbar();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void setTheSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getThingsByName(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getThingsByName(String query) {
        switch (layout_tab.getSelectedTabPosition()){
            case 0:
                getBooksByName(query);
                break;
            case 1:
                getArticlesByName(query);
                break;
            case 2:
                getForumByName(query);
                break;
            default:
                break;
        }
    }

    private void getForumByName(String query) {
        GetForumByNameService getForumByNameService=retrofit.create(GetForumByNameService.class);
        Subscription subscription=getForumByNameService.getForums(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Forum>>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(HomeActivity.this,
                                "查找失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(List<Forum> forums) {
                        talkfragment.getForumsByName(forums);
                    }
                });
    }

    private void getArticlesByName(String query) {
        GetArticleByNameService getArticlesByNameService=retrofit.create(GetArticleByNameService.class);
        Subscription subscription=getArticlesByNameService.getBooks(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Article>>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(HomeActivity.this,
                                "查找失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(List<Article> articles) {
                        commentfragment.getBooksByName(articles);
                    }
                });
    }


    public void getBooksByName(String query) {
        GetBookByNameService getBookByNameService=retrofit.create(GetBookByNameService.class);
        Subscription subscription=getBookByNameService.getBooks(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Book>>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(HomeActivity.this,"查找失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(List<Book> books) {
                        bookShowFragment.getBooksByName(books);
                    }
                });
    }


    private void setTheAddEvent() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeTheAddEvent();
            }
        });
    }

    private void judgeTheAddEvent() {
        switch (layout_tab.getSelectedTabPosition()){
            case 0:
                turnToAddBook();
                break;
            case 1:
                turnToAddArticle();
                break;
            case 2:
                turnToAddForum();
                break;
            default:
                break;
        }
    }

    private void turnToAddForum() {
        Intent intent=new Intent();
        intent.setClass(this,AddForumActivity.class);
        startActivity(intent);
        intent=null;
    }

    private void turnToAddArticle() {
        Intent intent=new Intent();
        intent.setClass(this,AddArticleActivity.class);
        startActivity(intent);
        intent=null;
    }

    private void turnToAddBook() {
        Intent intent=new Intent();
        intent.setClass(this,AddBookActivity.class);
        startActivity(intent);
        intent=null;
    }

    private void setTransition() {
        Fade fade = new Fade();
        fade.setDuration(MyApp.SHORT_DURATION);
        getWindow().setEnterTransition(fade);
    }


    private void manageTheView() {
        add.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);
        layout_tab.setTabMode(TabLayout.MODE_FIXED);
        layout_tab.setBackgroundColor(Color.parseColor(MyApp.COLOR_WHITE));
        layout_tab.setSelectedTabIndicatorHeight(6);
        layout_tab.setTabTextColors(Color.parseColor(MyApp.COLOR_TEXT_NOACTIVE),Color.parseColor(MyApp.COLOR_TEXT_ACTIVE));
        layout_tab.addTab(layout_tab.newTab().setText(MyApp.STRING_BOOK).setIcon(R.drawable.book));
        layout_tab.addTab(layout_tab.newTab().setText(MyApp.STRING_BOOK_COMMENT).setIcon(R.drawable.comment));
        layout_tab.addTab(layout_tab.newTab().setText(MyApp.STRING_TALK).setIcon(R.drawable.talk));
        layout_tab.addTab(layout_tab.newTab().setText(MyApp.STRING_SELF).setIcon(R.drawable.self));
        layout_tab.setupWithViewPager(viewPager);
        layout_tab.getTabAt(0).setText(MyApp.STRING_BOOK).setIcon(R.drawable.book);
        layout_tab.getTabAt(1).setText(MyApp.STRING_BOOK_COMMENT).setIcon(R.drawable.comment);
        layout_tab.getTabAt(2).setText(MyApp.STRING_TALK).setIcon(R.drawable.talk);
        layout_tab.getTabAt(3).setText(MyApp.STRING_SELF).setIcon(R.drawable.self);
    }

    //Set the status bar of the system
    private void setTheStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setStatusBarColor(Color.parseColor(MyApp.COLOR_STATUSBAR));
            //setTransition();
        }
    }

    private void hideTheToolbar() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(toolbar, "alpha", 1f,0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1);
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                toolbar.setVisibility(View.GONE);
            }
        });
        animSet.start();
    }

    private void showTheToolbar() {
        toolbar.setVisibility(View.VISIBLE);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(toolbar, "alpha", 0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1);
        animSet.setDuration(MyApp.MIDDLE_DURATION);
        animSet.start();
    }

    private void initView() {
        ButterKnife.bind(this);
        myFragments.add(bookShowFragment);
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
        viewPager.setAdapter(myAdapter);
        viewPager.setOffscreenPageLimit(myFragments.size()-1);
    }

    public void hideTheToolbarByCommentFragment(){
        if(layout_tab.getSelectedTabPosition()!=3) {
            hideTheToolbar();
        }
    }

    public void showTheToolbarByCommentFragment(){
        if(toolbar.getVisibility()==View.GONE&&layout_tab.getSelectedTabPosition()!=3) {
            showTheToolbar();
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
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(HomeActivity.this).setTitle("系统提示")//设置对话框标题
                .setMessage("确认退出BookExchange？")//设置显示的内5容
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        finish();
                    }
                }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        }).show();//在按键响应事件中显示此对话框
    }


}
