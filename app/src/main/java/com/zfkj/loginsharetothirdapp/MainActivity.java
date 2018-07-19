package com.zfkj.loginsharetothirdapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.zfkj.share.HTShareUtils;


public class MainActivity extends AppCompatActivity implements HTShareUtils.OnShareResultListener {
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HTShareUtils.init(this,"123456");
        HTShareUtils.getInstance().setOnShareResultListener(this);
        findViewById(R.id.tv_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp("com.htmessage.witalk");
            }
        });
        findViewById(R.id.tv_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) || !checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    return;
                }
                getImage();
            }
        });
    }

    /**
     * 仿App登录 此处做3秒延迟以实际需求为主. 部分机型需打开志工行app才可跳转
     */
    public void openApp(final String packageName) { //延迟三秒授权
        if (!HTShareUtils.getInstance().isAppInstalled(packageName)) {
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在申请授权,请稍后...");
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                if (!TextUtils.isEmpty(packageName)) {
//                    HTShareUtils.getInstance().shareTextMessage("http://pic.5577.com/up/2014-11/2014112016587774870.jpg");
//                    HTShareUtils.getInstance().shareImageMessage("http://pic.5577.com/up/2014-11/2014112016587774870.jpg", "2014112016587774870.jpg", "960,960", true);
                    HTShareUtils.getInstance().shareVideoMessage("https://pic.ibaotu.com/00/63/01/12h888piCneY.mp4",
                            "https://img.douxie.com/upload/upload/2015/08/03/55bec2eeaf498.jpg", "12h888piCneY.mp4",
                            14175896, true);
//                    HTShareUtils.getInstance().shareFileMessage("https://pic.ibaotu.com/00/63/01/12h888piCneY.mp4", "zbgfBP9WKhkr1527821185_10s.mp4", 1024, true);
//                    HTShareUtils.getInstance().shareLocationMessage("中国安徽省合肥市包河区马鞍山南路720号","http://pic.5577.com/up/2014-11/2014112016587774870.jpg",
//                            "2014112016587774870.jpg",31.8307500000,117.3026200000,true);
                }
            }
        }, 300);
    }

    @Override
    public void onSuccess() {
        Log.e(TAG, "成功");
        Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int type,String msg) {
        if (type ==1){
            Log.e(TAG, "初始化错误:"+msg);
        }else{
            Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCancle() {
        Log.e(TAG, "取消");
        Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        HTShareUtils.getInstance().destory();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 100:
                    if (data != null) {
                        String realPathFromUri = FilePathUtils.getPath(MainActivity.this, data.getData());
                        if (!TextUtils.isEmpty(realPathFromUri)) {
                            String name = realPathFromUri.substring(realPathFromUri.lastIndexOf("/") + 1);
                            HTShareUtils.getInstance().shareImageMessage(realPathFromUri, name, "960,960", false);
//                            HTShareUtils.getInstance().shareFileMessage(realPathFromUri, name, 1024, false);
                        } else {
                            Toast.makeText(this, "图片损坏，请重新选择", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "图片损坏，请重新选择", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private void getImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    100);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermission(String permissionName) {
        PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(permissionName, getPackageName()));
        if (permission) {
            return true;
        } else {
            requestPermissions(new String[]{permissionName},
                    REQUEST_CODE);
            return false;

        }
    }

    private static final int REQUEST_CODE = 100;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.CAMERA) || permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (PackageManager.PERMISSION_GRANTED == grantResults[i]) {
                    } else {
                    }
                }

            }
        }
    }
}
