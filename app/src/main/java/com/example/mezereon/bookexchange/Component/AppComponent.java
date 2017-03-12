package com.example.mezereon.bookexchange.Component;

import android.app.Application;
import android.support.v7.widget.RecyclerView;

import com.example.mezereon.bookexchange.AddArticleActivity;
import com.example.mezereon.bookexchange.AddBookActivity;
import com.example.mezereon.bookexchange.AddCommentActivity;
import com.example.mezereon.bookexchange.AddForumActivity;
import com.example.mezereon.bookexchange.ExchangeBookActivity;
import com.example.mezereon.bookexchange.Fragment.BeforeExchangeFragment;
import com.example.mezereon.bookexchange.Fragment.BookShowFragment;
import com.example.mezereon.bookexchange.Fragment.CommentFragment;
import com.example.mezereon.bookexchange.Fragment.ExchangedFragment;
import com.example.mezereon.bookexchange.Fragment.ExchangingFragment;
import com.example.mezereon.bookexchange.Fragment.SelfFragment;
import com.example.mezereon.bookexchange.Fragment.TalkFragment;
import com.example.mezereon.bookexchange.HomeActivity;
import com.example.mezereon.bookexchange.LoginActivity;
import com.example.mezereon.bookexchange.Module.BookExchangeApplicationModule;
import com.example.mezereon.bookexchange.MyExchangeActivity;
import com.example.mezereon.bookexchange.ReadActivity;
import com.example.mezereon.bookexchange.UserBookActivity;
import com.example.mezereon.bookexchange.UserInfoActivity;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by Mezereon on 2017/2/9.
 */

@Singleton
@Component(modules = {BookExchangeApplicationModule.class})
public interface AppComponent {
    void inject(HomeActivity homeActivity);
    void inject(LoginActivity loginActivity);
    void inject(CommentFragment commentFragment);
    void inject(TalkFragment talkFragment);
    void inject(BookShowFragment bookShowFragment);
    void inject(ReadActivity readActivity);
    void inject(UserInfoActivity userInfoActivity);
    void inject(AddForumActivity addForumActivity);
    void inject(AddCommentActivity addCommentActivity);
    void inject(AddArticleActivity addArticleActivity);
    void inject(AddBookActivity addBookActivity);
    void inject(ExchangeBookActivity exchangeBookActivity);
    void inject(UserBookActivity userBookActivity);
    void inject(BeforeExchangeFragment beforeExchangeFragment);
    void inject(ExchangingFragment exchangingFragment);
    void inject(ExchangedFragment exchangedFragment);
    void inject(MyExchangeActivity myExchangeActivity);
    void inject(SelfFragment selfFragment);

    Retrofit retrofit();
}