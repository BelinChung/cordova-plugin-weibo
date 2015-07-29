var exec = require('cordova/exec');

module.exports = {
	init: function(appKey, redirectURI, onSuccess, onFail){
		exec(function(r){
			onSuccess(r);
		},onFail, "weibo", "init",
		[{"appKey": appKey,
           "redirectURI": redirectURI
        }]);
	},
	login: function(onSuccess, onFail) {
		exec(function(r){
			onSuccess(r);
		},onFail, "weibo", "login", []);
	},
    vcodeLogin: function(title, onSuccess, onFail) {
        title = title || "验证码登录";
        exec(function(r){
            onSuccess(r);
        },onFail, "weibo", "vcodeLogin", [{
            title: title
        }]);
    },
    share: function(args,onSuccess, onFail) {
        exec(function(r){
            onSuccess(r);
        },onFail, "weibo", "share", [args]);
    },
    isInstalled: function(onSuccess, onFail) {
        exec(function(r){
            onSuccess(!!r);
        },onFail, "weibo", "isInstalled", []);
    }
}
