package com.lxyg.app.customer.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;


/**
 * Created by haha on 2015/9/16.
 */
public class TestAnim extends BaseActivity {
    private Animation mAnimation;

    private Button mBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test_anim);
//        mBtn = initButton(R.id.btn);
//
//        mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_car);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        mBtn.startAnimation(mAnimation);
//        AnimationUtil.translateAnim(v);
    }
}
