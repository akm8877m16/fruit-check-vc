package com.check.fruit.fruitcheck.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.check.fruit.fruitcheck.R;
import com.check.fruit.fruitcheck.fragments.imageProcess;
import com.check.fruit.fruitcheck.model.Record;

import java.util.List;

/**
 * Created by yinwen on 11/6/2017.
 */


public class dataAdapter extends android.widget.BaseAdapter
{
    private LayoutInflater mLayoutInflater;
    private List<String> mDataList;
    private List<String> mtitleList;

    public dataAdapter(Activity activity,List<String> dataList,List<String> titleList)
    {   mDataList = dataList;
        mtitleList = titleList;
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        mLayoutInflater = (LayoutInflater)activity.getSystemService(inflater);
    }

    //---returns the number of images---
    public int getCount() {
        return mDataList.size();
    }

    //---returns the item---
    public Integer getItem(int position) {
        return position;
    }

    //---returns the ID of an item---
    public long getItemId(int position) {
        return position;
    }

    public void notifyDataSetChanged(List<String> dataList,List<String> titleList) {
        this.mDataList = dataList;
        this.mtitleList = titleList;
        super.notifyDataSetChanged();
    }

    //---returns an ImageView view---
    public View getView(final int position, View convertView,
                        final ViewGroup parent)
    {
        View view;
        dataAdapter.ViewHolder holder;
        if (null == convertView) {
            view = mLayoutInflater.inflate(R.layout.report_item, parent, false);
            holder = new dataAdapter.ViewHolder();
            assert view != null;
            holder.col1 = (TextView) view.findViewById(R.id.column1);
            holder.col2 = (TextView) view.findViewById(R.id.column2);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (dataAdapter.ViewHolder) view.getTag();
        }
        holder.col1.setText(mtitleList.get(position));
        holder.col2.setText(mDataList.get(position));
        return view;
    }

    private class ViewHolder {
        public TextView col1;
        public TextView col2;
    }
}