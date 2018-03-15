<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
	<link rel="stylesheet" href="/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="/css/font-awesome.min.css">
	<link rel="stylesheet" href="/css/login.css">
	<style>

	</style>
  </head>
  <body>
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <div><a class="navbar-brand" href="index.html" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
        </div>
      </div>
    </nav>

    <div class="container">
      <form id="dologin" action="/doLogin" method="post" class="form-signin" role="form">
        <h2 class="form-signin-heading"><i class="glyphicon glyphicon-picture"></i> 用户登录</h2>
        <font color="#f00">${param.error }</font>
		  <div class="form-group has-success has-feedback">
			<input type="text" class="form-control" id="loginAccount" name="loginAccount" value="zhangsan" placeholder="请输入登录账号" autofocus>
			<span class="glyphicon glyphicon-user form-control-feedback"></span>
		  </div>
		  <div class="form-group has-success has-feedback">
			<input type="password" class="form-control" id="memberpswd" name="memberpswd" value="zhangsan" placeholder="请输入登录密码" style="margin-top:10px;">
			<span class="glyphicon glyphicon-lock form-control-feedback"></span>
		  </div>
		  <div class="form-group has-success has-feedback">
			<select class="form-control" >
                <option value="member">会员</option>
                <option value="user">管理</option>
            </select>
		  </div>
        <div class="checkbox">
          <label>
            <input type="checkbox" name="remember" value="remember-me"> 记住我
          </label>
          <br>
          <label>
            忘记密码
          </label>
          <label style="float:right">
            <a href="/regist">我要注册</a>
          </label>
        </div>
        <a class="btn btn-lg btn-success btn-block" onclick="dologin()" > 登录</a>
      </form>
    </div>
    <script src="/jquery/jquery-2.1.1.min.js"></script>
    <script src="/bootstrap/js/bootstrap.min.js"></script>
    <script src="/layer/layer.js"></script>
    <script>
    function dologin() {
        var loginAccount = $("#loginAccount");
        if("" == loginAccount.val().trim()){
        	//alert("用户名不能为空，请重新输入");
        	layer.msg("用户名不能为空，请重新输入", {time:1500, icon:5, shift:6}, function(){
        		loginAccount.focus();
        	});
        	return;
        }
        
        var memberpswd = $("#memberpswd");
        if("" == memberpswd.val()){
        	//alert("密码不能为空，请重新输入");
        	layer.msg("密码不能为空，请重新输入", {time:1500, icon:5, shift:6}, function(){
        		memberpswd.focus();
        	});
        	return;
        }
        
        var loadingIndex = 0;
        var ajaxEle = {
        		url: "/checkLogin",
        		data: {"loginAccount": loginAccount.val(),
        				"memberpswd": memberpswd.val()
        			},
        		type: "POST",
        		dataType: "json",
        		beforeSend: function(){
        			loadingIndex = layer.msg('正在登录中', {icon: 16});
        		},
        		success: function(result){
        			//true
         			if(result.success){
        				layer.close(loadingIndex);
         				layer.msg("登录成功", {time:1000, icon:1, shift:5}, function(){
         					window.location.href="/main";
         				});
         			}else{
         				layer.msg("用户名或密码不正确，请重新输入", {time:1000, icon:5, shift:6}, function(){});
         				
         			}
        		}
        	};
        $.ajax(ajaxEle);
    }
    </script>
  </body>
</html>