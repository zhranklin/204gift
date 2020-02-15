<#import "/spring.ftl" as spring/>
<html lang="en">
<head> 
    <meta
     charset="utf-8"> 
    <title>小天使砸礼物计划?</title> 
</head>
<body>
<div>
    1. 我已经填过地址了<br>
    <form action="/gift/view" method="post">
        我的手机:<br>
        <input type="text" name="phone" /><br>
        密码:<br>
        <input type="text" name="password" /><br>
        <input type="submit" />
    </form>
    <br>
    <br>
    <br>
    <br>
  <a href="/gift/new">2. 我还没填过地址</a>
</div>
</body>

</html>