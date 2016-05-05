package com.lxyg.app.customer.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.adapter.GuidePageViewAdapter;

/**
 * Created by 王沛栋 on 2015/11/5.
 */
public class GuidePageActivity extends BaseActivity {
    private ViewPager mViewPager;

    private GuidePageViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        initView();
    }

    private int [] mImgs = {R.mipmap.ic_guide1,R.mipmap.ic_guide2,R.mipmap.ic_guide3};
    private void initView(){
        mAdapter = new GuidePageViewAdapter(getSupportFragmentManager(),mImgs);
        mViewPager.setAdapter(mAdapter);
    }
}
