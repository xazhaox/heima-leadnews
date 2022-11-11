package com.itheima;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * @ClassName Tess4jTest.java 图片中的文字识别
 * @Author xazhao
 * @Create 2022.08.25
 * @UpdateUser
 * @UpdateDate 2022.08.25
 * @Description
 * @Version 1.0.0
 */
public class Tess4jTest {

    public static void main(String[] args) throws TesseractException {

        // 创建ITesseract
        ITesseract tesseract = new Tesseract();
        // 获取本地图片文件
        File file = new File("E:\\Users\\Desktop\\image.png");
        // 设置字体库路径
        tesseract.setDatapath("E:\\workspace\\Project\\tessdata");
        // 识别中文
        tesseract.setLanguage("chi_sim");
        // 执行ocr识别
        String result = tesseract.doOCR(file);
        // 识别结果一行显示
        result = result.replaceAll("\r|\n", "-");
        System.out.println("识别的结果为 : " + result);
    }
}
