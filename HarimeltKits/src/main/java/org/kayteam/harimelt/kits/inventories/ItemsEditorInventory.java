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
import org.kayteam.harimelt.kits.utils.itemstack.ItemStackUtil;

import java.util.ArrayList;
import java.util.List;

public class ItemsEditorInventory implements Listener {

    private final HarimeltKits harimeltKits;
    private String title;
    private String panel;
    private String cancel;
    private String save;

    public ItemsEditorInventory(HarimeltKits harimeltKits) {
        this.harimeltKits = harimeltKits;
        FileConfiguration configuration = harimeltKits.getConfiguration().getFileConfiguration();
        title = ChatColor.translateAlternateColorCodes('&', configuration.getString("inventory.kitEditor.title", ""));
        panel = configuration.getString("inventory.itemsEditor.items.panel");
        cancel = configuration.getString("inventory.itemsEditor.items.cancel");
        save = configuration.getString("inventory.itemsEditor.items.save");
    }


    public Inventory getInventory(String name) {
        Inventory inventory = Bukkit.createInventory(null, 45, title);
        for (int i=27; i < 54; i++) {
            inventory.setItem(i, ItemStackUtil.parseString(panel));
        }
        // Items
        Kit kit = harimeltKits.getKitManager().getKit(name);
        if (!kit.getItems().isEmpty()) {
            for (int i = 0; i < 27; i++) {
                if (kit.getItems().size() > i) {
                    inventory.setItem(i, kit.getItems().get(i));
                }
            }
        }
        // Buttons
        inventory.setItem(37, ItemStackUtil.parseString(cancel));
        inventory.setItem(43, ItemStackUtil.parseString(save));
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(title)) {
            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();
            String name = harimeltKits.getEditing().get(player.getUniqueId()).split(":")[1];
            if (slot > 26 && slot < 54) {
                event.setCancelled(true);
                if (slot == 37) {
                    harimeltKits.getEditing().put(player.getUniqueId(), "MENU:" + name);
                    player.closeInventory();
                    MenuEditorInventory menuEditorInventory = new MenuEditorInventory(harimeltKits);
                    player.openInventory(menuEditorInventory.getInventory(name));
                } else if (slot == 43) {
                    List<ItemStack> items = new ArrayList<>();
                    for (int i = 0; i < 27; i++) {
                        ItemStack itemStack = event.getInventory().getItem(i);
                        if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                            items.add(itemStack);
                        }
                    }
                    Kit kit = harimeltKits.getKitManager().getKit(name);
                    kit.setItems(items);
                    harimeltKits.getKitManager().saveKit(kit.getName());
                    harimeltKits.getEditing().put(player.getUniqueId(), "MENU:" + name);
                    player.closeInventory();
                    MenuEditorInventory menuEditorInventory = new MenuEditorInventory(harimeltKits);
                    player.openInventory(menuEditorInventory.getInventory(name));
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(title)) {
            Player player = (Player) event.getPlayer();
            String status = harimeltKits.getEditing().get(player.getUniqueId()).split(":")[0];
            if (status.equals("ITEMS")) {
                String name = harimeltKits.getEditing().get(player.getUniqueId()).split(":")[1];
                OpenInventoryTask openInventoryTask = new OpenInventoryTask(harimeltKits, player, event.getInventory());
                openInventoryTask.startScheduler();
            }
        }
    }

}