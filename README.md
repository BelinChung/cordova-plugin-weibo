# cordova-plugin-weibo
Cordova (PhoneGap) Plugin to connect to the native Weibo SDK

### Installation

	cordova plugins add com.soundario.weibo


### iOS

1\. 设置工程回调的 URL Types

	info -> URL Types 添加：  
	identifier: com.weibo  
	URL schemes: wbYouAppkey (wb239283940)  

2\. 修改 `weibo.m` 中的相关信息

	`weibo.m` 在 `iOS project > Plugins` 中

``` c++
- (void)login:(CDVInvokedUrlCommand*)command
{
    NSLog(@"登陆");

    //[WeiboSDK enableDebugMode:YES];
    [WeiboSDK registerApp:@"这里填写你的Appkey"];

    WBAuthorizeRequest *request = [WBAuthorizeRequest request];
    request.redirectURI = @"http://www.example.com/callback";
    request.scope = @"all";
    request.userInfo = @{@"SSO_From": @"CDVViewController",
                         @"Other_Info_1": [NSNumber numberWithInt:123],
                         @"Other_Info_2": @[@"obj1", @"obj2"],
                         @"Other_Info_3": @{@"key1": @"obj1", @"key2": @"obj2"}};
    [WeiboSDK sendRequest:request];
    self.pendingCommand = command;
}
```

3\. 重写 `AppDelegate.m`

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

Android 只需要修改 `Weibo.java` 中的相关信息
`Weibo.java` 在 `Android project > src > com > soundario` 中

``` java
private void buildSsoHandler() {
    Activity activity = this.cordova.getActivity();

    String key = "这里填写你的Appkey";
    // redirect_url and scope can be optional
    String url = "http://www.example.com/callback";
    String scope = "email,direct_messages_read,direct_messages_write,"
+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog," + "invitation_write";

    AuthInfo authInfo = new AuthInfo(activity, key, url, scope);

    this.mSsoHandler = new SsoHandler(activity, authInfo);
}
```

### Quick Example

#### Weibo SSO

``` javascript
$$('#wbLoginBtn').on('click',function(){
	window.weibo.login(function (res) {
	    console.log(res);
	});
})
```

### License

Copyright (c) 2015 Belin Chung. MIT Licensed, see [LICENSE] for details.

[LICENSE]:https://github.com/BelinChung/cordova-plugin-weibo/blob/master/LICENSE.md