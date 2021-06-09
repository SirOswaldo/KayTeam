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

package org.kayteam.harimelt.kits;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.harimelt.kits.commands.*;
import org.kayteam.harimelt.kits.inventories.ItemsEditorInventory;
import org.kayteam.harimelt.kits.inventories.MenuEditorInventory;
import org.kayteam.harimelt.kits.kit.KitManager;
import org.kayteam.harimelt.kits.listeners.PlayerToggleSneakListener;
import org.kayteam.harimelt.kits.utils.yaml.Yaml;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class HarimeltKits extends JavaPlugin {

    // Files
    private final Yaml configuration = new Yaml(this, "configuration");
    public Yaml getConfiguration() {
        return configuration;
    }
    private final Yaml messages = new Yaml(this, "messages");
    public Yaml getMessages() {
        return messages;
    }

    // KitManager
    private final KitManager kitManager = new KitManager(this);
    public KitManager getKitManager() {
        return kitManager;
    }

    private final HashMap<UUID, String> editing = new HashMap<>();
    public HashMap<UUID, String> getEditing() {
        return editing;
    }

    @Override
    public void onEnable() {
        // Files
        configuration.registerFileConfiguration();
        messages.registerFileConfiguration();
        // Commands
        Objects.requireNonNull(getCommand("HarimeltKits")).setExecutor(new HarimeltKitsCommand(this));
        Objects.requireNonNull(getCommand("CreateKit")).setExecutor(new CreateKitCommand(this));
        Objects.requireNonNull(getCommand("DeleteKit")).setExecutor(new DeleteKitCommand(this));
        Objects.requireNonNull(getCommand("EditKit")).setExecutor(new EditKitCommand(this));
        Objects.requireNonNull(getCommand("ClaimKit")).setExecutor(new ClaimKitCommand(this));
        Objects.requireNonNull(getCommand("ListKit")).setExecutor(new ListKitCommand(this));
        // Listeners
        getServer().getPluginManager().registerEvents(new MenuEditorInventory(this), this);
        getServer().getPluginManager().registerEvents(new ItemsEditorInventory(this), this);
        getServer().getPluginManager().registerEvents(new PlayerToggleSneakListener(this), this);
        // KitManager
        kitManager.loadAllKits();
    }

    @Override
    public void onDisable() {
        kitManager.unloadAllKits();
    }


}
