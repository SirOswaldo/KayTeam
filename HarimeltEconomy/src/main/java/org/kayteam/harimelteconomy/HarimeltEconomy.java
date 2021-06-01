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

package org.kayteam.harimelteconomy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.harimelteconomy.commands.BalanceCommand;
import org.kayteam.harimelteconomy.commands.MoneyCommand;
import org.kayteam.harimelteconomy.economy.YamlEconomy;
import org.kayteam.harimelteconomy.listeners.PlayerJoinListener;
import org.kayteam.harimelteconomy.utils.yaml.Yaml;

public class HarimeltEconomy extends JavaPlugin {

    // Files
    private final Yaml configuration = new Yaml(this, "configuration");
    public Yaml getConfiguration() {
        return configuration;
    }
    private final Yaml messages = new Yaml(this, "messages");
    public Yaml getMessages() {
        return messages;
    }

    // Economy
    private Economy economy;
    public Economy getEconomy() {
        return economy;
    }

    @Override
    public void onEnable() {
        // Register Yaml Files
        configuration.registerFileConfiguration();
        messages.registerFileConfiguration();
        // Setup Vault
        economy = new YamlEconomy(this);
        if (!setupEconomy()) {
            getLogger().info("This plugin need Vault to found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Vault found, Economy has been registered.");
        // Register Commands
        // Commands
        MoneyCommand moneyCommand = new MoneyCommand(this);
        getCommand("SimpleEconomy").setExecutor(moneyCommand);
        getCommand("SimpleEconomy").setTabCompleter(moneyCommand);
        BalanceCommand balanceCommand = new BalanceCommand(this);
        getCommand("Balance").setExecutor(balanceCommand);
        getCommand("Balance").setTabCompleter(balanceCommand);
        // Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    @Override
    public void onDisable() {

    }

    private boolean setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        this.getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, economy, this,
                ServicePriority.Highest);
        return true;
    }

}