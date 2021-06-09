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

package org.kayteam.harimelt.tags;

import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.harimelt.tags.commands.*;
import org.kayteam.harimelt.tags.listeners.AsyncPlayerPreLoginListener;
import org.kayteam.harimelt.tags.listeners.PlayerQuitListener;
import org.kayteam.harimelt.tags.placeholderapi.MoreTagsExpansion;
import org.kayteam.harimelt.tags.playerdata.PlayerDataManager;
import org.kayteam.harimelt.tags.storage.Storage;
import org.kayteam.harimelt.tags.storage.storages.MySqlStorage;
import org.kayteam.harimelt.tags.storage.storages.YamlStorage;
import org.kayteam.harimelt.tags.util.papi.PlaceholderAPIUtil;
import org.kayteam.harimelt.tags.inventories.TagEditInventory;
import org.kayteam.harimelt.tags.tag.TagManager;
import org.kayteam.harimelt.tags.util.input.BookInputManager;
import org.kayteam.harimelt.tags.util.yaml.Yaml;

import java.util.HashMap;
import java.util.UUID;

public class HarimeltTags extends JavaPlugin {

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

    private final TagManager tagManager = new TagManager(this);
    public TagManager getTagManager() {
        return tagManager;
    }

    private final HashMap<UUID, String[]> editing = new HashMap<>();
    public HashMap<UUID, String[]> getEditing() {
        return editing;
    }

    private BookInputManager bookInputManager;
    public BookInputManager getBookInputManager() {
        return bookInputManager;
    }

    @Override
    public void onEnable() {
        // Yaml
        configuration.registerFileConfiguration();
        messages.registerFileConfiguration();
        // Storage
        setupStorage();
        // Load All Tags
        tagManager.loadAll();
        // PlaceholderAPI
        PlaceholderAPIUtil.registerExpansion(new MoreTagsExpansion(this));
        // Commands
        HarimeltTagsCommand harimeltTagsCommand = new HarimeltTagsCommand(this);
        CreateTagCommand createTagCommand = new CreateTagCommand(this);
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(this);
        EditTagCommand editTagCommand = new EditTagCommand(this);
        ListTagsCommand listTagsCommand = new ListTagsCommand(this);
        SelectTagCommand selectTagCommand = new SelectTagCommand(this);
        // Listeners
        getServer().getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new TagEditInventory(this), this);
        // PlayerData
        playerDataManager = new PlayerDataManager(this);
        // Load Online Players PlayerData
        playerDataManager.loadOnlinePlayerData();
        // InputManagert
        bookInputManager =  new BookInputManager(this);

    }

    @Override
    public void onDisable() {
        // Unload Online Players PlayerData
        playerDataManager.unloadOnlinePlayerData();
        // Load All Tags
        tagManager.unloadAll();
    }

    public void setupStorage() {
        if ("MYSQL".equals(configuration.getFileConfiguration().getString("storage"))) {
            storage = new MySqlStorage(this);
        } else {
            storage = new YamlStorage(this);
        }
    }

}
