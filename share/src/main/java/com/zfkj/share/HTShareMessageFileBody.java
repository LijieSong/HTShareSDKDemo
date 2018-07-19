package com.zfkj.share;

/**
 * 项目名称：ShareSDK
 * 类描述：HTShareMessageFileBody 描述: 文件消息体
 * 创建人：songlijie
 * 创建时间：2017/7/19 10:21
 * 邮箱:814326663@qq.com
 */
public class HTShareMessageFileBody extends HTShareMessageBody {

    public HTShareMessageFileBody() {
    }

    public void setLocalPath(String localPath) {
        this.bodyJson.put("localPath", localPath);
    }

    public void setSize(long size) {
        this.bodyJson.put("fileSize", String.valueOf(size));
    }

    public void setFileName(String fileName) {
        this.bodyJson.put("fileName", fileName);
    }

    public void setRemotePath(String remotePath) {
        this.bodyJson.put("remotePath", remotePath);
    }

    public HTShareMessageFileBody(String body) {
        super(body);
    }

    public String getLocalBody() {
        return this.bodyJson.toJSONString();
    }
}
