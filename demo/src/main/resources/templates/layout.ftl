<#macro html>
<!doctype html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <link rel="stylesheet" href="/style.css">
  <link rel="stylesheet" href="/static/style.css">
  <title>PYMVC Demo</title>
</head>
<body>
<p>
  <a href="/?name=world">首页</a> | <a href="/about">关于</a> | <a href="/user/list">用户列表</a>
</p>

<#nested />
</body>
</html>
</#macro>
