package com.example.mezereon.bookexchange.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mezereon.bookexchange.R;

/**
 * Created by Mezereon on 2017/2/15.
 */

public class BookShowFragment extends Fragment {

    //Constructor of the BookShowFragment
    public BookShowFragment(){ }

    private View viewOnBookShowFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewOnBookShowFragment= inflater.inflate(R.layout.fragment_bookshow, container, false);
        return viewOnBookShowFragment;
    }

}
