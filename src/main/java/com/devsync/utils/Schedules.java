package com.devsync.utils;

import com.devsync.Repositories.TaskDao;
import com.devsync.Repositories.UserDao;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class Schedules {
    private static final Logger logger = Logger.getLogger(Schedules.class.getName());

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new UserDao().refreshTokensEachDay();
            }
        }, 0, 60000);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new TaskDao().tokenSoldIfManagerNotAnswer();
            }
        }, 0, 30000);
    }
}