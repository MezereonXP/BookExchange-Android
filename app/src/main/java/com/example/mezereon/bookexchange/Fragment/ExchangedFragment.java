package com.example.mezereon.bookexchange.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mezereon.bookexchange.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangedFragment extends Fragment {


    public ExchangedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_exchanged, container, false);
        return view;
    }

}
