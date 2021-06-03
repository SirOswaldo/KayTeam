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
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.kayteam.harimelteconomy.HarimeltEconomy;
import org.kayteam.harimelteconomy.utils.yaml.Yaml;

import java.util.List;

public class EconomyCommand implements CommandExecutor, TabCompleter {

    private final HarimeltEconomy harimeltEconomy;

    public EconomyCommand(HarimeltEconomy harimeltEconomy) {
        this.harimeltEconomy = harimeltEconomy;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Yaml messages = harimeltEconomy.getMessages();
        String path = "economy.";
        if (commandSender.hasPermission("HarimeltEconomy.Economy")) {
            if (strings.length > 0) {
                String option = strings[0].toLowerCase();
                switch (option) {
                    case "help":
                        messages.sendMessage(commandSender, path + "help", new String[][] {{"%command%", command.getName()}},true);
                        break;
                    case "give":
                        give(commandSender, strings);
                        break;
                    case "take":
                        take(commandSender, strings);
                        break;
                    case "set":
                        set(commandSender, strings);
                        break;
                    default:
                        messages.sendMessage(commandSender, path + "invalidOption", new String[][] {{"%option%", option}},true);
                }
            } else {
                messages.sendMessage(commandSender, path + "emptyOption", true);
            }
        } else {
            messages.sendMessage(commandSender, path + "noPermission", true);
        }
        return true;
    }


    private void give(CommandSender commandSender, String[] strings) {
        Yaml messages = harimeltEconomy.getMessages();
        String path = "economy.give.";
        if (commandSender.hasPermission("HarimeltEconomy.Economy.Give")) {
            if (strings.length > 1) {
                if (strings.length > 2) {
                    String amountString = strings[2];
                    double amount;
                    String name = strings[1];
                    try {
                        amount = Double.parseDouble(amountString);
                        OfflinePlayer offlinePlayer = harimeltEconomy.getServer().getOfflinePlayer(name);
                        if (offlinePlayer != null) {
                            Economy economy = harimeltEconomy.getEconomy();
                            if (economy.hasAccount(offlinePlayer)) {
                                EconomyResponse economyResponse = economy.depositPlayer(offlinePlayer, amount);
                                if (economyResponse.transactionSuccess()) {
                                    messages.sendMessage(commandSender, path + "transactionSuccess", new String[][] {{"%amount%", amountString}, {"%player%", name}, {"%money%", economyResponse.balance+""}}, true);
                                } else {
                                    messages.sendMessage(commandSender, path + "transactionFailed", new String[][] {{"%error%", economyResponse.errorMessage}}, true);
                                }
                            } else {
                                messages.sendMessage(commandSender, path + "noAccountPlayer", new String[][] {{"%player%", name}}, true);
                            }
                        } else {
                            messages.sendMessage(commandSender, path + "invalidPlayer", new String[][] {{"%player%", name}}, true);
                        }
                    } catch (NumberFormatException exception) {
                        messages.sendMessage(commandSender, path + "invalidAmount", new String[][] {{"%amount%", amountString}}, true);
                    }
                } else {
                    messages.sendMessage(commandSender, path + "emptyAmount", true);
                }
            } else {
                messages.sendMessage(commandSender, path + "emptyPlayer", true);
            }
        } else {
            messages.sendMessage(commandSender, path + "noPermission", true);
        }
    }
    private void take(CommandSender commandSender, String[] strings) {
        Yaml messages = harimeltEconomy.getMessages();
        String path = "economy.take.";
        if (commandSender.hasPermission("HarimeltEconomy.Economy.Take")) {
            if (strings.length > 1) {
                if (strings.length > 2) {
                    String amountString = strings[2];
                    double amount;
                    String name = strings[1];
                    try {
                        amount = Double.parseDouble(amountString);
                        OfflinePlayer offlinePlayer = harimeltEconomy.getServer().getOfflinePlayer(name);
                        if (offlinePlayer != null) {
                            Economy economy = harimeltEconomy.getEconomy();
                            if (economy.hasAccount(offlinePlayer)) {
                                EconomyResponse economyResponse = economy.withdrawPlayer(offlinePlayer, amount);
                                if (economyResponse.transactionSuccess()) {
                                    messages.sendMessage(commandSender, path + "transactionSuccess", new String[][] {{"%amount%", amountString}, {"%player%", name}, {"%money%", economyResponse.balance + ""}}, true);
                                } else {
                                    messages.sendMessage(commandSender, path + "transactionFailed", new String[][] {{"%error%", economyResponse.errorMessage}}, true);
                                }
                            } else {
                                messages.sendMessage(commandSender, path + "noAccountPlayer", new String[][] {{"%player%", name}}, true);
                            }
                        } else {
                            messages.sendMessage(commandSender, path + "invalidPlayer", new String[][] {{"%player%", name}}, true);
                        }
                    } catch (NumberFormatException exception) {
                        messages.sendMessage(commandSender, path + "invalidAmount", new String[][] {{"%amount%", amountString}}, true);
                    }
                } else {
                    messages.sendMessage(commandSender, path + "emptyAmount", true);
                }
            } else {
                messages.sendMessage(commandSender, path + "emptyPlayer", true);
            }
        } else {
            messages.sendMessage(commandSender, path + "noPermission", true);
        }
    }
    private void set(CommandSender commandSender, String[] strings) {
        Yaml messages = harimeltEconomy.getMessages();
        String path = "economy.set.";
        if (commandSender.hasPermission("HarimeltEconomy.Economy.Set")) {
            if (strings.length > 1) {
                if (strings.length > 2) {
                    String amountString = strings[2];
                    double amount;
                    String name = strings[1];
                    try {
                        amount = Double.parseDouble(amountString);
                        OfflinePlayer offlinePlayer = harimeltEconomy.getServer().getOfflinePlayer(name);
                        if (offlinePlayer != null) {
                            Economy economy = harimeltEconomy.getEconomy();
                            if (economy.hasAccount(offlinePlayer)) {
                                double money = economy.getBalance(offlinePlayer);
                                economy.withdrawPlayer(offlinePlayer, money);
                                EconomyResponse economyResponse = economy.depositPlayer(offlinePlayer, amount);
                                if (economyResponse.transactionSuccess()) {
                                    messages.sendMessage(commandSender, path + "transactionSuccess", new String[][] {{"%amount%", amountString}, {"%player%", name}}, true);
                                } else {
                                    messages.sendMessage(commandSender, path + "transactionFailed", new String[][] {{"%error%", economyResponse.errorMessage}}, true);
                                }
                            } else {
                                messages.sendMessage(commandSender, path + "noAccountPlayer", new String[][] {{"%player%", name}}, true);
                            }
                        } else {
                            messages.sendMessage(commandSender, path + "invalidPlayer", new String[][] {{"%player%", name}}, true);
                        }
                    } catch (NumberFormatException exception) {
                        messages.sendMessage(commandSender, path + "invalidAmount", new String[][] {{"%amount%", amountString}}, true);
                    }
                } else {
                    messages.sendMessage(commandSender, path + "emptyAmount", true);
                }
            } else {
                messages.sendMessage(commandSender, path + "emptyPlayer", true);
            }
        } else {
            messages.sendMessage(commandSender, path + "noPermission", true);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}