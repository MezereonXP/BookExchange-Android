package com.example.mezereon.bookexchange.Component;

import android.app.Application;

import com.example.mezereon.bookexchange.Fragment.CommentFragment;
import com.example.mezereon.bookexchange.Fragment.TalkFragment;
import com.example.mezereon.bookexchange.HomeActivity;
import com.example.mezereon.bookexchange.LoginActivity;
import com.example.mezereon.bookexchange.Module.BookExchangeApplicationModule;

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

    Retrofit retrofit();
}