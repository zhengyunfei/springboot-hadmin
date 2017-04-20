package com.sparrow.hadmin.common.utils;
/**
 * @Title:RandomColor.java
 * @Package:com.fusionchart.model
 * @Description:生成随机颜色
 * @author:贤云
 * @date:2014-1-15 下午11:29:51
 * @version V1.0
 */


import java.util.Random;

/**
 * 类功能说明
 * 类修改者 修改日期
 * 修改说明
 * <p>Title:RandomColor.java</p>
 * <p>Description:java随机生成颜色代码</p>
 * <p>Copyright:Copyright(c)2013</p>
 * @author:贤云
 * @date:2014-1-15 下午11:29:51
 * @version V1.0
 */
public class RandomColor {

    /**
     * @Title:main
     * @Description:生成随机颜色
     * @param:@param args
     * @return: void
     * @throws
     */
    public static String getColor()
    {
        //红色
        String red;
        //绿色
        String green;
        //蓝色
        String blue;
        //生成随机对象
        Random random = new Random();
        //生成红色颜色代码
        red = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //生成绿色颜色代码
        green = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //生成蓝色颜色代码
        blue = Integer.toHexString(random.nextInt(256)).toUpperCase();

        //判断红色代码的位数
        red = red.length()==1 ? "0" + red : red ;
        //判断绿色代码的位数
        green = green.length()==1 ? "0" + green : green ;
        //判断蓝色代码的位数
        blue = blue.length()==1 ? "0" + blue : blue ;
        //生成十六进制颜色值
        String color = "#"+red+green+blue;
        return color;
    }
}
