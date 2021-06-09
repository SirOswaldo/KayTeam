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

package org.kayteam.harimelt.tags.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.kayteam.harimelt.tags.HarimeltTags;
import org.kayteam.harimelt.tags.util.command.SimpleCommand;
import org.kayteam.harimelt.tags.util.yaml.Yaml;

import java.util.ArrayList;
import java.util.List;

public class HarimeltTagsCommand extends SimpleCommand {

    private final HarimeltTags harimeltTags;

    public HarimeltTagsCommand(HarimeltTags harimeltTags) {
        super(harimeltTags, "HarimeltTags");
        this.harimeltTags = harimeltTags;
    }

    @Override
    public boolean onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = harimeltTags.getMessages();
        if (player.hasPermission("harimelt.admin")) {
            if (arguments.length > 0) {
                String option = arguments[0].toLowerCase();
                switch (option) {
                    case "help":
                        messages.sendMessage(player, "HarimeltTags.help", new String[][] {{"%command%", command.getName()}});
                        break;
                    case "reload":
                        harimeltTags.getConfiguration().reloadFileConfiguration();
                        harimeltTags.getMessages().reloadFileConfiguration();
                        harimeltTags.getTagManager().unloadAll();
                        harimeltTags.getTagManager().loadAll();
                        harimeltTags.getPlayerDataManager().unloadOnlinePlayerData();
                        harimeltTags.getPlayerDataManager().loadOnlinePlayerData();
                        messages.sendMessage(player, "HarimeltTags.reloaded", new String[][] {{"%plugin%", harimeltTags.getDescription().getName()}});
                        break;
                    case "version":
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aHarimelt&2Tags &8» &fInformación del Complemento"));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fVersión: " + harimeltTags.getDescription().getVersion()));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fAutores: " + harimeltTags.getDescription().getAuthors()));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fWeb: " + harimeltTags.getDescription().getWebsite()));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&m                                                "));
                        break;
                    default:
                        messages.sendMessage(player, "HarimeltTags.optionInvalid", new String[][] {{"%option%", option}});
                }
            } else {
                messages.sendMessage(player, "HarimeltTags.optionEmpty");
            }
        } else {
            messages.sendMessage(player, "HarimeltTags.noPermission");
        }
        return true;
    }

    @Override
    public boolean onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = harimeltTags.getMessages();
        if (arguments.length > 0) {
            String option = arguments[0].toLowerCase();
            switch (option) {
                case "help":
                    messages.sendMessage(console, "HarimeltTags.help", new String[][] {{"%command%", command.getName()}});
                    break;
                case "reload":
                    harimeltTags.getConfiguration().reloadFileConfiguration();
                    harimeltTags.getMessages().reloadFileConfiguration();
                    harimeltTags.getTagManager().unloadAll();
                    harimeltTags.getTagManager().loadAll();
                    harimeltTags.getPlayerDataManager().unloadOnlinePlayerData();
                    harimeltTags.getPlayerDataManager().loadOnlinePlayerData();
                    messages.sendMessage(console, "HarimeltTags.reloaded", new String[][] {{"%plugin%", harimeltTags.getDescription().getName()}});
                    break;
                case "version":
                    console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aHarimelt&2Tags &8» &fInformación del Complemento"));
                    console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fVersión: " + harimeltTags.getDescription().getVersion()));
                    console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fAutores: " + harimeltTags.getDescription().getAuthors()));
                    console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fWeb: " + harimeltTags.getDescription().getWebsite()));
                    console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&m                                                "));
                    break;
                default:
                    messages.sendMessage(console, "HarimeltTags.optionInvalid", new String[][] {{"%option%", option}});
            }
        } else {
            messages.sendMessage(console, "HarimeltTags.optionEmpty");
        }
        return true;
    }

    @Override
    public List<String> onPlayerTabComplete(Player player, Command command, String[] arguments) {
        List<String> complete = new ArrayList<>();
        complete.add("help");
        complete.add("reload");
        complete.add("version");
        return complete;
    }

    @Override
    public List<String> onConsoleTabComplete(ConsoleCommandSender console, Command command, String[] arguments) {
        List<String> complete = new ArrayList<>();
        complete.add("help");
        complete.add("reload");
        complete.add("version");
        return complete;
    }
}