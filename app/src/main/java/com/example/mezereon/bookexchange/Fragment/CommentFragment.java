package com.example.mezereon.bookexchange.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter;
import com.example.mezereon.bookexchange.HomeActivity;
import com.example.mezereon.bookexchange.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment implements
        TabHost.TabContentFactory, GestureDetector.OnGestureListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GestureDetector detector;

    @Bind(R.id.recycle)
    RecyclerView comment;
    public CommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_comment, container, false);
        ButterKnife.bind(this,v);
        comment.setLayoutManager(new LinearLayoutManager(v.getContext()));//这里用线性显示 类似于listview
        comment.setAdapter(new NormalRecycleViewAdapter(v.getContext()));

        final GestureDetector mGestureDetector = new GestureDetector(
                getActivity(), this);
        HomeActivity.MyOnTouchListener myOnTouchListener = new HomeActivity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                boolean result = mGestureDetector.onTouchEvent(ev);
                return result;
            }
        };

        ((HomeActivity) getActivity())
                .registerMyOnTouchListener(myOnTouchListener);
        return v;
    }






    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        Log.d("tag","Scrolling!");
        if(distanceY>0){
            HomeActivity homeActivity=(HomeActivity)getActivity();
            homeActivity.animate();
        }else if(distanceY<0){
            HomeActivity homeActivity=(HomeActivity)getActivity();
            homeActivity.animate2();
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


    @Override
    public View createTabContent(String tag) {
        return null;
    }
}
