# cordova-plugin-weibo
Cordova (PhoneGap) Plugin to connect to the native Weibo SDK

### Installation

	cordova plugins add cordova-plugin-weibo


### iOS

1\. 设置工程回调的 URL Types

	info -> URL Types 添加：  
	identifier: com.weibo  
	URL schemes: wbYouAppkey (wb239283940)

2\. 重写 `AppDelegate.m`

`AppDelegate.m` 在 Xcode 项目的 `Classes` 文件夹中

* 在头部引入相关文件

```
#import "WeiboSDK.h"
#import "weibo.h"
```

* 重写 `AppDelegate` 的 `handleOpenURL` 和 `openURL` 方法  
以前这两个方法有内容就直接添加如下方法中代码

```
- (BOOL)application:(UIApplication*)application openURL:(NSURL*)url sourceApplication:(NSString*)sourceApplication annotation:(id)annotation
{
 	weibo *weiboPlugin = [self.viewController.pluginObjects objectForKey:@"weibo"];
    [WeiboSDK handleOpenURL:url delegate:weiboPlugin];

    return YES;
}

- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url
{
    [WeiboSDK handleOpenURL:url delegate:self ];
    
    return YES;
}
```

3\. 对应用跳转的支持

如果你需要用到微博的相关功能，如登陆，分享等。并且需要实现跳转到微博的功能，在iOS9系统中就需要在你的app的plist中添加下列键值对。否则在canOpenURL函数执行时，就会返回NO。了解详情请至[https://developer.apple.com/videos/wwdc/2015/?id=703](https://developer.apple.com/videos/wwdc/2015/?id=703)

```
<key>LSApplicationQueriesSchemes</key>
<array>
    <string>sinaweibohd</string>
    <string>sinaweibo</string>
    <string>weibosdk</string>
    <string>weibosdk2.5</string>
</array>
```  

### Android

Android 暂时无需修改任何配置或代码

### JavaScript API

初始化 appKey 和 redirectURI   

    window.weibo.init(appKey, redirectURI, onSuccess, onFail)

微博 SSO 授权登录(未安装将使用 Web 授权)

    window.weibo.login(onSuccess, onFail)

判断是否安装微博客户端

    window.weibo.isInstalled(onSuccess, onFail)

分享图文信息到微博

    window.weibo.share(args,onSuccess, onFail)


### Quick Example

``` javascript
window.weibo.init('3281812343','http://www.example.com/callback.html');

$$('#wbLoginBtn').on('click',function(){
	window.weibo.login(function (res) {
	    console.log(res);
	});
})

$$('#wbShareBtn').on('click',function(){
    window.weibo.share({
        type: 'image',
        data: 'http://ww3.sinaimg.cn/large/77565b1bjw1eqd6s01q6ej20c80c80t4.jpg',
        text: 'test my plugin'
    },function(res){
        console.log(res);
    });
})
```

更详细的使用教程可 checkout `example` 目录代码替换 cordova 项目的 `www` 目录即可

### License

Copyright (c) 2015 - 2016 Belin Chung. MIT Licensed, see [LICENSE] for details.

[LICENSE]:https://github.com/BelinChung/cordova-plugin-weibo/blob/master/LICENSE.md