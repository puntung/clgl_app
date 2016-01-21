package com.gdsx.clgl.view;

import android.app.Activity;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.gdsx.clgl.R;
import com.gdsx.clgl.tricks.FragPageAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/20.
 */
public class UploadedRecordActivity extends FragmentActivity implements View.OnClickListener{
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private ImageView ivBottomLine;
    private TextView tab_alread_upload,tab_not_upload;
    private int currIndex = 0;
    private int position_one;
    private Resources resources;
    private ImageView back_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_record);
        resources = getResources();
        initView();
        initViewPager();
    }

    public void initView(){
        ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
        back_img = (ImageView)findViewById(R.id.uploaded_back);
        tab_alread_upload = (TextView) findViewById(R.id.tab_already_upload);
        tab_not_upload = (TextView) findViewById(R.id.tab_not_upload);

        tab_alread_upload.setOnClickListener(new MyTabOnClickListener(0));
        tab_not_upload.setOnClickListener(new MyTabOnClickListener(1));
        back_img.setOnClickListener(this);
    }

    private void initViewPager(){
        mPager = (ViewPager) findViewById(R.id.vPager);
        FragmentAlreadyUploade alread_fragment =new FragmentAlreadyUploade();
        FragmentNotUpload not_fragment=new FragmentNotUpload();
        fragmentsList = new ArrayList<>();
        fragmentsList.add(alread_fragment);
        fragmentsList.add(not_fragment);
        mPager.setAdapter(new FragPageAdapter(getSupportFragmentManager(), fragmentsList));
        mPager.setCurrentItem(0);
        mPager.addOnPageChangeListener(new myOnPageChangeListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.uploaded_back:
                finish();
                break;
        }
    }

    /**
     *ViewPager切换Fragment
     */
    private class myOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            int width = ivBottomLine.getWidth();
            position_one = width;
            switch (position){
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                        tab_not_upload.setTextColor(resources.getColor(R.color.font_grey));
                    }
                    tab_alread_upload.setTextColor(resources.getColor(R.color.colorPrimary));
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, position_one, 0, 0);
                        tab_alread_upload.setTextColor(resources.getColor(R.color.font_grey));
                    }
                    tab_not_upload.setTextColor(resources.getColor(R.color.colorPrimary));
                    break;
            }
            currIndex = position;
            animation.setFillAfter(true);
            animation.setDuration(300);
            ivBottomLine.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public class MyTabOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyTabOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    };
}
