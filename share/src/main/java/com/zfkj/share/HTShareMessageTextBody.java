package com.zfkj.share;

/**
 * 项目名称：ShareSDK
 * 类描述：HTShareMessageTextBody 描述: 文字消息体
 * 创建人：songlijie
 * 创建时间：2017/7/19 10:21
 * 邮箱:814326663@qq.com
 */
public class HTShareMessageTextBody extends HTShareMessageBody {

    public HTShareMessageTextBody(String body) {
        super(body);
    }

    public HTShareMessageTextBody() {
    }

    public void setContent(String content) {
        this.bodyJson.put("content", content);
    }
}
