package com.example.mezereon.bookexchange;

import android.app.Application;
import android.text.format.Time;

import com.example.mezereon.bookexchange.Module.Article;
import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.Module.Exchange;
import com.example.mezereon.bookexchange.Module.Forum;

import java.util.List;

/**
 * Created by Mezereon on 2017/2/14.
 */

public class MyApp  {

    public static final int SHORT_DURATION=500;
    public static final int MIDDLE_DURATION=1000;
    public static final int LONG_DURATION=1500;
    public static final int COUNT_OF_SETTING_ITEMS=4;

    public static final String COLOR_WHITE="#FFFFFF";
    public static final String COLOR_TEXT_NOACTIVE="#6B6B6B";
    public static final String COLOR_TEXT_ACTIVE="#3399cc";
    public static final String COLOR_STATUSBAR="#01579b";

    public static final String STRING_BOOK="书籍";
    public static final String STRING_BOOK_COMMENT="书评";
    public static final String STRING_TALK="讨论";
    public static final String STRING_SELF="个人";

    private List<Article> articles;
    private  List<Book> books;
    private List<Forum> forums;

    public List<Exchange> getExchanges() {
        return exchanges;
    }

    public void setExchanges(List<Exchange> exchanges) {
        this.exchanges = exchanges;
    }

    private List<Exchange> exchanges;

    public static MyApp instance=new MyApp();

    public static MyApp getInstance() {
        return instance;
    }

    private MyApp(){

    }
    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Forum> getForums() {
        return forums;
    }

    public void setForums(List<Forum> forums) {
        this.forums = forums;
    }
    public String returnTime() {
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        String s = year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
        return s;
    }
}
