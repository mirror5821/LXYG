package com.lxyg.app.customer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.adapter.ForumListGridAdapter;
import com.lxyg.app.customer.adapter.LinerLayoutRVAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Forum;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppHttpClient;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.DpUtil;
import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PhoneFliter;
import dev.mirror.library.view.CircleImageView;
import dev.mirror.library.view.NoScrollGridView;

/**
 * Created by 王沛栋 on 2015/11/12.
 */
public class ForumDetailsActivity extends BaseActivity implements LinerLayoutRVAdapter.OnItemClickListener{
    private UltimateRecyclerView mRecycleView;

    private String mFId;
    private int mFrom = 0;
    private Forum mForum;

    private EditText mEtComment;
    private Button mBtnComment;
    private LinearLayout mView1;
    private TextView mTvListMsg;

    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_details);
        setBack();
        setTitleText("帖子详情");



        Bundle bundle = getIntent().getExtras();
        mFId = bundle.getString(INTENT_ID);
        mFrom = bundle.getInt("from");

        mRecycleView = (UltimateRecyclerView)findViewById(R.id.ultimate_recycler_view);

        mEtComment = (EditText)findViewById(R.id.et_comment);
        mBtnComment = (Button)findViewById(R.id.btn_comment);
        mView1 = (LinearLayout)findViewById(R.id.view1);
        mTvListMsg = (TextView)findViewById(R.id.tv_list);

        mBtnComment.setOnClickListener(this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //表示从个人帖子管理进入的
        if(mFrom == 1){
            setRightTitle("删除");
        }else{
            rightIconShare();
        }
        loadData();
    }

    private List<Forum.Comment> mList;

    private int mBtnLeftDrawableWidth;
    private LinerLayoutRVAdapter mAdapter;
    private void loadData(){
        mBtnLeftDrawableWidth = DpUtil.dip2px(getApplicationContext(), 18);
        JSONObject jb = new JSONObject();
        try {
            jb.put("form_id",mFId);
        }catch (JSONException e){

        }

        mBaseHttpClient.postData1(FORUM_DETAILS, jb, new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                mForum = JsonUtils.parse(data, Forum.class);

                if(mList == null){
                    mList = new ArrayList<>();

                    if( mForum.getReplays() != null){
                        mList.addAll(mForum.getReplays());
                        if(mAdapter == null){
                            initAdapter();
                        }


                    }else {
                        if (mAdapter == null) {
                            initAdapter();
                        }
                    }
                }else{

                    mList.clear();
                    if(mForum.getReplays() != null){
                        mList.addAll(mForum.getReplays());
                    }

                    mAdapter.notifyDataSetChanged();
                }


                mRecycleView.setRefreshing(false);


            }

            @Override
            public void onError(String error) {
                mRecycleView.setRefreshing(false);

            }
        });
    }

    private void initAdapter(){
        mAdapter = new LinerLayoutRVAdapter(mList,getApplicationContext()) {
            @Override
            public int setItemLayoutId() {
                return R.layout.item_forum_comment;
            }

            @Override
            public void setHeaderView(RecyclerView.ViewHolder holder, int position) {

            }
        };

        mRecycleView.setAdapter(mAdapter);

        mRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setSaveEnabled(true);
        mRecycleView.setClipToPadding(false);
        mRecycleView.enableLoadmore();
        mRecycleView.setNormalHeader(initHeaderView());

        mRecycleView.disableLoadmore();

        mRecycleView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        mAdapter.setOnItemClickListener(this);


    }


    private TextView mTitle,mName,mContent,mTime;
    private CircleImageView mImgHeader;
    private NoScrollGridView mGridView;
    private Button mBtnZan,mBtnCommentL,mBtnDelete;

    private int mZanNum = 0;

    private View initHeaderView(){

        View mView = View.inflate(getApplicationContext(), R.layout.activity_forum_detail_header, null);

        mBtnDelete = (Button)mView.findViewById(R.id.btn_delete);
        mBtnDelete.setVisibility(View.GONE);
        mTitle = (TextView) mView.findViewById(R.id.title);
        mName = (TextView) mView.findViewById(R.id.name);
        mContent = (TextView) mView.findViewById(R.id.content);
        mTime = (TextView) mView.findViewById(R.id.time);
        mBtnZan = (Button) mView.findViewById(R.id.btn_zan);
        mBtnCommentL = (Button) mView.findViewById(R.id.btn_comment);
        mImgHeader = (CircleImageView) mView.findViewById(R.id.c_img);
        mGridView = (NoScrollGridView) mView.findViewById(R.id.gridview);
        mContent.setVisibility(View.VISIBLE);

        String name = mForum.getName();
        if(name.length() == 11 && name.startsWith("1")){
            name = PhoneFliter.hideMiddleFourNum(name);
        }
        mName.setText(name);
        mContent.setText(mForum.getContent());
        mTitle.setText(mForum.getTitle());
        mTime.setText(mForum.getCreate_time());

        if (null != mForum.getZans()) {
            mZanNum = mForum.getZans().size();
        }

        int commentCount = mForum.getReplayNum();
        mBtnZan.setText(mZanNum + "");
        mBtnCommentL.setText(commentCount + "");
        AppContext.displayImage(mImgHeader, mForum.getHead_img());

        List<Forum.Imgs> imgs = mForum.getFormImgs();
        if (imgs != null) {
            mGridView.setVisibility(View.VISIBLE);
            mGridView.setAdapter(setImgAdapter());

        } else {
            mGridView.setVisibility(View.GONE);
        }


        //判断点赞的图标
        if (mForum.getIsZan()) {
            Drawable drawable= ContextCompat.getDrawable(getActivity(), R.mipmap.ic_zan_s);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, mBtnLeftDrawableWidth,mBtnLeftDrawableWidth);
            mBtnZan.setCompoundDrawables(drawable, null, null, null);
//            mImgZan.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_zan_d));

        } else {
            Drawable drawable= ContextCompat.getDrawable(getActivity(), R.mipmap.ic_zan_n);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, mBtnLeftDrawableWidth,mBtnLeftDrawableWidth);
            mBtnZan.setCompoundDrawables(drawable, null, null, null);

