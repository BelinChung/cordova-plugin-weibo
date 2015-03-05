 /*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
*/

#include <sys/types.h>
#include <sys/sysctl.h>

#import <Cordova/CDV.h>
#import <Cordova/CDVViewController.h>

#import "WeiboSDK.h"

#import "weibo.h"


@interface weibo () {


}
@property (nonatomic, strong) CDVInvokedUrlCommand *pendingCommand;

@end

@implementation weibo

- (void)login:(CDVInvokedUrlCommand*)command
{
    NSLog(@"登陆");


    //[WeiboSDK enableDebugMode:YES];
    [WeiboSDK registerApp:@"请填写key"];

    WBAuthorizeRequest *request = [WBAuthorizeRequest request];
    request.redirectURI = @"https://api.weibo.com/oauth2/default.html";
    request.scope = @"all";
    request.userInfo = @{@"SSO_From": @"CDVViewController",
                         @"Other_Info_1": [NSNumber numberWithInt:123],
                         @"Other_Info_2": @[@"obj1", @"obj2"],
                         @"Other_Info_3": @{@"key1": @"obj1", @"key2": @"obj2"}};
    [WeiboSDK sendRequest:request];
    self.pendingCommand = command;



}


- (void)didReceiveWeiboResponse:(WBBaseResponse *)response
{
    NSLog(response.debugDescription);

    if ([response isKindOfClass:WBSendMessageToWeiboResponse.class])
    {
        //NSString *title = @"发送结果";
        //NSString *message = [NSString stringWithFormat:@"响应状态: %d\n响应UserInfo数据: %@\n原请求UserInfo数据: %@",(int)response.statusCode, response.userInfo, response.requestUserInfo];


    }
    else if ([response isKindOfClass:WBAuthorizeResponse.class])
    {


        //success
        if(response.statusCode==0){
            NSDictionary *info=[NSDictionary dictionaryWithObjectsAndKeys:[(WBAuthorizeResponse *)response userID],@"uid",[(WBAuthorizeResponse*)response accessToken],@"token" , nil];

            CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:info];
            [self.commandDelegate sendPluginResult:result
                                        callbackId:self.pendingCommand.callbackId];

            NSLog([NSString stringWithFormat:@"响应状态：%d userId：%@  accessToken：%@",response.statusCode,[(WBAuthorizeResponse *)response userID],[(WBAuthorizeResponse *)response accessToken]]);


        }else{
            //error
            CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
            [self.commandDelegate sendPluginResult:result
                                        callbackId:self.pendingCommand.callbackId];
        }



        self.pendingCommand = nil;

    }
}

@end