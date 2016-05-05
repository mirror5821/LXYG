package com.lxyg.app.customer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.adapter.ImageAddsAdapter;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.ImageTools;
import dev.mirror.library.view.NoScrollGridView;

/**
 * Created by 王沛栋 on 2015/11/10.
 */
public class ForumAddActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private NoScrollGridView mGridView;
    private EditText mEtTitle,mEtContent;

    private ImageAddsAdapter mAdapter;
    private List<String> mListImg = new ArrayList<String>();
    private List<String> mList;

    private ImageTools mImageTools;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_add);
        setTitleText("添加话题");
        setRightTitle("添加");

        mEtContent = (EditText)findViewById(R.id.et_content);
        mEtTitle = (EditText)findViewById(R.id.et_title);

        mImageTools = new ImageTools(this);

        mGridView = (NoScrollGridView)findViewById(R.id.gridview);

        mList = new ArrayList<>();
        mList.add(null);

        mAdapter = new ImageAddsAdapter(getApplicationContext(),mList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.right_text:
                submit();
//                sub();
                break;
        }
    }

    private void sub(){
        String title = mEtTitle.getText().toString();
        String content = mEtContent.getText().toString();
        JSONObject jb = new JSONObject();
        try {
            jb.put("title",title);
            jb.put("content",content);

            int l = mListImg.size();
            if(l>0){
                JSONArray ja = new JSONArray();
                String [] imgs = new String[l];
                for (int i=0;i<l;i++){
                    ja.put(mListImg.get(i));
                }
                jb.put("imgs",ja.toString());
            }

        }catch (JSONException e){
            showToast(e.getLocalizedMessage());
        }
        mBaseHttpClient.postData1(FORUM_ADD, jb, new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                showToast(msg);

                Uri uri = Uri.parse("add_scuess");
                Intent intent = new Intent(null, uri);
                setResult(RESULT_OK, intent);
                cancelProgressDialog();
                finish();

            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //表示点击上传照片的按钮
        if(position == mList.size()-1){
            openImage();
//            mImageTools.showGetImageDialog("选择照片的方式!");
        }else{//这里应该做其他动作
            mList.remove(position);
            mAdapter.notifyDataSetChanged();
        }
    }


    private ArrayList<String> mSelectPath;
    private static final int REQUEST_IMAGE = 2;
    private void openImage(){
        int selectedMode = MultiImageSelectorActivity.MODE_MULTI;;
//        selectedMode = MultiImageSelectorActivity.MODE_SINGLE;


        int maxNum = 5;
        Intent intent = new Intent(ForumAddActivity.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
        // 默认选择
//        if (mSelectPath != null && mSelectPath.size() > 0) {
//            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
//        }
        if (mList != null && mList.size() > 1) {
            mList.remove(mList.size()-1);
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, (ArrayList)mList);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mList.clear();
                mAdapter.notifyDataSetChanged();

                mList.addAll(mSelectPath);
                mList.add(null);

                mAdapter.notifyDataSetChanged();
//                StringBuilder sb = new StringBuilder();
//                for(String p: mSelectPath){
////                    upLoadImg(p);
//                    sb.append(p);
//                    sb.append("\n");
//                }
//
//                mResultText.setText(sb.toString());


            }
        }


//        if(resultCode == RESULT_OK){
//            switch (requestCode) {
//                case ImageTools.CAMARA:
//                    mImageTools.getBitmapFromCamara(new ImageTools.OnBitmapCreateListener() {
//
//                        @Override
//                        public void onBitmapCreate(Bitmap bitmap, String path) {
//                            upLoadImg(bitmap);
//                        }
//                    });
//                    break;
//
//                case ImageTools.GALLERY:
//                    upLoadImg(mImageTools.getBitmapFromGallery(data));
//
//                    break;
//
//            }
//        }
    }

    private AppHttpClient uploadHttpClient;
//    private void upLoadImg(){
//        showProgressDialog("正在上传照片");
//        if(uploadHttpClient == null)
//            uploadHttpClient = new AppHttpClient();
//        uploadHttpClient.uploadImg(mImageTools.bitmapToString(bitmap), new AppAjaxCallback.onResultListener() {
//            @Override
//            public void onResult(String data, String msg) {
//                mList.add(mListImg.size(), bitmap);
//                mAdapter.notifyDataSetChanged();
//                showToast(msg);
//                cancelProgressDialog();
//                mListImg.add(data);
//            }
//
//            @Override
//            public void onError(String msg) {
//                cancelProgressDialog();
//                showToast(msg);
//            }
//        });
//
//    }

    private static final int DOWNLOAD_IMG = 1;
    private void submit(){
        showProgressDialog("正在提交数据", false);

        if(mList.size()<=1){
            sub();
        }else{
            if(uploadHttpClient == null){
                uploadHttpClient = new AppHttpClient();
            }
            for(String p: mSelectPath){

                uploadHttpClient.uploadImg(mImageTools.filePathToString(p), new AppAjaxCallback.onResultListener() {
                    @Override
                    public void onResult(String data, String msg) {
                        Message  message = Message.obtain();
                        message.obj = data;
                        message.what = DOWNLOAD_IMG;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(String msg) {

                    }
                });

            }

        }

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == DOWNLOAD_IMG){
                mListImg.add((String)msg.obj);

                if(mListImg.size() == mSelectPath.size()){
                    sub();
                }
            }
        }
    };
}
