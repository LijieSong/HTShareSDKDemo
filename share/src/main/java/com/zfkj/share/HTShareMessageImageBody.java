package com.zfkj.share;

/**
 * 项目名称：ShareSDK
 * 类描述：HTShareMessageImageBody 描述: 图片消息体
 * 创建人：songlijie
 * 创建时间：2017/7/19 10:21
 * 邮箱:814326663@qq.com
 */
public class HTShareMessageImageBody extends HTShareMessageBody {

    public HTShareMessageImageBody() {
    }

    public void setLocalPath(String localPath) {
        this.bodyJson.put("localPath", localPath);
    }

    public void setSize(String size) {
        this.bodyJson.put("size", size);
    }

    public void setFileName(String fileName) {
        this.bodyJson.put("fileName", fileName);
    }

    public void setRemotePath(String remotePath) {
        this.bodyJson.put("remotePath", remotePath);
    }

    public HTShareMessageImageBody(String body) {
        super(body);
    }

    public String getLocalBody() {
        return this.bodyJson.toJSONString();
    }
}