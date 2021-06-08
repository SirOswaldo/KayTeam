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

package org.kayteam.harimelt.kits.commands;

import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.inventories.MenuEditorInventory;
import org.kayteam.harimelt.kits.kit.KitManager;
import org.kayteam.harimelt.kits.utils.command.SimpleCommand;
import org.kayteam.harimelt.kits.utils.yaml.Yaml;

public class EditKitCommand extends SimpleCommand {

    private final HarimeltKits plugin;

    public EditKitCommand(HarimeltKits plugin) {
        super(plugin, "EditKit");
        this.plugin = plugin;
    }

    @Override
    public boolean onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = plugin.getMessages();
        if (player.hasPermission("harimelt.edit.kit")) {
            if (arguments.length > 0) {
                KitManager kitManager = plugin.getKitManager();
                String kitName = arguments[0];
                if (kitManager.existKit(kitName)) {
                    MenuEditorInventory menuEditorInventory = new MenuEditorInventory(plugin);
                    plugin.getEditing().put(player.getUniqueId(), "MENU:" + kitName);
                    player.openInventory(menuEditorInventory.getInventory(kitName));
                } else {
                    messages.sendMessage(player, "EditKit.kitNoExist", new String[][] {{"%kit.name%", kitName}});
                }
            } else {
                messages.sendMessage(player, "EditKit.kitNameEmpty");
            }
        } else {
            messages.sendMessage(player, "EditKit.noPermission");
        }
        return true;
    }

    @Override
    public boolean onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = plugin.getMessages();
        messages.sendMessage(console, "EditKit.isConsole");
        return true;
    }
}
