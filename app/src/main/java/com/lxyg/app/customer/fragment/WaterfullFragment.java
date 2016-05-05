package com.lxyg.app.customer.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.bean.Forum;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.fragment.DevRefreshRecycleViewFragment;
import dev.mirror.library.view.recycleview.DevRecycleViewHolder;

/**
 * Created by 王沛栋 on 2015/10/30.
 */
public class WaterfullFragment extends DevRefreshRecycleViewFragment<Forum> implements Constants{

    @Override
    public void getView(DevRecycleViewHolder holder, Forum item, int position) {
        List<Forum.Imgs> imgs = item.getFormImgs();

        ImageView iv = holder.findViewById(R.id.pic);

        if (imgs != null){
            iv.setVisibility(View.VISIBLE);
            AppContext.displayImage(iv, item.getFormImgs().get(0).getImg_url());
        }else{
            iv.setVisibility(View.GONE);
        }
        holder.setText(R.id.name, item.getTitle());
        holder.setText(R.id.btn_zan,item.getZanNum()+"");
        holder.setText(R.id.btn_comment, item.getReplayNum() + "");
    }

    @Override
    public void LoadCompletion() {
        super.LoadCompletion();

    }


    @Override
    public int setLayoutId() {
        return R.layout.fragment_forum_new;
    }

    @Override
    public int setItemLayoutId() {
        return R.layout.item_forum_new;
    }

    private AppHttpClient mHttpClient;
    @Override
    public void loadData() {

        if(mHttpClient == null){
            mHttpClient = new AppHttpClient();
        }

        JSONObject jb = new JSONObject();
        try {
            jb.put("pg",mPageNo);
        }catch (JSONException e){

        }

        mHttpClient.postData(FORUM_LIST, jb, new AppAjaxCallback.onRecevieDataListener<Forum>() {
            @Override
            public void onReceiverData(List<Forum> data, String msg) {
                if (mList == null) {
                    mList = new ArrayList<Forum>();
                }

                if(mPageNo == mPageDefault){
                    mList.clear();
                }

                mList.addAll(data);
                setListAdapter();
            }

            @Override
            public void onReceiverError(String msg) {
            }

            @Override
            public Class<Forum> dataTypeClass() {
                return Forum.class;
            }
        });
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }
}
