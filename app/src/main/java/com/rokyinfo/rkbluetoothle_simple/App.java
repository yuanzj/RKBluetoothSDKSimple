package com.rokyinfo.rkbluetoothle_simple;

import android.app.Application;
import android.content.Context;
import android.util.Base64;

import com.rokyinfo.ble.AuthCodeCreator;
import com.rokyinfo.ble.BleError;
import com.rokyinfo.ble.BleLog;
import com.rokyinfo.ble.toolbox.AuthCodeDeliverer;
import com.rokyinfo.ble.toolbox.RkBluetoothClient;
import com.rokyinfo.ble.toolbox.protocol.Rk410BleUtil;
import com.rokyinfo.ble.toolbox.protocol.model.AuthResult;
import com.rokyinfo.ble.toolbox.protocol.model.ConfigResult;
import com.rokyinfo.ble.toolbox.protocol.model.RemoteController;
import com.rokyinfo.ble.toolbox.protocol.model.SyncRemoteControllerResult;
import com.rokyinfo.rkbluetoothle_simple.mock.DAOServiceApiMock;
import com.rokyinfo.rkbluetoothle_simple.mock.HTTPServiceApiMock;
import com.rokyinfo.rkbluetoothle_simple.mock.RkCCUDevice;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by apple on 16/5/24.
 */
public class App extends Application {

    private RkBluetoothClient rkBluetoothClient;

    private RkCCUDevice currentRkCCUDevice;

    /**
     * In practise you will use some kind of dependency injection pattern.
     */
    public static RkBluetoothClient getRkBluetoothClient(Context context) {
        App application = (App) context.getApplicationContext();
        return application.rkBluetoothClient;
    }

    public static RkCCUDevice getCurrentRkCCUDevice(Context context) {
        App application = (App) context.getApplicationContext();
        return application.currentRkCCUDevice;
    }

    public static void setCurrentRkCCUDevice(Context context, RkCCUDevice currentRkCCUDevice) {
        App application = (App) context.getApplicationContext();
        application.currentRkCCUDevice = currentRkCCUDevice;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //开启SDK log
        BleLog.setDEBUG(true);
        rkBluetoothClient = RkBluetoothClient.create(this);
        try {
            rkBluetoothClient.setQueueSize(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rkBluetoothClient.getYadeaApiService().setAuthCodeCreator(new AuthCodeCreator() {
            @Override
            public void getAuthCode(AuthCodeDeliverer callBack) {

                //查询本地存储当前手机如果已经连接过设备并鉴权成功则直接发送默认鉴权码鉴权
                if (DAOServiceApiMock.authStatus(getApplicationContext(), currentRkCCUDevice.getMacAddress())) {

                    callBack.postAuthCode(Rk410BleUtil.createCommonAuthCodeStr(), 0, null);

                } else {

                    //1.联网掉接口获取鉴权码
                    HTTPServiceApiMock.getAuthCode(currentRkCCUDevice.getSn(), new HTTPServiceApiMock.AuthCodeCallback() {
                        @Override
                        public void success(String authCode) {
                            //2.提交鉴权码
                            callBack.postAuthCode(authCode, 0, null);
                        }

                        @Override
                        public void error(Exception e) {
                            callBack.postAuthCode(null, 0, new BleError(e));
                        }
                    });

                }

            }
        });
        rkBluetoothClient.observeAuthResult().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<AuthResult>() {
            @Override
            public void call(AuthResult authResult) {

                if (authResult.isSuccess()) {

                    //处理存储鉴权通过的状态
                    if (!Rk410BleUtil.isCommonAuthCode(authResult.getAuthCode())) {
                        syncCurrentSmartPhoneAddress(authResult);
                    }

                } else {

                    DAOServiceApiMock.saveAuthStatus(getApplicationContext(), authResult.getConnectedDeviceAddress(), false);

                }

            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        rkBluetoothClient.disconnect();
    }

    private void syncCurrentSmartPhoneAddress(AuthResult authResult) {
        RemoteController mRemoteController = new RemoteController();
        mRemoteController.setIndex(0);
        mRemoteController.setMacAddress(authResult.getMacAddress());
        Observable<ConfigResult> syncCurRemoteController = rkBluetoothClient.getYadeaApiService().syncRemoteController(authResult.getConnectedDeviceAddress(), mRemoteController);
        syncCurRemoteController.observeOn(AndroidSchedulers.mainThread()).subscribe(configResult -> {

            if (configResult.isSuccess()) {
                DAOServiceApiMock.saveAuthStatus(getApplicationContext(), authResult.getConnectedDeviceAddress(), true);
            }

        }, throwable1 -> {

        });
    }

}
