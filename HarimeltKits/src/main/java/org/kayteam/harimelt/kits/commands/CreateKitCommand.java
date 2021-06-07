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
import org.bukkit.inventory.ItemStack;
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.kit.Kit;
import org.kayteam.harimelt.kits.kit.KitManager;
import org.kayteam.harimelt.kits.utils.command.SimpleCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateKitCommand extends SimpleCommand {

    public CreateKitCommand(HarimeltKits harimeltKits) {
        super(harimeltKits, "CreateKit", "harimelt.create.kit");
    }

    @Override
    public void onCommand(CommandSender sender, String command, String[] strings) {
        if (isPlayer(sender)) {
            if (strings.length > 0) {
                String name = strings[0];
                KitManager kitManager = getHarimeltKits().getKitManager();
                if (!kitManager.existKit(name)) {
                    if (strings.length > 1) {
                        String claimTimeString = strings[1];
                        try {
                            int claimTime = Integer.parseInt(claimTimeString);
                            if (claimTime >= 0) {
                                kitManager.createKit(name, claimTime);
                                Kit kit = kitManager.getKit(name);
                                Player player = getPlayer(sender);
                                kit.setItems(new ArrayList<>(Arrays.asList(player.getInventory().getContents())));
                                kitManager.saveKit(name);
                                sendMessage(sender, "CreateKit.createComplete", new String[][] {{"%command%", getCommand()}, {"%name%", name}, {"%time%", claimTimeString}});
                            } else {
                                sendMessage(sender, "CreateKit.negativeClaimTime", new String[][] {{"%command%", getCommand()}, {"%name%", name}, {"%time%", claimTimeString}});
                            }
                        } catch (NumberFormatException e) {
                            sendMessage(sender, "CreateKit.invalidClaimTime", new String[][] {{"%command%", getCommand()}, {"%name%", name}, {"%time%", claimTimeString}});
                        }
                    } else {
                        kitManager.createKit(name, 0);
                        sendMessage(sender, "CreateKit.createComplete", new String[][] {{"%command%", getCommand()}, {"%name%", name}});
                    }
                } else {
                    sendMessage(sender, "CreateKit.invalidName", new String[][] {{"%command%", getCommand()}, {"%name%", name}});
                }
            } else {
                sendMessage(sender, "CreateKit.emptyName", new String[][] {{"%command%", getCommand()}});
            }
        } else {
            sendMessage(sender, "CreateKit.isConsole", new String[][] {{"%command%", getCommand()}});
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String[] strings) {
        return new ArrayList<>();
    }
}
