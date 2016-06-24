package com.rokyinfo.rkbluetoothle_simple.mock;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本地存储虚拟类
 * Created by YuanZhiJian on 16/5/24.
 */
public class DAOServiceApiMock {


    public static void saveAuthStatus(Context context, String connectedDeviceAddress,boolean status) {

        //此处建议使用数据库进行处理,这里为mock代码使用xml 文件存储方式保存
        SharedPreferences mySharedPreferences= context.getSharedPreferences("DAOServiceApiMock",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(connectedDeviceAddress, status);
        editor.commit();

    }

    public static boolean authStatus(Context context, String connectedDeviceAddress){
        SharedPreferences mySharedPreferences= context.getSharedPreferences("DAOServiceApiMock",
                Activity.MODE_PRIVATE);
        return mySharedPreferences.getBoolean(connectedDeviceAddress,false);

    }
}
