// Generated code from Butter Knife. Do not modify!
package com.example.mezereon.bookexchange.Fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class TalkFragment$$ViewBinder<T extends com.example.mezereon.bookexchange.Fragment.TalkFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493055, "field 'talk'");
    target.talk = finder.castView(view, 2131493055, "field 'talk'");
    view = finder.findRequiredView(source, 2131493056, "field 'spinKitView'");
    target.spinKitView = finder.castView(view, 2131493056, "field 'spinKitView'");
  }

  @Override public void unbind(T target) {
    target.talk = null;
    target.spinKitView = null;
  }
}
