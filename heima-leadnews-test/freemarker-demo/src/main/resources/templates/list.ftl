<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>

<#-- list 数据的展示 -->
<b>展示list中的stu数据:</b>
<br>
<br>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
        <td>时间</td>
    </tr>
    <#if studentList??>
        <#list studentList as student>
            <#if student.name='Marry'>
                <tr style="color: red">
                    <td>${student_index + 1}</td>
                    <td>${student.name}</td>
                    <td>${student.age}</td>
                    <td>${student.money}</td>
                    <td>${student.birthday?datetime}</td>
                </tr>
            <#else >
                <tr>
                    <td>${student_index + 1}</td>
                    <td>${student.name}</td>
                    <td>${student.age}</td>
                    <td>${student.money}</td>
                    <td>${student.birthday?datetime}</td>
                </tr>
            </#if>

        </#list>
    </#if>

</table>
<hr>
<#if stringStudentMap??>
    <#-- Map 数据的展示 -->
    <b>map数据的展示：</b>
    <br/><br/>
    <a href="###">方式一：通过map['keyname'].property</a><br/>
    输出stu1的学生信息：<br/>
    姓名：${stringStudentMap['student'].name}<br/>
    年龄：${stringStudentMap['student'].age}<br/>
    余额：${stringStudentMap['student'].money}<br/>
    时间：${stringStudentMap['student'].birthday?datetime}<br/>
    <br/>

    <a href="###">方式二：通过map.keyname.property</a><br/>
    输出stu2的学生信息：<br/>
    姓名：${stringStudentMap.student1.name}<br/>
    年龄：${stringStudentMap.student1.age}<br/>
    余额：${stringStudentMap.student1.money}<br/>
    时间：${stringStudentMap.student1.birthday?datetime}<br/>

    <br/>
</#if>

<#if stringStudentMap??>
    <a href="###">遍历map中两个学生信息：</a><br/>
    <table>
        <tr>
            <td>序号</td>
            <td>姓名</td>
            <td>年龄</td>
            <td>钱包</td>
            <td>时间</td>
        </tr>
        <#list stringStudentMap?keys as key>
            <tr>
                <td>${key_index + 1}</td>
                <td>${stringStudentMap[key].name}</td>
                <td>${stringStudentMap[key].age}</td>
                <td>${stringStudentMap[key].money}</td>
                <td>${stringStudentMap[key].birthday?datetime}</td>
            </tr>
        </#list>
    </table>
</#if>

<b>算数运算符</b>
<br/><br/>
100+5 运算：  ${100 + 5}<br/>
100 - 5 * 5运算：${100 - 5 * 5}<br/>
5 / 2运算：${5 / 2}<br/>
12 % 10运算：${12 % 10}<br/>
<hr>
<hr>

<b>逻辑运算符</b>
<br/>
<br/>
<#if (10 lt 12 )&&( 10  gt  5 )  >
    (10 lt 12 )&&( 10  gt  5 )  显示为 true
</#if>
<br/>
<br/>
<#if !false>
    false 取反为true
</#if>
<hr>

<#if today??>
    <b>获得日期</b><br>

    显示年月日: ${today?date}       <br>

    显示时分秒：${today?time}<br>

    显示日期+时间：${today?datetime}<br>

    自定义格式化：  ${today?string("yyyy年MM月dd日")}<br>
</#if>

<#if point??>
    ${point?c}
</#if>


</body>
</html>