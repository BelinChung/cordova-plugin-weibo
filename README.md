# cordova-plugin-weibo
Cordova (PhoneGap) Plugin to connect to the native Weibo SDK

### Installation

	cordova plugins add com.hiliaox.weibo


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

* 重写 `AppDelegate` 的 `handleOpenURL` 和 `openURL` 方法  
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

### Android

Android 暂时无需修改任何配置或代码

### JavaScript API

初始化 appKey 和 redirectURI   

    window.weibo.init(appKey, redirectURI, onSuccess, onFail)

微博SSO授权登录(未安装将使用 Web 授权)

    window.weibo.login(onSuccess, onFail)


### Quick Example

``` javascript
window.weibo.init('3281812343','http://www.example.com/callback.html');

$$('#wbLoginBtn').on('click',function(){
	window.weibo.login(function (res) {
	    console.log(res);
	});
})
```

### License

Copyright (c) 2015 Belin Chung. MIT Licensed, see [LICENSE] for details.

[LICENSE]:https://github.com/BelinChung/cordova-plugin-weibo/blob/master/LICENSE.md