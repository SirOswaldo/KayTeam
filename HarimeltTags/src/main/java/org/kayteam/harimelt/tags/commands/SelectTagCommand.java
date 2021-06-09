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

import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.kayteam.harimelt.tags.HarimeltTags;
import org.kayteam.harimelt.tags.playerdata.PlayerData;
import org.kayteam.harimelt.tags.playerdata.PlayerDataManager;
import org.kayteam.harimelt.tags.tag.TagManager;
import org.kayteam.harimelt.tags.util.command.SimpleCommand;
import org.kayteam.harimelt.tags.util.yaml.Yaml;

import java.util.List;

public class SelectTagCommand extends SimpleCommand {

    private final HarimeltTags harimeltTags;

    public SelectTagCommand(HarimeltTags harimeltTags) {
        super(harimeltTags, "SelectTag");
        this.harimeltTags = harimeltTags;
    }

    @Override
    public boolean onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = harimeltTags.getMessages();
        if (player.hasPermission("harimelt.select.tag")) {
            if (arguments.length > 0) {
                if (arguments.length > 1) {
                    if (player.hasPermission("harimelt.select.other.tag")) {
                        String targetName = arguments[0];
                        String name = arguments[1];
                        Player target = harimeltTags.getServer().getPlayerExact(targetName);
                        if (target != null) {
                            TagManager tagManager = harimeltTags.getTagManager();
                            if (target == player) {
                                if (tagManager.exist(name)) {
                                    if (player.hasPermission("harimelt.select.tag." + name)) {
                                        PlayerDataManager playerDataManager = harimeltTags.getPlayerDataManager();
                                        PlayerData playerData = playerDataManager.getPlayerData(player.getUniqueId());
                                        if (!playerData.getTag().equals(name)) {
                                            playerData.setTag(name);
                                            messages.sendMessage(player, "SelectTag.tagSelected", new String[][] {{"%tag.name%", name}});
                                        } else {
                                            messages.sendMessage(player, "SelectTag.tagAlreadySelected", new String[][] {{"%tag.name%", name}});
                                        }
                                    } else {
                                        messages.sendMessage(player, "SelectTag.tagNoPermission", new String[][] {{"%tag.name%", name}});
                                    }
                                } else {
                                    messages.sendMessage(player, "SelectTag.tagNoExist", new String[][] {{"%tag.name%", name}});
                                }
                            } else {
                                if (tagManager.exist(name)) {
                                    if (player.hasPermission("harimelt.select.other.tag." + name)) {
                                        PlayerDataManager playerDataManager = harimeltTags.getPlayerDataManager();
                                        PlayerData playerData = playerDataManager.getPlayerData(target.getUniqueId());
                                        if (playerData.getTag().equals(name)) {
                                            playerData.setTag(name);
                                            messages.sendMessage(player, "SelectTag.tagSelectedOther", new String[][] {{"%player.name%", targetName}, {"%tag.name%", name}});
                                            messages.sendMessage(target, "SelectTag.tagSelectedOtherNotify", new String[][] {{"%tag.name%", name}});
                                        } else {
                                            messages.sendMessage(player, "SelectTag.tagAlreadySelectedOther", new String[][] {{"%player.name%", targetName}, {"%tag.name%", name}});
                                        }
                                    } else {
                                        messages.sendMessage(player, "SelectTag.tagNoPermissionOther", new String[][] {{"%tag.name%", name}});
                                    }
                                } else {
                                    messages.sendMessage(player, "SelectTag.tagNoExist", new String[][] {{"%tag.name%", name}});
                                }
                            }
                        } else {
                            messages.sendMessage(player, "SelectTag.playerNoExist", new String[][] {{"%player.name%", targetName}});
                        }
                    } else {
                        messages.sendMessage(player, "SelectTag.noPermissionOthers");
                    }
                } else {
                    TagManager tagManager = harimeltTags.getTagManager();
                    String name = arguments[0];
                    if (tagManager.exist(name)) {
                        if (player.hasPermission("harimelt.select.tag." + name)) {
                            PlayerDataManager playerDataManager = harimeltTags.getPlayerDataManager();
                            PlayerData playerData = playerDataManager.getPlayerData(player.getUniqueId());
                            if (!playerData.getTag().equals(name)) {
                                playerData.setTag(name);
                                messages.sendMessage(player, "SelectTag.tagSelected", new String[][] {{"%tag.name%", name}});
                            } else {
                                messages.sendMessage(player, "SelectTag.tagAlreadySelected", new String[][] {{"%tag.name%", name}});
                            }
                        } else {
                            messages.sendMessage(player, "SelectTag.tagNoPermission", new String[][] {{"%tag.name%", name}});
                        }
                    } else {
                        messages.sendMessage(player, "SelectTag.tagNoExist", new String[][] {{"%tag.name%", name}});
                    }
                }
            } else {
                messages.sendMessage(player, "SelectTag.tagNameEmpty");
            }
        } else {
            messages.sendMessage(player, "SelectTag.noPermission");
        }
        return true;
    }

    @Override
    public boolean onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = harimeltTags.getMessages();
        if (arguments.length > 0) {
            if (arguments.length > 1) {
                String targetName = arguments[0];
                String name = arguments[1];
                Player target = harimeltTags.getServer().getPlayerExact(targetName);
                if (target != null) {
                    TagManager tagManager = harimeltTags.getTagManager();
                    if (tagManager.exist(name)) {
                        PlayerDataManager playerDataManager = harimeltTags.getPlayerDataManager();
                        PlayerData playerData = playerDataManager.getPlayerData(target.getUniqueId());
                        if (playerData.getTag().equals(name)) {
                            playerData.setTag(name);
                            messages.sendMessage(console, "SelectTag.tagSelectedOther", new String[][] {{"%player.name%", targetName}, {"%tag.name%", name}});
                            messages.sendMessage(target, "SelectTag.tagSelectedOtherNotify", new String[][] {{"%tag.name%", name}});
                        } else {
                            messages.sendMessage(console, "SelectTag.tagAlreadySelectedOther", new String[][] {{"%player.name%", targetName}, {"%tag.name%", name}});
                        }
                    } else {
                        messages.sendMessage(console, "SelectTag.tagNoExist", new String[][] {{"%tag.name%", name}});
                    }
                } else {
                    messages.sendMessage(console, "SelectTag.playerNoExist", new String[][] {{"%player.name%", targetName}});
                }
            } else {
                messages.sendMessage(console, "SelectTag.tagNameEmpty");
            }
        } else {
            messages.sendMessage(console, "SelectTag.playerNameEmpty");
        }
        return true;
    }

    @Override
    public List<String> onPlayerTabComplete(Player player, Command command, String[] arguments) {
        TagManager tagManager = harimeltTags.getTagManager();
        List<String> tags = tagManager.getNames();
        if (!tags.isEmpty()) {
            for (int i = 0; i < tags.size(); i++) {
                if (!player.hasPermission("harimelt.select.tag." + tags.get(i))) {
                    try {
                        tags.remove(i);
                    } catch (UnsupportedOperationException | ArrayIndexOutOfBoundsException ignored) { }
                }
            }
        }
        return tags;
    }

    @Override
    public List<String> onConsoleTabComplete(ConsoleCommandSender console, Command command, String[] arguments) {
        TagManager tagManager = harimeltTags.getTagManager();
        return tagManager.getNames();
    }
}