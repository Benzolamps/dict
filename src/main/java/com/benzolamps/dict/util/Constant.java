package com.benzolamps.dict.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public interface Constant {

    String DATE_FORMAT = "yyyy-MM-dd";

    String TIME_FORMAT = "HH:mm:ss";

    // language=RegExp
    String IDENTIFIER_PATTERN = "^[$_a-zA-Z][$_a-zA-Z0-9]*$";

    DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT + " " + TIME_FORMAT);
}
