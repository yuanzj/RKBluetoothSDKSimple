## Andrid
### 概述

####  锐祺蓝牙Android SDK V1.1.10     
锐祺蓝牙Android SDK是一套基于Android4.3及以上版本设备的应用程序接口，不仅提供基本的上电，断电，设防，撤防，寻车，开启坐桶遥控操作指令，还提供当前车况信息查询服务如总理程，剩余电量，车辆状态等。

在蓝牙连接成功的状态下会自动根据一定的策略进行数据上报充分保证服务器采集到的车辆信息为最新状态。


### 开发指南

#### 简介

##### 什么是锐祺蓝牙Android SDK?
- 遥控器（上电，断电，设防，撤防，寻车，开启坐桶）
- 实时车况查询（状态，总里程，剩余电量，......）
- 故障查询
- 序列号，固件版本查询
- 参数配置（三色灯，来电及短信提醒）
- 车况信息上传


#### 配置开发环境
android studio开发工具下使用gradle依赖。

在app的build.gradle脚本文件中添加以下依赖


```
dependencies {
	compile 'com.rokyinfo:rkbluetoothle:1.1.10'
}
```




#### 蓝牙操作实例初始化配置


获得客户端操作实例
```
RkBluetoothClient rkBluetoothClient = RkBluetoothClient.create(this);
```
配置获取鉴权码的回调

```
rkBluetoothClient.getRk4103ApiService().setAuthCodeCreator(new AuthCodeCreator() {
            @Override
            public void getAuthCode(AuthCodeDeliverer callBack) {
            	//todo 获取鉴权码authCode
                callBack.postAuthCode(authCode, 0, null);
            }
 });
```

建议配置实践代码


```
public class App extends Application {

    private RkBluetoothClient rkBluetoothClient;

    public static RkBluetoothClient getRkBluetoothClient(Context context) {
        App application = (App) context.getApplicationContext();
        return application.rkBluetoothClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        rkBluetoothClient = RkBluetoothClient.create(this);
        rkBluetoothClient.getRk4103ApiService().setAuthCodeCreator(new AuthCodeCreator() {
            @Override
            public void getAuthCode(AuthCodeDeliverer callBack) {
                //获取鉴权码
                obtainAuthCode(callBack);
            }
        });
    }

}
```


#### 遥控器

##### 上电

调用代码如下：


```
App.getRkBluetoothClient(context).getRk4103ApiService().powerOn(macAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RemoteControlResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(RemoteControlResult remoteControlResult) {
                    }
                });
```


##### 断电

调用代码如下


```
App.getRkBluetoothClient(context).getRk4103ApiService().powerOff(macAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RemoteControlResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(RemoteControlResult remoteControlResult) {
                    }
                });
```



##### 寻车

调用代码如下


```
App.getRkBluetoothClient(this).getRk4103ApiService().find(macAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RemoteControlResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(RemoteControlResult remoteControlResult) {
                    }
                });
```


##### 车况查询

调用代码如下


```
App.getRkBluetoothClient(this).getRk4103ApiService().getVehicleStatus(macAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VehicleStatus>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(VehicleStatus vehicleStatus) {

                    }
                });
```


##### 故障查询

调用代码如下


```
App.getRkBluetoothClient(this).getRk4103ApiService().getFault(macAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RK4103Fault>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RK4103Fault rk4103Fault) {

                    }
                });
```


##### 固件版本查询

调用代码如下

```
//0 :CCU 1:ECU  2:PCU
App.getRkBluetoothClient(this).getRk4103ApiService().getFirmwareVersion(macAddress, 0)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<FirmwareVersion>() {
            @Override
            public void onCompleted() {
                
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(FirmwareVersion firmwareVersion) {

            }
        });
```
##### 序列号查询

调用代码如下

```
//1:ECU  2:PCU
App.getRkBluetoothClient(this).getRk4103ApiService().getUeUDID(macAddress, 1)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<UeUDID>() {
            @Override
            public void onCompleted() {
                
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(UeUDID ueUDID) {

            }
        });
```


##### 参数配置

调用代码如下
 
```
YadeaCustParameter yadeaCustParameter = new YadeaCustParameter();
        yadeaCustParameter.setThreeColorLampSupport("0x0000ff");
        yadeaCustParameter.setDelayCloseLight(30);
        yadeaCustParameter.setTimingStartTime("17:00");
        yadeaCustParameter.setTimingEndTime("20:00");
        yadeaCustParameter.setGearsType(1);
        App.getRkBluetoothClient(this).getYadeaApiService().setCustParameter(macAddress, yadeaCustParameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ConfigResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ConfigResult configResult) {

                    }
                });
```


#### 车况信息上传

##### 简介
蓝连接成功后SDK会监听车辆信息的变化,在发现有数据变化后立即上报到后台服务。数据上报的最快频率支持可配置模式,确保在频繁变化后对服务造成太的负载。
   此项功能SDK内部自动完成,第三方无需主动调用相关接口。
   

### 更新日志

### 相关下载
[锐祺蓝牙Android SDK调试Demo下载地址](https://github.com/caoyy/RKBluetoothSDKSimple)
