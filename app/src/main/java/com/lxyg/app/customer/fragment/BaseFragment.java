package com.lxyg.app.customer.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.utils.AppHttpClient;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import dev.mirror.library.fragment.DevBaseFragment;

/**
 * fragment基类
 * @author 王沛栋
 *
 */
public class BaseFragment extends DevBaseFragment implements Constants {
	public AppHttpClient mBaseHttpClient = new AppHttpClient();
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * 设置标题
	 * @param title
	 */
//	TextView mTvTitle;
	public void setTitleText(String title){
//		if(mTvTitle == null){
//			mTvTitle  = (TextView)getView().findViewById(R.id.bar_title);
//		}
		TextView tv = (TextView)getView().findViewById(R.id.bar_title);
		tv.setText(title);
	}
	
	/**
	 * 设置返回事件
	 */
	public void setBack(){
		ImageView img = (ImageView)getView().findViewById(R.id.left_icon);
		img.setImageResource(R.mipmap.ic_back_w);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
	}



}
