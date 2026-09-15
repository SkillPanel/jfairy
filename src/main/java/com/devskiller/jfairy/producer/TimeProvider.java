/*
 * Copyright (c) 2013. Codearte
 */

package com.devskiller.jfairy.producer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeProvider {

    public int getCurrentYear() {
        return getCurrentTime().getYear();
    }

    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now(ZoneId.systemDefault());
    }

    public LocalDate getCurrentDate() {
        return LocalDate.now(ZoneId.systemDefault());
    }
}
