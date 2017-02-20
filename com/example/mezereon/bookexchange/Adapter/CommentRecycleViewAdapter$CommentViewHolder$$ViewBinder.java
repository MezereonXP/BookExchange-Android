// Generated code from Butter Knife. Do not modify!
package com.example.mezereon.bookexchange.Adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CommentRecycleViewAdapter$CommentViewHolder$$ViewBinder<T extends com.example.mezereon.bookexchange.Adapter.CommentRecycleViewAdapter.CommentViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493063, "field 'time'");
    target.time = finder.castView(view, 2131493063, "field 'time'");
    view = finder.findRequiredView(source, 2131493061, "field 'userName'");
    target.userName = finder.castView(view, 2131493061, "field 'userName'");
    view = finder.findRequiredView(source, 2131493062, "field 'content'");
    target.content = finder.castView(view, 2131493062, "field 'content'");
    view = finder.findRequiredView(source, 2131493060, "field 'circleImageView'");
    target.circleImageView = finder.castView(view, 2131493060, "field 'circleImageView'");
    view = finder.findRequiredView(source, 2131493059, "field 'cardView'");
    target.cardView = finder.castView(view, 2131493059, "field 'cardView'");
  }

  @Override public void unbind(T target) {
    target.time = null;
    target.userName = null;
    target.content = null;
    target.circleImageView = null;
    target.cardView = null;
  }
}
