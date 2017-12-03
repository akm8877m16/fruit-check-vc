package com.check.fruit.fruitcheck.util;

import android.widget.ImageView;

import com.check.fruit.fruitcheck.R;

/**
 * Created by yinwen on 11/8/2017.
 */

public class AnalysisUtils {
    public static String checkResultWithPic(String type, float average, ImageView result){
        float Low = 0;
        float Med = 0;
        if(type.equals("维生素C")){
            Med = 20;
            Low = 3;
        }else if(type.equals("果糖")){
            Med = (float)(50/2.8);
            Low = (float)(5/2.8);
        }else{
            Med = (float)(20*0.0186*100);
            Low = (float)(5*0.0186*100);
        }

        if(average <= Low){
            result.setImageResource(R.drawable.low);
            return type + "含量"+ "贫瘠";
        }else if(average >= Med){
            result.setImageResource(R.drawable.high);
            return type + "含量"+ "丰富";
        }else{
            result.setImageResource(R.drawable.med);
            return type + "含量"+ "中等";
        }
    }

    public static String checkResult(String type, float average){
        float Low = 0;
        float Med = 0;
        if(type.equals("维生素C")){
            Med = 20;
            Low = 3;
        }else if(type.equals("果糖")){
            Med = (float)(50/2.8);
            Low = (float)(5/2.8);
        }else{
            Med = (float)(20*0.0186*100);
            Low = (float)(5*0.0186*100);
        }

        if(average <= Low){
            return type + "含量"+ "贫瘠";
        }else if(average >= Med){
            return type + "含量"+ "丰富";
        }else{
            return type + "含量"+ "中等";
        }
    }
}
