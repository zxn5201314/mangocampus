package manggo.com.util;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;
    public static Calendar calendar = Calendar.getInstance();
    /**
     * 距离今天的绝对时间
     *
     * @param date
     * @return
     * @author zxn
     */
    public static String toToday(Date date) {
        long time = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟前";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "小时前";
        else if (ago <= ONE_DAY * 2)
            return "1天前";
        else if (ago <= ONE_DAY * 3) {
            return "2天前" ;
        } else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            return day + "天前";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            return month + "个月前";
        } else {
            long year = ago / ONE_YEAR;
            return year + "年前";
        }

    }

    /**
     * 多少时间前的方法
     * @param nowTime
     * @return
     * @throws Exception
     */
    public static String toDay(String nowTime){
        Date date = new Date();
        String toTime=null;
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = simple.parse(nowTime);
            toTime=DateUtils.toToday(date);
            return toTime;
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("当前toTime",toTime);
    return toTime;
    }

}