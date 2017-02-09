package com.example.mezereon.bookexchange.Module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mezereon on 2017/2/9.
 */

@Module
public class BookExchangeApplicationModule {


    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder().client(new OkHttpClient())
                .baseUrl("http://xplovesaber.top/bookEx/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
