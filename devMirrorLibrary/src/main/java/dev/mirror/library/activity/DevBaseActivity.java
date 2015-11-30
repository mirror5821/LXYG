package dev.mirror.library.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import dev.mirror.library.R;

/**
 * Created by mirror on 2015/7/3.
 */
public class DevBaseActivity extends FragmentActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public String getVersionName(){
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);
            return packageInfo.versionName;
        }catch (PackageManager.NameNotFoundException e){
            return "";
        }
    }


    public void showToast(int resId){
        if(getActivity()!=null){
            Toast.makeText(getActivity(),resId,Toast.LENGTH_LONG).show();;
        }
    }

    public void showToast(Character text){
        if(getActivity()!=null){
            Toast.makeText(getActivity(),text,Toast.LENGTH_LONG).show();;
        }
    }

    private Toast mToast;
    private Toast getToastInstance(){
        if(mToast == null){
            mToast = new Toast(getActivity());
        }
        return mToast;
    }
    public void showToast(String str){
        if(getActivity()!=null){
            Toast t = getToastInstance();
            View view = getActivity().getLayoutInflater().inflate(R.layout.base_toast, null);
            TextView tv = (TextView)view.findViewById(R.id.tv_base_toast);
            tv.setText(str);
            t.setView(view);
            t.show();
//            Toast.makeText(getActivity(),str,Toast.LENGTH_LONG).show();;
        }
    }
    public Activity getActivity(){
        return this;
    }
    @Override
    public void onClick(View v) {

    }

    private ProgressDialog mProgressDialog;
    /**
     * 显示加载dialog
     * @param msg
     */
    public void showProgressDialog(String msg){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(true);
        }
        //如果显示的字符为空，则设置默认值
        if(TextUtils.isEmpty(msg)){
            msg = "正在加载数据...";
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    /**
     * 显示加载dialog
     * @param msg
     * @param cancelable
     */
    public void showProgressDialog(String msg,boolean cancelable){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(cancelable);
        }
        //如果显示的字符为空，则设置默认值
        if(TextUtils.isEmpty(msg)){
            msg = "正在加载数据...";
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }
    /**
     * 取消加载dialog
     */
    public void cancelProgressDialog(){
        if(mProgressDialog != null){
            mProgressDialog.cancel();
        }
    }

    /**
     * 初始化TextView
     * @param resId
     * @return
     *
     */
    public TextView initTextView(int resId){
        TextView tv = (TextView)findViewById(resId);
        tv.setOnClickListener((OnClickListener) getActivity());
        return tv;
    }

    /**
     * 初始化LinearLayout
     * @param resId
     * @return
     */
    public LinearLayout initLinearLayout(int resId){
        LinearLayout btn = (LinearLayout)getActivity().findViewById(resId);
        btn.setOnClickListener((OnClickListener) getActivity());
        return btn;
    }

    /**
     * 初始化Edittext
     * @param resId
     * @return
     */
    public EditText initEditText(int resId){
        EditText tv = (EditText)findViewById(resId);
//    	tv.setOnClickListener((OnClickListener) getActivity());
        return tv;
    }

    /**
     * 初始化ImageView
     * @param resId
     * @return
     */
    public ImageView initImageView(int resId){
        ImageView img = (ImageView)findViewById(resId);
        img.setOnClickListener((OnClickListener) getActivity());
        return img;
    }

    /**
     * 初始化Button
     * @param resId
     * @return
     */
    public Button initButton(int resId){
        Button btn = (Button)findViewById(resId);
        btn.setOnClickListener((OnClickListener)getActivity());
        return btn;
    }

    private AlertDialog.Builder mBuilder;
    /**
     * 弹出提示框
     * @param title
     * @param msg
     * @param btnStr
     * @param l
     */
    public void showNormalDialog(String title,String msg,String btnStr,DialogInterface.OnClickListener l){
        if(mBuilder == null){
            mBuilder = new AlertDialog.Builder(getActivity());
        }
        mBuilder.setTitle(title);
        mBuilder.setMessage(msg);
        mBuilder.setPositiveButton(btnStr, l);
        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        Dialog d = mBuilder.create();
        d.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
