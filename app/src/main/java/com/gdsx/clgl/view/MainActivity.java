package com.gdsx.clgl.view;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gdsx.clgl.R;
import com.gdsx.clgl.tricks.FragPageAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewPager mPager;
    private RadioGroup mGroup;
    private ArrayList<Fragment> fragmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initViewPager();
    }

    private void initView(){
        mPager=(ViewPager)findViewById(R.id.main_viewpager);
        mGroup=(RadioGroup)findViewById(R.id.main_radiogroup);
        mGroup.setOnCheckedChangeListener(new myCheckChangeListener());
    }

    /**
     *RadioButton切换Fragment
     */
    private class myCheckChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            mGroup.check(checkedId);
            switch (checkedId){
                case R.id.rb_home:
                    //ViewPager显示第一个Fragment且关闭页面切换动画效果
                    mPager.setCurrentItem(0,false);
                    break;
                case R.id.rb_account:
                    mPager.setCurrentItem(1,false);
                    break;
            }
        }
    }

    /**
     *ViewPager切换Fragment,RadioGroup做相应变化
     */
    private class myOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    mGroup.check(R.id.rb_home);
                    break;
                case 1:
                    mGroup.check(R.id.rb_account);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void initViewPager(){
        FragmentHome home_fragment =new FragmentHome();
        FragmentAccount account_fragment=new FragmentAccount();
        fragmentList=new ArrayList<Fragment>();
        fragmentList.add(home_fragment);
        fragmentList.add(account_fragment);
        //ViewPager设置适配器
        mPager.setAdapter(new FragPageAdapter(getSupportFragmentManager(), fragmentList));
        //ViewPager显示第一个Fragment
        mPager.setCurrentItem(1);
        mGroup.check(R.id.rb_home);
        //ViewPager页面切换监听
        mPager.addOnPageChangeListener(new myOnPageChangeListener());
    }
}
