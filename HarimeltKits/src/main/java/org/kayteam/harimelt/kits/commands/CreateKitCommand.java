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
import org.bukkit.inventory.ItemStack;
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.kit.Kit;
import org.kayteam.harimelt.kits.kit.KitManager;
import org.kayteam.harimelt.kits.utils.command.SimpleCommand;
import org.kayteam.harimelt.kits.utils.yaml.Yaml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateKitCommand extends SimpleCommand {

    private final HarimeltKits plugin;

    public CreateKitCommand(HarimeltKits plugin) {
        super(plugin, "CreateKit");
        this.plugin = plugin;
    }

    @Override
    public boolean onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = plugin.getMessages();
        if (player.hasPermission("harimelt.create.kit")) {
            if (arguments.length > 0) {
                KitManager kitManager = plugin.getKitManager();
                String kitName = arguments[0].toLowerCase();
                if (!kitManager.existKit(kitName)) {
                    int claimTime = 0;
                    if (arguments.length > 1) {
                        try {
                            claimTime = Integer.parseInt(arguments[1]);
                            if (claimTime < 0) {
                                claimTime = 0;
                                messages.sendMessage(player, "CreateKit.claimTimeIsNegative");
                            }
                        } catch (NumberFormatException ignored) {
                            messages.sendMessage(player, "CreateKit.claimTimeContainIllegalChars");
                        }
                    }
                    Kit kit = new Kit(kitName);
                    List<ItemStack> items = new ArrayList<>();
                    Collections.addAll(items, player.getInventory().getContents());
                    kit.setClaimTime(claimTime);
                    kit.setItems(items);
                    kitManager.addKit(kit);
                    kitManager.saveKit(kitName);
                    messages.sendMessage(player, "CreateKit.kitCreated", new String[][] {{"%kit.name%", kitName}});
                } else {
                    messages.sendMessage(player, "CreateKit.kitAlreadyExist", new String[][] {{"%kit.name%", kitName}});
                }
            } else {
                messages.sendMessage(player, "CreateKit.kitNameEmpty");
            }
        } else {
            messages.sendMessage(player, "CreateKit.noPermission");
        }
        return true;
    }

    @Override
    public boolean onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = plugin.getMessages();
        messages.sendMessage(console, "CreateKit.isConsole");
        return true;
    }

}