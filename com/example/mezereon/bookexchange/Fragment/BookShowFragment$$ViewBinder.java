// Generated code from Butter Knife. Do not modify!
package com.example.mezereon.bookexchange.Fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BookShowFragment$$ViewBinder<T extends com.example.mezereon.bookexchange.Fragment.BookShowFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493045, "field 'bookRecycleView'");
    target.bookRecycleView = finder.castView(view, 2131493045, "field 'bookRecycleView'");
    view = finder.findRequiredView(source, 2131493046, "field 'spinKitViewInBookShow'");
    target.spinKitViewInBookShow = finder.castView(view, 2131493046, "field 'spinKitViewInBookShow'");
  }

  @Override public void unbind(T target) {
    target.bookRecycleView = null;
    target.spinKitViewInBookShow = null;
  }
}
