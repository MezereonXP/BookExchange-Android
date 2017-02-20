// Generated code from Butter Knife. Do not modify!
package com.example.mezereon.bookexchange.Adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NormalRecycleViewAdapter2$NormalTextViewHolder$$ViewBinder<T extends com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter2.NormalTextViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493071, "field 'title'");
    target.title = finder.castView(view, 2131493071, "field 'title'");
    view = finder.findRequiredView(source, 2131493070, "field 'name'");
    target.name = finder.castView(view, 2131493070, "field 'name'");
    view = finder.findRequiredView(source, 2131493069, "field 'pic'");
    target.pic = finder.castView(view, 2131493069, "field 'pic'");
    view = finder.findRequiredView(source, 2131493073, "field 'good'");
    target.good = finder.castView(view, 2131493073, "field 'good'");
    view = finder.findRequiredView(source, 2131493075, "field 'concern'");
    target.concern = finder.castView(view, 2131493075, "field 'concern'");
    view = finder.findRequiredView(source, 2131493074, "field 'numOfComment'");
    target.numOfComment = finder.castView(view, 2131493074, "field 'numOfComment'");
    view = finder.findRequiredView(source, 2131493068, "field 'card'");
    target.card = finder.castView(view, 2131493068, "field 'card'");
    view = finder.findRequiredView(source, 2131493072, "field 'content'");
    target.content = finder.castView(view, 2131493072, "field 'content'");
  }

  @Override public void unbind(T target) {
    target.title = null;
    target.name = null;
    target.pic = null;
    target.good = null;
    target.concern = null;
    target.numOfComment = null;
    target.card = null;
    target.content = null;
  }
}
