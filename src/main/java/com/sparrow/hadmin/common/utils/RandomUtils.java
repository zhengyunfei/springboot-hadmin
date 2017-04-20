package com.sparrow.hadmin.common.utils;

import com.sun.org.apache.xpath.internal.operations.String;

import java.util.Random;

/**
 * Created by 贤云 on 2017/4/19.
 * 随机数产生工具类
 */
public class RandomUtils {
    public static void main(String[] args) {
        int max=20;
        int min=10;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        System.out.println(s);
    }

    /**
     * 获取min到max之间的随机数
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(int min,int max){
        int res=0;
        Random random = new Random();
        res = random.nextInt(max)%(max-min+1) + min;
        return res;
    }
}
