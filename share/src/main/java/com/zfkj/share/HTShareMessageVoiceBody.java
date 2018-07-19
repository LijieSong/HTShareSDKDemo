package com.zfkj.share;

/**
 * 项目名称：ShareSDK
 * 类描述：HTShareMessageVoiceBody 描述: 语音消息体
 * 创建人：songlijie
 * 创建时间：2017/7/19 10:21
 * 邮箱:814326663@qq.com
 */
public class HTShareMessageVoiceBody extends HTShareMessageBody {

    public HTShareMessageVoiceBody() {
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

    public void setAudioDuration(int audioDuration) {
        this.bodyJson.put("audioDuration", audioDuration);
    }

    public HTShareMessageVoiceBody(String body) {
        super(body);
    }
}
