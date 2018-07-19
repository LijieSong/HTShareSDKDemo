package com.zfkj.share;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 项目名称：ShareSDK
 * 类描述：HTShareMessageBody 描述: 消息体基类
 * 创建人：songlijie
 * 创建时间：2017/7/19 10:21
 * 邮箱:814326663@qq.com
 */
public class HTShareMessageBody implements Parcelable {
    public JSONObject bodyJson = new JSONObject();
    public static final Creator<HTShareMessageBody> CREATOR = new Creator<HTShareMessageBody>() {
        @Override
        public HTShareMessageBody createFromParcel(Parcel parcel) {
            return new HTShareMessageBody(parcel);
        }

        @Override
        public HTShareMessageBody[] newArray(int i) {
            return new HTShareMessageBody[i];
        }
    };

    public HTShareMessageBody(String content) {
        if (!TextUtils.isEmpty(content)) {
            this.bodyJson = JSONObject.parseObject(content);
        }
    }

    public HTShareMessageBody() {
    }

    public String getLocalBody() {
        return this.bodyJson.toJSONString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int position) {
        parcel.writeString(this.bodyJson.toJSONString());
    }

    protected HTShareMessageBody(Parcel parcel) {
        String content = parcel.readString();
        this.bodyJson = JSONObject.parseObject(content);
    }
}
