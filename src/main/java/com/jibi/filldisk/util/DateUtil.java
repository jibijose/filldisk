package com.jibi.filldisk.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getDateTimeFormatted() {
        String pattern = "yyyyMMdd-HHmmssSSS";
        return new SimpleDateFormat(pattern).format(new Date());
    }

}
