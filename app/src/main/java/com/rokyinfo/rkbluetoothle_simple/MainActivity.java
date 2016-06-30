package com.rokyinfo.rkbluetoothle_simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.rokyinfo.ble.toolbox.protocol.custom.YadeaApiService;
import com.rokyinfo.ble.toolbox.protocol.model.CallAndMsgParameter;
import com.rokyinfo.ble.toolbox.protocol.model.ConfigResult;
import com.rokyinfo.ble.toolbox.protocol.model.RemoteControlResult;
import com.rokyinfo.ble.toolbox.protocol.model.VehicleStatus;
import com.rokyinfo.ble.toolbox.protocol.model.YadeaCustParameter;
import com.rokyinfo.ble.toolbox.protocol.model.YadeaFault;
import com.rokyinfo.rkbluetoothle_simple.mock.RkCCUDevice;
import rx.Observable;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RkCCUDevice mRkCCUDevice = new RkCCUDevice();
//        mRkCCUDevice.setSn("B00G4LB6B3");
//        mRkCCUDevice.setMacAddress("C0:27:15:09:A7:E9");
//        mRkCCUDevice.setSn("B00G3PC1Q4");
//        mRkCCUDevice.setMacAddress("C0:27:15:09:B2:F8");

        mRkCCUDevice.setSn("B00GDV5DZ3");
        mRkCCUDevice.setMacAddress("C0:27:15:09:AE:92");
        App.setCurrentRkCCUDevice(this, mRkCCUDevice);
    }

    public void lock(View view) {

        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();
        Observable<RemoteControlResult> mObservable = yadeaApiService.lock(App.getCurrentRkCCUDevice(this).getMacAddress());

        mObservable.subscribe(mRemoteControlResult -> {
            Log.d(TAG, "isSuccess:" + mRemoteControlResult.isSuccess());
            if (mRemoteControlResult.isSuccess()) {
                Toast.makeText(this, "设防成功", Toast.LENGTH_SHORT).show();
            }
        }, throwable -> {
            Log.d(TAG, "" + throwable);
        });

    }

    public void unlock(View view) {

        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();
        Observable<RemoteControlResult> mObservable = yadeaApiService.unlock(App.getCurrentRkCCUDevice(this).getMacAddress());

        mObservable.subscribe(mRemoteControlResult -> {
            Log.d(TAG, "isSuccess:" + mRemoteControlResult.isSuccess());
            if (mRemoteControlResult.isSuccess()) {
                Toast.makeText(this, "撤防成功", Toast.LENGTH_SHORT).show();
            }
        }, throwable -> {
            Log.d(TAG, "" + throwable);
        });

    }

    public void powerOn(View view) {

        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();
        Observable<RemoteControlResult> mObservable = yadeaApiService.powerOn(App.getCurrentRkCCUDevice(this).getMacAddress());

        mObservable.subscribe(mRemoteControlResult -> {
            Log.d(TAG, "isSuccess:" + mRemoteControlResult.isSuccess());
            if (mRemoteControlResult.isSuccess()) {
                Toast.makeText(this, "上电成功", Toast.LENGTH_SHORT).show();
            }
        }, throwable -> {
            Log.d(TAG, "" + throwable);
        });

    }

    public void powerOff(View view) {
        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();


        Observable<RemoteControlResult> mObservable = yadeaApiService.powerOff(App.getCurrentRkCCUDevice(this).getMacAddress());

        mObservable.subscribe(mRemoteControlResult -> {
            Log.d(TAG, "isSuccess:" + mRemoteControlResult.isSuccess());
            if (mRemoteControlResult.isSuccess()) {
                Toast.makeText(this, "断电成功", Toast.LENGTH_SHORT).show();
            }
        }, throwable -> {
            Log.d(TAG, "" + throwable);
        });

    }

    public void find(View view) {
        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();


        Observable<RemoteControlResult> mObservable = yadeaApiService.find(App.getCurrentRkCCUDevice(this).getMacAddress());

        mObservable.subscribe(mRemoteControlResult -> {
            Log.d(TAG, "isSuccess:" + mRemoteControlResult.isSuccess());
            if (mRemoteControlResult.isSuccess()) {
                Toast.makeText(this, "寻车成功", Toast.LENGTH_SHORT).show();
            }
        }, throwable -> {
            Log.d(TAG, "" + throwable);
        });

    }

    public void getCarStatus(View view) {

        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();
        Observable<VehicleStatus> mObservable = yadeaApiService.getVehicleStatus(App.getCurrentRkCCUDevice(this).getMacAddress());

        mObservable.subscribe(vehicleStatus -> {
            Toast.makeText(this, vehicleStatus.toString(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "" + vehicleStatus.toString());
        }, throwable -> {
            Log.d(TAG, "" + throwable);
        });
    }

    public void getFault(View view) {

        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();
        Observable<YadeaFault> mObservable = yadeaApiService.getFault(App.getCurrentRkCCUDevice(this).getMacAddress());

        mObservable.subscribe(fault -> {
            Toast.makeText(this, fault.toString(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "" + fault.toString());
        }, throwable -> {
            Log.d(TAG, "" + throwable);
        });
    }

    public void setCustParams(View view) {
//        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();
//
//        YadeaCustParameter custParameter = new YadeaCustParameter();
//        //颜色hex值
//        custParameter.setThreeColorLampSupport("FF82AB");
//        custParameter.setDelayCloseLight(22);
//        //17:30
//        custParameter.setTimingStartTime(1730);
//        //20:30
//        custParameter.setTimingStartTime(2030);
//
//        Observable<ConfigResult> mObservable = yadeaApiService.setCustParameter(App.getCurrentRkCCUDevice(this).getMacAddress(), custParameter);
//
//        mObservable.subscribe(configResult -> {
//            Toast.makeText(this, String.valueOf(configResult.isSuccess()), Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "" + configResult.isSuccess());
//        }, throwable -> {
//            Log.d(TAG, "" + throwable);
//        });

        Intent intent = new Intent(this, CustParamsActivity.class);
        startActivity(intent);
    }

    public void getCustParams(View view) {
        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();

        Observable<YadeaCustParameter> mObservable = yadeaApiService.getCustParameter(App.getCurrentRkCCUDevice(this).getMacAddress());

        mObservable.subscribe(custParameter -> {
            Toast.makeText(this, custParameter.toString(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "" + custParameter.toString());
        }, throwable -> {
            Log.d(TAG, "" + throwable);
        });
    }

    public void setCallAndMsgParams(View view) {
        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();

        CallAndMsgParameter callAndMsgParameter = new CallAndMsgParameter();
        callAndMsgParameter.setCallPrompt(1);
        callAndMsgParameter.setMsgPrompt(0);

        Observable<ConfigResult> mObservable = yadeaApiService.setCallAndMsgParameter(App.getCurrentRkCCUDevice(this).getMacAddress(), callAndMsgParameter);

        mObservable.subscribe(configResult -> {
            Toast.makeText(this, configResult.toString(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "" + configResult.toString());
        }, throwable -> {
            Log.d(TAG, "" + throwable);
        });
    }

}
