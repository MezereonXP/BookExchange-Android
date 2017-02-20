// Generated code from Butter Knife. Do not modify!
package com.example.mezereon.bookexchange.Fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CommentFragment$$ViewBinder<T extends com.example.mezereon.bookexchange.Fragment.CommentFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493011, "field 'comment'");
    target.comment = finder.castView(view, 2131493011, "field 'comment'");
    view = finder.findRequiredView(source, 2131493048, "field 'spinKitView'");
    target.spinKitView = finder.castView(view, 2131493048, "field 'spinKitView'");
  }

  @Override public void unbind(T target) {
    target.comment = null;
    target.spinKitView = null;
  }
}
