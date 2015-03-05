var exec = require('cordova/exec');

module.exports = {
	login: function(onSuccess,onFail) {
		exec(function(r){
			onSuccess(r);
		},onFail, "weibo", "login", []);
	}
}
