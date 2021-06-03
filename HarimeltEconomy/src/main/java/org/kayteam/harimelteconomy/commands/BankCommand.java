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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.kayteam.harimelteconomy.HarimeltEconomy;
import org.kayteam.harimelteconomy.utils.yaml.Yaml;

import java.util.ArrayList;
import java.util.List;

public class BankCommand implements CommandExecutor, TabCompleter {

    private final HarimeltEconomy harimeltEconomy;

    public BankCommand(HarimeltEconomy harimeltEconomy) {
        this.harimeltEconomy = harimeltEconomy;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Yaml messages = harimeltEconomy.getMessages();
        String path = "bank.";
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length > 0) {
                switch (strings[0].toLowerCase()) {
                    case "information":
                        information(player);
                        break;
                    case "deposit":
                        deposit(player, strings);
                        break;
                    case "withdraw":
                        withdraw(player, strings);
                        break;
                    case "help":
                        messages.sendMessage(player, path + "help", true);
                        break;
                    default:
                        messages.sendMessage(player, path + "invalidOption", new String[][] {{"%option%", strings[0]}}, true);
                }
            } else {
                messages.sendMessage(player, path + "emptyOption", true);
            }
        } else {
            messages.sendMessage(commandSender, path + "isConsole", true);
        }
        return true;
    }

    private void information(Player player) {
        Yaml messages = harimeltEconomy.getMessages();
        String path = "bank.";
        Economy economy = harimeltEconomy.getEconomy();
        messages.sendMessage(player, path + "information", new String[][] {
                {"%bank%", economy.getBalance(player, "bank") + ""}
        }, true);
    }

    private void deposit(Player player, String[] strings) {
        Yaml messages = harimeltEconomy.getMessages();
        String path = "bank.deposit.";
        if (strings.length > 1) {
            String amountString = strings[1];
            try {
                double amount = Double.parseDouble(amountString);
                Economy economy = harimeltEconomy.getEconomy();
                if (economy.getBalance(player) >= amount) {
                    economy.withdrawPlayer(player, "balance", amount);
                    economy.depositPlayer(player, "bank", amount);
                    messages.sendMessage(player, path + "amountDeposit", new String[][] {{"%amount%", amountString}, {"%bank%", economy.getBalance(player, "bank") + ""}}, true);
                } else {
                    messages.sendMessage(player, path + "noSufficientBalance", true);
                }
            } catch (NumberFormatException e) {
                messages.sendMessage(player, path + "invalidAmount", new String[][] {{"%amount%", amountString}}, true);
            }
        } else {
            messages.sendMessage(player, path + "emptyAmount", true);
        }
    }

    private void withdraw(Player player, String[] strings) {
        Yaml messages = harimeltEconomy.getMessages();
        String path = "bank.withdraw.";
        if (strings.length > 1) {
            String amountString = strings[1];
            try {
                double amount = Double.parseDouble(amountString);
                Economy economy = harimeltEconomy.getEconomy();
                if (economy.getBalance(player, "bank") >= amount) {
                    economy.withdrawPlayer(player, "bank", amount);
                    economy.depositPlayer(player, "balance", amount);
                    messages.sendMessage(player, path + "amountWithdraw", new String[][] {{"%amount%", amountString}, {"%bank%", economy.getBalance(player, "bank") + ""}}, true);
                } else {
                    messages.sendMessage(player, path + "noSufficientBalance", true);
                }
            } catch (NumberFormatException e) {
                messages.sendMessage(player, path + "invalidAmount", new String[][] {{"%amount%", amountString}}, true);
            }
        } else {
            messages.sendMessage(player, path + "emptyAmount", true);
        }
    }



    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>();
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("CreateAccount")) {

            } else if (strings[0].equalsIgnoreCase("Help")) {

            }
        } else {
            list.add("CreateAccount");
            list.add("Deposit");
            list.add("Withdraw");
            list.add("Help");
        }
        return list;
    }
}
