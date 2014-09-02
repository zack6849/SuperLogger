package com.zack6849.superlogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 * Created by zack6849 on 9/1/14.
 */
public class LoggerAbstraction {
    private BufferedWriter writer;
    private File file;
    private LoggingCategory category;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public LoggingCategory getCategory() {
        return this.category;
    }

    public void setCategory(LoggingCategory category) {
        this.category = category;
    }

}
