package com.zfkj.share;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 项目名称：ShareSDK
 * 类描述：HTShareUtils 描述: 分享工具类
 * 创建人：songlijie
 * 创建时间：2017/7/19 10:21
 * 邮箱:814326663@qq.com
 */
public class HTShareUtils {
    private String TAG = HTShareUtils.class.getSimpleName();
    private static Toast toast;
    private Context context;
    private static HTShareUtils locationUtils = null;
    private ShareResultReciver resultReciver;
    private OnShareResultListener listener;
    private String appId = null;

    /**
     * 注册工具类相关监听
     *
     * @param context
     */
    private HTShareUtils(Context context, String appId) {
        if (context == null) {
            log("please init first!");
            throw new RuntimeException("please init first!");
        }
        if (TextUtils.isEmpty(appId)) {
            log("appId不能为空");
        }
        this.context = context;
        this.appId = appId;
        resultReciver = new ShareResultReciver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("SHARE_APP_RESULT_BY_APP");
        filter.addCategory("SHARE_APP_RESULT_BY_APP");
        context.registerReceiver(resultReciver, filter);
    }

    /**
     * 初始化工具类
     *
     * @param cxt
     */
    public static void init(Context cxt, String appId) {
        synchronized (HTShareUtils.class) {
            if (locationUtils == null) {
                locationUtils = new HTShareUtils(cxt, appId);
            }
        }
    }

    /**
     * 获取工具类单例
     *
     * @param
     * @return
     */
    public synchronized static HTShareUtils getInstance() {
        if (locationUtils == null) {
            throw new RuntimeException("please init first!");
        }
        return locationUtils;
    }

    /**
     * 启动App
     */
    public void launchapp(String packageName) {
        if (context == null) {
            return;
        }
        // 判断是否安装过App，否则去市场下载
        if (isAppInstalled(packageName)) {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
        } else {
            goToMarket(packageName);
        }
    }

