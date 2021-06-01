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

package org.kayteam.moretags.util.yaml;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.moretags.util.papi.PlaceholderAPIUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
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


    public boolean existFileConfiguration(){
        file = new File(dir, name + ".yml");
        return file.exists();
    }

    /**
     * Borrar el archivo
     */
    public void deleteFileConfiguration(){
        file = new File(dir, name + ".yml");
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Obtener el archivo de configuracion
     * @return fileConfiguration
     */
    public FileConfiguration getFileConfiguration() {
        if (fileConfiguration == null) {
            reloadFileConfiguration();
        }
        return fileConfiguration;
    }

    /**
     * Recarga el archivo
     */
    public void reloadFileConfiguration() {
        if (fileConfiguration == null) {
            file = new File(dir, name + ".yml");
            File dirFile = new File(dir);
            if (dirFile.exists()){
                dirFile.mkdir();
                try{
                    file.createNewFile();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (javaPlugin.getResource(name + ".yml") != null){
            Reader defConfigStream = new InputStreamReader(javaPlugin.getResource(name + ".yml"), StandardCharsets.UTF_8);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            fileConfiguration.setDefaults(defConfig);
        }
    }

    /**
     * Guarda el archivo
     */
    public void saveFileConfiguration() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            javaPlugin.getLogger().log(Level.SEVERE, "No se pudo guardar el archivo.");
        }
    }

    /**
     * Registra el archivo
     */
    public void registerFileConfiguration() {
        file = new File(dir, name + ".yml");
        if (!file.exists()) {
            getFileConfiguration().options().copyDefaults(true);
            saveFileConfiguration();
        }
        //Bukkit.getLogger().info("Registered [FILE] ("+dir+")/("+name+")");
    }


    public void generateBackup() {
        file = new File(dir, "backup-" + name + ".yml");
        File dirFile = new File(dir);
        if (dirFile.exists()){
            dirFile.mkdir();
            try{
                file.createNewFile();
            } catch (IOException e){
                e.printStackTrace();
            }
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

    public void sendMessage(Player player, String path) {
        if (fileConfiguration == null) {
            reloadFileConfiguration();
        }
        if (fileConfiguration.contains(path)) {
            if (fileConfiguration.isList(path)) {
                List<String> messages = fileConfiguration.getStringList(path);
                for (String message:messages) {
                    message = PlaceholderAPIUtil.addPlaceholders(player, message);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            } else {
                String message = fileConfiguration.getString(path);
                message = PlaceholderAPIUtil.addPlaceholders(player, message);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            javaPlugin.getLogger().info("Path '" + path + "' no found in the '" + name + ".yml' file");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cContact any admin."));
        }
    }
    public void sendMessage(Player player, String path, boolean prefix) {
        if (fileConfiguration == null) {
            reloadFileConfiguration();
        }
        if (fileConfiguration.contains(path)) {
            if (fileConfiguration.isList(path)) {
                List<String> messages = fileConfiguration.getStringList(path);
                for (String message:messages) {
                    message = PlaceholderAPIUtil.addPlaceholders(player, message);
                    if (prefix) {
                        if (fileConfiguration.contains("prefix")) {
                            if (fileConfiguration.isString("prefix")) {
                                message = message.replaceAll("%prefix%", fileConfiguration.getString("prefix"));
                            }
                        }
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            } else {
                String message = fileConfiguration.getString(path);
                message = PlaceholderAPIUtil.addPlaceholders(player, message);
                if (prefix) {
                    if (fileConfiguration.contains("prefix")) {
                        if (fileConfiguration.isString("prefix")) {
                            message = message.replaceAll("%prefix%", fileConfiguration.getString("prefix"));
                        }
                    }
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            javaPlugin.getLogger().info("Path '" + path + "' no found in the '" + name + ".yml' file");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cContact any admin."));
        }
    }
    public void sendMessage(Player player, String path, String[][] replacements) {
        if (fileConfiguration == null) {
            reloadFileConfiguration();
        }
        if (fileConfiguration.contains(path)) {
            if (fileConfiguration.isList(path)) {
                List<String> messages = fileConfiguration.getStringList(path);
                for (String message:messages) {
                    for (String[] values: replacements){
                        message = message.replaceAll(values[0], values[1]);
                    }
                    message = PlaceholderAPIUtil.addPlaceholders(player, message);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            } else {
                String message = fileConfiguration.getString(path);
                for (String[] values: replacements){
                    message = message.replaceAll(values[0], values[1]);
                }
                message = PlaceholderAPIUtil.addPlaceholders(player, message);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            javaPlugin.getLogger().info("Path '" + path + "' no found in the '" + name + ".yml' file");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cContact any admin."));
        }
    }
    public void sendMessage(Player player, String path, String[][] replacements, boolean prefix) {
        if (fileConfiguration == null) {
            reloadFileConfiguration();
        }
        if (fileConfiguration.contains(path)) {
            if (fileConfiguration.isList(path)) {
                List<String> messages = fileConfiguration.getStringList(path);
                for (String message:messages) {
                    for (String[] values: replacements){
                        message = message.replaceAll(values[0], values[1]);
                    }
                    message = PlaceholderAPIUtil.addPlaceholders(player, message);
                    if (prefix) {
                        if (fileConfiguration.contains("prefix")) {
                            if (fileConfiguration.isString("prefix")) {
                                message = message.replaceAll("%prefix%", fileConfiguration.getString("prefix"));
                            }
                        }
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            } else {
                String message = fileConfiguration.getString(path);
                for (String[] values: replacements){
                    message = message.replaceAll(values[0], values[1]);
                }
                message = PlaceholderAPIUtil.addPlaceholders(player, message);
                if (prefix) {
                    if (fileConfiguration.contains("prefix")) {
                        if (fileConfiguration.isString("prefix")) {
                            message = message.replaceAll("%prefix%", fileConfiguration.getString("prefix"));
                        }
                    }
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            javaPlugin.getLogger().info("Path '" + path + "' no found in the '" + name + ".yml' file");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cContact any admin."));
        }
    }

    public void sendMessage(CommandSender commandSender, String path) {
        if (commandSender instanceof Player) {
            sendMessage((Player) commandSender, path);
        } else {
            sendMessage((ConsoleCommandSender) commandSender, path);
        }
    }
    public void sendMessage(CommandSender commandSender, String path, boolean prefix) {
        if (commandSender instanceof Player) {
            sendMessage((Player) commandSender, path, prefix);
        } else {
            sendMessage((ConsoleCommandSender) commandSender, path, prefix);
        }
    }
    public void sendMessage(CommandSender commandSender, String path, String[][] replacements) {
        if (commandSender instanceof Player) {
            sendMessage((Player) commandSender, path, replacements);
        } else {
            sendMessage((ConsoleCommandSender) commandSender, path, replacements);
        }
    }
    public void sendMessage(CommandSender commandSender, String path, String[][] replacements, boolean prefix) {
        if (commandSender instanceof Player) {
            sendMessage((Player) commandSender, path, replacements, prefix);
        } else {
            sendMessage((ConsoleCommandSender) commandSender, path, replacements, prefix);
        }
    }

    public void sendMessage(ConsoleCommandSender consoleCommandSender, String path) {
        if (fileConfiguration == null) {
            reloadFileConfiguration();
        }
        if (fileConfiguration.contains(path)) {
            if (fileConfiguration.isList(path)) {
                List<String> messages = fileConfiguration.getStringList(path);
                for (String message:messages) {
                    consoleCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            } else {
                String message = fileConfiguration.getString(path);
                consoleCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            javaPlugin.getLogger().info("Path '" + path + "' no found in the '" + name + ".yml' file");
            consoleCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cContact any admin."));
        }
    }
    public void sendMessage(ConsoleCommandSender consoleCommandSender, String path, boolean prefix) {
        if (fileConfiguration == null) {
            reloadFileConfiguration();
        }
        if (fileConfiguration.contains(path)) {
            if (fileConfiguration.isList(path)) {
                List<String> messages = fileConfiguration.getStringList(path);
                for (String message:messages) {
                    if (prefix) {
                        if (fileConfiguration.contains("prefix")) {
                            if (fileConfiguration.isString("prefix")) {
                                message = message.replaceAll("%prefix%", fileConfiguration.getString("prefix"));
                            }
                        }
                    }
                    consoleCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            } else {
                String message = fileConfiguration.getString(path);
                consoleCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            javaPlugin.getLogger().info("Path '" + path + "' no found in the '" + name + ".yml' file");
            consoleCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cContact any admin."));
        }
    }
    public void sendMessage(ConsoleCommandSender consoleCommandSender, String path, String[][] replacements) {
        if (fileConfiguration == null) {
            reloadFileConfiguration();
        }
        if (fileConfiguration.contains(path)) {
            if (fileConfiguration.isList(path)) {
                List<String> messages = fileConfiguration.getStringList(path);
                for (String message:messages) {
                    for (String[] values: replacements){
                        message = message.replaceAll(values[0], values[1]);
                    }
                    consoleCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            } else {
                String message = fileConfiguration.getString(path);
                for (String[] values: replacements){
                    message = message.replaceAll(values[0], values[1]);
                }
                consoleCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            javaPlugin.getLogger().info("Path '" + path + "' no found in the '" + name + ".yml' file");
            consoleCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cContact any admin."));
        }
    }
    public void sendMessage(ConsoleCommandSender consoleCommandSender, String path, String[][] replacements, boolean prefix) {
        if (fileConfiguration == null) {
            reloadFileConfiguration();
        }
        if (fileConfiguration.contains(path)) {
            if (fileConfiguration.isList(path)) {
                List<String> messages = fileConfiguration.getStringList(path);
                for (String message:messages) {
                    for (String[] values: replacements){
                        message = message.replaceAll(values[0], values[1]);
                    }
                    if (prefix) {
                        if (fileConfiguration.contains("prefix")) {
                            if (fileConfiguration.isString("prefix")) {
                                message = message.replaceAll("%prefix%", fileConfiguration.getString("prefix"));
                            }
                        }
                    }
                    consoleCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            } else {
                String message = fileConfiguration.getString(path);
                for (String[] values: replacements){
                    message = message.replaceAll(values[0], values[1]);
                }
                if (prefix) {
                    if (fileConfiguration.contains("prefix")) {
                        if (fileConfiguration.isString("prefix")) {
                            message = message.replaceAll("%prefix%", fileConfiguration.getString("prefix"));
                        }
                    }
                }
                consoleCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            javaPlugin.getLogger().info("Path '" + path + "' no found in the '" + name + ".yml' file");
            consoleCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cContact any admin."));
        }
    }
}