/*
* This file is part of SuperLogger.
*
* SuperLogger is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/

package com.zack6849.superlogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class LoggerAbstraction {
    private BufferedWriter writer;
    private File file;
    private LoggingCategory category;
    private int day;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
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

    public int getDay() {
        return day;
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
