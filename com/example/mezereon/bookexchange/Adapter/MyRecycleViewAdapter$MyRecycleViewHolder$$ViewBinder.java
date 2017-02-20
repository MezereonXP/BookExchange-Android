// Generated code from Butter Knife. Do not modify!
package com.example.mezereon.bookexchange.Adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MyRecycleViewAdapter$MyRecycleViewHolder$$ViewBinder<T extends com.example.mezereon.bookexchange.Adapter.MyRecycleViewAdapter.MyRecycleViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493076, "field 'settingCard'");
    target.settingCard = finder.castView(view, 2131493076, "field 'settingCard'");
    view = finder.findRequiredView(source, 2131493078, "field 'name'");
    target.name = finder.castView(view, 2131493078, "field 'name'");
    view = finder.findRequiredView(source, 2131493077, "field 'pic'");
    target.pic = finder.castView(view, 2131493077, "field 'pic'");
  }

  @Override public void unbind(T target) {
    target.settingCard = null;
    target.name = null;
    target.pic = null;
  }
}
