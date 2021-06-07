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
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.inventories.MenuEditorInventory;
import org.kayteam.harimelt.kits.kit.KitManager;
import org.kayteam.harimelt.kits.utils.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EditKitCommand extends SimpleCommand {

    public EditKitCommand(HarimeltKits harimeltKits) {
        super(harimeltKits, "KitEdit", "harimelt.kitedit");
    }

    @Override
    public void onCommand(CommandSender commandSender, String command, String[] strings) {
        if (isPlayer(commandSender)) {
            if (strings.length > 0) {
                String name = strings[0];
                KitManager kitManager = getHarimeltKits().getKitManager();
                if (kitManager.existKit(name)) {
                    Player player = getPlayer(commandSender);
                    UUID uuid = player.getUniqueId();
                    MenuEditorInventory menuEditorInventory = new MenuEditorInventory(getHarimeltKits());
                    getHarimeltKits().getEditing().put(uuid, "MENU:" + name);
                    player.openInventory(menuEditorInventory.getInventory(name));
                } else {
                    sendMessage(commandSender, "EditKit.invalidName", new String[][] {{"%command%", getCommand()}, {"%name%", name}});
                }
            } else {
                sendMessage(commandSender, "EditKit.emptyName", new String[][] {{"%command%", getCommand()}});
            }
        } else {
            sendMessage(commandSender, "EditKit.isConsole", new String[][] {{"%command%", getCommand()}});
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