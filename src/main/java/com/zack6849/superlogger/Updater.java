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
    private String slug;
    private JavaPlugin plugin;
    private File pluginFile;
    private JsonObject data;

    public Updater(JavaPlugin plugin, File pluginFile, String slug){
        this.plugin = plugin;
        this.pluginFile = pluginFile;
        this.slug = slug;
    }

    public void fetchData(){
        String apidata = fetchContent("https://api.bukget.org/3/updates?slugs=" + slug);
        JsonParser parser = new JsonParser();
        data = parser.parse(apidata).getAsJsonArray().get(0).getAsJsonObject();
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

        return !getLatestVersion().equalsIgnoreCase(plugin.getDescription().getVersion());
    }

    public String getDownloadURL(){
        return data.get("versions").getAsJsonObject().get("latest").getAsJsonObject().get("download").getAsString();
    }

    public String getLatestVersion(){
        return data.get("versions").getAsJsonObject().get("latest").getAsJsonObject().get("version").getAsString();
    }

    public String fetchContent(String url){
        try {
            return Resources.toString(new URL(url), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "nothing.";
    }

}
