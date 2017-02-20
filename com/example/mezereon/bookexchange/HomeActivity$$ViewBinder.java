// Generated code from Butter Knife. Do not modify!
package com.example.mezereon.bookexchange;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HomeActivity$$ViewBinder<T extends com.example.mezereon.bookexchange.HomeActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492992, "field 'add'");
    target.add = finder.castView(view, 2131492992, "field 'add'");
    view = finder.findRequiredView(source, 2131492993, "field 'viewPager'");
    target.viewPager = finder.castView(view, 2131492993, "field 'viewPager'");
    view = finder.findRequiredView(source, 2131492990, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131492990, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131492991, "field 'searchView'");
    target.searchView = finder.castView(view, 2131492991, "field 'searchView'");
    view = finder.findRequiredView(source, 2131492994, "field 'layout_tab'");
    target.layout_tab = finder.castView(view, 2131492994, "field 'layout_tab'");
  }

  @Override public void unbind(T target) {
    target.add = null;
    target.viewPager = null;
    target.toolbar = null;
    target.searchView = null;
    target.layout_tab = null;
  }
}
