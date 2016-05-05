package com.lxyg.app.customer.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.alexvasilkov.gestures.views.interfaces.GestureView;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.adapter.PaintingsImagesAdapter;
import com.lxyg.app.customer.bean.Forum;

import java.util.List;

import dev.mirror.library.utils.JsonUtils;

/**
 * Created by 王沛栋 on 2015/11/12.
 */
public class GestureViewsActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener,PaintingsImagesAdapter.OnSetupGestureViewListener{

    private ViewPager mViewPager;
    private String mIntentStr;
    private List<Forum.Imgs> mLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gesture_view);

        mIntentStr = getIntent().getExtras().getString(INTENT_ID);

        mLists = JsonUtils.parseList(mIntentStr,Forum.Imgs.class);

        mViewPager = (ViewPager)findViewById(R.id.paintings_view_pager);
        mViewPager.setAdapter(new PaintingsImagesAdapter(mViewPager,mLists,this,GestureViewsActivity.this));

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSetupGestureView(GestureView view) {

    }
}
