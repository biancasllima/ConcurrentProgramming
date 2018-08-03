package com.edu.ufcg.question4.impl;

import com.edu.ufcg.question4.interfaces.Utils;

public class UtilsImpl implements Utils {

    public long milliSecondsUntil(long timeStamp) {
        return timeStamp - System.currentTimeMillis();
    }
}
