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

package org.kayteam.harimelteconomy.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kayteam.harimelteconomy.HarimeltEconomy;
import org.kayteam.harimelteconomy.utils.yaml.Yaml;

import java.util.List;

public class PayCommand implements CommandExecutor, TabCompleter {

    private final HarimeltEconomy harimeltEconomy;

    public PayCommand(HarimeltEconomy harimeltEconomy) {
        this.harimeltEconomy = harimeltEconomy;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Yaml messages = harimeltEconomy.getMessages();
        String path = "pay.";
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("HarimeltEconomy.Pay")) {
                if (strings.length > 0) {
                    if (strings.length > 1) {
                        String name = strings[0];
                        String amountString = strings[1];
                        OfflinePlayer offlinePlayer = harimeltEconomy.getServer().getOfflinePlayer(name);
                        if (offlinePlayer != null) {
                            try {
                                Yaml configuration = harimeltEconomy.getConfiguration();
                                FileConfiguration fileConfiguration = configuration.getFileConfiguration();
                                double minimum = fileConfiguration.getDouble("minimumPayments");
                                double amount = Double.parseDouble(amountString);
                                if (amount >= minimum) {
                                    Economy economy = harimeltEconomy.getEconomy();
                                    if (economy.hasAccount(offlinePlayer)) {
                                        if (economy.getBalance(player) > amount) {
                                            economy.withdrawPlayer(player, amount);
                                            economy.depositPlayer(offlinePlayer, amount);
                                            messages.sendMessage(player, path + "paymentSuccessful", new String[][] {{"%player%", name}, {"%amount%", amountString}}, true);
                                            if (offlinePlayer.isOnline()) {
                                                messages.sendMessage(offlinePlayer.getPlayer(), path + "paymentSuccessfulNotify", new String[][] {{"%player%", player.getName()}, {"%amount%", amountString}}, true);
                                            }
                                        } else {
                                            messages.sendMessage(player, path + "noSufficientBalance", true);
                                        }
                                    } else {
                                        messages.sendMessage(player, path + "noAccountPlayer", new String[][] {{"%player%", name}}, true);
                                    }
                                } else {
                                    messages.sendMessage(player, path + "noMinimumPayment", new String[][] {{"%minimum%", minimum + ""}}, true);
                                }
                            } catch (NumberFormatException e) {
                                messages.sendMessage(player, path + "invalidAmount", new String[][] {{"%amount%", amountString}}, true);
                            }
                        } else {
                            messages.sendMessage(player, path + "invalidPlayer", new String[][] {{"%player%", name}}, true);
                        }
                    } else {
                        messages.sendMessage(player, path + "emptyAmount", true);
                    }
                } else {
                    messages.sendMessage(player, path + "emptyPlayer", true);
                }
            } else {
                messages.sendMessage(player, path + "noPermission", true);
            }
        } else {
            messages.sendMessage(commandSender, path + "isConsole", true);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
