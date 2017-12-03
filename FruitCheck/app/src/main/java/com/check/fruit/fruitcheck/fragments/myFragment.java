package com.check.fruit.fruitcheck.fragments;


import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.check.fruit.fruitcheck.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link myFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class myFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //private RelativeLayout detail1;
    //private RelativeLayout detail2;
    private TextView detailView1;
    private TextView detailView2;
    private Boolean detail1Status;
    private Boolean detail2Status;
    private Boolean detail3Status;
    private ImageButton showContactDetail;
    private ImageButton showAboutUsDetail;
    private ImageButton showInstructDetail;
    private ImageView detailView3;

    public myFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment myFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static myFragment newInstance(String param1, String param2) {
        myFragment fragment = new myFragment();
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
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        //detail1 = (RelativeLayout) view.findViewById(R.id.about_us_col);
        //detail2 = (RelativeLayout) view.findViewById(R.id.contact_us_col);
        //detail1.setOnClickListener(this);
        //detail2.setOnClickListener(this);
        detailView1 = (TextView) view.findViewById(R.id.about_us_detail);
        detailView2 = (TextView) view.findViewById(R.id.contact_us_detail);
        detailView3 = (ImageView) view.findViewById(R.id.instruct_detail);
        detailView1.setVisibility(View.GONE);
        detail1Status = false;
        detailView2.setVisibility(View.GONE);
        detail2Status = false;
        detailView3.setVisibility(View.GONE);
        detail3Status = false;

        showAboutUsDetail = view.findViewById(R.id.show_detail3);
        showAboutUsDetail.setOnClickListener(this);
        showContactDetail = view.findViewById(R.id.show_detail4);
        showContactDetail.setOnClickListener(this);
        showInstructDetail = view.findViewById(R.id.show_detail5);
        showInstructDetail.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.show_detail3){
            if(detail1Status){
                detailView1.setVisibility(View.VISIBLE);
                showAboutUsDetail.setImageResource(R.drawable.ic_navigate_next_black_18dp_90);
            }else{
                detailView1.setVisibility(View.GONE);
                showAboutUsDetail.setImageResource(R.drawable.ic_navigate_next_black_18dp);
            }
            detail1Status = !detail1Status;
        }else if(id == R.id.show_detail4){
            if(detail2Status){
                detailView2.setVisibility(View.VISIBLE);
                showContactDetail.setImageResource(R.drawable.ic_navigate_next_black_18dp_90);
            }else{
                detailView2.setVisibility(View.GONE);
                showContactDetail.setImageResource(R.drawable.ic_navigate_next_black_18dp);
            }
            detail2Status = !detail2Status;
        }else if(id == R.id.show_detail5){
            if(detail3Status){
                detailView3.setVisibility(View.VISIBLE);
                showInstructDetail.setImageResource(R.drawable.ic_navigate_next_black_18dp_90);
            }else{
                detailView3.setVisibility(View.GONE);
                showInstructDetail.setImageResource(R.drawable.ic_navigate_next_black_18dp);
            }
            detail3Status = !detail3Status;
        }
    }

}
