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
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.tasks.OpenInventoryTask;
import org.kayteam.harimelt.kits.utils.itemstack.ItemStackUtil;

import java.util.Objects;

public class MenuEditorInventory implements Listener {

    private final HarimeltKits harimeltKits;
    private String title;
    private String panel;
    private String close;
    private String claimTime;
    private String items;

    public MenuEditorInventory(HarimeltKits harimeltKits) {
        this.harimeltKits = harimeltKits;
        FileConfiguration configuration = harimeltKits.getConfiguration().getFileConfiguration();
        title = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(configuration.getString("inventory.kitEditor.title")));
        panel = configuration.getString("inventory.kitEditor.panel");
        close = configuration.getString("inventory.kitEditor.close");
        claimTime = configuration.getString("inventory.kitEditor.claim-time");
        items = configuration.getString("inventory.kitEditor.items");
    }

    public Inventory getInventory(String name) {
        Inventory inventory = Bukkit.createInventory(null, 45, title);
        // Panels
        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, ItemStackUtil.parseString(panel));
        }
        // Buttons
        inventory.setItem(10, ItemStackUtil.parseString(claimTime));
        inventory.setItem(11, ItemStackUtil.parseString(items));
        inventory.setItem(40, ItemStackUtil.parseString(close));
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(title)) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();
            String name = harimeltKits.getEditing().get(player.getUniqueId()).split(":")[1];
            switch (slot) {
                case 40:
                    harimeltKits.getEditing().remove(player.getUniqueId());
                    player.closeInventory();
                    harimeltKits.getMessages().sendMessage(player, "EditKit.menuClose", new String[][] {{"%name%", name}});
                    break;
                case 10:
                    harimeltKits.getEditing().put(player.getUniqueId(), "CLAIM-TIME:" + name);
                    player.closeInventory();
                    harimeltKits.getMessages().sendMessage(player, "EditKit.inputWaitTime");
                    break;
                case 11:
                    harimeltKits.getEditing().put(player.getUniqueId(), "ITEMS:" + name);
                    player.closeInventory();
                    ItemsEditorInventory itemsEditorInventory = new ItemsEditorInventory(harimeltKits);
                    player.openInventory(itemsEditorInventory.getInventory(name));
                    break;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(title)) {
            Player player = (Player) event.getPlayer();
            String status = harimeltKits.getEditing().get(player.getUniqueId()).split(":")[0];
            if (status.equals("MENU")) {
                OpenInventoryTask openInventoryTask = new OpenInventoryTask(harimeltKits, player, event.getInventory());
                openInventoryTask.startScheduler();
            }
        }
    }

}
