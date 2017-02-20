// Generated code from Butter Knife. Do not modify!
package com.example.mezereon.bookexchange.Adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BookRecycleViewAdapter$BookViewHolder$$ViewBinder<T extends com.example.mezereon.bookexchange.Adapter.BookRecycleViewAdapter.BookViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493067, "field 'bookIntroduce'");
    target.bookIntroduce = finder.castView(view, 2131493067, "field 'bookIntroduce'");
    view = finder.findRequiredView(source, 2131493064, "field 'cardView'");
    target.cardView = finder.castView(view, 2131493064, "field 'cardView'");
    view = finder.findRequiredView(source, 2131493065, "field 'bookPic'");
    target.bookPic = finder.castView(view, 2131493065, "field 'bookPic'");
    view = finder.findRequiredView(source, 2131493066, "field 'bookname'");
    target.bookname = finder.castView(view, 2131493066, "field 'bookname'");
  }

  @Override public void unbind(T target) {
    target.bookIntroduce = null;
    target.cardView = null;
    target.bookPic = null;
    target.bookname = null;
  }
}
