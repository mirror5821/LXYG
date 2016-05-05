package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by 王沛栋 on 2016/1/27.
 */
@Table(name="kk_message")
public class Message implements Parcelable{
    public Message(){

    }

    private int id;
    private String title;
    private String content;
    private String createTime;


    protected Message(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        createTime = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(createTime);
    }
}
