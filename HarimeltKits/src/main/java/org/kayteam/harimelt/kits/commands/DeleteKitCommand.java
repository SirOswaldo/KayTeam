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
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.kit.KitManager;
import org.kayteam.harimelt.kits.utils.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

public class DeleteKitCommand extends SimpleCommand {

    public DeleteKitCommand(HarimeltKits harimeltKits) {
        super(harimeltKits, "DeleteKit", "harimelt.delete.kit");
    }

    @Override
    public void onCommand(CommandSender commandSender, String command, String[] strings) {
        if (strings.length > 0) {
            String name = strings[0];
            KitManager kitManager = getHarimeltKits().getKitManager();
            if (kitManager.existKit(name)) {
                kitManager.deleteKit(name);
                sendMessage(commandSender, "DeleteKit.deleteComplete", new String[][] {{"%command%", getCommand()}, {"%name%", name}});
            } else {
                sendMessage(commandSender, "DeleteKit.invalidName", new String[][] {{"%command%", getCommand()}, {"%name%", name}});
            }
        } else {
            sendMessage(commandSender, "DeleteKit.emptyName", new String[][] {{"%command%", getCommand()}});
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String[] strings) {
        if (strings.length == 0) {
            return getHarimeltKits().getKitManager().getKitsNames();
        }
        return new ArrayList<>();
    }
}