    /**
     * 检测某个应用是否安装
     *
     * @param packageName
     * @return
     */
    public boolean isAppInstalled(String packageName) {
        if (context == null) {
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 去市场下载页面
     */
    public void goToMarket(String packageName) {
        if (context == null) {
            return;
        }
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享文字
     *
     * @param content 文字内容
     */
    public void shareTextMessage(String content) {
        if (context == null || TextUtils.isEmpty(content)) {
            return;
        }
        if (!checkAppAuth(appId)) {
            log("Key is null or not invalid");
            return;
        }
        Uri uri = Uri.parse("witalkapp.com://witalk/sharesinge");   //   app://test" 相当于 http://www.baidu.com
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        HTShareMessage sendMessage = HTShareMessage.createTextSendMessage(content);
        intent.putExtra("APP_SHARE_CONTENT", sendMessage.toXmppMessageBody());
        context.startActivity(intent);
    }

    /**
     * 分享网络地址
     *
     * @param content 文字内容
     */
    public void shareWebUrlMessage(String content) {
        if (context == null || TextUtils.isEmpty(content)) {
            return;
        }
        if (!checkAppAuth(appId)) {
            log("Key is null or not invalid");
            return;
        }
        Uri uri = Uri.parse("witalkapp.com://witalk/sharesinge");   //   app://test" 相当于 http://www.baidu.com
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        HTShareMessage sendMessage = HTShareMessage.createTextSendMessage(content);
        intent.putExtra("APP_SHARE_CONTENT", sendMessage.toXmppMessageBody());
        context.startActivity(intent);
    }

    /**
     * 分享图片
     *
     * @param localPath 本地文件地址  必传
     * @param size      图片大小  即图片宽高 以","隔开
     * @param isNet     是否是网络的  true 网络图片  false 本地图片
     */
    public void shareImageMessage(String localPath, String fileName, String size, boolean isNet) {
        if (context == null || TextUtils.isEmpty(localPath)) {
            return;
        }
        if (!checkAppAuth(appId)) {
            log("Key is null or not invalid");
            return;
        }
        Uri uri = Uri.parse("witalkapp.com://witalk/sharesinge");   //   app://test" 相当于 http://www.baidu.com
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        HTShareMessage sendMessage = null;
        if (isNet) {
            if (localPath.endsWith(".png") || localPath.endsWith(".PNG") ||
                    localPath.endsWith(".jpg") || localPath.endsWith(".JPG") ||
                    localPath.endsWith(".jpeg") || localPath.endsWith(".JPEG") ||
                    localPath.endsWith("bmp")) {
                sendMessage = new HTShareMessage();
                sendMessage.setType(HTShareMessage.Type.IMAGE);
                HTShareMessageImageBody body = new HTShareMessageImageBody();
                body.setRemotePath(localPath);
                body.setFileName(fileName);
                body.setSize(size);
                sendMessage.setBody(body);
            } else {
                sendMessage = HTShareMessage.createTextSendMessage(localPath);
            }
        } else {
            sendMessage = HTShareMessage.createImageSendMessage(localPath, size);
        }
        intent.putExtra("APP_SHARE_CONTENT", sendMessage.toXmppMessageBody());
        context.startActivity(intent);
    }

    /**
     * 分享文件
     *
     * @param localPath 本地文件地址 必传
     * @param size      文件大小 必传
     * @param isNet     是否是网络的  true 网络  false 本地
     */
    public void shareFileMessage(String localPath, String fileName, long size, boolean isNet) {
        if (context == null || TextUtils.isEmpty(localPath)) {
            return;
        }
        if (!checkAppAuth(appId)) {
            log("Key is null or not invalid");
            return;
        }
        Uri uri = Uri.parse("witalkapp.com://witalk/sharesinge");   //   app://test" 相当于 http://www.baidu.com
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        HTShareMessage sendMessage = null;
        if (isNet) {
            sendMessage = new HTShareMessage();
            sendMessage.setType(HTShareMessage.Type.FILE);
            HTShareMessageFileBody body = new HTShareMessageFileBody();
            body.setFileName(fileName);
            body.setRemotePath(localPath);
            body.setSize(size);
            sendMessage.setBody(body);
        } else {
            sendMessage = HTShareMessage.createFileSendMessage(localPath, size);
        }
        intent.putExtra("APP_SHARE_CONTENT", sendMessage.toXmppMessageBody());
        context.startActivity(intent);
    }

    /**
     * 分享语音
     *
     * @param localPath     本地地址 必传
     * @param audioDuration 语音长短 必传
     * @param isNet         是否是网络的     true  网络 false 本地
     */
    private void shareVoiceMessage(String localPath, String filename, int audioDuration, boolean isNet) {
        if (context == null||TextUtils.isEmpty(localPath)){
            return;
        }
          if (!checkAppAuth(appId)) {
              log("Key is null or not invalid");
              return;
          }
        Uri uri = Uri.parse("witalkapp.com://witalk/sharesinge");   //   app://test" 相当于 http://www.baidu.com
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        HTShareMessage sendMessage = null;
        if (isNet) {
            sendMessage = new HTShareMessage();
            sendMessage.setType(HTShareMessage.Type.VOICE);
            HTShareMessageVoiceBody body = new HTShareMessageVoiceBody();
            body.setRemotePath(localPath);
            body.setAudioDuration(audioDuration);
            body.setFileName(filename);
            sendMessage.setBody(body);
        } else {
            sendMessage = HTShareMessage.createVoiceSendMessage(localPath, audioDuration);
        }
        intent.putExtra("APP_SHARE_CONTENT", sendMessage.toXmppMessageBody());
        context.startActivity(intent);
    }

    /**
     * 分享视频  仅支持MP4格式
     *
     * @param isNet         是否是网络的     true  网络 false 本地
     * @param localPath     本地地址                必传
     * @param thumbPath     缩略图地址              必传  当为网络视频时
     * @param videoDuration 视频长短                必传
     */
    public void shareVideoMessage(String localPath, String thumbPath, String filename, int videoDuration, boolean isNet) {
        if (context == null || TextUtils.isEmpty(localPath) || TextUtils.isEmpty(thumbPath)) {
            return;
        }
        if (!checkAppAuth(appId)) {
            log("Key is null or not invalid");
            return;
        }
        Uri uri = Uri.parse("witalkapp.com://witalk/sharesinge");   //   app://test" 相当于 http://www.baidu.com
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        HTShareMessage sendMessage = null;
        if (isNet) {
            if (localPath.endsWith(".MP4") || localPath.endsWith(".mp4") || localPath.endsWith(".3gp") || localPath.endsWith(".avi")) {
                sendMessage = new HTShareMessage();
                sendMessage.setType(HTShareMessage.Type.VIDEO);
                HTShareMessageVideoBody body = new HTShareMessageVideoBody();
                body.setFileName(filename);
                body.setRemotePath(localPath);
                body.setThumbnailRemotePath(thumbPath);
                body.setVideoDuration(videoDuration);
                sendMessage.setBody(body);
            } else {
                sendMessage = HTShareMessage.createTextSendMessage(localPath);
            }
        } else {
            sendMessage = HTShareMessage.createVideoSendMessage(localPath, thumbPath, videoDuration);
        }
        intent.putExtra("APP_SHARE_CONTENT", sendMessage.toXmppMessageBody());
        context.startActivity(intent);
    }

    /**
     * 分享位置
     *
     * @param isNet          是否是网络的  true  网络 false 本地
     * @param address        分享的地址
     * @param thumbLocalPath 图片的本地地址
     * @param lat            经度
     * @param lng            维度
     */
    private void shareLocationMessage(String address, String thumbLocalPath, String filename, double lat, double lng, boolean isNet) {
        if (context == null || TextUtils.isEmpty(address) || TextUtils.isEmpty(thumbLocalPath) || lat <= 0 || lng <= 0) {
            return;
        }
        if (!checkAppAuth(appId)) {
            log("Key is null or not invalid");
            return;
        }
        Uri uri = Uri.parse("witalkapp.com://witalk/sharesinge");   //   app://test" 相当于 http://www.baidu.com
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        HTShareMessage sendMessage = null;
        if (isNet) {
            sendMessage = new HTShareMessage();
            sendMessage.setType(HTShareMessage.Type.LOCATION);
            HTShareMessageLocationBody body = new HTShareMessageLocationBody();
            body.setAddress(address);
            body.setFileName(filename);
            body.setRemotePath(thumbLocalPath);
            body.setLatitude(lat);
            body.setLongitude(lng);
            sendMessage.setBody(body);
        } else {
            sendMessage = HTShareMessage.createLocationSendMessage(lat, lng, address, thumbLocalPath);
        }
        intent.putExtra("APP_SHARE_CONTENT", sendMessage.toXmppMessageBody());
        context.startActivity(intent);
    }

    /**
     * 去下载页面
     */
    public void goToDownLoad(String url) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 获取当前应用程序的包名
     *
     * @return 返回包名
     */
    public String getAppProcessName() {
        if (context == null) {
            return null;
        }
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return null;
    }

    /**
     * 获取程序 图标
     *
     * @param packname 应用包名
     * @return
     */
    public Drawable getAppIcon(String packname) {
        if (context == null) {
            return null;
        }
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            //获取到应用信息
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取程序的版本号
     *
     * @param packname
     * @return
     */
    public String getAppVersion(String packname) {
        if (context == null) {
            return null;
        }
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            return packinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packname;
    }


    /**
     * 获取程序的名字
     *
     * @param packname
     * @return
     */
    public String getAppName(String packname) {
        if (context == null) {
            return null;
        }
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packname;
    }

    /**
     * 获取程序的权限
     */
    public String[] getAllPermissions(String packname) {
        if (context == null) {
            return null;
        }
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
            //获取到所有的权限
            return packinfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取程序的签名
     *
     * @param packname
     * @return
     */
    public String getAppSignature(String packname) {
        if (context == null) {
            return null;
        }
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
            //获取当前应用签名
            return packinfo.signatures[0].toCharsString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packname;
    }

    /**
     * 获取当前展示 的Activity名称
     *
     * @return
     */
    public String getCurrentActivityName() {
        if (context == null) {
            return null;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }

    /**
     * 短吐司
     *
     * @param msg
     */
    public void showToastShort(String msg) {
        if (context == null) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 短吐司
     *
     * @param msg
     */
    public void showToastShort(int msg) {
        if (context == null) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 短吐司
     *
     * @param msg
     */
    public void showToastShort(Object msg) {
        if (context == null) {
            return;
        }
        if (msg instanceof String) {
            showToastShort((String) msg);
        } else if (msg instanceof Integer) {
            showToastShort(context.getString(((int) msg)));
        }
    }


    /**
     * 长吐司
     *
     * @param msg
     */
    public void showToastLong(String msg) {
        if (context == null) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 短吐司
     *
     * @param msg
     */
    public void showToastLong(Object msg) {
        if (context == null) {
            return;
        }
        if (msg instanceof String) {
            showToastLong((String) msg);
        } else if (msg instanceof Integer) {
            showToastLong(context.getString(((int) msg)));
        }
    }

    /**
     * 长吐司
     *
     * @param msg
     */
    public void showToastLong(int msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 回调监听的广播
     */
    private class ShareResultReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Set<String> strings = intent.getCategories();
            int code = intent.getIntExtra("RESULT_CODE", -1);
            if (action.equals("SHARE_APP_RESULT_BY_APP")) {
                if (strings != null) {
                    if (strings.contains("SHARE_APP_RESULT_BY_APP")) {
                        if (code == 1) {
                            if (listener != null) {
                                listener.onSuccess();
                            }
                        } else if (code == 0) {
                            if (listener != null) {
                                listener.onCancle();
                            }
                        } else {
                            if (listener != null) {
                                String error = intent.getStringExtra("ERROR");
                                if (!TextUtils.isEmpty(error)) {
                                    listener.onError(0,error);
                                } else {
                                    listener.onError(0,null);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 分享结果的回调
     */
    public interface OnShareResultListener {
        void onSuccess();

        /**
         *   1 为初始化错误  0 为分享错误
         * @param type  1 为初始化错误  0 为分享错误
         * @param msg  错误信息
         */
        void onError(int type,String msg);

        void onCancle();
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnShareResultListener(OnShareResultListener listener) {
        this.listener = listener;
    }

    /**
     * 再退出APP是 需要销毁这个广播监听及回调接口
     */
    public void destory() {
        if (resultReciver != null) {
            context.unregisterReceiver(resultReciver);
        }
        listener = null;
    }

    /**
     * 权限查询
     * @param appId
     * @return
     */
    private boolean checkAppAuth(String appId) {
        return TextUtils.isEmpty(appId) ? false : true;
    }

    /**
     * log输出到外面的回调
     * @param msg
     */
    private  void log(String msg){
      if (listener !=null){
          listener.onError(1,msg);
      }
    }
}
