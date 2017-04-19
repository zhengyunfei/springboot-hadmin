package com.sparrow.hadmin.api.github;

import org.eclipse.egit.github.core.Repository;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/18.
 */
public class MyComparator implements Comparator<Object> {
    @Override
    public int compare(Object o1, Object o2) {
        Repository r1=(Repository)o1;
        Repository r2=(Repository)o2;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String r1Date = formatter.format(r1.getPushedAt());
        String r2Date = formatter.format(r2.getPushedAt());
        int flag=compare_date(r2Date,r1Date);
        return flag;
    }

    /**
     * 比较2个日期的大小
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compare_date(String DATE1, String DATE2) {


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
