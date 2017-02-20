// Generated code from Butter Knife. Do not modify!
package com.example.mezereon.bookexchange.Fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SelfFragment$$ViewBinder<T extends com.example.mezereon.bookexchange.Fragment.SelfFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493054, "field 'name'");
    target.name = finder.castView(view, 2131493054, "field 'name'");
    view = finder.findRequiredView(source, 2131493052, "field 'setting'");
    target.setting = finder.castView(view, 2131493052, "field 'setting'");
    view = finder.findRequiredView(source, 2131493053, "field 'sign'");
    target.sign = finder.castView(view, 2131493053, "field 'sign'");
    view = finder.findRequiredView(source, 2131493049, "field 'bg'");
    target.bg = finder.castView(view, 2131493049, "field 'bg'");
    view = finder.findRequiredView(source, 2131493050, "field 'pic'");
    target.pic = finder.castView(view, 2131493050, "field 'pic'");
  }

  @Override public void unbind(T target) {
    target.name = null;
    target.setting = null;
    target.sign = null;
    target.bg = null;
    target.pic = null;
  }
}
