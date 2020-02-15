<#import "/spring.ftl" as spring/>
<html lang="en">
<head> 
    <meta
     charset="utf-8"> 
    <title>小天使砸礼物计划?</title> 
</head>
<body>
<div style="float:left;width:400px;">
  <h3>204小天使砸礼物🎁计划<br>提交后一定要截图!!!!!!!</h3>
    <ul>
      <li>单向匿名，每个人抽签知道自己送谁礼物，收礼物的人不知道自己的小天使是谁！</li>
      <li>礼物预算控制在总价204rmb以内为佳（不包括邮费），可以是实物，也可以是虚拟物品，数量不限，可以直接下单了寄，也可以小天使收集好了发一个快递。</li>
      <li>收到礼物以后可以简单做一个开箱，记录一下自己拆礼物的心情、猜测一下自己的天使是谁！视频不露脸也没有关系！</li>
    </ul>
<#if password?? && number??>
        <pre><code>
我是: ${number}, 我要把礼物送给${tn}, <#if t??>他/她(${tn})的个人信息是:
地址: ${(t.district)!""}, ${t.address!""}
手机: ${(t.phone)!""}
收件人: ${(t.name)!""}
邮箱: ${(t.email)!""}<#else>他/她(${tn})还没填写个人信息哦
</#if>


已知的全局关系:
<#list mappings as s>
${s}
</#list>
    </code></pre>
    </#if>
    <form action="/gift/submit" method="post">
        手机:<br>
        <input type="text" name="phone" value="${phone!""}" /><br>
        设置密码:<br>
        <input type="text" name="password" value="${password!""}"/><br><br>
        省市区:<br>
        <input type="text" name="district" value="${district!""}"/><br>
        详细地址:<br>
        <input type="text" name="address" value="${address!""}" /><br>
        收件人:<br>
        <input type="text" name="name" value="${name!""}" /><br>
        邮箱(便于收取虚拟物品):<br>
        <input type="text" name="email" value="${email!""}" /><br>
        <#if password??>
            <input name="oldPass" type="hidden" value="${password}">
        </#if>
        <input type="submit" value="提交/修改"/>
    </form>
    <br> <br> <br> <br>
    <form action="/gift/delete" method="post">
        <#if password??>
          <input type="hidden" name="phone" value="${phone!""}" /><br>
          <input name="oldPass" type="hidden" value="${password!""}">
        </#if>
      <input type="submit" value="我要退出"/>
    </form>
</div>
</body>

</html>
