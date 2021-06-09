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

public class CreateTagCommand extends SimpleCommand {

    private final HarimeltTags harimeltTags;

    public CreateTagCommand(HarimeltTags harimeltTags) {
        super(harimeltTags, "CreateTag");
        this.harimeltTags = harimeltTags;
    }

    @Override
    public boolean onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = harimeltTags.getMessages();
        if (player.hasPermission("harimelt.create.tag")) {
            if (arguments.length > 0) {
                TagManager tagManager = harimeltTags.getTagManager();
                String name = arguments[0].toLowerCase();
                if (!tagManager.exist(name)) {
                    Tag tag = new Tag(name);
                    tagManager.add(tag);
                    tagManager.save(name);
                    messages.sendMessage(player, "CreateTag.tagCreated", new String[][] {{"%tag.name%", name}});
                } else {
                    messages.sendMessage(player, "CreateTag.tagAlreadyExist", new String[][] {{"%tag.name%", name}});
                }
            } else {
                messages.sendMessage(player, "CreateTag.tagNameEmpty");
            }
        } else {
            messages.sendMessage(player, "CreateTag.noPermission");
        }
        return true;
    }

    @Override
    public boolean onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = harimeltTags.getMessages();
        if (arguments.length > 0) {
            TagManager tagManager = harimeltTags.getTagManager();
            String name = arguments[0].toLowerCase();
            if (!tagManager.exist(name)) {
                Tag tag = new Tag(name);
                tagManager.add(tag);
                tagManager.save(name);
                messages.sendMessage(console, "CreateTag.tagCreated", new String[][] {{"%tag.name%", name}});
            } else {
                messages.sendMessage(console, "CreateTag.tagAlreadyExist", new String[][] {{"%tag.name%", name}});
            }
        } else {
            messages.sendMessage(console, "CreateTag.tagNameEmpty");
        }
        return true;
    }

}