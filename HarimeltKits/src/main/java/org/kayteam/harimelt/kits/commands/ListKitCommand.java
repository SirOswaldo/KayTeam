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
import org.kayteam.harimelt.kits.kit.Kit;
import org.kayteam.harimelt.kits.kit.KitManager;
import org.kayteam.harimelt.kits.utils.command.SimpleCommand;
import org.kayteam.harimelt.kits.utils.yaml.Yaml;

import java.util.List;

public class ListKitCommand extends SimpleCommand {

    private final HarimeltKits harimeltKits;

    public ListKitCommand(HarimeltKits harimeltKits) {
        super(harimeltKits, "ListKit");
        this.harimeltKits = harimeltKits;
    }

    @Override
    public boolean onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = harimeltKits.getMessages();
        if (player.hasPermission("harimelt.list.kit")) {
            KitManager kitManager = harimeltKits.getKitManager();
            List<String> kitNames = kitManager.getKitsNames();
            if (kitNames.isEmpty()) {
                messages.sendMessage(player, "ListKit.isEmpty");
            } else {
                messages.sendMessage(player, "ListKit.header");
                for (String kitName:kitNames) {
                    Kit kit = kitManager.getKit(kitName);
                    messages.sendMessage(player, "ListKit.format", new String[][] {
                            {"%kit.name%", kitName},
                            {"%kit.claim.time%", kit.getClaimTime() + ""}
                    });
                }
                messages.sendMessage(player, "ListKit.footer");
            }
        } else {
            messages.sendMessage(player, "ListKit.noPermission");
        }
        return true;
    }

    @Override
    public boolean onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = harimeltKits.getMessages();
        KitManager kitManager = harimeltKits.getKitManager();
        List<String> kitNames = kitManager.getKitsNames();
        if (kitNames.isEmpty()) {
            messages.sendMessage(console, "ListKit.isEmpty");
        } else {
            messages.sendMessage(console, "ListKit.header");
            for (String kitName:kitNames) {
                Kit kit = kitManager.getKit(kitName);
                messages.sendMessage(console, "ListKit.format", new String[][] {
                        {"%kit.name%", kitName},
                        {"%kit.claim.time%", kit.getClaimTime() + ""}
                });
            }
            messages.sendMessage(console, "ListKit.footer");
        }
        return true;
    }
}
