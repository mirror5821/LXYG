package com.lxyg.app.customer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.AreaSelectListActivity;
import com.lxyg.app.customer.activity.ShopSelectActivity;
import com.lxyg.app.customer.bean.Constants;

public class GuidePagerViewFragment extends Fragment {


	public GuidePagerViewFragment newInstance(int pager,int resId) {
		GuidePagerViewFragment fragment = new GuidePagerViewFragment();
        Bundle args = new Bundle();
        args.putInt("pager", pager);
        args.putInt("resId", resId);
        fragment.setArguments(args);
        return fragment;
    }

    private int mPager;
    private int mResId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这里我只是简单的用num区别标签，其实具体应用中可以使用真实的fragment对象来作为叶片
        mPager = getArguments().getInt("pager");
        mResId = getArguments().getInt("resId");

    }
    /**为Fragment加载布局时调用**/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_guide, null);
        Button btn = (Button)view.findViewById(R.id.btn);
        ImageView iv = (ImageView) view.findViewById(R.id.img_s);
        if (mPager == 2){
            btn.setVisibility(View.VISIBLE);
        }else{
            btn.setVisibility(View.GONE);
        }

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //传这个值 是为了取消第一次选择区域的回退按钮
//                startActivity(new Intent(getActivity(),AreaSelectListActivity.class).putExtra(Constants.INTENT_ID,2001));
                startActivity(new Intent(getActivity(),ShopSelectActivity.class).putExtra(Constants.INTENT_ID,1));
                getActivity().finish();
            }
        });

        iv.setImageResource(mResId);
        return view;
    }
}