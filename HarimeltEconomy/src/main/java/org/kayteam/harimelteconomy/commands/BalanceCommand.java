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
import org.bukkit.entity.Player;
import org.kayteam.harimelteconomy.HarimeltEconomy;
import org.kayteam.harimelteconomy.utils.yaml.Yaml;

import java.util.List;

public class BalanceCommand implements CommandExecutor, TabCompleter {

    private final HarimeltEconomy harimeltEconomy;
    public BalanceCommand(HarimeltEconomy harimeltEconomy) {
        this.harimeltEconomy = harimeltEconomy;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Yaml messages = harimeltEconomy.getMessages();
        String path = "balance.";
        if (commandSender instanceof Player) {
            if (strings.length > 0) {
                String name = strings[0];
                seeOtherBalance(commandSender, name);
            } else {
                seeOwnBalance(commandSender);
            }
        } else {
            if (strings.length > 0) {
                String name = strings[0];
                seeOtherBalance(commandSender, name);
            } else {
                messages.sendMessage(commandSender, path + "emptyPlayer", true);
            }
        }
        return true;
    }

    private void seeOtherBalance(CommandSender commandSender, String name) {
        Yaml messages = harimeltEconomy.getMessages();
        String path = "balance.";
        if (commandSender.hasPermission("SimpleEconomy.Balance.Others")) {
            OfflinePlayer offlinePlayer = harimeltEconomy.getServer().getOfflinePlayer(name);
            if (offlinePlayer != null) {
                Economy economy = harimeltEconomy.getEconomy();
                double balance = economy.getBalance(offlinePlayer);
                messages.sendMessage(commandSender, path + "seeOtherBalance", new String[][] {{"%player%", name}, {"%balance%", balance + ""}}, true);
            } else {
                messages.sendMessage(commandSender, path + "invalidPlayer", new String[][] {{"%player%", name}}, true);
            }
        } else {
            messages.sendMessage(commandSender, path + "noPermissionOthers", true);
        }
    }

    private void seeOwnBalance(CommandSender commandSender) {
        Yaml messages = harimeltEconomy.getMessages();
        String path = "balance.";
        if (commandSender.hasPermission("SimpleEconomy.Balance")) {
            Economy economy = harimeltEconomy.getEconomy();
            double balance = economy.getBalance(commandSender.getName());
            messages.sendMessage(commandSender, path + "seeOwnBalance", new String[][] {{"%balance%", balance + ""}}, true);
        } else {
            messages.sendMessage(commandSender, path + "noPermission", true);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
