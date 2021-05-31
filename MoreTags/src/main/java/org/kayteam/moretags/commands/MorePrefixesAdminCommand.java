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
import org.bukkit.entity.Player;
import org.kayteam.moretags.MoreTags;
import org.kayteam.moretags.playerdata.PlayerData;
import org.kayteam.moretags.playerdata.PlayerDataManager;
import org.kayteam.moretags.tag.Tag;
import org.kayteam.moretags.util.yaml.Yaml;

import java.util.UUID;

public class MorePrefixesAdminCommand implements CommandExecutor {

    private final MoreTags moreTags;

    public MorePrefixesAdminCommand(MoreTags moreTags) {
        this.moreTags = moreTags;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Yaml messages = moreTags.getMessages();
        String path = "moreTagsAdmin.";
        if (commandSender.hasPermission("moretags.admin")) {
            if (strings.length > 0) {
                String option = strings[0].toLowerCase();
                switch (option) {
                    case "reload":
                        // Files
                        moreTags.getConfiguration().reloadFileConfiguration();
                        moreTags.getMessages().reloadFileConfiguration();
                        // Storage
                        moreTags.setupStorage();
                        // Tags
                        moreTags.getTagManager().unloadAllTags();
                        moreTags.getTagManager().loadAllTags();
                        // Online PlayerData
                        moreTags.getPlayerDataManager().unloadOnlinePlayerData();
                        moreTags.getPlayerDataManager().loadOnlinePlayerData();
                        // Send Message
                        messages = moreTags.getMessages();
                        messages.sendMessage(commandSender, path + "reloaded", true);
                        break;
                    case "clear":
                        if (strings.length > 1) {
                            String playerName = strings[1];
                            Player player = moreTags.getServer().getPlayerExact(playerName);
                            if (player != null) {
                                UUID uuid = player.getUniqueId();
                                PlayerDataManager playerDataManager = moreTags.getPlayerDataManager();
                                PlayerData playerData = playerDataManager.getPlayerData(uuid);
                                playerData.setTag("");
                                messages.sendMessage(commandSender, path + "clear.playerTagCleared", new String[][] {{"%player%", playerName}},true);
                                messages.sendMessage(player, path + "clear.playerTagClearedNotify", true);
                            } else {
                                messages.sendMessage(commandSender, path + "clear.invalidPlayer", new String[][] {{"%player%", playerName}}, true);
                            }
                        } else {
                            messages.sendMessage(commandSender, path + "clear.emptyPlayer", true);
                        }
                        break;
                    case "select":
                        if (strings.length > 1) {
                            String playerName = strings[1];
                            Player player = moreTags.getServer().getPlayerExact(playerName);
                            if (player != null) {
                                if (strings.length > 2) {
                                    String tagName = strings[2].toLowerCase();
                                    if (moreTags.getTagManager().containTag(tagName)) {
                                        PlayerDataManager playerDataManager = moreTags.getPlayerDataManager();
                                        PlayerData playerData = playerDataManager.getPlayerData(player.getUniqueId());
                                        if (!playerData.getTag().equals(tagName)) {
                                            playerData.setTag(tagName);
                                            messages.sendMessage(commandSender, path + "select.tagSelected", new String[][] {{"%tag%", tagName}, {"%player%", playerName}}, true);
                                            messages.sendMessage(commandSender, path + "select.tagSelectedNotify", new String[][] {{"%tag%", tagName}}, true);
                                        } else {
                                            messages.sendMessage(commandSender, path + "select.alreadySelectedTag", new String[][] {{"%tag%", tagName}, {"%player%", playerName}}, true);
                                        }
                                    } else {
                                        messages.sendMessage(commandSender, path + "select.invalidTag", new String[][] {{"%tag%", tagName}}, true);
                                    }
                                } else {
                                    messages.sendMessage(commandSender, path + "select.emptyTag", true);
                                }
                            } else {
                                messages.sendMessage(commandSender, path + "select.invalidPlayer", new String[][] {{"%player%", playerName}}, true);
                            }
                        } else {
                            messages.sendMessage(commandSender, path + "select.emptyPlayer", true);
                        }
                        break;
                    case "help":
                        messages.sendMessage(commandSender, path + "help", true);
                        break;
                    default:
                        messages.sendMessage(commandSender, path + "invalidArguments", true);
                }
            } else {
                messages.sendMessage(commandSender, path + "emptyArguments", true);
            }
        } else {
            messages.sendMessage(commandSender, path + "noPermission", true);
        }
        return true;
    }

}
