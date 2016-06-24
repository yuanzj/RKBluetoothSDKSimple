package com.rokyinfo.rkbluetoothle_simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.rokyinfo.ble.toolbox.protocol.custom.YadeaApiService;
import com.rokyinfo.ble.toolbox.protocol.model.CallAndMsgParameter;
import com.rokyinfo.ble.toolbox.protocol.model.ConfigResult;
import com.rokyinfo.ble.toolbox.protocol.model.CustParameter;
import com.rokyinfo.ble.toolbox.protocol.model.RK4102ECUParameter;
import com.rokyinfo.ble.toolbox.protocol.model.RemoteControlResult;
import com.rokyinfo.ble.toolbox.protocol.model.VehicleStatus;
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
        mRkCCUDevice.setSn("B00G10B6F3");
        mRkCCUDevice.setMacAddress("C0:27:15:09:A7:E6");
        App.setCurrentRkCCUDevice(this, mRkCCUDevice);
    }

    public void powerOn(View view) {

        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();
        Observable<RemoteControlResult> mObservable = yadeaApiService.powerOn(App.getCurrentRkCCUDevice(this).getMacAddress());

        mObservable.subscribe(mRemoteControlResult -> {
            Log.d(TAG, "isSuccess:" + mRemoteControlResult.isSuccess());
            if (mRemoteControlResult.isSuccess()) {
                Toast.makeText(this, "设防成功", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "撤防成功", Toast.LENGTH_SHORT).show();
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

    public void setCenterControlParams(View view) {
        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();

        RK4102ECUParameter ecuParameter = new RK4102ECUParameter();
        ecuParameter.setVibrationLevel(1);
        ecuParameter.setLcdScreenCode(2);
        ecuParameter.setTraRemoteControlSwitch(1);
        ecuParameter.setBatteryType(1);
        ecuParameter.setGpsType(0);
        ecuParameter.setPagingCycle(600);

        Observable<ConfigResult> mObservable = yadeaApiService.setECUParameter(App.getCurrentRkCCUDevice(this).getMacAddress(), ecuParameter);

        mObservable.subscribe(configResult -> {
            Toast.makeText(this, String.valueOf(configResult.isSuccess()), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "" + configResult.isSuccess());
        }, throwable -> {
            Log.d(TAG, "" + throwable);
        });
    }

    public void getCenterControlParams(View view) {
        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();

        Observable<RK4102ECUParameter> mObservable = yadeaApiService.getECUParameter(App.getCurrentRkCCUDevice(this).getMacAddress());

        mObservable.subscribe(ecuParameter -> {
            Toast.makeText(this, ecuParameter.toString(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "" + ecuParameter.toString());
        }, throwable -> {
            Log.d(TAG, "" + throwable);
        });
    }

    public void setCustParams(View view) {
        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();

        CustParameter custParameter = new CustParameter();
        custParameter.setVibrationType(2);
        custParameter.setAutoLockTime(3);
        custParameter.setAutoParkTime(5);
        custParameter.setHornVolume(1);
        custParameter.setThreeColorLampSupport(0xFF82AB);
        custParameter.setDelayCloseLight(22);
        custParameter.setTimingOpenTime(128);

        Observable<ConfigResult> mObservable = yadeaApiService.setCustParameter(App.getCurrentRkCCUDevice(this).getMacAddress(), custParameter);

        mObservable.subscribe(configResult -> {
            Toast.makeText(this, String.valueOf(configResult.isSuccess()), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "" + configResult.isSuccess());
        }, throwable -> {
            Log.d(TAG, "" + throwable);
        });
    }

    public void getCustParams(View view) {
        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();

        Observable<CustParameter> mObservable = yadeaApiService.getCustParameter(App.getCurrentRkCCUDevice(this).getMacAddress());

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
