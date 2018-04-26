package com.example.test;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    // 要申请的权限...可以自行添加需要检测的权限
    private String[] permissions = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("jade", "Build.VERSION.SDK_INT--->" + Build.VERSION.SDK_INT);
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i++) {
                // 检查该权限是否已经获取
                int sign = ContextCompat.checkSelfPermission(this, permissions[i]);
                Log.e("jade", "sign--->" + sign);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (sign != PackageManager.PERMISSION_GRANTED) {
                    String permission = permissions[i];
                    Log.e("jade", "permission--->" + permission);
                    // 如果没有授予该权限，就去提示用户请求
                    //                    switch (permission) {
                    //                        case "android.permission.READ_PHONE_STATE":
                    //                            showDialogTipUserRequestPermission("");
                    //                            break;
                    //                        case "android.permission.ACCESS_FINE_LOCATION":
                    //                            showDialogTipUserRequestPermission(permission);
                    //                            break;
                    //                    }
                    startRequestPermission();
                }
            }
        }
    }

    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 1);
    }

    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission(String permission) {

        new AlertDialog.Builder(this)
                .setTitle(permission)
                .setMessage(permission)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }


    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        // 判断用户是否 勾选了禁止后不再询问。(检测该权限是否还可以申请)
                        boolean b = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                        Log.e("jade", "b--->" + b);
                        if (!b) {
                            // 用户还是想用我的 APP 的
                            // 提示用户去应用设置界面手动开启权限
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("需要开启权限才能使用app")
                                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //引导用户到设置中去进行设置
                                            Intent intent = new Intent();
                                            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .create()
                                    .show();
                        } else {
                            finish();
                        }
                    } else {
                        //Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
