package com.lxyg.app.customer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.ActivityActivity;
import com.lxyg.app.customer.activity.ProductDetailsActivity;
import com.lxyg.app.customer.activity.WebViewNormalActivity;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Activitys;
import com.lxyg.app.customer.bean.Constants;

public class PagerViewFragment extends Fragment {


	public PagerViewFragment newInstance(Activitys activity) {
		PagerViewFragment fragment = new PagerViewFragment();
        Bundle args = new Bundle();
        args.putParcelable("num", activity);
        fragment.setArguments(args);
        return fragment;
    }

    private Activitys mActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这里我只是简单的用num区别标签，其实具体应用中可以使用真实的fragment对象来作为叶片
        mActivity = getArguments().getParcelable("num");

    }
    /**为Fragment加载布局时调用**/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_imageview, null);
        ImageView iv = (ImageView) view.findViewById(R.id.img_s);
        AppContext.displayImage(iv, mActivity.getImg_url());

        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                Toast.makeText(getActivity(),mActivity.getType()+"",Toast.LENGTH_LONG).show();
                switch (Integer.valueOf(mActivity.getType())) {
                    case 0:
                        //什么也不做
                        break;
                    case 1:
                        //跳转到网页
                        startActivity(new Intent(getActivity(), WebViewNormalActivity.class).putExtra(Constants.INTENT_ID,
                                mActivity.getAlt()).putExtra("TITLE",mActivity.getLabel_cn()));
                        break;
                    case 2:
                        //activityId
                        startActivity(new Intent(getActivity(), ActivityActivity.class).putExtra(Constants.INTENT_ID,
                                mActivity.getAlt()));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(), ProductDetailsActivity.class).putExtra(Constants.INTENT_ID,
                                mActivity.getAlt()));
                        break;
                }

            }
        });


        return view;
    }
}