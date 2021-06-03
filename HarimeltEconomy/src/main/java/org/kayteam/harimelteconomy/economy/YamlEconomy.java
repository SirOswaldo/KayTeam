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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class YamlEconomy implements Economy {

    private final HarimeltEconomy harimeltEconomy;

    private final Yaml bank;

    public YamlEconomy(HarimeltEconomy harimeltEconomy) {
        this.harimeltEconomy = harimeltEconomy;
        bank = new Yaml(harimeltEconomy, "bank");
        bank.registerFileConfiguration();
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
    public boolean hasAccount(String name) {
        Yaml yaml = new Yaml(harimeltEconomy, "players", name);
        return yaml.existFileConfiguration();
    }
    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return hasAccount(offlinePlayer.getName());
    }
    public boolean hasAccount(UUID uuid) {
        return hasAccount(harimeltEconomy.getServer().getOfflinePlayer(uuid));
    }
    @Override
    public boolean hasAccount(String name, String type) {
        if (type.equals("bank")) {
            return bank.getFileConfiguration().contains("members." + name);
        } else {
            return hasAccount(name);
        }
    }
    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String type) {
        return hasAccount(offlinePlayer.getName(), type);
    }
    public boolean hasAccount(UUID uuid, String type) {
        return hasAccount(harimeltEconomy.getServer().getOfflinePlayer(uuid), type);
    }



    @Override
    public double getBalance(String name) {
        Yaml yaml = new Yaml(harimeltEconomy, "players", name);
        yaml.registerFileConfiguration();
        FileConfiguration fileConfiguration = yaml.getFileConfiguration();
        return fileConfiguration.getDouble("balance");
    }
    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return getBalance(offlinePlayer.getName());
    }
    public double getBalance(UUID uuid) {
        return getBalance(harimeltEconomy.getServer().getOfflinePlayer(uuid));
    }
    @Override
    public double getBalance(String name, String type) {
        if (type.equals("bank")) {
            return bankBalance(name).balance;
        }
        return getBalance(name);
    }
    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String type) {
        return getBalance(offlinePlayer.getName(), type);
    }
    public double getBalance(UUID uuid, String type) {
        return getBalance(harimeltEconomy.getServer().getOfflinePlayer(uuid), type);
    }



    @Override
    public EconomyResponse withdrawPlayer(String name, double amount) {
        Yaml yaml = new Yaml(harimeltEconomy, "players", name);
        yaml.registerFileConfiguration();
        FileConfiguration fileConfiguration = yaml.getFileConfiguration();
        double balance = fileConfiguration.getDouble("balance");
        balance = balance - amount;
        fileConfiguration.set("balance", balance);
        yaml.saveFileConfiguration();
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "NONE");
    }
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        return withdrawPlayer(offlinePlayer.getName(), amount);
    }
    public EconomyResponse withdrawPlayer(UUID uuid, double amount) {
        return withdrawPlayer(harimeltEconomy.getServer().getOfflinePlayer(uuid), amount);
    }
    @Override
    public EconomyResponse withdrawPlayer(String name, String type, double amount) {
        if (type.equals("bank")) {
            return bankWithdraw(name, amount);
        }
        return withdrawPlayer(name, amount);
    }
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String type, double amount) {
        return withdrawPlayer(offlinePlayer.getName(), type,amount);
    }
    public EconomyResponse withdrawPlayer(UUID uuid, String type, double amount) {
        return withdrawPlayer(harimeltEconomy.getServer().getOfflinePlayer(uuid), type, amount);
    }


    @Override
    public EconomyResponse depositPlayer(String name, double amount) {
        Yaml yaml = new Yaml(harimeltEconomy, "players", name);
        yaml.registerFileConfiguration();
        FileConfiguration fileConfiguration = yaml.getFileConfiguration();
        double balance = fileConfiguration.getDouble("balance");
        balance = balance + amount;
        fileConfiguration.set("balance", balance);
        yaml.saveFileConfiguration();
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "NONE");
    }
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        return depositPlayer(offlinePlayer.getName(), amount);
    }
    public EconomyResponse depositPlayer(UUID uuid, double amount) {
        return depositPlayer(harimeltEconomy.getServer().getOfflinePlayer(uuid), amount);
    }
    @Override
    public EconomyResponse depositPlayer(String name, String type, double amount) {
        if (type.equals("bank")) {
            return bankDeposit(name, amount);
        }
        return depositPlayer(name, amount);
    }
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String type, double amount) {
        return depositPlayer(offlinePlayer.getName(), type, amount);
    }
    public EconomyResponse depositPlayer(UUID uuid, String type, double amount) {
        return depositPlayer(harimeltEconomy.getServer().getOfflinePlayer(uuid), type, amount);
    }



    @Override
    public EconomyResponse createBank(String bank, String name) {
        FileConfiguration fileConfiguration = this.bank.getFileConfiguration();
        fileConfiguration.set("members." + name, 0);
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, "");
    }
    @Override
    public EconomyResponse createBank(String bank, OfflinePlayer offlinePlayer) {
        return createBank(bank, offlinePlayer.getName());
    }
    public EconomyResponse createBank(String bank, UUID uuid) {
        return createBank(bank, harimeltEconomy.getServer().getOfflinePlayer(uuid));
    }


    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }


    @Override
    public EconomyResponse bankBalance(String name) {
        double balance = bank.getFileConfiguration().getDouble("members." + name);
        return new EconomyResponse(0, balance, EconomyResponse.ResponseType.SUCCESS, "");
    }
    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }
    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        FileConfiguration fileConfiguration = bank.getFileConfiguration();
        double balance = fileConfiguration.getDouble("members." + name);
        balance = balance - amount;
        fileConfiguration.set("members." + name, balance);
        bank.saveFileConfiguration();
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "NONE");
    }
    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        FileConfiguration fileConfiguration = bank.getFileConfiguration();
        double balance = fileConfiguration.getDouble("members." + name);
        balance = balance + amount;
        fileConfiguration.set("members." + name, balance);
        bank.saveFileConfiguration();
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "NONE");
    }
    @Override
    public EconomyResponse isBankOwner(String bank, String name) {
        return null;
    }
    @Override
    public EconomyResponse isBankOwner(String bank, OfflinePlayer offlinePlayer) {
        return null;
    }
    @Override
    public EconomyResponse isBankMember(String bank, String name) {
        if (this.bank.getFileConfiguration().contains("members." + name)) {
            return new EconomyResponse(0,bankBalance(name).balance, EconomyResponse.ResponseType.SUCCESS, "");
        }
        return new EconomyResponse(0,0, EconomyResponse.ResponseType.FAILURE, "No Account in Bank.");
    }
    @Override
    public EconomyResponse isBankMember(String bank, OfflinePlayer offlinePlayer) {
        return isBankMember(bank, offlinePlayer.getName());
    }
    public EconomyResponse isBankMember(String bank, UUID uuid) {
        return isBankMember(bank, harimeltEconomy.getServer().getOfflinePlayer(uuid));
    }


    @Override
    public List<String> getBanks() {
        List<String> banks = new ArrayList<>();
        banks.add(getName());
        return banks;
    }


    @Override
    public boolean createPlayerAccount(String name) {
        Yaml yaml = new Yaml(harimeltEconomy, "players", name);
        yaml.registerFileConfiguration();
        Yaml configuration = harimeltEconomy.getConfiguration();
        harimeltEconomy.getLogger().info("Creando cuenta");
        yaml.getFileConfiguration().set("balance", configuration.getFileConfiguration().getDouble("startBalance"));
        yaml.saveFileConfiguration();
        return true;
    }
    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return createPlayerAccount(offlinePlayer.getName());
    }
    public boolean createPlayerAccount(UUID uuid) {
        createPlayerAccount(harimeltEconomy.getServer().getOfflinePlayer(uuid));
        return true;
    }
    @Override
    public boolean createPlayerAccount(String name, String type) {
        if (type.equals("bank")) {
            return createBank("", name).transactionSuccess();
        }
        return createPlayerAccount(name);
    }
    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String type) {
        return createPlayerAccount(offlinePlayer.getName(), type);
    }
    public boolean createPlayerAccount(UUID uuid, String type) {
        return createPlayerAccount(harimeltEconomy.getServer().getOfflinePlayer(uuid), type);
    }


}