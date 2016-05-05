package com.lxyg.app.customer.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lxyg.app.customer.R;

import java.io.File;

import dev.mirror.library.utils.ImageTools;

/**
 * Created by haha on 2015/9/10.
 */
public class TestImg extends BaseActivity {
    private ImageView mImageView;
    private Button mBtn;
    private ImageTools mImageTools;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_img);
        mBtn = (Button)findViewById(R.id.btn);
        mImageView = (ImageView)findViewById(R.id.img);

        mImageTools = new ImageTools(this);

        mBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn:
                mImageTools.showGetImageDialog("选择照片的方式!");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == Activity.RESULT_OK){


            switch (requestCode) {
                case ImageTools.CAMARA:


                    mImageTools.getBitmapFromCamara(new ImageTools.OnBitmapCreateListener() {

                        @Override
                        public void onBitmapCreate(Bitmap bitmap, String path) {
                            mImageTools.startZoomPhoto(Uri.fromFile(new File(path)), 2000, 2000);
                        }
                    });
                    break;

                case ImageTools.GALLERY:
                    mImageTools.startZoomPhoto(data.getData(),4000,4000);
                    break;
                case ImageTools.BITMAP:
                    mImageView.setImageBitmap(mImageTools.getBitmapFromZoomPhoto(data));
                    break;
            }
        }
    }
}
