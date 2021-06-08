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
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.kit.KitManager;
import org.kayteam.harimelt.kits.utils.command.SimpleCommand;
import org.kayteam.harimelt.kits.utils.yaml.Yaml;

public class DeleteKitCommand extends SimpleCommand {

    private final HarimeltKits plugin;

    public DeleteKitCommand(HarimeltKits plugin) {
        super(plugin, "DeleteKit");
        this.plugin = plugin;
    }

    @Override
    public boolean onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = plugin.getMessages();
        if (player.hasPermission("harimelt.delete.kit")) {
            if (arguments.length > 0) {
                KitManager kitManager = plugin.getKitManager();
                String kitName = arguments[0];
                if (kitManager.existKit(kitName)) {
                    kitManager.deleteKit(kitName);
                    messages.sendMessage(player, "DeleteKit.kitDeleted", new String[][] {{"%kit.name%", kitName}});
                } else {
                    messages.sendMessage(player, "DeleteKit.kitNoExist", new String[][] {{"%kit.name%", kitName}});
                }
            } else {
                messages.sendMessage(player, "DeleteKit.kitNameEmpty");
            }
        } else {
            messages.sendMessage(player, "DeleteKit.noPermission");
        }
        return true;
    }

    @Override
    public boolean onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = plugin.getMessages();
        if (arguments.length > 0) {
            KitManager kitManager = plugin.getKitManager();
            String kitName = arguments[0];
            if (kitManager.existKit(kitName)) {
                kitManager.deleteKit(kitName);
                messages.sendMessage(console, "DeleteKit.kitDeleted", new String[][] {{"%kit.name%", kitName}});
            } else {
                messages.sendMessage(console, "DeleteKit.kitNoExist", new String[][] {{"%kit.name%", kitName}});
            }
        } else {
            messages.sendMessage(console, "DeleteKit.kitNameEmpty");
        }
        return true;
    }

}