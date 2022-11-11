package com.heima.behavior.mapper;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @ClassName Test.java
 * @Author xazhao
 * @Create 2022.09.15
 * @UpdateUser
 * @UpdateDate 2022.09.15
 * @Description
 * @Version 1.0.0
 */
public class Test {

    public static void main(String[] args) {

        int[] nums = {2, 2, 3, 6, 3, 5, 8, 7, 9, 7};
        System.out.println("Array : " + Arrays.toString(nums));

        Set<Integer> set = new HashSet();

        for (int i = 0; i < nums.length; i++) {
            set.add(nums[i]);
        }
        Object[] objects = set.toArray();
        System.out.println(Arrays.toString(objects));

        System.out.println("Set : " + set);

        int[] ints = Arrays.stream(nums).distinct().toArray();
        System.out.println(Arrays.toString(ints));
    }
}
