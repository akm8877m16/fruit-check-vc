package com.check.fruit.fruitcheck.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.check.fruit.fruitcheck.R;
import com.check.fruit.fruitcheck.adaptor.reportsAdapter;
import com.check.fruit.fruitcheck.model.Record;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link reports.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link reports#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reports extends Fragment implements SwipeItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    protected SwipeMenuRecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;

    protected reportsAdapter mAdapter;
    protected List<Record> mDataList;
    private ArrayList<Boolean> mDataVisibility;

    public reports() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment reports.
     */
    // TODO: Rename and change types and number of parameters
    public static reports newInstance(String param1, String param2) {
        reports fragment = new reports();
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
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        mRecyclerView = (SwipeMenuRecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = createLayoutManager();
        mAdapter = new reportsAdapter(getActivity(),getActivity(),mDataList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setSwipeItemClickListener(this);

        mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        mRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mDataVisibility = new ArrayList<Boolean>();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        DataSupport.findAllAsync(Record.class).listen(new FindMultiCallback(){
            @Override
            public <T> void onFinish(List<T> t) {
                mDataList = (List<Record>) t;
                mDataVisibility.clear();
                for(int i = 0;i<mDataList.size();i++){
                    mDataVisibility.add(false);
                }
                mAdapter.notifyDataSetChanged(mDataList);
            }
        });
    }

    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    public void onItemClick(View itemView, int position) {
        RelativeLayout reportLayout = (RelativeLayout)itemView.findViewById(R.id.report_detail);
        ImageView arrow = (ImageView)itemView.findViewById(R.id.show_detail);
        Boolean visible = mDataVisibility.get(position);
        if(visible){
            reportLayout.setVisibility(View.VISIBLE);
            arrow.setImageResource(R.drawable.ic_navigate_next_black_18dp_90);
        }else{
            reportLayout.setVisibility(View.GONE);
            arrow.setImageResource(R.drawable.ic_navigate_next_black_18dp);
        }
        mDataVisibility.set(position,!visible);
    }

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_50);

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = getResources().getDimensionPixelSize(R.dimen.dp_50);
            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity())
                        .setBackground(R.drawable.selector_red)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
            }
        }
    };
    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            //因为只有一个菜单选项，所以不用考虑选择
            Record deleteRecord= mDataList.get(adapterPosition);
            deleteRecord.delete();
            DataSupport.findAllAsync(Record.class).listen(new FindMultiCallback(){
                @Override
                public <T> void onFinish(List<T> t) {
                    mDataList = (List<Record>) t;
                    mDataVisibility.clear();
                    for(int i = 0;i<mDataList.size();i++){
                        mDataVisibility.add(false);
                    }
                    mAdapter.notifyDataSetChanged(mDataList);
                }
            });
        }
    };

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
