package com.zack649.superlogger;

/**
 * Created by zack6849 on 9/1/14.
 */
public enum LoggingCategory {
    ALL("log.log"),
    CHAT("chat.log"),
    COMMANDS("commands.log"),
    DEATH("deaths.log"),
    JOIN("connections.log"),
    KICK("connections.log"),
    QUIT("connections.log"),
    FAILED_LOGIN("connections.log");

    private String name;

    LoggingCategory(String fileName) {
        this.name = name;
    }

    public String getFileName() {
        return this.name;
    }
}
