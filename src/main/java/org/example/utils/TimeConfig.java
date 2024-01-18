package org.example.utils;

import java.time.Clock;
import java.time.LocalDateTime;

public class TimeConfig {
    public static LocalDateTime getTime() {

        Clock cl = Clock.systemUTC();
        LocalDateTime lt = LocalDateTime.now(cl);
        return lt;
    }
}
