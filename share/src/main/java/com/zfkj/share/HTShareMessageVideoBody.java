package com.zfkj.share;
/**
 * 项目名称：ShareSDK
 * 类描述：HTShareMessageVideoBody 描述: 视频消息体
 * 创建人：songlijie
 * 创建时间：2017/7/19 10:21
 * 邮箱:814326663@qq.com
 */
public class HTShareMessageVideoBody extends HTShareMessageBody {

    public HTShareMessageVideoBody() {
    }

    public void setLocalPath(String localPath) {
        this.bodyJson.put("localPath", localPath);
    }

    public void setFileName(String fileName) {
        this.bodyJson.put("fileName", fileName);
    }

    public void setRemotePath(String remotePath) {
        this.bodyJson.put("remotePath", remotePath);
    }

    public void setThumbnailRemotePath(String thumbnailRemotePath) {
        this.bodyJson.put("thumbnailRemotePath", thumbnailRemotePath);
    }

    public void setLocalPathThumbnail(String localPathThumbnail) {
        this.bodyJson.put("localPathThumbnail", localPathThumbnail);
    }

    public void setVideoDuration(int videoDuration) {
        this.bodyJson.put("videoDuration", videoDuration);
    }

    public HTShareMessageVideoBody(String body) {
        super(body);
    }
}
