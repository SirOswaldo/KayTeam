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
import org.bukkit.entity.Player;
import org.kayteam.harimelt.kits.inventories.MenuEditorInventory;
import org.kayteam.harimelt.kits.kit.Kit;
import org.kayteam.harimelt.kits.kit.KitManager;
import org.kayteam.harimelt.kits.utils.command.SimpleCommand;
import org.kayteam.harimelt.kits.HarimeltKits;

import java.util.List;

public class AdminCommand extends SimpleCommand {

    public AdminCommand(HarimeltKits harimeltKits) {
        super(harimeltKits, "HarimeltKitsAdmin", "harimelt.admin.kits");
        registerCommand();
    }

    @Override
    public void onCommand(CommandSender sender, String command, String[] strings) {
        if (strings.length > 0) {
            String option = strings[0].toLowerCase();
            switch (option) {
                case "reload":
                    // reload
                    sendMessage(sender, "Admin.reloadComplete", new String[][] {{"%command%", getCommand()}});
                    break;
                case "version":
                    // version
                    sendMessage(sender, "Admin.version", new String[][] {{"%command%", getCommand()}});
                    break;
                case "help":
                    sendMessage(sender, "Admin.help", new String[][] {{"%command%", getCommand()}});
                    break;
                default:
                    sendMessage(sender, "Admin.invalidOption", new String[][] {{"%option%", option}});
            }
        } else {
            sendMessage(sender, "Admin.emptyOption");
        }
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String[] strings) {
        return null;
    }
}
