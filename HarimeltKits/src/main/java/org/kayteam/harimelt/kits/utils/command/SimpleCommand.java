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

package org.kayteam.harimelt.kits.utils.command;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kayteam.harimelt.kits.utils.yaml.Yaml;
import org.kayteam.harimelt.kits.HarimeltKits;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public abstract class SimpleCommand implements CommandExecutor, TabCompleter {

    private final HarimeltKits harimeltKits;
    private final String command;
    private final String permission;

    public SimpleCommand(HarimeltKits harimeltKits, String command, String permission) {
        this.harimeltKits = harimeltKits;
        this.command = command;
        this.permission = permission;
    }

    public void registerCommand() {
        PluginCommand pluginCommand = harimeltKits.getCommand(command);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
            harimeltKits.getLogger().info("The command '" + command + "' has been registered");
        } else {
            harimeltKits.getLogger().info("The command '" + command + "' no exist in plugin.yml");
            harimeltKits.getLogger().info("Try register without this.");
            try {
                final Field bukkitCommandMap = harimeltKits.getServer().getClass().getDeclaredField("commandMap");
                bukkitCommandMap.setAccessible(true);
                CommandMap commandMap = (CommandMap) bukkitCommandMap.get(harimeltKits.getServer());
                Command command = new Command(getCommand()) {
                    @Override
                    public boolean execute(CommandSender commandSender, String command, String[] strings) {
                        if (commandSender.hasPermission(permission)) {
                            onCommand(commandSender, command, strings);
                        } else {
                            getMessages().sendMessage(commandSender, command + ".noPermission", true);
                        }
                        return true;
                    }
                };
                commandMap.register("cmd", command);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public HarimeltKits getHarimeltKits() {
        return harimeltKits;
    }
    public String getCommand() {
        return command;
    }
    public String getPermission() {
        return permission;
    }

    public Yaml getConfiguration() { return harimeltKits.getConfiguration();}
    public Yaml getMessages() {
        return harimeltKits.getMessages();
    }

    public boolean isPlayer(CommandSender commandSender) {
        return commandSender instanceof Player;
    }
    public Player getPlayer(CommandSender commandSender) { return (Player) commandSender;}
    public boolean isConsole(CommandSender commandSender) {
        return commandSender instanceof ConsoleCommandSender;
    }
    public ConsoleCommandSender getConsole(CommandSender commandSender) { return (ConsoleCommandSender) commandSender;}

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission(permission)) {
            onCommand(commandSender, getCommand(), strings);
        } else {
            getMessages().sendMessage(commandSender, command + ".noPermission", true);
        }
        return true;
    }
    public abstract void onCommand(CommandSender commandSender, String command, String[] strings);


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return onTabComplete(commandSender, command, strings);
    }
    public abstract List<String> onTabComplete(CommandSender commandSender, Command command, String[] strings);

    public void sendMessage(Player player, String path) {
        FileConfiguration file = harimeltKits.getMessages().getFileConfiguration();
        if (file == null) return;
        if (!file.contains(path)) return;
        if (file.isList(path)) {
            List<String> messages = file.getStringList(path);
            for (String message:messages) {
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    message = PlaceholderAPI.setPlaceholders(player, message);
                }
                if (file.contains("prefix") && file.isString("prefix")) {
                    message = message.replaceAll("%prefix%", Objects.requireNonNull(file.getString("prefix")));
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            String message = file.getString(path);
            if (message != null) {
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    message = PlaceholderAPI.setPlaceholders(player, message);
                }
                if (file.contains("prefix") && file.isString("prefix")) {
                    message = message.replaceAll("%prefix%", Objects.requireNonNull(file.getString("prefix")));
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }
    public void sendMessage(Player player, String path, String[][] replacements) {
        FileConfiguration file = harimeltKits.getMessages().getFileConfiguration();
        if (file == null) return;
        if (!file.contains(path)) return;
        if (file.isList(path)) {
            List<String> messages = file.getStringList(path);
            for (String message:messages) {
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    message = PlaceholderAPI.setPlaceholders(player, message);
                }
                if (file.contains("prefix") && file.isString("prefix")) {
                    message = message.replaceAll("%prefix%", Objects.requireNonNull(file.getString("prefix")));
                }
                for (String[] values: replacements){
                    message = message.replaceAll(values[0], values[1]);
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            String message = file.getString(path);
            if (message != null) {
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    message = PlaceholderAPI.setPlaceholders(player, message);
                }
                if (file.contains("prefix") && file.isString("prefix")) {
                    message = message.replaceAll("%prefix%", Objects.requireNonNull(file.getString("prefix")));
                }
                for (String[] values: replacements){
                    message = message.replaceAll(values[0], values[1]);
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }
    public void sendMessage(CommandSender commandSender, String path) {
        if (commandSender instanceof Player) {
            sendMessage((Player) commandSender, path);
        } else {
            sendMessage((ConsoleCommandSender) commandSender, path);
        }
    }
    public void sendMessage(CommandSender commandSender, String path, String[][] replacements) {
        if (commandSender instanceof Player) {
            sendMessage((Player) commandSender, path, replacements);
        } else {
            sendMessage((ConsoleCommandSender) commandSender, path, replacements);
        }
    }
    public void sendMessage(ConsoleCommandSender console, String path) {
        FileConfiguration file = harimeltKits.getMessages().getFileConfiguration();
        if (file == null) return;
        if (!file.contains(path)) return;
        if (file.isList(path)) {
            List<String> messages = file.getStringList(path);
            for (String message:messages) {
                if (file.contains("prefix") && file.isString("prefix")) {
                    message = message.replaceAll("%prefix%", Objects.requireNonNull(file.getString("prefix")));
                }
                console.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            String message = file.getString(path);
            if (message != null) {
                if (file.contains("prefix") && file.isString("prefix")) {
                    message = message.replaceAll("%prefix%", Objects.requireNonNull(file.getString("prefix")));
                }
                console.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }
    public void sendMessage(ConsoleCommandSender console, String path, String[][] replacements) {
        FileConfiguration file = harimeltKits.getMessages().getFileConfiguration();
        if (file == null) return;
        if (!file.contains(path)) return;
        if (file.isList(path)) {
            List<String> messages = file.getStringList(path);
            for (String message:messages) {
                if (file.contains("prefix") && file.isString("prefix")) {
                    message = message.replaceAll("%prefix%", Objects.requireNonNull(file.getString("prefix")));
                }
                for (String[] values: replacements){
                    message = message.replaceAll(values[0], values[1]);
                }
                console.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            String message = file.getString(path);
            if (message != null) {
                if (file.contains("prefix") && file.isString("prefix")) {
                    message = message.replaceAll("%prefix%", Objects.requireNonNull(file.getString("prefix")));
                }
                for (String[] values: replacements){
                    message = message.replaceAll(values[0], values[1]);
                }
                console.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }

}
