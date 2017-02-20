// Generated code from Butter Knife. Do not modify!
package com.example.mezereon.bookexchange;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class UserInfoActivity$$ViewBinder<T extends com.example.mezereon.bookexchange.UserInfoActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493028, "field 'name'");
    target.name = finder.castView(view, 2131493028, "field 'name'");
    view = finder.findRequiredView(source, 2131493030, "field 'sex'");
    target.sex = finder.castView(view, 2131493030, "field 'sex'");
    view = finder.findRequiredView(source, 2131493031, "field 'email'");
    target.email = finder.castView(view, 2131493031, "field 'email'");
    view = finder.findRequiredView(source, 2131493032, "field 'phone'");
    target.phone = finder.castView(view, 2131493032, "field 'phone'");
    view = finder.findRequiredView(source, 2131493033, "field 'changeInfo'");
    target.changeInfo = finder.castView(view, 2131493033, "field 'changeInfo'");
  }

  @Override public void unbind(T target) {
    target.name = null;
    target.sex = null;
    target.email = null;
    target.phone = null;
    target.changeInfo = null;
  }
}
