// Generated code from Butter Knife. Do not modify!
package com.example.mezereon.bookexchange;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ReadActivity$$ViewBinder<T extends com.example.mezereon.bookexchange.ReadActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493018, "field 'title'");
    target.title = finder.castView(view, 2131493018, "field 'title'");
    view = finder.findRequiredView(source, 2131493021, "field 'comments'");
    target.comments = finder.castView(view, 2131493021, "field 'comments'");
    view = finder.findRequiredView(source, 2131493016, "field 'scrollViewInRead'");
    target.scrollViewInRead = finder.castView(view, 2131493016, "field 'scrollViewInRead'");
    view = finder.findRequiredView(source, 2131493017, "field 'userPic'");
    target.userPic = finder.castView(view, 2131493017, "field 'userPic'");
    view = finder.findRequiredView(source, 2131493020, "field 'userName'");
    target.userName = finder.castView(view, 2131493020, "field 'userName'");
    view = finder.findRequiredView(source, 2131493015, "field 'spinKitViewInRead'");
    target.spinKitViewInRead = finder.castView(view, 2131493015, "field 'spinKitViewInRead'");
    view = finder.findRequiredView(source, 2131493019, "field 'content'");
    target.content = finder.castView(view, 2131493019, "field 'content'");
  }

  @Override public void unbind(T target) {
    target.title = null;
    target.comments = null;
    target.scrollViewInRead = null;
    target.userPic = null;
    target.userName = null;
    target.spinKitViewInRead = null;
    target.content = null;
  }
}
