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
import org.kayteam.moretags.tag.Tag;

public class MorePrefixesCommand implements CommandExecutor {

    private final MoreTags moreTags;

    public MorePrefixesCommand(MoreTags moreTags) {
        this.moreTags = moreTags;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("moretags.user")) {
                if (strings.length > 0) {
                    switch (strings[0].toLowerCase()) {
                        case "help":
                            moreTags.getMessages().sendMessage(player, "moreTags.help", true);
                            break;
                        case "select":
                            if (strings.length > 1) {
                                String tag = strings[1].toLowerCase();
                                if (moreTags.getTagManager().containTag(tag)) {
                                    if (player.hasPermission("moretags.tag." + tag)) {
                                        PlayerData playerData = moreTags.getPlayerDataManager().getPlayerData(player.getUniqueId());
                                        if (!playerData.getTag().equals(tag)) {
                                            playerData.setTag(tag);
                                            moreTags.getMessages().sendMessage(player, "moreTags.select.tagSelected", new String[][] {{"%tag%", tag}}, true);
                                        } else {
                                            moreTags.getMessages().sendMessage(player, "moreTags.select.alreadySelectedTag", new String[][] {{"%tag%", tag}}, true);
                                        }
                                    } else {
                                        moreTags.getMessages().sendMessage(player, "moreTags.select.noPermissionTag", new String[][] {{"%tag%", tag}}, true);
                                    }
                                } else {
                                    moreTags.getMessages().sendMessage(player, "moreTags.select.invalidTag", new String[][] {{"%tag%", tag}}, true);
                                }
                            } else {
                                moreTags.getMessages().sendMessage(player, "moreTags.select.emptyTag", true);
                            }
                            break;
                        case "clear":
                            moreTags.getPlayerDataManager().getPlayerData(player.getUniqueId()).setTag("");
                            moreTags.getMessages().sendMessage(player, "moreTags.clear.tagCleared", true);
                            break;
                        case "info":
                            PlayerData playerData = moreTags.getPlayerDataManager().getPlayerData(player.getUniqueId());
                            Tag tag = moreTags.getTagManager().getTag(playerData.getTag());
                            moreTags.getMessages().sendMessage(player, "moreTags.info", new String[][] {{"%tag%", playerData.getTag()}, {"%prefix%", tag.getPrefix()}, {"%suffix%", tag.getSuffix()}}, true);
                            break;
                        default:
                            moreTags.getMessages().sendMessage(player, "moreTags.invalidArguments", true);
                    }
                } else {
                    moreTags.getMessages().sendMessage(player, "moreTags.emptyArguments", true);
                }
            } else {
                moreTags.getMessages().sendMessage(player, "moreTags.noPermission", true);
            }
        } else {
            moreTags.getMessages().sendMessage(commandSender, "moreTags.isConsole", true);
        }
        return true;
    }
}
