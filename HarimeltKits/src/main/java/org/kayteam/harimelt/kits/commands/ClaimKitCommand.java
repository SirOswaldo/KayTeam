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

import java.util.Objects;

public class ClaimKitCommand extends SimpleCommand {

    private final HarimeltKits plugin;

    public ClaimKitCommand(HarimeltKits plugin) {
        super(plugin, "ClaimKit");
        this.plugin = plugin;
    }

    @Override
    public boolean onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = plugin.getMessages();
        if (player.hasPermission("harimelt.claim.kit")) {
            if (arguments.length > 0) {
                String kitName = arguments[0];
                KitManager kitManager = plugin.getKitManager();
                if (kitManager.existKit(kitName)) {
                    Kit kit = kitManager.getKit(kitName);
                    if (player.hasPermission("harimelt.claim.kit." + kitName)) {
                        Yaml data = new Yaml(plugin, "players.", player.getName());
                        data.registerFileConfiguration();
                        if (kit.getClaimTime() == 0) {
                            if (data.contains(kitName)) {
                                data.set(kitName, 0);
                                data.saveFileConfiguration();
                                for (ItemStack itemStack:kit.getItems()) {
                                    if (player.getInventory().firstEmpty() != -1) {
                                        player.getInventory().addItem(itemStack);
                                    } else {
                                        Objects.requireNonNull(player.getLocation().getWorld()).dropItem(player.getLocation(), itemStack);
                                    }
                                }
                                messages.sendMessage(player, "ClaimKit.kitClaimed", new String[][] {{"%kit.name%", kitName}});
                            } else {
                                messages.sendMessage(player, "ClaimKit.oneTimeClaimAlreadyTaken", new String[][] {{"%kit.name%", kitName}});
                            }
                        } else {
                            if (data.contains(kitName)) {
                                data.set(kitName, (int) (System.currentTimeMillis() / 1000));
                                data.saveFileConfiguration();
                                for (ItemStack itemStack:kit.getItems()) {
                                    if (player.getInventory().firstEmpty() != -1) {
                                        player.getInventory().addItem(itemStack);
                                    } else {
                                        Objects.requireNonNull(player.getLocation().getWorld()).dropItem(player.getLocation(), itemStack);
                                    }
                                }
                                messages.sendMessage(player, "ClaimKit.kitClaimed", new String[][] {{"%kit.name%", kitName}});
                            } else {
                                int claimTime = kit.getClaimTime();
                                int lastClaimTime = data.getInt(kitName);
                                int currentTime = (int) System.currentTimeMillis() / 1000;
                                if (currentTime - lastClaimTime >= claimTime) {
                                    data.set(kitName, (int) (System.currentTimeMillis() / 1000));
                                    data.saveFileConfiguration();
                                    for (ItemStack itemStack:kit.getItems()) {
                                        if (player.getInventory().firstEmpty() != -1) {
                                            player.getInventory().addItem(itemStack);
                                        } else {
                                            Objects.requireNonNull(player.getLocation().getWorld()).dropItem(player.getLocation(), itemStack);
                                        }
                                    }
                                    messages.sendMessage(player, "ClaimKit.kitClaimed", new String[][] {{"%kit.name%", kitName}});
                                } else {
                                    messages.sendMessage(player, "ClaimKit.needWaitToClaim", new String[][] {{"%kit.name%", kitName}, {"%seconds%", "" + (claimTime - (currentTime - lastClaimTime))}});
                                }
                            }
                        }
                    } else {
                        messages.sendMessage(player, "ClaimKit.noKitPermission", new String[][] {{"%kit.name%", kitName}});
                    }
                } else {
                    messages.sendMessage(player, "ClaimKit.kitNoExist", new String[][] {{"%kit.name%", kitName}});
                }
            } else {
                messages.sendMessage(player, "ClaimKit.kitNameEmpty");
            }
        } else {
            messages.sendMessage(player, "ClaimKit.noPermission");
        }
        return true;
    }

    @Override
    public boolean onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = plugin.getMessages();
        messages.sendMessage(console, "ClaimTime.isConsole");
        return true;
    }

}