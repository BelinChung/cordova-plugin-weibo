/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        app.weiboInit();        
    },
    weiboInit: function(){
        // 初始化微博插件
        window.weibo.init('2798261xxx','http://www.sina.com.cn/callback.html');

        // 按钮事件绑定
        var parentElement = document.getElementById('wrapper');
        parentElement.querySelector('.check-install').addEventListener('click', app.checkInstall, false);
        parentElement.querySelector('.sso-login').addEventListener('click', app.ssoLogin, false);
        parentElement.querySelector('.share-text').addEventListener('click', app.shareText, false);
        parentElement.querySelector('.vcode-login').addEventListener('click', app.vcodeLogin, false);
    },
    checkInstall: function(){
        window.weibo.isInstalled(function(status){
            var text = status ? '已安装微博客户端！' : '未安装微博客户端！';
            alert(text);
        })
    },
    ssoLogin: function(){
        window.weibo.login(function(res){
            alert('授权成功！\n uid: ' + res.uid + '\n access_token: ' + res.token + '\n refresh_token: ' + res.refresh_token);
        },function(err){
            alert('授权失败！');
        })
    },
    shareText: function(){
        window.weibo.isInstalled(function(status){
            if(status){
                window.weibo.share({
                    type: 'image',
                    data: 'http://ww3.sinaimg.cn/large/77565b1bjw1eqd6s01q6ej20c80c80t4.jpg',
                    text: 'test my plugin'
                },function(res){
                    alert('发送成功');
                },function(res){
                    if(res === 'cancel') {
                        alert('用户取消分享')
                    } else {
                        alert('发送失败');
                    }
                });    
            }else{
                alert('请先安装微博客户端！');
            }
        })
    },
    vcodeLogin: function(){
        window.weibo.vcodeLogin("tetetete",function(res){
            alert('授权成功！\n phone_number' + res.phone_number);
        },function(err){
            alert('授权失败！');
        })
    }
};

app.initialize();