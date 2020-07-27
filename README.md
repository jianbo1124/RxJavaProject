# RxJavaProject
RxJava,retrofit,rxbinding的用法
环境
网络请求
android 9.0之后 不允许使用http请求，可以在application中配置usesCleartextTraffic属性

<application
    android:name=".MyApplication"
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:supportsRtl="true"
    android:theme="@style/AppTheme"
    android:usesCleartextTraffic="true">
    <activity android:name=".MainActivity">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />

            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
</application>

引用
implementation 'com.jakewharton.rxbinding4:rxbinding:4.0.0'
implementation 'com.jakewharton.rxbinding4:rxbinding-core:4.0.0'
implementation 'com.jakewharton.rxbinding4:rxbinding-appcompat:4.0.0'

implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.retrofit2:adapter-rxjava3:2.9.0'
RxJava和retrofit的使用
多个异步请求
当使用retrofit和RxJava的时候，经常遇到需要对网络请求的结果进行整理的情况，

可以使用RxJava中的concatMap连接多个处理请求

举个栗子：

客户端需要展示二维码的时候，二维码的内容是从服务端获取的，而创建二维码的过程是个耗时操作，并且执行有先后顺序

可以使用concatMap

override fun loadQRCode(): Observable<Bitmap> {
    var qrCodeValueObservable =
        RetrofitClient.getInstance().create(MainPageApi::class.java).getQRCodeValue()
    return qrCodeValueObservable.concatMap {
        return@concatMap Observable.create { emitter: ObservableEmitter<Bitmap> ->
            it?.result.let { result ->
                QRCodeUtil.createQRCode(object : ResultCallBack<Bitmap> {
                    override fun onSuccess(wrapper: Bitmap) {
                        emitter.onNext(wrapper)
                    }
                    override fun onError(e: Exception) {
                        emitter.onError(e)
                    }
                }, result?.get(0)?.name)
            }
        }
    }
}
循环请求
在实际应用场景中，会遇到定时刷新的情况，可以使用Rxjava中的repeat

栗子：

getModule()?.loop()
    ?.delay(5, TimeUnit.SECONDS, true)//定时5秒
    ?.repeat()//重复请求
    ?.retry()//异常重试
    ?.doOnSubscribe { addDisposable(it) }
    ?.subscribeOn(Schedulers.io())
    ?.observeOn(AndroidSchedulers.mainThread())
    ?.subscribe({
        Log.e("loadLoginState", Date().toString())
    }) {
        Log.e("loadLoginState", it.toString())
    }
超时请求
如果执行耗时操作的时候，无论处理结果如何都有一个返回结果，提示超时或者其他文案

可以使用RxJava中的timeout方法

*.timeout(Constants.REQUEST_TIME_OUT, TimeUnit.SECONDS)
rxbinding的使用
单次点击防抖，3秒内只响应一次

addDisposable(ivQRCode.clicks().throttleFirst(3, TimeUnit.SECONDS).subscribe { //to do })

长按事件

addDisposable(ivQRCode.longClicks().subscribe {
    getP()?.loadQRCode()
})

