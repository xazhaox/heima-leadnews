package com.heima.freemarker.test;

import com.heima.FreemarkerTestApplication;
import com.heima.freemarker.entity.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName FreemarkerTest.java
 * @Author xazhao
 * @Create 2022.08.18
 * @Description
 */

@SpringBootTest(classes = FreemarkerTestApplication.class)
@RunWith(SpringRunner.class)
public class FreemarkerTest {

    @Autowired
    private Configuration configuration;

    @Test
    public void freemarkerTest() throws IOException, TemplateException {
        // freemarker的对象模板, 获取模板
        Template template = configuration.getTemplate("list.ftl");
        Map params = getData();
        // 合成, 第一个参数 数据模型, 第二个参数 输出流
        template.process(params, new FileWriter("E:\\workspace\\Project\\list.html"));
    }

    public Map getData() {

        Map<String, Object> map = new HashMap<>();

        Student student = new Student();
        student.setName("小安");
        student.setAge(22);
        student.setMoney(6888F);
        student.setBirthday(new Date());

        Student student1 = new Student();
        student1.setName("Marry");
        student1.setAge(26);
        student1.setMoney(6999F);
        student1.setBirthday(new Date());

        List<Student> studentList = new ArrayList<>();
        studentList.add(student);
        studentList.add(student1);

        map.put("studentList", studentList);

        //创建Map数据
        HashMap<String, Student> studentHashMap = new HashMap<>();
        studentHashMap.put("student", student);
        studentHashMap.put("student1", student1);


        //向map中存放Map数据
        map.put("studentHashMap", studentHashMap);

        Date date = new Date();
        map.put("today", date);

        map.put("point", 102920122);

        return map;
    }
}
