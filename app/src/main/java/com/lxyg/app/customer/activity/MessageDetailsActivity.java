package com.lxyg.app.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.bean.Message;

/**
 * Created by 王沛栋 on 2016/4/20.
 */
public class MessageDetailsActivity extends BaseActivity {
    private TextView mTvContent;
    private Message mMessage;

    private boolean isPush = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        setTitleText("推送消息详情");

        isPush = getIntent().getBooleanExtra("IS_PUSH",false);
        mMessage = getIntent().getParcelableExtra(INTENT_ID);
        mTvContent = (TextView)findViewById(R.id.content);

        mTvContent.setText(mMessage.getContent());

        ImageView iv = (ImageView)findViewById(R.id.left_icon);
        iv.setImageResource(R.mipmap.ic_back_w);
        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isPush){
                    Intent i = new Intent(MessageDetailsActivity.this, SplashActivity.class);
                	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                	startActivity(i);
                }else{
                    finish();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isPush){
            Intent i = new Intent(MessageDetailsActivity.this, SplashActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity(i);
        }else{
            finish();
        }
    }
}
