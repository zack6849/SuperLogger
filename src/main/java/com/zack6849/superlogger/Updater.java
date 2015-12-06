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

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by zack6849 on 11/30/2015.
 */
public class Updater {
    private int id;
    private JavaPlugin plugin;
    private File pluginFile;
    private JsonObject data;

    public Updater(JavaPlugin plugin, File pluginFile, int id){
        this.plugin = plugin;
        this.pluginFile = pluginFile;
        this.id = id;
    }

    public void fetchData(){
        String apidata = fetchContent("https://api.curseforge.com/servermods/files?projectIds=" + id);
        JsonParser parser = new JsonParser();
        if(apidata.equalsIgnoreCase("nothing.")){
            data = parser.parse("{}").getAsJsonObject();
        }else{
            JsonArray dataarray = parser.parse(apidata).getAsJsonArray();
            data = dataarray.get(dataarray.size() - 1).getAsJsonObject();
        }
    }

    public void updatePlugin() throws IOException {
        FileUtils.copyURLToFile(new URL(getDownloadURL()), pluginFile);
    }


    public JsonObject getData(){
        if(this.data == null){
            fetchData();
        }
        return this.data;
    }

    public boolean isUpdateAvailible(){
        if(!data.has("fileUrl")){
            return false;
        }
        Version current = new Version(plugin.getDescription().getVersion());
        Version availible = new Version(getLatestVersion().split("v")[1]);
        //return if the current version is smaller than the new version.
        return current.compareTo(availible) == -1;
        }

    public String getDownloadURL(){
        if(!data.has("fileUrl")){
            return "N/A";
        }
        return data.get("fileUrl").getAsString();
    }

    public String getLatestVersion(){
        if(!data.has("fileUrl")){
            return "N/A";
        }
        return data.get("name").getAsString();
    }

    public String fetchContent(String url){
        try {
            return Resources.toString(new URL(url), Charsets.UTF_8);
        } catch (IOException e) {
            plugin.getLogger().warning("Couldn't fetch data from curse API!");
        }
        return "nothing.";
    }

}
