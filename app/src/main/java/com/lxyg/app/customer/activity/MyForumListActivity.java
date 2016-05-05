package com.lxyg.app.customer.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.adapter.ForumListGridAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Forum;
import com.lxyg.app.customer.utils.AppAjaxCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.DpUtil;
import dev.mirror.library.view.CircleImageView;
import dev.mirror.library.view.NoScrollGridView;

/**
 * Created by 王沛栋 on 2015/11/18.
 */
public class MyForumListActivity extends BaseListActivity<Forum> {

    @Override
    public int setLayoutId() {
        return R.layout.activity_forum_list_my;
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
        Button delete = (Button)DevListBaseAdapter.ViewHolder.get(convertView,R.id.btn_delete);
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

        //删除帖子
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteForum(position);
            }
        });

        //判断点赞的图标
        if(f.getIsZan()){
            Drawable drawable= ContextCompat.getDrawable(getActivity(), R.mipmap.ic_zan_d);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            zan.setCompoundDrawables(drawable, null, null, null);

//            ivZan.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_zan_d));

        }else{
            Drawable drawable= ContextCompat.getDrawable(getActivity(), R.mipmap.ic_zan_n);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
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

    private void deleteForum(final int position){
        if(mBuilder == null){
            mBuilder = new AlertDialog.Builder(MyForumListActivity.this);
        }
        mBuilder.setTitle("删除帖子");
        mBuilder.setMessage("确定删除该帖?");
        mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(position);
            }
        });
        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        mBuilder.show();
    }


    /**
     * 删除帖子
     */
    private void delete(final int position){
        showProgressDialog("正在提交数据");

        JSONObject jb = new JSONObject();
        try{
            jb.put("form_id",mList.get(position).getForm_id());
        }catch (JSONException e){

        }
        mBaseHttpClient.postData1(FOURM_DELETE, jb, new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
                cancelProgressDialog();
                showToast(msg);
            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });

        cancelProgressDialog();
    }

    private AlertDialog.Builder mBuilder;
    @Override
    public void LoadCompletion() {
        super.LoadCompletion();

        setBack();
        setTitleText("帖子管理");

        mListView.getRefreshableView().setDividerHeight(DpUtil.dip2px(getActivity(), 4));
        mListView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (mBuilder == null) {
                    mBuilder = new AlertDialog.Builder(MyForumListActivity.this);
                }
                mBuilder.setTitle("删除帖子");
                mBuilder.setMessage("确定删除该帖?");
                mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(mList.get(position - 1).getForm_id());
                    }
                });
                mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });
                mBuilder.show();

                return false;
            }
        });

        mListView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);

    }


    /**
     * 删除帖子
     */
    private void delete(String fId){
        showProgressDialog("正在提交数据");
        JSONObject jb = new JSONObject();
        try{
            jb.put("form_id",fId);
        }catch (JSONException e){

        }

        mBaseHttpClient.postData1(FOURM_DELETE, jb, new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                cancelProgressDialog();
                showToast(msg);
            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });

        cancelProgressDialog();
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

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void loadData() {
        mBaseHttpClient.postData(MY_FOURM, new JSONObject(), new AppAjaxCallback.onRecevieDataListener<Forum>() {
            @Override
            public void onReceiverData(List<Forum> data, String msg) {
                if (mList == null) {
                    mList = new ArrayList<Forum>();
                }
                if (pageNo == mDefaultPage) {
                    mList.clear();
                }

                mList.addAll(data);
                setListAdapter();
            }

            @Override
            public void onReceiverError(String msg) {
                setLoadingFailed("暂无内容!");
            }

            @Override
            public Class<Forum> dataTypeClass() {
                return Forum.class;
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> paramAdapterView, View view, int position, long num) {
        startActivity(new Intent(getActivity(), ForumDetailsActivity.class).putExtra(INTENT_ID,
                mList.get(position - 1).getForm_id()).putExtra("from",1));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){

        }

    }
}
