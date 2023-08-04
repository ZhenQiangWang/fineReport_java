package com.fr.data;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class test {
    public static void main(String[] args) throws ParseException {
        //262741
        //1671589693 now
        //1671324054
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long l = System.currentTimeMillis() / 1000;
        java.util.Date now = df.parse("2022-12-21 10:02:30");
        System.out.println(now.getTime()/1000);
        java.util.Date start = df.parse("2022-12-18 08:40:54");
        System.out.println(start.getTime()/1000);
        System.out.println(now.getTime()/1000 - start.getTime()/1000 - 262741);

    }
}
