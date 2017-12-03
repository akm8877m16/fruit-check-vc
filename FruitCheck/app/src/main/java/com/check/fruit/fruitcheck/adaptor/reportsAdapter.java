package com.check.fruit.fruitcheck.adaptor;

/*
 * Copyright 2016 Yan Zhenjie
         *
         * Licensed under the Apache License, Version 2.0 (the "License");
         * you may not use this file except in compliance with the License.
         * You may obtain a copy of the License at
         *
         *      http://www.apache.org/licenses/LICENSE-2.0
         *
         * Unless required by applicable law or agreed to in writing, software
         * distributed under the License is distributed on an "AS IS" BASIS,
         * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
         * See the License for the specific language governing permissions and
         * limitations under the License.
*/
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.check.fruit.fruitcheck.R;
import com.check.fruit.fruitcheck.model.Record;
import com.check.fruit.fruitcheck.util.AnalysisUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class reportsAdapter extends BaseAdapter<reportsAdapter.ViewHolder> {

    private List<Record> mDataList;
    private  ArrayList<String> mTitleList;
    public reportsAdapter(Context context,Activity activity1,List<Record> dataList){
        super(context, activity1);
        mDataList = dataList;
        mTitleList = new ArrayList<String>();
        mTitleList.add("采样点");
        mTitleList.add("0(基准)");
        mTitleList.add("1");
        mTitleList.add("2");
        mTitleList.add("3");
        mTitleList.add("4");
        mTitleList.add("5");
        mTitleList.add("平均值");
    }

    public void notifyDataSetChanged(List<Record> dataList) {
        this.mDataList = dataList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.report, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Record record = mDataList.get(position);
        ArrayList<String> datas = new ArrayList<String>();
        datas.add(record.getType());
        String type = record.getType();
        String danwei = "";
        if(type.equals("维生素C")){
            danwei = "mg/100g";
        }else if(type.equals("葡萄糖")){
            danwei = "g/L";
        }else{
            danwei = "g/100g";
        }
        datas.add(String.format("%.2f "+danwei,record.getV0()));
        datas.add(String.format("%.2f "+danwei,record.getV1()));
        datas.add(String.format("%.2f "+danwei,record.getV2()));
        datas.add(String.format("%.2f "+danwei,record.getV3()));
        datas.add(String.format("%.2f "+danwei,record.getV4()));
        datas.add(String.format("%.2f "+danwei,record.getV5()));
        datas.add(String.format("%.2f "+danwei,record.getV6()));
        dataAdapter dataAdapter1 = new dataAdapter(getActivity(),datas,mTitleList);

        holder.setResult(AnalysisUtils.checkResult(type,record.getV6()));
        holder.setName(record.getName());
        holder.setAdapter(dataAdapter1);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView result;
        TextView name;
        ListView content;

        public ViewHolder(View itemView) {
            super(itemView);
            result = (TextView) itemView.findViewById(R.id.report_summary2);
            content = (ListView) itemView.findViewById(R.id.report_content2);
            name = (TextView) itemView.findViewById(R.id.reportList_name);
        }

        public void setResult(String summary) {
            this.result.setText(summary);
        }

        public  void setName(String name) {
            this.name.setText(name);
        }

        public void setAdapter(dataAdapter adapter){
            this.content.setAdapter(adapter);
            //this.content.deferNotifyDataSetChanged();
        }
    }

}
