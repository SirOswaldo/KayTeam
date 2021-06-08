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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.tasks.OpenInventoryTask;
import org.kayteam.harimelt.kits.utils.yaml.Yaml;

import java.util.Objects;

public class MenuEditorInventory implements Listener {

    private final HarimeltKits plugin;
    private String title;

    public MenuEditorInventory(HarimeltKits plugin) {
        this.plugin = plugin;
        FileConfiguration configuration = plugin.getConfiguration().getFileConfiguration();
        title = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(configuration.getString("inventory.kitEditor.title")));
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Inventory getInventory(String name) {
        Yaml configuration = plugin.getConfiguration();
        Inventory inventory = Bukkit.createInventory(null, 45, title);
        // Panels
        ItemStack panel = configuration.getItemStack("inventory.kitEditor.items.panel");
        for (int i = 0; i < 45; i++) {
            inventory.setItem(i, panel);
        }
        // Buttons
        inventory.setItem(10, configuration.getItemStack("inventory.kitEditor.items.claim-time"));
        inventory.setItem(11, configuration.getItemStack("inventory.kitEditor.items.items"));
        inventory.setItem(31, configuration.getItemStack("inventory.kitEditor.items.close"));
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(title)) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();
            String name = plugin.getEditing().get(player.getUniqueId()).split(":")[1];
            switch (slot) {
                case 31:
                    plugin.getEditing().remove(player.getUniqueId());
                    player.closeInventory();
                    plugin.getMessages().sendMessage(player, "EditKit.menuClose", new String[][] {{"%name%", name}});
                    break;
                case 10:
                    plugin.getEditing().put(player.getUniqueId(), "CLAIM-TIME:" + name);
                    player.closeInventory();
                    plugin.getMessages().sendMessage(player, "EditKit.inputWaitTime");
                    break;
                case 11:
                    plugin.getEditing().put(player.getUniqueId(), "ITEMS:" + name);
                    player.closeInventory();
                    ItemsEditorInventory itemsEditorInventory = new ItemsEditorInventory(plugin);
                    player.openInventory(itemsEditorInventory.getInventory(name));
                    break;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(title)) {
            Player player = (Player) event.getPlayer();
            if (plugin.getEditing().containsKey(player.getUniqueId())) {
                String status = plugin.getEditing().get(player.getUniqueId()).split(":")[0];
                if (status.equals("MENU")) {
                    OpenInventoryTask openInventoryTask = new OpenInventoryTask(plugin, player, event.getInventory());
                    openInventoryTask.startScheduler();
                }
            }
        }
    }

}