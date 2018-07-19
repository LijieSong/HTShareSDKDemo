package com.zfkj.share;

/**
 * 项目名称：ShareSDK
 * 类描述：HTShareMessageLocationBody 描述: 位置消息体
 * 创建人：songlijie
 * 创建时间：2017/7/19 10:21
 * 邮箱:814326663@qq.com
 */
public class HTShareMessageLocationBody extends HTShareMessageBody {

    public HTShareMessageLocationBody() {
    }

    public HTShareMessageLocationBody(String body) {
        super(body);
    }

    public void setLatitude(double latitude) {
        this.bodyJson.put("latitude", latitude);
    }

    public void setLongitude(double longitude) {
        this.bodyJson.put("longitude", longitude);
    }

    public void setAddress(String address) {
        this.bodyJson.put("address", address);
    }

    public void setFileName(String fileName) {
        this.bodyJson.put("fileName", fileName);
    }

    public void setRemotePath(String remotePath) {
        this.bodyJson.put("remotePath", remotePath);
    }

    public void setLocalPath(String localPath) {
        this.bodyJson.put("localPath", localPath);
    }
}
