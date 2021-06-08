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
import org.kayteam.harimelt.kits.utils.command.SimpleCommand;
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.utils.yaml.Yaml;

public class HarimeltKitsCommand extends SimpleCommand {

    private final HarimeltKits plugin;

    public HarimeltKitsCommand(HarimeltKits plugin) {
        super(plugin, "HarimeltKits");
        this.plugin = plugin;
    }

    @Override
    public boolean onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = plugin.getMessages();
        if (player.hasPermission("harimelt.admin")) {
            if (arguments.length > 0) {
                String option = arguments[0].toLowerCase();
                switch (option) {
                    case "reload":
                        plugin.getConfiguration().reloadFileConfiguration();
                        plugin.getMessages().reloadFileConfiguration();
                        plugin.getKitManager().unloadAllKits();
                        plugin.getKitManager().loadAllKits();
                        plugin.getEditing().clear();
                        messages.sendMessage(player, "Admin.pluginReloaded");
                        break;
                    case "help":
                        messages.sendMessage(player, "Admin.help");
                        break;
                    default:
                        messages.sendMessage(player, "Admin.optionIsInvalid");
                }
            } else {
                messages.sendMessage(player, "Admin.optionIsEmpty");
            }
        } else {
            messages.sendMessage(player, "Admin.noPermission");
        }
        return true;
    }

    @Override
    public boolean onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = plugin.getMessages();
        if (arguments.length > 0) {
            String option = arguments[0].toLowerCase();
            switch (option) {
                case "reload":
                    plugin.getConfiguration().reloadFileConfiguration();
                    plugin.getMessages().reloadFileConfiguration();
                    plugin.getKitManager().unloadAllKits();
                    plugin.getKitManager().loadAllKits();
                    plugin.getEditing().clear();
                    messages.sendMessage(console, "Admin.pluginReloaded");
                    break;
                case "help":
                    messages.sendMessage(console, "Admin.help");
                    break;
                default:
                    messages.sendMessage(console, "Admin.optionIsInvalid");
            }
        } else {
            messages.sendMessage(console, "Admin.optionIsEmpty");
        }
        return true;
    }

}