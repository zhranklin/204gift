<#import "/spring.ftl" as spring/>
<html lang="en">
<head> 
    <meta
     charset="utf-8"> 
    <title>小天使砸礼物计划?</title> 
</head>
<body>
<div>
    <form action="/gift/shuffle" method="post">
        <#list users as user>
            <label><input name="${user.phone}" type="checkbox" value="$$phone" />${user.name}</label>
        </#list>
        <input type="submit" />
    </form>
</div>
</body>

</html>