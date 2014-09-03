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

import java.util.Set;

public class Settings {
    private boolean logCommands;
    private boolean logJoin;
    private boolean logChat;
    private boolean logKick;
    private boolean logQuit;
    private boolean logDeath;
    private boolean logDisallowedConnections;
    private boolean logCoordinates;
    private boolean logPlayerIp;
    private boolean logPlayerUUID;
    private boolean useOldLogging;
    private boolean checkCommandExists;
    private boolean autoUpdate;
    private Set<String> filteredCommands;

    public boolean isLogCommands() {
        return logCommands;
    }

    public void setLogCommands(boolean logCommands) {
        this.logCommands = logCommands;
    }

    public boolean isLogJoin() {
        return logJoin;
    }

    public void setLogJoin(boolean logJoin) {
        this.logJoin = logJoin;
    }

    public boolean isLogChat() {
        return logChat;
    }

    public void setLogChat(boolean logChat) {
        this.logChat = logChat;
    }

    public boolean isLogKick() {
        return logKick;
    }

    public void setLogKick(boolean logKick) {
        this.logKick = logKick;
    }

    public boolean isLogQuit() {
        return logQuit;
    }

    public void setLogQuit(boolean logQuit) {
        this.logQuit = logQuit;
    }

    public boolean isLogDeath() {
        return logDeath;
    }

    public void setLogDeath(boolean logDeath) {
        this.logDeath = logDeath;
    }

    public boolean isLogDisallowedConnections() {
        return logDisallowedConnections;
    }

    public void setLogDisallowedConnections(boolean logDisallowedConnections) {
        this.logDisallowedConnections = logDisallowedConnections;
    }

    public boolean isLogCoordinates() {
        return logCoordinates;
    }

    public void setLogCoordinates(boolean logCoordinates) {
        this.logCoordinates = logCoordinates;
    }

    public boolean isLogPlayerIp() {
        return logPlayerIp;
    }

    public void setLogPlayerIp(boolean logPlayerIp) {
        this.logPlayerIp = logPlayerIp;
    }

    public boolean isLogPlayerUUID() {
        return logPlayerUUID;
    }

    public void setLogPlayerUUID(boolean logPlayerUUID) {
        this.logPlayerUUID = logPlayerUUID;
    }

    public boolean isUseOldLogging() {
        return useOldLogging;
    }

    public void setUseOldLogging(boolean useOldLogging) {
        this.useOldLogging = useOldLogging;
    }

    public boolean isCheckCommandExists() {
        return checkCommandExists;
    }

    public void setCheckCommandExists(boolean checkCommandExists) {
        this.checkCommandExists = checkCommandExists;
    }

    public Set<String> getFilteredCommands() {
        return filteredCommands;
    }

    public void setFilteredCommands(Set<String> filteredCommands) {
        this.filteredCommands = filteredCommands;
    }

    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }
}
