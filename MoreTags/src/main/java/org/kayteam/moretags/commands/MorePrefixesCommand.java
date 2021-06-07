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

package org.kayteam.moretags.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kayteam.moretags.MoreTags;
import org.kayteam.moretags.playerdata.PlayerData;
import org.kayteam.moretags.util.yaml.Yaml;

public class MorePrefixesCommand implements CommandExecutor {

    private final MoreTags moreTags;

    public MorePrefixesCommand(MoreTags moreTags) {
        this.moreTags = moreTags;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Yaml messages = moreTags.getMessages();
        String path = "moreTags.";
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("moretags.user")) {
                if (strings.length > 0) {
                    switch (strings[0].toLowerCase()) {
                        case "help":
                            messages.sendMessage(player, path + "help", true);
                            break;
                        case "select":
                            select(player, strings);
                            break;
                        case "clear":
                            moreTags.getPlayerDataManager().getPlayerData(player.getUniqueId()).setTag("");
                            messages.sendMessage(player, path + "clear.tagCleared", true);
                            break;
                        case "info":
                            information(player);
                            break;
                        default:
                            messages.sendMessage(player, path + "invalidArguments", true);
                    }
                } else {
                    messages.sendMessage(player, path + "emptyArguments", true);
                }
            } else {
                messages.sendMessage(player, path + "noPermission", true);
            }
        } else {
            messages.sendMessage(commandSender, path + "isConsole", true);
        }
        return true;
    }

    private void select(Player player, String[] strings) {
        Yaml messages = moreTags.getMessages();
        String path = "moreTags.select";
        if (strings.length > 1) {
            String tag = strings[1].toLowerCase();
            FileConfiguration configuration = moreTags.getConfiguration().getFileConfiguration();
            if (configuration.contains("tags." + tag)) {
                if (player.hasPermission("moretags.tag." + tag)) {
                    PlayerData playerData = moreTags.getPlayerDataManager().getPlayerData(player.getUniqueId());
                    if (!playerData.getTag().equals(tag)) {
                        playerData.setTag(tag);
                        messages.sendMessage(player, path + "select.tagSelected", new String[][] {{"%tag%", tag}}, true);
                    } else {
                        messages.sendMessage(player, path + "select.alreadySelectedTag", new String[][] {{"%tag%", tag}}, true);
                    }
                } else {
                    messages.sendMessage(player, path + "select.noPermissionTag", new String[][] {{"%tag%", tag}}, true);
                }
            } else {
                messages.sendMessage(player, path + "select.invalidTag", new String[][] {{"%tag%", tag}}, true);
            }
        } else {
            messages.sendMessage(player, path + "select.emptyTag", true);
        }
    }

    private void information(Player player) {
        Yaml messages = moreTags.getMessages();
        String path = "moreTags.";
        PlayerData playerData = moreTags.getPlayerDataManager().getPlayerData(player.getUniqueId());
        String prefix = moreTags.getConfiguration().getFileConfiguration().getString("tags." + playerData.getTag() + ".prefix", "");
        String suffix = moreTags.getConfiguration().getFileConfiguration().getString("tags." + playerData.getTag() + ".suffix", "");
        messages.sendMessage(player, path + "information", new String[][] {{"%tag%", playerData.getTag()}, {"%prefix%", prefix}, {"%suffix%", suffix}}, true);
    }
}
