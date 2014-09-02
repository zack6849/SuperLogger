package com.zack6849.superlogger;

import java.util.Set;

/**
 * Created by zack6849 on 9/1/14.
 */
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
}
