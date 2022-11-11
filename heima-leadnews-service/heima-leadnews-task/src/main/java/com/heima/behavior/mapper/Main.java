package com.heima.behavior.mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @ClassName Main.java
 * @Author xazhao
 * @Create 2022.09.15
 * @UpdateUser
 * @UpdateDate 2022.09.15
 * @Description
 * @Version 1.0.0
 */
public class Main {

    public static void main(String[] args) throws IOException {

        // 使用BufferedReader获取用户输入的字符串
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = br.readLine();

        // 穿件Map集合，key是字符串中的字符，value是字符的个数
        HashMap<Character, Integer> map = new HashMap<>();

        // 遍历字符串，获取每一个字符
        for (char c : str.toCharArray()) {
            // containsKey()判断是否包含指定的key
            if (map.containsKey(c)) {
                // key存在
                Integer value = map.get(c);
                value++;
                map.put(c, value);
            } else {
                // key不存在
                map.put(c, 1);
            }
        }

        // 遍历Map集合，输出结果
        StringBuilder sb = new StringBuilder();
        // keySet()获取或有key
        for (Character key : map.keySet()) {
            Integer value = map.get(key);
            sb.append(key);
            sb.append("_");
            sb.append(value);
            sb.append("_");
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb);

        /*int x = 5, y = 7; int u = 9; int v = 6;
        System.out.println(x > y ? x + 2 : u > v ? u - 3 : v + 2);

        int a = 8;
        System.out.println(a >>> 1);*/

        /*int x = 4; int y = (x++) + (++x) + (x * 10);
        System.out.println(x);
        System.out.println(y);*/

        // TODO 冒泡排序
        int[] nums = new int[]{8, 4, 1, 6, 4, 2, 7, 3, 8, 9};

        for (int i = 1; i < nums.length; i++) {

            // 设定一个标记, true表示该次没有交换元素, 说明排序已经完成
            boolean isFlag = true;

            for (int j = 0; j < nums.length - i; j++) {

                if (nums[j] > nums[j + 1]) {
                    int tmp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = tmp;

                    // false表示交换元素, 说明排序未完成
                    isFlag = false;
                }
            }

            // isFlag为true表示已经排序完成, 结束循环
            if (isFlag) {
                break;
            }
        }

        // 输出排序后的数组
        System.out.println(Arrays.toString(nums));
    }
}
