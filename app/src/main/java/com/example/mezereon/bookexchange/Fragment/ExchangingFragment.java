package com.example.mezereon.bookexchange.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.R;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangingFragment extends Fragment {


    private View view;
    public ExchangingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_exchanging, container, false);
        bindAllTheView();
        injectByDagger();
        return view;
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void bindAllTheView() {
        ButterKnife.bind(this,view);
    }

}
