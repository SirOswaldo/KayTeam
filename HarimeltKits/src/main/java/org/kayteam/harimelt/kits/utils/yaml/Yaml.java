/*
 * Copyright (C) 2021 SirOswaldo
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.kayteam.harimelt.kits.utils.yaml;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;


public class Yaml {

    private final JavaPlugin javaPlugin;
    private final String dir;
    private final String name;
    private File file;
    private FileConfiguration fileConfiguration;

    public Yaml(JavaPlugin javaPlugin, String name) {
        this.javaPlugin = javaPlugin;
        this.dir = javaPlugin.getDataFolder().getPath();
        this.name = name;
    }
    public Yaml(JavaPlugin javaPlugin, String dir, String name){
        this.javaPlugin = javaPlugin;
        this.dir = javaPlugin.getDataFolder().getPath() + File.separator + dir;
        this.name = name;
    }

    public FileConfiguration getFileConfiguration() {
        if (fileConfiguration == null) {
            reloadFileConfiguration();
        }
        return fileConfiguration;
    }
    public void reloadFileConfiguration() {
        if (fileConfiguration == null) {
            file = new File(dir, name + ".yml");
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                if (dirFile.mkdir()) {
                    javaPlugin.getLogger().info("The directory '" + dir +"' has been created.");
                }
            }
            try{
                if (file.createNewFile()) {
                    javaPlugin.getLogger().info("The file '" + name +".yml' has been created.");
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (javaPlugin.getResource(name + ".yml") != null){
            Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(javaPlugin.getResource(name + ".yml")), StandardCharsets.UTF_8);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            fileConfiguration.setDefaults(defConfig);
            saveFileConfiguration();
        }
    }
    public void saveFileConfiguration() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            javaPlugin.getLogger().log(Level.SEVERE, "No se pudo guardar el archivo.");
        }
    }
    public void registerFileConfiguration() {
        file = new File(dir, name + ".yml");
        if (!file.exists()) {
            getFileConfiguration().options().copyDefaults(true);
            saveFileConfiguration();
        } else {
            reloadFileConfiguration();
        }
    }
    public boolean deleteFileConfiguration(){
        file = new File(dir, name + ".yml");
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public boolean existFileConfiguration(){
        file = new File(dir, name + ".yml");
        return file.exists();
    }
    public void generateBackup() {
        file = new File(dir, "backup-" + name + ".yml");
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            if (dirFile.mkdir()) {
                javaPlugin.getLogger().info("The directory '" + dir +"' has been created.");
            }
        }
        try{
            if (file.createNewFile()) {
                javaPlugin.getLogger().info("The file 'backup-" + name +".yml' has been created.");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
        fileConfig.setDefaults(fileConfiguration);
    }
    public void saveWithOtherFileConfiguration(FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            javaPlugin.getLogger().log(Level.SEVERE, "No se pudo guardar el archivo.");
        }
    }

    public void sendMessage(CommandSender commandSender, String path) {
        sendMessage(commandSender, path, new String[][] {});
    }
    public void sendMessage(CommandSender commandSender, String path, String[][] replacements) {
        if (fileConfiguration == null) return;
        if (!fileConfiguration.contains(path)) return;
        if (fileConfiguration.isList(path)) {
            List<String> messages = fileConfiguration.getStringList(path);
            for (String message:messages) {
                if (commandSender instanceof Player && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    message = PlaceholderAPI.setPlaceholders((Player) commandSender, message);
                }
                if (fileConfiguration.contains("prefix") && fileConfiguration.isString("prefix")) {
                    message = message.replaceAll("%prefix%", Objects.requireNonNull(fileConfiguration.getString("prefix")));
                }
                for (String[] values:replacements){
                    message = message.replaceAll(values[0], values[1]);
                }
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            String message = fileConfiguration.getString(path);
            if (message != null) {
                if (commandSender instanceof Player && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    message = PlaceholderAPI.setPlaceholders((Player) commandSender, message);
                }
                if (fileConfiguration.contains("prefix") && fileConfiguration.isString("prefix")) {
                    message = message.replaceAll("%prefix%", Objects.requireNonNull(fileConfiguration.getString("prefix")));
                }
                for (String[] values:replacements){
                    message = message.replaceAll(values[0], values[1]);
                }
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }

    // Default
    public void set(String path, Object value) { fileConfiguration.set(path, value); }
    public boolean contains(String path) { return fileConfiguration.contains(path); }
    public boolean isBoolean(String path) { return fileConfiguration.isBoolean(path); }
    public boolean getBoolean(String path) {
        return fileConfiguration.getBoolean(path);
    }
    public boolean getBoolean(String path, boolean def) {
        return fileConfiguration.getBoolean(path, def);
    }
    public boolean isInt(String path) { return fileConfiguration.isInt(path); }
    public int getInt(String path) { return fileConfiguration.getInt(path); }
    public int getInt(String path, int def) { return fileConfiguration.getInt(path, def); }

}