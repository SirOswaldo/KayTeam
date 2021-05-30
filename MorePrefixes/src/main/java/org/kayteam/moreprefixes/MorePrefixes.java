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

package org.kayteam.moreprefixes;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.moreprefixes.commands.MorePrefixesAdminCommand;
import org.kayteam.moreprefixes.commands.MorePrefixesCommand;
import org.kayteam.moreprefixes.listeners.AsyncPlayerPreLoginListener;
import org.kayteam.moreprefixes.listeners.PlayerQuitListener;
import org.kayteam.moreprefixes.placeholderapi.MorePrefixesExpansion;
import org.kayteam.moreprefixes.playerdata.PlayerDataManager;
import org.kayteam.moreprefixes.storage.Storage;
import org.kayteam.moreprefixes.storage.storages.MySqlStorage;
import org.kayteam.moreprefixes.storage.storages.YamlStorage;
import org.kayteam.moreprefixes.util.papi.PlaceholderAPIUtil;
import org.kayteam.moreprefixes.util.yaml.Yaml;

import java.util.UUID;

public class MorePrefixes extends JavaPlugin {

    private final Yaml configuration = new Yaml(this, "configuration");
    public Yaml getConfiguration() {
        return configuration;
    }
    private final Yaml messages = new Yaml(this, "messages");
    public Yaml getMessages() {
        return messages;
    }

    private PlayerDataManager playerDataManager;
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    private Storage storage;
    public Storage getStorage() {
        return storage;
    }

    @Override
    public void onEnable() {
        // Yaml
        configuration.registerFileConfiguration();
        messages.registerFileConfiguration();
        // Storage
        if ("MYSQL".equals(configuration.getFileConfiguration().getString("storage"))) {
            storage = new MySqlStorage(this);
        } else {
            storage = new YamlStorage(this);
        }
        // PlayerData
        playerDataManager = new PlayerDataManager(this);
        // PlaceholderAPI
        PlaceholderAPIUtil.registerExpansion(new MorePrefixesExpansion(this));
        // Commands
        getCommand("moreprefixesadmin").setExecutor(new MorePrefixesAdminCommand(this));
        getCommand("moreprefixes").setExecutor(new MorePrefixesCommand(this));
        // Listeners
        getServer().getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        // Load Online Players PlayerData
        for (Player player:getServer().getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            playerDataManager.load(uuid);
        }
    }

    @Override
    public void onDisable() {
        // Unload Online Players PlayerData
        for (Player player:getServer().getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            playerDataManager.unload(uuid);
        }
    }
}
