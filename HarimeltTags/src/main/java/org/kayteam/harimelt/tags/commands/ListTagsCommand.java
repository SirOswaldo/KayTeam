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
import org.kayteam.harimelt.tags.tag.Tag;
import org.kayteam.harimelt.tags.tag.TagManager;
import org.kayteam.harimelt.tags.util.command.SimpleCommand;
import org.kayteam.harimelt.tags.util.yaml.Yaml;

public class ListTagsCommand extends SimpleCommand {

    private final HarimeltTags harimeltTags;

    public ListTagsCommand(HarimeltTags harimeltTags) {
        super(harimeltTags, "ListTags");
        this.harimeltTags = harimeltTags;
    }

    @Override
    public boolean onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = harimeltTags.getMessages();
        if (player.hasPermission("harimelt.list.tag")) {
            TagManager tagManager = harimeltTags.getTagManager();
            if (!tagManager.getNames().isEmpty()) {
                messages.sendMessage(player, "ListTags.header");
                for (String name:tagManager.getNames()) {
                    Tag tag = tagManager.get(name);
                    messages.sendMessage(player, "ListTags.format", new String[][] {
                            {"%tag.name%", tag.getName()},
                            {"%tag.prefix%", tag.getPrefix()},
                            {"%tag.suffix%", tag.getSuffix()}
                    });
                }
                messages.sendMessage(player, "ListTags.footer");
            } else {
                messages.sendMessage(player, "ListTags.listIsEmpty");
            }
        } else {
            messages.sendMessage(player, "ListTags.noPermission");
        }
        return true;
    }

    @Override
    public boolean onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = harimeltTags.getMessages();
        TagManager tagManager = harimeltTags.getTagManager();
        if (!tagManager.getNames().isEmpty()) {
            messages.sendMessage(console, "ListTags.header");
            for (String name:tagManager.getNames()) {
                Tag tag = tagManager.get(name);
                messages.sendMessage(console, "ListTags.format", new String[][] {
                        {"%tag.name%", tag.getName()},
                        {"%tag.prefix%", tag.getPrefix()},
                        {"%tag.suffix%", tag.getSuffix()}
                });
            }
            messages.sendMessage(console, "ListTags.footer");
        } else {
            messages.sendMessage(console, "ListTags.listIsEmpty");
        }
        return true;
    }

}