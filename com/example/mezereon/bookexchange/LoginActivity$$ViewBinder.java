// Generated code from Butter Knife. Do not modify!
package com.example.mezereon.bookexchange;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginActivity$$ViewBinder<T extends com.example.mezereon.bookexchange.LoginActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493010, "field 'spinKitView'");
    target.spinKitView = finder.castView(view, 2131493010, "field 'spinKitView'");
    view = finder.findRequiredView(source, 2131492997, "field 'title'");
    target.title = finder.castView(view, 2131492997, "field 'title'");
    view = finder.findRequiredView(source, 2131492996, "field 'logo'");
    target.logo = finder.castView(view, 2131492996, "field 'logo'");
    view = finder.findRequiredView(source, 2131493001, "field 'name'");
    target.name = finder.castView(view, 2131493001, "field 'name'");
    view = finder.findRequiredView(source, 2131493000, "field 'signIn'");
    target.signIn = finder.castView(view, 2131493000, "field 'signIn'");
    view = finder.findRequiredView(source, 2131493007, "field 'btn_signIn'");
    target.btn_signIn = finder.castView(view, 2131493007, "field 'btn_signIn'");
    view = finder.findRequiredView(source, 2131492998, "field 'layout1'");
    target.layout1 = finder.castView(view, 2131492998, "field 'layout1'");
    view = finder.findRequiredView(source, 2131493006, "field 'haveAccount'");
    target.haveAccount = finder.castView(view, 2131493006, "field 'haveAccount'");
    view = finder.findRequiredView(source, 2131493008, "field 'btn_logIn'");
    target.btn_logIn = finder.castView(view, 2131493008, "field 'btn_logIn'");
    view = finder.findRequiredView(source, 2131493009, "field 'white'");
    target.white = finder.castView(view, 2131493009, "field 'white'");
    view = finder.findRequiredView(source, 2131493004, "field 'pwd'");
    target.pwd = finder.castView(view, 2131493004, "field 'pwd'");
    view = finder.findRequiredView(source, 2131492999, "field 'signUp'");
    target.signUp = finder.castView(view, 2131492999, "field 'signUp'");
  }

  @Override public void unbind(T target) {
    target.spinKitView = null;
    target.title = null;
    target.logo = null;
    target.name = null;
    target.signIn = null;
    target.btn_signIn = null;
    target.layout1 = null;
    target.haveAccount = null;
    target.btn_logIn = null;
    target.white = null;
    target.pwd = null;
    target.signUp = null;
  }
}
