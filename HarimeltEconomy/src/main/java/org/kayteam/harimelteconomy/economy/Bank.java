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

package org.kayteam.harimelteconomy.economy;

import org.bukkit.OfflinePlayer;
import org.kayteam.harimelteconomy.HarimeltEconomy;
import org.kayteam.harimelteconomy.utils.yaml.Yaml;

import java.util.List;

public class Bank {

    private final HarimeltEconomy harimeltEconomy;
    private final String name;
    public Bank(HarimeltEconomy harimeltEconomy, String name) {
        this.harimeltEconomy = harimeltEconomy;
        this.name = name;
        bank = new Yaml(harimeltEconomy, "banks", name);
        bank.registerFileConfiguration();
    }

    public String getName() {
        return name;
    }

    private Yaml bank;

    public boolean hasAccount(String name) {
        return bank.getFileConfiguration().contains("members." + name);
    }
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return hasAccount(offlinePlayer.getName());
    }

    public void createAccount(String name) {
        bank.getFileConfiguration().set("members." + name, 0.0);
        bank.saveFileConfiguration();
    }
    public void createAccount(OfflinePlayer offlinePlayer) {
        createAccount(offlinePlayer.getName());
    }

    public double getBalance(String name) {
        return bank.getFileConfiguration().getDouble("members." + name);
    }
    public double getBalance(OfflinePlayer offlinePlayer) {
        return getBalance(offlinePlayer.getName());
    }

    public boolean setBalance(String name, double amount) {
        bank.getFileConfiguration().set("members." + name, amount);
        bank.saveFileConfiguration();
        return getBalance(name) == amount;
    }
    public boolean setBalance(OfflinePlayer offlinePlayer, double amount) {
        return setBalance(offlinePlayer.getName(), amount);
    }

    public boolean deposit(String name, double amount) {
        double balance = getBalance(name);
        double total = balance + amount;
        setBalance(name, total);
        return getBalance(name) == total;
    }
    public boolean deposit(OfflinePlayer offlinePlayer, double amount) {
        return deposit(offlinePlayer.getName(), amount);
    }

    public boolean withdraw(String name, double amount) {
        double balance = getBalance(name);
        if (balance >= amount) {
               setBalance(name, balance - amount);
               harimeltEconomy.getEconomy().depositPlayer(name, amount);
               return true;
        }
        return false;
    }
    public boolean withdraw(OfflinePlayer offlinePlayer, double amount) {
        return withdraw(offlinePlayer.getName(), amount);
    }

}
