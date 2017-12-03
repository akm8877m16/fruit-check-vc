package com.check.fruit.fruitcheck.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.check.fruit.fruitcheck.R;
import com.check.fruit.fruitcheck.adaptor.dataAdapter;
import com.check.fruit.fruitcheck.model.Record;
import com.check.fruit.fruitcheck.util.AnalysisUtils;
import com.check.fruit.fruitcheck.util.AppConstant;
import com.check.fruit.fruitcheck.util.CameraUtil;
import com.check.fruit.fruitcheck.views.circleView;
import com.soundcloud.android.crop.Crop;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static android.app.Activity.RESULT_OK;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link imageProcess.OnImageProcessInteractionListener} interface
 * to handle interaction events.
 * Use the {@link imageProcess#newInstance} factory method to
 * create an instance of this fragment.
 */
public class imageProcess extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LOG_TAG = "Fruit Check";
    public static final int TAKE_PHOTO=1;//拍照常量
    public static final int CROP_PHOTO=2;//裁剪常量
    public static final int PROCESS_FINISH = 10;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String imagePath;
    private float imageProcessViewWidth;

    private OnImageProcessInteractionListener mListener;
    private Button actionBtn;
    public Uri imageUri;//图片的Uri
    private  RelativeLayout imageView;
    private ImageView processFrame;
    private Spinner testChoice;
    private ListView reportTable;
    private ArrayList<String> reportContentCol1;
    private ArrayList<String> reportContentCol2;
    private Bitmap dataBitmap;
    private circleView circle0;
    private circleView circle1;
    private circleView circle2;
    private circleView circle3;
    private circleView circle4;
    private circleView circle5;
    private Button saveBtn;
    private Handler myUiHandler;
    private TextView reportName;
    private TextView reportSummary;
    //private reportListAdapter madapter;
    private dataAdapter madapter;
    private ImageView check_status;
    private Boolean canSave;


    public imageProcess() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment imageProcess.
     */
    // TODO: Rename and change types and number of parameters
    public static imageProcess newInstance(String param1, String param2) {
        imageProcess fragment = new imageProcess();
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
        reportContentCol1 = new ArrayList<String>();
        reportContentCol2 = new ArrayList<String>();
        reportContentCol1.add("采样点");
        reportContentCol1.add("0(基准)");
        reportContentCol1.add("1");
        reportContentCol1.add("2");
        reportContentCol1.add("3");
        reportContentCol1.add("4");
        reportContentCol1.add("5");
        reportContentCol1.add("平均值");

        reportContentCol2.add("维生素C");
        reportContentCol2.add("0.00 mg/100g");
        reportContentCol2.add("0.00 mg/100g");
        reportContentCol2.add("0.00 mg/100g");
        reportContentCol2.add("0.00 mg/100g");
        reportContentCol2.add("0.00 mg/100g");
        reportContentCol2.add("0.00 mg/100g");
        reportContentCol2.add("0.00 mg/100g");
        myUiHandler = new Handler(){
            public void handleMessage(Message msg) {
                if (msg.what == PROCESS_FINISH){
                    refreshReport();
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_process, container, false);
        imageView =view.findViewById(R.id.image_process_view_layout);
        actionBtn = view.findViewById(R.id.action);
        processFrame = view.findViewById(R.id.processView);
        processFrame.measure(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageProcessViewWidth = getActivity().getResources().getDisplayMetrics().heightPixels;
        int imageViewWidth = (int)(imageProcessViewWidth*0.5*0.6);
        RelativeLayout.LayoutParams layoutParms = new RelativeLayout.LayoutParams(imageViewWidth,imageViewWidth);
        layoutParms.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParms.setMargins(0,16,0,0);
        imageView.setLayoutParams(layoutParms);

        int radius = 25;
        int bigRadius= imageViewWidth/2;
        int bigRadius2 = imageViewWidth/2-radius;

        circle0 = new circleView(getActivity(),imageViewWidth/2,radius+10,0,imageViewWidth,imageViewWidth);
        circle1 = new circleView(getActivity(), (float) (bigRadius+bigRadius2*sin(60/180.0*Math.PI)),(float)(bigRadius*0.6),5,imageViewWidth,imageViewWidth);
        circle2 = new circleView(getActivity(),(float) (bigRadius+bigRadius2*sin(120/180.0*Math.PI)),(float)(bigRadius*1.4),4,imageViewWidth,imageViewWidth);
        circle3 = new circleView(getActivity(),(float) (bigRadius+bigRadius*sin(180/180.0*Math.PI)),(float)(bigRadius-bigRadius*cos(180/180*Math.PI)-radius-10),3,imageViewWidth,imageViewWidth);
        circle4 = new circleView(getActivity(),(float) (bigRadius+bigRadius2*sin(240/180.0*Math.PI)),(float)(bigRadius*1.4),2,imageViewWidth,imageViewWidth);
        circle5 = new circleView(getActivity(),(float) (bigRadius+bigRadius2*sin(300/180.0*Math.PI)),(float)(bigRadius*0.6),1,imageViewWidth,imageViewWidth);


        imageView.addView(circle0);
        imageView.addView(circle1);
        imageView.addView(circle2);
        imageView.addView(circle3);
        imageView.addView(circle4);
        imageView.addView(circle5);
        reportName = view.findViewById(R.id.report_name);
        reportSummary = view.findViewById(R.id.report_summary);
        actionBtn.setOnClickListener(this);
        testChoice = view.findViewById(R.id.test_option);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.test_choices, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        testChoice.setAdapter(adapter);
        reportTable = view.findViewById(R.id.report_content);
        madapter = new dataAdapter(getActivity(),reportContentCol2,reportContentCol1);
        reportTable.setAdapter(madapter);
        saveBtn = view.findViewById(R.id.saveRecord);
        saveBtn.setOnClickListener(this);
        canSave = false;
        check_status = view.findViewById(R.id.result_image);
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
        if (context instanceof OnImageProcessInteractionListener) {
            mListener = (OnImageProcessInteractionListener) context;
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
    public interface OnImageProcessInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

        @Override
        public void onClick(View v){
            if(v == actionBtn){
                if(actionBtn.getText().equals("点击拍照")){
                    CameraUtil.getInstance().camera(getActivity());
                }else{
                    //let's do some simple image process
                    imageProcessThread processThread = new imageProcessThread();
                    processThread.run();
                }
            }
            if(v == saveBtn){
                if(!canSave){
                    Toast.makeText(getActivity(), "请先拍照后点击确定检测", Toast.LENGTH_SHORT).show();
                }else{
                    List<Record> mDestList = DataSupport.where("name = ?", reportName.getText().toString()).find(Record.class);
                    if(mDestList.size()>0){
                        Toast.makeText(getActivity(), "检测结果已经保存", Toast.LENGTH_SHORT).show();
                    }else{
                        Record record = new Record();
                        record.setName(reportName.getText().toString());
                        record.setType(reportContentCol2.get(0));
                        record.setV0(Float.valueOf((reportContentCol2.get(1).split(" "))[0]));
                        record.setV1(Float.valueOf((reportContentCol2.get(2).split(" "))[0]));
                        record.setV2(Float.valueOf((reportContentCol2.get(3).split(" "))[0]));
                        record.setV3(Float.valueOf((reportContentCol2.get(4).split(" "))[0]));
                        record.setV4(Float.valueOf((reportContentCol2.get(5).split(" "))[0]));
                        record.setV5(Float.valueOf((reportContentCol2.get(6).split(" "))[0]));
                        record.setV6(Float.valueOf((reportContentCol2.get(7).split(" "))[0]));
                        record.save();
                        canSave = false;
                        Toast.makeText(getActivity(), "检测结果保存成功", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null ){
            String cropPhotoPath = data.getStringExtra(AppConstant.KEY.IMG_PATH);
            int width = (int)(imageProcessViewWidth*0.5*0.6);
            int height = (int)(imageProcessViewWidth*0.5*0.6);
            Bitmap b = BitmapFactory.decodeFile(cropPhotoPath);
            dataBitmap = Bitmap.createScaledBitmap(b, width, height, false);
            processFrame.setImageBitmap(dataBitmap);
            actionBtn.setText("确认");
            if(!b.isRecycled()){
                b.recycle();
            }
        }

    }

    public Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type)
    {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type)
    {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = null;
        try
        {
            // This location works best if you want the created images to be
            // shared
            // between applications and persist after your app has been
            // uninstalled.
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "FruitCheck");

            Log.d(LOG_TAG, "Successfully created mediaStorageDir: "
                    + mediaStorageDir);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(LOG_TAG, "Error in Creating mediaStorageDir: "
                    + mediaStorageDir);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                // 在SD卡上创建文件夹需要权限：
                // <uses-permission
                // android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                Log.d(LOG_TAG,
                        "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }
        else if (type == MEDIA_TYPE_VIDEO)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        }
        else
        {
            return null;
        }

        return mediaFile;
    }

    private void refreshReport(){
        String option = testChoice.getSelectedItem().toString();
        SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time=dateformat1.format(new Date());
        reportName.setText(option + " "+ time);
        actionBtn.setText("点击拍照");
        canSave = true;
        madapter.notifyDataSetChanged(reportContentCol2,reportContentCol1);
        float average = Float.valueOf(reportContentCol2.get(7).split(" ")[0]);
        reportSummary.setText(AnalysisUtils.checkResultWithPic(testChoice.getSelectedItem().toString(),average,check_status));
    }
    /*
    private class reportListAdapter extends BaseAdapter
    {
        private LayoutInflater mLayoutInflater;

        public reportListAdapter()
        {
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            mLayoutInflater = (LayoutInflater)getActivity().getSystemService(inflater);
        }

        //---returns the number of images---
        public int getCount() {
            return reportContentCol1.size();
        }

        //---returns the item---
        public Integer getItem(int position) {
            return position;
        }

        //---returns the ID of an item---
        public long getItemId(int position) {
            return position;
        }

        //---returns an ImageView view---
        public View getView(final int position, View convertView,
                            final ViewGroup parent)
        {
            View view;
            ViewHolder holder;
            if (null == convertView) {
                view = mLayoutInflater.inflate(R.layout.report_item, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.col1 = (TextView) view.findViewById(R.id.column1);
                holder.col2 = (TextView) view.findViewById(R.id.column2);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            holder.col1.setText(reportContentCol1.get(position));
            holder.col2.setText(reportContentCol2.get(position));
            return view;
        }

        private class ViewHolder {
            public TextView col1;
            public TextView col2;
        }
    }
    */
    private class imageProcessThread implements Runnable {
        public void run() {
            String option = testChoice.getSelectedItem().toString();
            double result0,result1,result2,result3,result4,result5,result6;
            int value0 = getAverageIntensity((int)(circle0.getCurrentX()),(int)(circle0.getCurrentY()),(int)(circleView.RADIUS));
            int value1 = getAverageIntensity((int)(circle1.getCurrentX()),(int)(circle1.getCurrentY()),(int)(circleView.RADIUS));
            int value2 = getAverageIntensity((int)(circle2.getCurrentX()),(int)(circle2.getCurrentY()),(int)(circleView.RADIUS));
            int value3 = getAverageIntensity((int)(circle3.getCurrentX()),(int)(circle3.getCurrentY()),(int)(circleView.RADIUS));
            int value4 = getAverageIntensity((int)(circle4.getCurrentX()),(int)(circle4.getCurrentY()),(int)(circleView.RADIUS));
            int value5 = getAverageIntensity((int)(circle5.getCurrentX()),(int)(circle5.getCurrentY()),(int)(circleView.RADIUS));
            float xishu = 1;
            String danwei = "";
            if(option.equals("维生素C")){
               xishu = (float) 0.4;
                danwei = "mg/100g";
            }else if(option.equals("果糖")){
               xishu = (float) (0.7/2.8);
                danwei = "g/100g";
            }else{
                xishu = (float) (0.3*0.0186*100);
                danwei = "g/L";
            }
            result0 = 0;
            result1 = abs((value0-value1)*xishu);
            result2 = abs((value0-value2)*xishu);
            result3 = abs((value0-value3)*xishu);
            result4 = abs((value0-value4)*xishu);
            result5 = abs((value0-value5)*xishu);
            result6 = (result1+result2+result3+result4+result5)/5;


            reportContentCol2.clear();
            reportContentCol2.add(option);
            reportContentCol2.add(String.format("%.2f "+danwei,result0));
            reportContentCol2.add(String.format("%.2f "+danwei,result1));
            reportContentCol2.add(String.format("%.2f "+danwei,result2));
            reportContentCol2.add(String.format("%.2f "+danwei,result3));
            reportContentCol2.add(String.format("%.2f "+danwei,result4));
            reportContentCol2.add(String.format("%.2f "+danwei,result5));
            reportContentCol2.add(String.format("%.2f "+danwei,result6));



            Message message = new Message();
            message.what = PROCESS_FINISH;
            myUiHandler.sendMessage(message);
        }
        //get average intersity from image
        public int getAverageIntensity(int centerX,int centerY,int radius){
            int mPhotoWidth = 2*radius;
            int mPhotoHeight = 2*radius;
            int[] pix = new int[mPhotoWidth * mPhotoHeight];
            int centerXd = centerX;
            int centerYd = centerY;
            int bitmapHeight = dataBitmap.getHeight();
            int bitmapWidth = dataBitmap.getWidth();
            dataBitmap.getPixels(pix, 0, mPhotoWidth, centerX-radius, centerY-radius, mPhotoWidth, mPhotoHeight);
            int r, g, b,index;
            int R = 0, G = 0, B = 0;
            int realLength = 0;

            int center = (int)(radius);
            for (int y = 0; y < mPhotoHeight; y++) {
                for (int x = 0; x < mPhotoWidth; x++) {
                    if((x-center)*(x-center)+(y-center)*(y-center) <(radius-5)*(radius-5)){
                        realLength++;
                        index = y * mPhotoWidth + x;
                        r = (pix[index] >> 16) & 0xff;
                        g = (pix[index] >> 8) & 0xff;
                        b = pix[index] & 0xff;
                        R = R+r;
                        B = B+b;
                        G = G+g;
                    }
                }
            }
            double Ra = R / realLength;
            double Ba = B / realLength;
            double Ga = G / realLength;
            return (int)(0.2126*Ra + 0.7152*Ga + 0.0722*Ba);
        }

    }
}

