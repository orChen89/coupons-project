package com.or.thread;

import lombok.Data;

@Data
public class DailyJob {

private final String name;
private boolean quit;

    public DailyJob() {
        this.name = Thread.currentThread().getName();
    }

    public boolean rubJob() {
        this.quit = false;
        return false;
    }

    public boolean stop(){
        this.quit = true;
        return true;
    }
}
