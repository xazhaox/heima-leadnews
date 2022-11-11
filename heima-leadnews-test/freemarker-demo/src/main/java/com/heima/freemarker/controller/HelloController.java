package com.heima.freemarker.controller;

import com.heima.freemarker.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

/**
 * @ClassName HelloController.java
 * @Author xazhao
 * @Create 2022.08.18
 * @Description
 */

@Controller
public class HelloController {

    @GetMapping("/basic")
    public String frameworkTest(Model model) {

        // 纯文本的参数
        model.addAttribute("name", "frameworkTest");

        // 实体类相关的参数
        Student student = new Student();
        student.setName("Marry");
        student.setAge(26);
        student.setMoney(6888F);
        student.setBirthday(new Date());
        model.addAttribute("student", student);

        return "basic";
    }


    @GetMapping("/list")
    public String frameworkTestList(Model model) {
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

        model.addAttribute("studentList", studentList);

        //----------------------------------------------

        Map<String, Student> stringStudentMap = new HashMap<>();
        stringStudentMap.put("student", student);
        stringStudentMap.put("student1", student1);
        model.addAttribute("stringStudentMap", stringStudentMap);

        Date date = new Date();
        model.addAttribute("today", date);

        //1.3 将两个对象模型数据存放到List集合中
        List<Student> stus = new ArrayList<>();
        stus.add(student);
        stus.add(student1);
        model.addAttribute("stus", stus);

        model.addAttribute("point", 102920122);

        return "list";
    }
}
