package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by 王沛栋 on 2015/11/11.
 */
public class Forum implements Parcelable {

    private String content;//private String ccssddprivate String ,
    private int zanNum;//0,
    private String title;//private String testprivate String ,
    private int replayNum;//0,
    private List<Imgs> formImgs;//[],
    private String name;//null,
    private String u_uid;//private String testprivate String ,
    private String head_img;//null,
    private String create_time;//private String 2015-11-10 14:10:24private String ,
    private String form_id;//3
    private boolean isZan;
    private List<Zan> zans;
    private List<Comment> replays;
    
    
    public static class Comment{
        private String content;//private String xxaasdsdxczprivate String ,
        private String replayId;//2,
        private String name;//private String 辛德瑞拉private String ,
        private String u_uid;//private String b4f8f2652073400bprivate String ,
        private String head_img;//private String http:\/\/wx.qlogo.cn\/mmopen\/nw7CX0TAiaYwJtQjVuWQhYiaNbrkF9mu3t6hDgDBBmJo8WgBibGd7Uxrgib6f0ib3icEIFlymYgiaFmEzarZw01VvLTqaw66ymqjgyK\/0private String ,
        private String to_u_uid;//private String ad5d333b591e42faprivate String ,
        private String form_id;//5
        private String tu_head_img;
        private String tu_name;

        public String getTu_head_img() {
            return tu_head_img;
        }

        public void setTu_head_img(String tu_head_img) {
            this.tu_head_img = tu_head_img;
        }

        public String getTu_name() {
            return tu_name;
        }

        public void setTu_name(String tu_name) {
            this.tu_name = tu_name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getReplayId() {
            return replayId;
        }

        public void setReplayId(String replayId) {
            this.replayId = replayId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getU_uid() {
            return u_uid;
        }

        public void setU_uid(String u_uid) {
            this.u_uid = u_uid;
        }

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public String getTo_u_uid() {
            return to_u_uid;
        }

        public void setTo_u_uid(String to_u_uid) {
            this.to_u_uid = to_u_uid;
        }

        public String getForm_id() {
            return form_id;
        }

        public void setForm_id(String form_id) {
            this.form_id = form_id;
        }
    }

    public static class Zan{
        private String name;//private String 18837145615private String ,
        private String u_uid;//private String b4f8f2652073400bprivate String ,
        private String head_img;//null,
        private String form_id;//5,
        private String zId;//3

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getU_uid() {
            return u_uid;
        }

        public void setU_uid(String u_uid) {
            this.u_uid = u_uid;
        }

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public String getForm_id() {
            return form_id;
        }

        public void setForm_id(String form_id) {
            this.form_id = form_id;
        }

        public String getzId() {
            return zId;
        }

        public void setzId(String zId) {
            this.zId = zId;
        }
    }

    public  static class Imgs{
        private String img_url;//private String xxxxxxxxprivate String ,
        private String formImgId;//7,
        private String form_id;//3

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getFormImgId() {
            return formImgId;
        }

        public void setFormImgId(String formImgId) {
            this.formImgId = formImgId;
        }

        public String getForm_id() {
            return form_id;
        }

        public void setForm_id(String form_id) {
            this.form_id = form_id;
        }
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getZanNum() {
        return zanNum;
    }

    public void setZanNum(int zanNum) {
        this.zanNum = zanNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReplayNum() {
        return replayNum;
    }

    public void setReplayNum(int replayNum) {
        this.replayNum = replayNum;
    }

    public List<Imgs> getFormImgs() {
        return formImgs;
    }

    public void setFormImgs(List<Imgs> formImgs) {
        this.formImgs = formImgs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getU_uid() {
        return u_uid;
    }

    public void setU_uid(String u_uid) {
        this.u_uid = u_uid;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public boolean getIsZan() {
        return isZan;
    }

    public void setIsZan(boolean isZan) {
        this.isZan = isZan;
    }

    public List<Zan> getZans() {
        return zans;
    }

    public void setZans(List<Zan> zans) {
        this.zans = zans;
    }

    public List<Comment> getReplays() {
        return replays;
    }

    public void setReplays(List<Comment> replays) {
        this.replays = replays;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
