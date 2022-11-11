<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Hello World!</title>
    </head>
    <body>
        <b>普通文本 String 展示：</b><br><br>
        Hello ${name} <br>
        <hr>
            <b>对象Student中的数据展示：</b><br/>
            姓名：${student.name}<br/>
            年龄：${student.age}<br/>
            余额：${student.money}<br/>
            时间：${student.birthday?datetime}<br/>
        <hr>
    </body>
</html>