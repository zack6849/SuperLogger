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
        this.name = fileName;
    }

    public String getFileName() {
        return this.name;
    }
}
