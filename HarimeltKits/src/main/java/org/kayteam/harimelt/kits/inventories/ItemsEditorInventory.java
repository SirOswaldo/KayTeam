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

package org.kayteam.harimelt.kits.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.kit.Kit;
import org.kayteam.harimelt.kits.tasks.OpenInventoryTask;
import org.kayteam.harimelt.kits.utils.yaml.Yaml;

import java.util.ArrayList;
import java.util.List;

public class ItemsEditorInventory implements Listener {

    private final HarimeltKits plugin;
    private String title;

    public ItemsEditorInventory(HarimeltKits plugin) {
        this.plugin = plugin;
        FileConfiguration configuration = plugin.getConfiguration().getFileConfiguration();
        title = ChatColor.translateAlternateColorCodes('&', configuration.getString("inventory.itemsEditor.title", ""));
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Inventory getInventory(String name) {
        Yaml configuration = plugin.getConfiguration();
        Inventory inventory = Bukkit.createInventory(null, 54, title);
        // Panels
        ItemStack panel = configuration.getItemStack("inventory.itemsEditor.items.panel");
        for (int i = 27; i < 54; i++) {
            inventory.setItem(i, panel);
        }
        // Items
        Kit kit = plugin.getKitManager().getKit(name);
        if (!kit.getItems().isEmpty()) {
            for (int i = 0; i < 27; i++) {
                if (kit.getItems().size() > i) {
                    inventory.setItem(i, kit.getItems().get(i));
                }
            }
        }
        // Buttons
        inventory.setItem(37, configuration.getItemStack("inventory.itemsEditor.items.cancel"));
        inventory.setItem(43, configuration.getItemStack("inventory.itemsEditor.items.save"));
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(title)) {
            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();
            if (plugin.getEditing().containsKey(player.getUniqueId())) {
                String name = plugin.getEditing().get(player.getUniqueId()).split(":")[1];
                if (slot > 26 && slot < 54) {
                    event.setCancelled(true);
                    if (slot == 37) {
                        plugin.getEditing().put(player.getUniqueId(), "MENU:" + name);
                        player.closeInventory();
                        MenuEditorInventory menuEditorInventory = new MenuEditorInventory(plugin);
                        player.openInventory(menuEditorInventory.getInventory(name));
                    } else if (slot == 43) {
                        List<ItemStack> items = new ArrayList<>();
                        for (int i = 0; i < 27; i++) {
                            ItemStack itemStack = event.getInventory().getItem(i);
                            if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                                items.add(itemStack);
                            }
                        }
                        Kit kit = plugin.getKitManager().getKit(name);
                        kit.setItems(items);
                        plugin.getKitManager().saveKit(kit.getName());
                        plugin.getEditing().put(player.getUniqueId(), "MENU:" + name);
                        player.closeInventory();
                        MenuEditorInventory menuEditorInventory = new MenuEditorInventory(plugin);
                        player.openInventory(menuEditorInventory.getInventory(name));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(title)) {
            Player player = (Player) event.getPlayer();
            if (plugin.getEditing().containsKey(player.getUniqueId())) {
                String status = plugin.getEditing().get(player.getUniqueId()).split(":")[0];
                if (status.equals("ITEMS")) {
                    OpenInventoryTask openInventoryTask = new OpenInventoryTask(plugin, player, event.getInventory());
                    openInventoryTask.startScheduler();
                }
            }
        }
    }

}