package com.lxyg.app.customer.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by 王沛栋 on 2015/10/22.
 */
public class ImmersionModeUtils {
    public static final int Immersionstate =
            //只隐藏状态栏等，但还是占地方，是原来县市区为白色(布局本身北京色，可手动改变)
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    //布局拉伸但是虚拟按键显示
                    |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    //布局拉伸但是状态栏还显示
                    |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    //布局拉伸，虚拟按键隐藏
                    |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    //布局拉伸，状态栏隐藏
                    |View.SYSTEM_UI_FLAG_FULLSCREEN
                    //粘性沉浸模式
                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    public static final int UnImmersionstate = 0;
    public static View mDecorView;
    /**
     * 进入沉浸模式
     */
    public static void setImmersion(){
        mDecorView.setSystemUiVisibility(Immersionstate);
    }
    public static void setImmersion(View v){
        v.setSystemUiVisibility(Immersionstate);
    }

    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity,int color) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (AppContext.IsImmersed) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
    }
}
