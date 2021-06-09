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
import org.kayteam.harimelt.tags.inventories.TagEditInventory;
import org.kayteam.harimelt.tags.tag.TagManager;
import org.kayteam.harimelt.tags.util.command.SimpleCommand;
import org.kayteam.harimelt.tags.util.yaml.Yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EditTagCommand extends SimpleCommand {

    private final HarimeltTags harimeltTags;

    public EditTagCommand(HarimeltTags harimeltTags) {
        super(harimeltTags, "EditTag");
        this.harimeltTags = harimeltTags;
    }

    @Override
    public boolean onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = harimeltTags.getMessages();
        if (player.hasPermission("harimelt.edit.tag")) {
            if (arguments.length > 0) {
                TagManager tagManager = harimeltTags.getTagManager();
                String name = arguments[0];
                if (tagManager.exist(name)) {
                    UUID uuid = player.getUniqueId();
                    harimeltTags.getEditing().put(uuid, new String[] {"MENU", name});
                    TagEditInventory tagEditInventory = new TagEditInventory(harimeltTags);
                    player.openInventory(tagEditInventory.getInventory(name));
                } else {
                    messages.sendMessage(player, "EditTag.tagNoExist", new String[][] {{"%tag.name%", name}});
                }
            } else {
                messages.sendMessage(player, "EditTag.tagNameEmpty");
            }
        } else {
            messages.sendMessage(player, "EditTag.noPermission");
        }
        return true;
    }

    @Override
    public boolean onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = harimeltTags.getMessages();
        messages.sendMessage(console, "EditTag.isConsole");
        return true;
    }

    @Override
    public List<String> onPlayerTabComplete(Player player, Command command, String[] arguments) {
        if (player.hasPermission("harimelt.edit.tag")) {
            TagManager tagManager = harimeltTags.getTagManager();
            return tagManager.getNames();
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> onConsoleTabComplete(ConsoleCommandSender console, Command command, String[] arguments) {
        TagManager tagManager = harimeltTags.getTagManager();
        return tagManager.getNames();
    }

}
