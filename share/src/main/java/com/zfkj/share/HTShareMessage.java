package com.zfkj.share;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;

/**
 * 项目名称：ShareSDK
 * 类描述：HTShareMessage 描述: 消息的module
 * 创建人：songlijie
 * 创建时间：2017/7/19 10:21
 * 邮箱:814326663@qq.com
 */
public class HTShareMessage implements Parcelable {
    private long size;
    private long time;
    private HTShareMessage.Type type;
    private HTShareMessageBody body;
    public static final Creator<HTShareMessage> CREATOR = new Creator<HTShareMessage>() {
        @Override
        public HTShareMessage createFromParcel(Parcel parcel) {
            return new HTShareMessage(parcel);
        }

        @Override
        public HTShareMessage[] newArray(int i) {
            return new HTShareMessage[i];
        }
    };

    public HTShareMessage(Parcel parcel) {
        this.size = parcel.readLong();
        this.time = parcel.readLong();
        int msgType = parcel.readInt();
        String bodyContent = parcel.readString();
        this.body = new HTShareMessageBody(bodyContent);
        if (msgType == HTShareMessage.Type.TEXT.ordinal()) {
            this.type = HTShareMessage.Type.TEXT;
        } else if (msgType == HTShareMessage.Type.IMAGE.ordinal()) {
            this.type = HTShareMessage.Type.IMAGE;
        } else if (msgType == HTShareMessage.Type.VOICE.ordinal()) {
            this.type = HTShareMessage.Type.VOICE;
        } else if (msgType == HTShareMessage.Type.VIDEO.ordinal()) {
            this.type = HTShareMessage.Type.VIDEO;
        } else if (msgType == HTShareMessage.Type.FILE.ordinal()) {
            this.type = HTShareMessage.Type.FILE;
        } else if (msgType == HTShareMessage.Type.LOCATION.ordinal()) {
            this.type = HTShareMessage.Type.LOCATION;
        }

    }

    public HTShareMessage() {
    }

    public static HTShareMessage createTextSendMessage(String content) {
        HTShareMessage message = createMessage();
        message.setType(HTShareMessage.Type.TEXT);
        HTShareMessageTextBody textBody = new HTShareMessageTextBody();
        textBody.setContent(content);
        message.setBody(textBody);
        return message;
    }

    public static HTShareMessage createImageSendMessage(String path, String size) {
        HTShareMessage message = createMessage();
        message.setType(HTShareMessage.Type.IMAGE);
        HTShareMessageImageBody imageBody = new HTShareMessageImageBody();
        imageBody.setLocalPath(path);
        imageBody.setSize(size);
        imageBody.setFileName(path.substring(path.lastIndexOf("/") + 1));
        message.setBody(imageBody);
        return message;
    }

    public static HTShareMessage createVoiceSendMessage(String path, int audioDuration) {
        HTShareMessage message = createMessage();
        message.setType(HTShareMessage.Type.VOICE);
        HTShareMessageVoiceBody voiceBody = new HTShareMessageVoiceBody();
        voiceBody.setLocalPath(path);
        voiceBody.setAudioDuration(audioDuration);
        voiceBody.setFileName(path.substring(path.lastIndexOf("/") + 1));
        message.setBody(voiceBody);
        return message;
    }

    public static HTShareMessage createVideoSendMessage(String path, String thumbPath, int videoDuration) {
        HTShareMessage message = createMessage();
        message.setType(HTShareMessage.Type.VIDEO);
        HTShareMessageVideoBody videoBody = new HTShareMessageVideoBody();
        videoBody.setFileName(path.substring(path.lastIndexOf("/") + 1));
        videoBody.setLocalPath(path);
        videoBody.setLocalPathThumbnail(thumbPath);
        videoBody.setVideoDuration(videoDuration);
        message.setBody(videoBody);
        return message;
    }

    public static HTShareMessage createLocationSendMessage(double lat, double lng, String address, String imagePath) {
        HTShareMessage message = createMessage();
        message.setType(HTShareMessage.Type.LOCATION);
        HTShareMessageLocationBody locationBody = new HTShareMessageLocationBody();
        locationBody.setAddress(address);
        locationBody.setFileName(imagePath.substring(imagePath.lastIndexOf("/") + 1));
        locationBody.setLatitude(lat);
        locationBody.setLocalPath(imagePath);
        locationBody.setLongitude(lng);
        message.setBody(locationBody);
        return message;
    }

    public static HTShareMessage createFileSendMessage(String path, long size) {
        HTShareMessage message = createMessage();
        message.setType(HTShareMessage.Type.FILE);
        HTShareMessageFileBody fileBody = new HTShareMessageFileBody();
        fileBody.setLocalPath(path);
        fileBody.setFileName(path.substring(path.lastIndexOf("/") + 1));
        fileBody.setSize(size);
        message.setBody(fileBody);
        return message;
    }

    private static HTShareMessage createMessage() {
        HTShareMessage message = new HTShareMessage();
        return message;
    }


    public void setType(HTShareMessage.Type messageType) {
        this.type = messageType;
    }

    public HTShareMessage.Type getType() {
        return this.type;
    }

    public void setBody(HTShareMessageBody messageBody) {
        this.body = messageBody;
    }


    public HTShareMessageBody getBody() {
        if (this.type == HTShareMessage.Type.TEXT) {
            return new HTShareMessageTextBody(this.body.getLocalBody());
        } else if (this.type == HTShareMessage.Type.IMAGE) {
            return new HTShareMessageImageBody(this.body.getLocalBody());
        } else if (this.type == HTShareMessage.Type.VOICE) {
            return new HTShareMessageVoiceBody(this.body.getLocalBody());
        } else if (this.type == HTShareMessage.Type.VIDEO) {
            return new HTShareMessageVideoBody(this.body.getLocalBody());
        } else if (this.type == HTShareMessage.Type.LOCATION) {
            return new HTShareMessageLocationBody(this.body.getLocalBody());
        } else {
            return (HTShareMessageBody) (this.type == HTShareMessage.Type.FILE ? new HTShareMessageFileBody(this.body.getLocalBody()) : this.body);
        }
    }

    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel parcel, int position) {
        parcel.writeLong(this.size);
        parcel.writeLong(this.time);
        parcel.writeInt(this.type.ordinal());
        parcel.writeString(this.body.getLocalBody());
    }


    public String toXmppMessageBody() {
        short typeOrder = 2001;
        if (this.type == HTShareMessage.Type.TEXT) {
            typeOrder = 2001;
        } else if (this.type == HTShareMessage.Type.IMAGE) {
            typeOrder = 2002;
        } else if (this.type == HTShareMessage.Type.VOICE) {
            typeOrder = 2003;
        } else if (this.type == HTShareMessage.Type.VIDEO) {
            typeOrder = 2004;
        } else if (this.type == HTShareMessage.Type.FILE) {
            typeOrder = 2005;
        } else if (this.type == HTShareMessage.Type.LOCATION) {
            typeOrder = 2006;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("body", this.body.getLocalBody());
        jsonObject.put("msgType", Integer.valueOf(typeOrder));
        return jsonObject.toJSONString();
    }

    public static enum Type {
        IMAGE,
        TEXT,
        VOICE,
        VIDEO,
        FILE,
        LOCATION;

        private Type() {
        }
    }
}