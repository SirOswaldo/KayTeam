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
import org.kayteam.harimelt.kits.kit.Kit;
import org.kayteam.harimelt.kits.kit.KitManager;
import org.kayteam.harimelt.kits.utils.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

public class ListKitCommand extends SimpleCommand {

    public ListKitCommand(HarimeltKits harimeltKits) {
        super(harimeltKits, "Kits", "harimelt.kits");
        registerCommand();
    }

    @Override
    public void onCommand(CommandSender sender, String command, String[] strings) {
        KitManager kitManager = getHarimeltKits().getKitManager();
        List<String> names = kitManager.getKitsNames();
        if (!names.isEmpty()) {
            sendMessage(sender, "ListKit.header");
            for (String name:names) {
                Kit kit = kitManager.getKit(name);
                sendMessage(sender, "ListKit.format", new String[][] {
                        {"%name%", name},
                        {"%claim-time%", kit.getClaimTime() + ""}
                });
            }
            sendMessage(sender, "ListKit.footer");
        } else {
            sendMessage(sender, "ListKit.isEmpty");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String[] strings) {
        return new ArrayList<>();
    }
}
