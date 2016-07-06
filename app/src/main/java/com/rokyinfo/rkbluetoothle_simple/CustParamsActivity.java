package com.rokyinfo.rkbluetoothle_simple;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.rokyinfo.ble.toolbox.protocol.custom.YadeaApiService;
import com.rokyinfo.ble.toolbox.protocol.model.ConfigResult;
import com.rokyinfo.ble.toolbox.protocol.model.YadeaCustParameter;
import com.rokyinfo.convert.util.ByteConvert;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by caoyy on 16/6/28.
 */
public class CustParamsActivity extends AppCompatActivity {

    private View colorSelectView;
    private ColorPicker colorPicker;
    private String hexNewColor;

    TextView threeColor_value, delay_lamp_value, timing_start_value, timing_end_value, gear_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_params);

        threeColor_value = (TextView) findViewById(R.id.threeColor_value);
        delay_lamp_value = (TextView) findViewById(R.id.delay_lamp_value);
        timing_start_value = (TextView) findViewById(R.id.timing_start_value);
        timing_end_value = (TextView) findViewById(R.id.timing_end_value);
        gear_value = (TextView) findViewById(R.id.gear_value);

        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();

        Observable<YadeaCustParameter> mObservable = yadeaApiService.getCustParameter(App.getCurrentRkCCUDevice(this).getMacAddress());

        mObservable.subscribe(custParameter -> {
            threeColor_value.setText(String.valueOf(custParameter.getThreeColorLampSupport()));
            delay_lamp_value.setText(String.valueOf(custParameter.getDelayCloseLight()));
            timing_start_value.setText(String.valueOf(custParameter.getTimingStartTime()));
            timing_end_value.setText(String.valueOf(custParameter.getTimingEndTime()));
            gear_value.setText(String.valueOf(custParameter.getGearsType()));
            Log.d("cyy", "" + custParameter.toString());
        }, throwable -> {
            Log.d("cyy", "" + throwable);
        });

        colorSelectView = getLayoutInflater().inflate(R.layout.color_select, null);
        colorPicker = (ColorPicker) colorSelectView.findViewById(R.id.colorPicker);


        getColorPickerObservable().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("cyy", "color:"+integer);
                hexNewColor = Integer.toHexString(integer);
                hexNewColor = hexNewColor.substring(2);
                threeColor_value.setText(String.valueOf(hexNewColor));
                setCustParams();
            }
        });
    }

    private Observable<Integer> getColorPickerObservable(){
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener(){
                    @Override
                    public void onColorChanged(int color) {
                        subscriber.onNext(color);
                    }
                });
            }
        }).throttleFirst(500, TimeUnit.MILLISECONDS);
    }

    public void threeColorSetting(View view){
//        new MaterialDialog.Builder(this)
//                .title("三色灯支持")
////                        .content(R.string.nick_name_label)
//                .inputType(InputType.TYPE_CLASS_TEXT)
//                .negativeText("取消")
//                .input("", threeColor_value.getText(), new MaterialDialog.InputCallback() {
//                    @Override
//                    public void onInput(MaterialDialog dialog, CharSequence input) {
//                        if (!TextUtils.isEmpty(input)) {
//                            threeColor_value.setText(input);
//                            setCustParams();
//                        } else {
//                            Toast.makeText(CustParamsActivity.this, "输入值不能为空", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }).show();

        new MaterialDialog.Builder(this)
                .title("三色灯支持")
                .customView(colorSelectView, false)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        threeColor_value.setText(String.valueOf(hexNewColor));
//                        colorPicker.setOldCenterColor((int)Long.parseLong(hexNewColor, 16));
//                        setCustParams();
//                    }
//                })
                .show();
    }

    public void delayLampSetting(View view){
        new MaterialDialog.Builder(this)
                .title("延时关闭大灯")
//                        .content(R.string.nick_name_label)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .negativeText("取消")
                .input("", delay_lamp_value.getText(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (!TextUtils.isEmpty(input)) {
                            delay_lamp_value.setText(input);
                            setCustParams();
                        } else {
                            Toast.makeText(CustParamsActivity.this, "输入值不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }

    public void timeStartSetting(View view){
        new MaterialDialog.Builder(this)
                .title("定时开启时间(起始)")
//                        .content(R.string.nick_name_label)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .negativeText("取消")
                .input("", timing_start_value.getText(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (!TextUtils.isEmpty(input)) {
                            timing_start_value.setText(input);
                            setCustParams();
                        } else {
                            Toast.makeText(CustParamsActivity.this, "输入值不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }

    public void timingEndSetting(View view){
        new MaterialDialog.Builder(this)
                .title("定时开启时间(结束)")
//                        .content(R.string.nick_name_label)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .negativeText("取消")
                .input("", timing_end_value.getText(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (!TextUtils.isEmpty(input)) {
                            timing_end_value.setText(input);
                            setCustParams();
                        } else {
                            Toast.makeText(CustParamsActivity.this, "输入值不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }

    public void setGearType(View view){
        new MaterialDialog.Builder(this)
                .title("档位信息")
//                        .content(R.string.nick_name_label)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .negativeText("取消")
                .input("", gear_value.getText(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (!TextUtils.isEmpty(input)) {
                            gear_value.setText(input);
                            setCustParams();
                        } else {
                            Toast.makeText(CustParamsActivity.this, "输入值不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }


    public void setCustParams(){
        String threeColor = "FFFFFF";
        if(!TextUtils.isEmpty(threeColor_value.getText())){
            threeColor = threeColor_value.getText().toString();
        }
        int delayLamp = 0;
        if(!TextUtils.isEmpty(delay_lamp_value.getText())){
            delayLamp = Integer.parseInt(delay_lamp_value.getText().toString());
        }
        String timingStart = "17:30";
        if(!TextUtils.isEmpty(timing_start_value.getText())){
            timingStart = timing_start_value.getText().toString();
        }
        String timingEnd = "20:30";
        if(!TextUtils.isEmpty(timing_end_value.getText())){
            timingEnd = timing_end_value.getText().toString();
        }
        int gearType = 0;
        if(!TextUtils.isEmpty(gear_value.getText())){
            gearType = Integer.parseInt(gear_value.getText().toString());
        }

        YadeaCustParameter yadeaCustParameter = new YadeaCustParameter();
        yadeaCustParameter.setThreeColorLampSupport(threeColor);
        yadeaCustParameter.setDelayCloseLight(delayLamp);
        yadeaCustParameter.setTimingStartTime(timingStart);
        yadeaCustParameter.setTimingEndTime(timingEnd);
        yadeaCustParameter.setGearsType(gearType);

        YadeaApiService yadeaApiService = App.getRkBluetoothClient(this).getYadeaApiService();
        Observable<ConfigResult> mObservable = yadeaApiService.setCustParameter(App.getCurrentRkCCUDevice(this).getMacAddress(), yadeaCustParameter);

        mObservable.subscribe(configResult -> {
            Toast.makeText(this, String.valueOf(configResult.isSuccess()), Toast.LENGTH_SHORT).show();
            Log.d("cyy", "" + configResult.isSuccess());
        }, throwable -> {
            Log.d("cyy", "" + throwable);
        });
    }

}
