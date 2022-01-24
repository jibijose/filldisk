package com.jibi.filldisk.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static String getDateTimeFormatted() {
        String pattern = "yyyyMMdd-HHmmssSSS";
        return new SimpleDateFormat(pattern).format(new Date());
    }

}
