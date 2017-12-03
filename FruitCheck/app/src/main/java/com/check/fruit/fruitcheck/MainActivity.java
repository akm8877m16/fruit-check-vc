package com.check.fruit.fruitcheck;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.check.fruit.fruitcheck.fragments.imageProcess;
import com.check.fruit.fruitcheck.fragments.myFragment;
import com.check.fruit.fruitcheck.fragments.reports;
import com.check.fruit.fruitcheck.util.AppConstant;
import com.check.fruit.fruitcheck.util.StatusBarUtils;
import com.soundcloud.android.crop.Crop;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity implements imageProcess.OnImageProcessInteractionListener, reports.OnFragmentInteractionListener {

    private TextView mTextMessage;
    private Fragment currentFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //image process fragment
                    currentFragment = imageProcess.newInstance("test1","test2");
                    FragmentManager fragmentManager =  getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.content, currentFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                case R.id.navigation_dashboard:
                    //reports fragment
                    currentFragment = reports.newInstance("test1","test2");
                    FragmentManager fragmentManager2 =  getSupportFragmentManager();
                    FragmentTransaction transaction2 = fragmentManager2.beginTransaction();
                    transaction2.replace(R.id.content, currentFragment);
                    transaction2.addToBackStack(null);
                    transaction2.commit();
                    return true;
                case R.id.navigation_notifications:
                    currentFragment = myFragment.newInstance("test1","test2");
                    FragmentManager fragmentManager3 =  getSupportFragmentManager();
                    FragmentTransaction transaction3 = fragmentManager3.beginTransaction();
                    transaction3.replace(R.id.content, currentFragment);
                    transaction3.addToBackStack(null);
                    transaction3.commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtils.setWindowStatusBarColor(MainActivity.this,R.color.colorSkyBlue);
        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
        SQLiteDatabase db = LitePal.getDatabase();
    }

    public void onFragmentInteraction(Uri uri) {
        Log.d("info", "test");
    }

    public void onResume(){
        super.onResume();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == AppConstant.REQUEST_CODE.CAMERA) {
            currentFragment.onActivityResult(requestCode,resultCode,data);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

}
