package com.itheima;

import java.util.*;
import org.json.JSONObject;
import com.baidu.aip.ocr.AipOcr;

/**
 * @ClassName BaiduOcr.java
 * @Author xazhao
 * @Create 2022.08.25
 * @UpdateUser
 * @UpdateDate 2022.08.25
 * @Description
 * @Version 1.0.0
 */

public class BaiduOcr {

    //设置APPID/AK/SK
    public static final String APP_ID = "27175666";
    public static final String API_KEY = "500c7b41b50d44c8b5c54c6182de07af";
    public static final String SECRET_KEY = "bac13f6cbde64e529d9ffbfa820738ca";

    public static void main(String[] args) {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 调用接口
        String path = "E:\\Users\\Desktop\\image.png";
        // 识别本地图片
        JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        // 打印识别结果, 结果为Json对象
        System.out.println(res.toString(2));

    }
}
