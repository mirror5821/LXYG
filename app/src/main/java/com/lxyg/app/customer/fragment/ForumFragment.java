package com.lxyg.app.customer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.ForumAddActivity;
import com.lxyg.app.customer.activity.ForumDetailsActivity;
import com.lxyg.app.customer.activity.WechatAndPhoneLoginActivity;
import com.lxyg.app.customer.adapter.ForumListGridAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Forum;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;
import com.lxyg.app.customer.utils.ImmersionModeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.DpUtil;
import dev.mirror.library.view.CircleImageView;
import dev.mirror.library.view.NoScrollGridView;

/**
 * Created by 王沛栋 on 2015/11/10.
 */
public class ForumFragment extends BaseListFragment<Forum> {
    private TextView mTvRight;
    @Override
    public int setLayoutId() {
        return R.layout.fragment_forum;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = getActivity().getLayoutInflater().inflate(R.layout.item_forum, null);
        }


        TextView title = (TextView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.title);
        TextView name = (TextView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.name);
        TextView content = (TextView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.content);
        TextView time = (TextView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.time);
        Button zan = (Button) DevListBaseAdapter.ViewHolder.get(convertView,R.id.btn_zan);
        Button comment = (Button) DevListBaseAdapter.ViewHolder.get(convertView,R.id.btn_comment);
//        ImageView ivZan = (ImageView) DevListBaseAdapter.ViewHolder.get(convertView,R.id.iv_zan);
        CircleImageView header = (CircleImageView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.c_img);
        NoScrollGridView gView = (NoScrollGridView) DevListBaseAdapter.ViewHolder.get(convertView,R.id.gridview);

        final Forum f = mList.get(position);
        name.setText(f.getName());
        content.setText(f.getContent());
        title.setText(f.getTitle());
        time.setText(f.getCreate_time());
        zan.setText(f.getZanNum()+"");
        comment.setText(f.getReplayNum()+"");

        AppContext.displayImage(header, f.getHead_img());

        List<Forum.Imgs> imgs = f.getFormImgs();
        if (imgs != null){
            gView.setVisibility(View.VISIBLE);
            gView.setAdapter(setImgAdapter(position));
            gView.deferNotifyDataSetChanged();

        }else{
            gView.setVisibility(View.GONE);
        }

        //判断点赞的图标
        if(f.getIsZan()){
            Drawable drawable= ContextCompat.getDrawable(getActivity(), R.mipmap.ic_zan_s);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, mBtnLeftDrawableWidth,mBtnLeftDrawableWidth);
            zan.setCompoundDrawables(drawable, null, null, null);


        }else{
            Drawable drawable= ContextCompat.getDrawable(getActivity(), R.mipmap.ic_zan_n);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, mBtnLeftDrawableWidth,mBtnLeftDrawableWidth);
            zan.setCompoundDrawables(drawable, null, null, null);

//            ivZan.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_zan_n));
            zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(f.getIsZan()){
                        showToast("亲,您已经点过赞!");
                    }else {
                        zan(position);
                    }

                }
            });
        }


        return convertView;
    }

    private void zan(final int position){
        JSONObject jb = new JSONObject();
        try{
            jb.put("form_id",mList.get(position).getForm_id());
        }catch (JSONException e){

        }

        mBaseHttpClient.postData1(FORUM_ZAN_ADD, jb, new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                mList.get(position).setZanNum(mList.get(position).getZanNum()+1);
                mList.get(position).setIsZan(true);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });

    }


    private ForumListGridAdapter setImgAdapter(int position){
        List<Forum.Imgs> datas = mList.get(position).getFormImgs();
        return new ForumListGridAdapter(getActivity(),datas);
    }
    private ForumListGridAdapter mImgsAdapter;
    private List<Forum.Imgs> mListImg = new ArrayList<>();
    private ForumListGridAdapter setImgsAdapter(int position){
        mListImg.clear();
        mListImg.addAll(mList.get(position).getFormImgs());
        if(mImgsAdapter == null){
            mImgsAdapter = new ForumListGridAdapter(getActivity(),mListImg);
        }

        return mImgsAdapter;
    }


    private int mBtnLeftDrawableWidth = DpUtil.dip2px(getActivity(),18);
    @Override
    public void loadData() {
        JSONObject jb = new JSONObject();
        try {
            jb.put("pg",pageNo);
        }catch (JSONException e){

        }
        mBaseHttpClient.postData(FORUM_LIST, jb, new AppAjaxCallback.onRecevieDataListener<Forum>() {
            @Override
            public void onReceiverData(List<Forum> data, String msg) {
                if(mList == null){
                    mList = new ArrayList<Forum>();
                }
                if(pageNo == mDefaultPage){
                    mList.clear();
                }

                mList.addAll(data);
                setListAdapter();
            }

            @Override
            public void onReceiverError(String msg) {
                setLoadingFailed("暂无帖子内容!");
            }

            @Override
            public Class<Forum> dataTypeClass() {
                return Forum.class;
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> paramAdapterView, View view, int position, long num) {
        startActivity(new Intent(getActivity(), ForumDetailsActivity.class).
                putExtra(INTENT_ID, mList.get(position - 1).getForm_id()).putExtra("from",0));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionModeUtils.setTranslucentStatus(getActivity(), R.color.main_red);
        mTvRight = initTextView(R.id.right_text);
        mTvRight.setText("发起话题");
        setTitleText("社区互动");

        mListView.getRefreshableView().setDividerHeight(DpUtil.dip2px(getActivity(), 4));

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.right_text:
                if(!AppContext.IS_LOGIN) {
                    startActivityForResult(new Intent(getActivity(), WechatAndPhoneLoginActivity.class), LOGIN_CODE1);
                }else{
                    startActivityForResult(new Intent(getActivity(), ForumAddActivity.class), FORUM_ADD_CODE);
                }

                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {
                case LOGIN_CODE1:
                    startActivity(new Intent(getActivity(), ForumAddActivity.class));
                    break;
                case FORUM_ADD_CODE:
                    //重新加载数据
                    loadData();
                    break;
            }
        }
    }
}
