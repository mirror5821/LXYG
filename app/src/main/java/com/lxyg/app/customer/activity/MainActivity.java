package com.lxyg.app.customer.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.bean.Forum;
import com.lxyg.app.customer.fragment.ForumWaterfullFragment;
import com.lxyg.app.customer.fragment.Index3Fragment;
import com.lxyg.app.customer.fragment.LifeServiceFragment;
import com.lxyg.app.customer.fragment.MyNewFragment;
import com.lxyg.app.customer.iface.OnSelectTab;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppHttpClient;
import com.lxyg.app.customer.utils.SharePreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 主栏目界面
 * @author 王沛栋
 *
 */
public class MainActivity extends MainTabActivity implements OnSelectTab {

	@Override
	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//得判断第三选项卡中是否已经登陆
//		mFragmentTabHost.setOnTabChangedListener(new OnTabChangeListener() {
//
//			@Override
//			public void onTabChanged(String tabId) {
//
//
//				showToast(tabId);
//			}
//		});

	}


	/**
	 * 设置底部文字
	 */
	@Override
	public String[] setTabTitles() {
		return  new String[] {"生活超市","便民","社区互动","个人中心"};
	}


	/**
	 * 设置底部图标
	 */
	@Override
	public int[] setTabIcons() {
		return new int[]{R.drawable.tab1,R.drawable.tab4,R.drawable.tab2,R.drawable.tab3};
	}


	/**
	 * 设置切换fragment
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Fragment> Class<T>[] setFragment() {
//

//		return  new Class[] { Index2Fragment.class,CategoryFragment.class,ShopingCarFragment.class,
//				MyNewFragment.class };
		//ForumFragment
		return  new Class[] { Index3Fragment.class,LifeServiceFragment.class,ForumWaterfullFragment.class,
				MyNewFragment.class };


	}

	/**
	 * 此方法不用任何操作
	 */
	@Override
	public Bundle setFragmentArgment(int position) {
		return null;
	}


	/**
	 * 切换tab
	 */
	@Override
	public void onSelect(int index) {
		mFragmentTabHost.setCurrentTab(index);
	}



	@Override
	public void onShopping() {
//		mTvs[2].setVisibility(View.VISIBLE);
//		mTvs[2].setText("新");

//		List<Car> userList = AppContext.mDb.findAll(Car.class);//查询所有的用户
//		if(userList != null){
//			int i =userList.size();
//			if(i == 0){
//				mTvs[2].setVisibility(View.GONE);
//			}else {
//				//如果使用tab气泡  请将下面视图显示
////				mTvs[2].setVisibility(View.VISIBLE);
//				mTvs[2].setVisibility(View.GONE);
//				mTvs[2].setText(i + "");
//			}
//		}else{
//			mTvs[2].setVisibility(View.GONE);
//		}
	}

	@Override
	public void onShopping(boolean isFourmNew) {
		if(isFourmNew){
			mTvs[2].setVisibility(View.VISIBLE);
			mTvs[2].setText("新");
		}else{
			mTvs[2].setVisibility(View.GONE);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData();
//		onShopping(true);
	}

	public void loadData() {
		JSONObject jb = new JSONObject();
		try {
			jb.put("pg",1);
		}catch (JSONException e){

		}
		if(mBaseHttpClient==null){
			mBaseHttpClient = new AppHttpClient();
		}
		mBaseHttpClient.postData(FORUM_LIST, jb, new AppAjaxCallback.onRecevieDataListener<Forum>() {
			@Override
			public void onReceiverData(List<Forum> data, String msg) {
				if(data.size()==0){
					onShopping(false);
				}else{
					String id = data.get(0).getForm_id();
					String fId = SharePreferencesUtil.getFourmId(MainActivity.this, id);
					if(TextUtils.isEmpty(fId)){
						onShopping(true);
					}else{
						if(id.equals(fId)){
							onShopping(false);
						}else{
							onShopping(true);
						}
					}
				}




			}

			@Override
			public void onReceiverError(String msg) {
				onShopping(false);
			}

			@Override
			public Class<Forum> dataTypeClass() {
				return Forum.class;
			}
		});

	}


				/**
                 * 设置两次退出程序
                 */
	private long last = 0;
	@Override
	public void onBackPressed() {
		if (mFragmentTabHost.getCurrentTab() != 0) {
			mFragmentTabHost.setCurrentTab(0);
		} else {
			if (System.currentTimeMillis() - last > 2000) {
				showToast( "再按一次返回键退出");
				last = System.currentTimeMillis();
			} else {
				super.onBackPressed();
			}
		}

//		if (System.currentTimeMillis() - last > 2000) {
//			showToast( "再按一次返回键退出");
//			last = System.currentTimeMillis();
//		} else {
//			super.onBackPressed();
//		}

	}
}