//            mImgZan.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_zan_n));
            mBtnZan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mForum.getIsZan()) {
                        showToast("亲,您已经点过赞!");
                    } else {
                        zan();
                    }

                }
            });
        }

        //评论
        mBtnCommentL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToUid = "null";
                mView1.setVisibility(View.VISIBLE);
                imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
            }
        });
        return mView;


    }

    private AlertDialog.Builder mBuilder;
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_comment:
                comment(v);
                break;
            case R.id.right_text:
                if(mBuilder == null){
                    mBuilder = new AlertDialog.Builder(ForumDetailsActivity.this);
                }
                mBuilder.setTitle("删除帖子");
                mBuilder.setMessage("确定删除该帖?");
                mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();
                    }
                });
                mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });
                mBuilder.show();


                break;
        }
    }



    private AppHttpClient mHttpClient2;

    /**
     * 点赞
     */
    private void zan(){
        showProgressDialog("正在点赞");
        JSONObject jb = new JSONObject();
        try{
            jb.put("form_id",mFId);
        }catch (JSONException e){

        }

        mBaseHttpClient.postData1(FORUM_ZAN_ADD, jb, new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                mForum.setIsZan(true);
                mBtnZan.setText((mZanNum + 1) + "");

                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.ic_zan_s);
                /// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, mBtnLeftDrawableWidth, mBtnLeftDrawableWidth);
                mBtnZan.setCompoundDrawables(drawable, null, null, null);
            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });

        cancelProgressDialog();
    }

    /**
     * 删除帖子
     */
    private void delete(){
        showProgressDialog("正在提交数据");
        JSONObject jb = new JSONObject();
        try{
            jb.put("form_id",mFId);
        }catch (JSONException e){

        }
        mBaseHttpClient.postData1(FOURM_DELETE, jb , new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                cancelProgressDialog();
                showToast(msg);
                finish();
            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });

        cancelProgressDialog();
    }

    private String mToUid;
    private void comment(final View v){
        showProgressDialog("正在提交评论");
        final String content = mEtComment.getText().toString();
        JSONObject jb = new JSONObject();
        try{
            //"formid":3,"uid":"b4f8f2652073400b","to_uid":"ad5d333b591e42fa","content":"xxaasdsdxcz"
            jb.put("form_id",mFId);
            if(!mToUid.equals("null")){
                jb.put("to_uid",mToUid);
            }

            jb.put("content",content);
        }catch (JSONException e){

        }
        mBaseHttpClient.postData1(FORUM_COMMENT, jb, new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                mEtComment.setText("");
                mView1.setVisibility(View.GONE);

                Forum.Comment c = new Forum.Comment();
                c.setContent(content);
                c.setU_uid(AppContext.USER_ID);
                c.setTo_u_uid(mToUid);
                c.setName("我:");

                if(!mToUid.equals("null")){
                    c.setTu_name(mToUserName);
                }


                if(mList == null){
                    mList = new ArrayList<Forum.Comment>();
                }

                mList.add(c);

                mAdapter.notifyDataSetChanged();
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘

            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });

        cancelProgressDialog();
    }

    private ForumListGridAdapter mImgsAdapter;

    private ForumListGridAdapter setImgAdapter(){
        if(mImgsAdapter == null){
            List<Forum.Imgs> mListImg = new ArrayList<>();
            mListImg.addAll(mForum.getFormImgs());
            mImgsAdapter = new ForumListGridAdapter(getActivity(),mListImg);
        }

        return mImgsAdapter;
    }

    private String mToUserName;
    @Override
    public void onItemClickListener(View v,int position) {
        Forum.Comment c = mList.get(position-1);
        mToUid = c.getU_uid();
        mToUserName = c.getName();
        mView1.setVisibility(View.VISIBLE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }
}
