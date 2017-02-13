package com.example.mezereon.bookexchange;

import android.app.Application;

import com.example.mezereon.bookexchange.Module.Article;
import com.example.mezereon.bookexchange.Module.Book;
import com.example.mezereon.bookexchange.Module.Forum;

import java.util.List;

/**
 * Created by Mezereon on 2017/2/14.
 */

public class MyApp  {
    private List<Article> articles;
    private  List<Book> books;
    private List<Forum> forums;
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
}
