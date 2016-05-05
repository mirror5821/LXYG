package com.lxyg.app.customer.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lxyg.app.customer.R;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.recycleview.DevItemDecoration;
import dev.mirror.library.recycleview.DevRecycleViewAdapter;
import dev.mirror.library.recycleview.DevRecycleViewHolder;
import dev.mirror.library.recycleview.PullToRefreshRecycleView;

public class RecycleViewActivity extends Activity implements DevRecycleViewAdapter.OnItemClickListener {

    ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);
        for (int i = 0; i < 100; i++) {
            data.add("Item" + i);
        }
        final PullToRefreshRecycleView mainView = (PullToRefreshRecycleView) findViewById(R.id.mianView);

        DVAdapter adapter = new DVAdapter(this, data, android.R.layout.simple_list_item_1);
        mainView.getRefreshableView().setAdapter(adapter);


        ImageView headerView = new ImageView(this);
        headerView.setImageResource(R.mipmap.ic_launcher);

        adapter.addHeaderView(headerView); //添加头视图


        Button footerView = new Button(this);
        footerView.setText("load");
        adapter.addFooterView(footerView); //添加尾视图


        DevItemDecoration item = new DevItemDecoration(this, DevItemDecoration.VERTICAL_LIST);
        //item.setIsShowSecondItemDecoration(false); //不显示第一行 分割线
        item.setIsShowFirstItemDecoration(false);  //不显示第二行 分割线
        item.setMarginLeftDP(10);   //分割线左边距
        item.setMarginRightDP(10);  //分割线右边距

        mainView.getRefreshableView().addItemDecoration(item);  //添加分割线

        mainView.getRefreshableView().setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(this); //设置点击事件

        mainView.setScrollingWhileRefreshingEnabled(true);
        mainView.setMode(PullToRefreshBase.Mode.BOTH);
        mainView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                Toast.makeText(RecycleViewActivity.this, "下拉刷新", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mainView.onRefreshComplete();
                    }
                }, 4000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                Toast.makeText(RecycleViewActivity.this, "上拉加载", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mainView.onRefreshComplete();
                    }
                }, 4000);
            }
        });
    }

    @Override
    public void onItemClickListener(int posotion) {
        Toast.makeText(this, data.get(posotion), Toast.LENGTH_LONG).show();
    }

    class DVAdapter extends DevRecycleViewAdapter<String> {


        protected DVAdapter(Context context, List<String> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        /**
         * @param holder itemHolder
         * @param item   每一Item显示的数据
         */
        @Override
        public void convert(DevRecycleViewHolder holder, String item) {
            //holder.setText(android.R.id.text1, item);
            //或者
            TextView text = holder.getView(android.R.id.text1);
            text.setText(item);
        }
    }


}
