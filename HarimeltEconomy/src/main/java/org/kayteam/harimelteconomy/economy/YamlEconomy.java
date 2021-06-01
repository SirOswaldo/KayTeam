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

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.kayteam.harimelteconomy.HarimeltEconomy;
import org.kayteam.harimelteconomy.utils.yaml.Yaml;

import java.util.List;

public class YamlEconomy implements Economy {

    private final HarimeltEconomy harimeltEconomy;

    public YamlEconomy(HarimeltEconomy harimeltEconomy) {
        this.harimeltEconomy = harimeltEconomy;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "HarimeltEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return true;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return v+"";
    }

    @Override
    public String currencyNamePlural() {
        return "null";
    }

    @Override
    public String currencyNameSingular() {
        return "null";
    }

    @Override
    public boolean hasAccount(String name) {
        return account(name);
    }
    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        String name = offlinePlayer.getName();
        return account(name);
    }
    @Override
    public boolean hasAccount(String name, String type) {
        return account(name);
    }
    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String type) {
        String name = offlinePlayer.getName();
        return account(name);
    }
    private boolean account(String name) {
        Yaml yaml = new Yaml(harimeltEconomy, "players", name);
        return yaml.existFileConfiguration();
    }


    @Override
    public double getBalance(String name) {
        return balance(name);
    }
    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        String name = offlinePlayer.getName();
        return balance(name);
    }
    @Override
    public double getBalance(String name, String type) {
        return balance(name);
    }
    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String type) {
        String name = offlinePlayer.getName();
        return balance(name);
    }
    private double balance(String name) {
        Yaml yaml = new Yaml(harimeltEconomy, "players", name);
        yaml.registerFileConfiguration();
        FileConfiguration fileConfiguration = yaml.getFileConfiguration();
        return fileConfiguration.getDouble("money", 0);
    }


    @Override
    public boolean has(String s, double v) {
        return false;
    }
    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return false;
    }
    @Override
    public boolean has(String s, String s1, double v) {
        return false;
    }
    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return false;
    }


    @Override
    public EconomyResponse withdrawPlayer(String name, double amount) {
        return withdraw(name, amount);
    }
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        String name = offlinePlayer.getName();
        return withdraw(name, amount);
    }
    @Override
    public EconomyResponse withdrawPlayer(String name, String s1, double amount) {
        return withdraw(name, amount);
    }
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double amount) {
        String name = offlinePlayer.getName();
        return withdraw(name, amount);
    }
    private EconomyResponse withdraw(String name, double amount) {
        Yaml yaml = new Yaml(harimeltEconomy, "players", name);
        yaml.registerFileConfiguration();
        FileConfiguration fileConfiguration = yaml.getFileConfiguration();
        double money = fileConfiguration.getDouble("money");
        money = money - amount;
        fileConfiguration.set("money", money);
        yaml.saveFileConfiguration();
        return new EconomyResponse(amount, money, EconomyResponse.ResponseType.SUCCESS, "NONE");
    }

    @Override
    public EconomyResponse depositPlayer(String name, double amount) {
        return deposit(name, amount);
    }
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        String name = offlinePlayer.getName();
        return deposit(name, amount);
    }
    @Override
    public EconomyResponse depositPlayer(String name, String type, double amount) {
        return deposit(name, amount);
    }
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String type, double amount) {
        String name = offlinePlayer.getName();
        return deposit(name, amount);
    }
    private EconomyResponse deposit(String name, double amount) {
        Yaml yaml = new Yaml(harimeltEconomy, "players", name);
        yaml.registerFileConfiguration();
        FileConfiguration fileConfiguration = yaml.getFileConfiguration();
        double money = fileConfiguration.getDouble("money");
        money = money + amount;
        fileConfiguration.set("money", money);
        yaml.saveFileConfiguration();
        return new EconomyResponse(amount, money, EconomyResponse.ResponseType.SUCCESS, "NONE");
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }
    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }
    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }
    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }
    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }
    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }
    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }
    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }
    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }
    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }
    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }
    @Override
    public List<String> getBanks() {
        return null;
    }


    @Override
    public boolean createPlayerAccount(String name) {
        create(name);
        return true;
    }
    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        String name = offlinePlayer.getName();
        create(name);
        return true;
    }
    @Override
    public boolean createPlayerAccount(String name, String type) {
        create(name);
        return true;
    }
    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String type) {
        String name = offlinePlayer.getName();
        create(name);
        return true;
    }
    private void create(String name) {
        Yaml yaml = new Yaml(harimeltEconomy, "players", name);
        yaml.registerFileConfiguration();
        FileConfiguration playerFileConfiguration = yaml.getFileConfiguration();
        // Get Default Values
        Yaml configuration = harimeltEconomy.getConfiguration();
        FileConfiguration fileConfiguration = configuration.getFileConfiguration();
        double startBalance = fileConfiguration.getDouble("money.startBalance");
        // Set Start Balance
        playerFileConfiguration.set("money", startBalance);
        // Save
        yaml.saveFileConfiguration();
    }

}