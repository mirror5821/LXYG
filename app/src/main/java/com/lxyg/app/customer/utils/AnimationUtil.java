package com.lxyg.app.customer.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by haha on 2015/9/16.
 */
public class AnimationUtil {
    private static AnimationSet mSet;
    private static Animation mAnimation;

    private static void getInstance(){
        if(mSet == null){
            mSet = new AnimationSet(true);
        }
//        if(mAnimation == null){
//            mAnimation = new AlphaAnimation(0,1);//AlphaAnimation 控制渐变透明的效果
//        }
//
//        mAnimation.setDuration(500);//设置动画毫秒数
//        mSet.addAnimation(mAnimation);

    }

    //旋转 RotateAnimation
    public static void rotateAnim(View view){
        getInstance();
        RotateAnimation rotateAnimation  = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5F);
        rotateAnimation.setDuration(1000);
        mSet.addAnimation(rotateAnimation);
        view.startAnimation(mSet);

    }

    //移动
    public static void translateAnim(View view){
        getInstance();
        //放大
        ScaleAnimation scaleAnimation = null;
        if(scaleAnimation == null) {
            scaleAnimation = new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
                    Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        }

        scaleAnimation.setDuration(200);//设置时间

        TranslateAnimation translateAnim = null;
        if(translateAnim == null){
            translateAnim = new TranslateAnimation(0.1f,100.0f,0.1f,100.0f);
        }
        translateAnim.setDuration(1000);//设置时间
        translateAnim.setRepeatCount(1);//设置重复次数 -1 表示不重复
        translateAnim.setRepeatMode(Animation.REVERSE);//设置反方向执行
        mSet.addAnimation(translateAnim);
        mSet.addAnimation(scaleAnimation);
        view.startAnimation(mSet);
        translateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSet.cancel();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
