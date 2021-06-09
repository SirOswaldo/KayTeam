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

package org.kayteam.harimelt.tags.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kayteam.harimelt.tags.HarimeltTags;
import org.kayteam.harimelt.tags.inputs.PrefixInput;
import org.kayteam.harimelt.tags.inputs.RenameInput;
import org.kayteam.harimelt.tags.inputs.SuffixInput;
import org.kayteam.harimelt.tags.tag.Tag;
import org.kayteam.harimelt.tags.tag.TagManager;
import org.kayteam.harimelt.tags.tasks.OpenInventoryTask;
import org.kayteam.harimelt.tags.util.yaml.Yaml;

import java.util.List;
import java.util.UUID;

public class TagEditInventory implements Listener {

    private final HarimeltTags harimeltTags;
    private String title;

    public TagEditInventory(HarimeltTags harimeltTags) {
        this.harimeltTags = harimeltTags;
        Yaml configuration = harimeltTags.getConfiguration();
        title = ChatColor.translateAlternateColorCodes('&', (configuration.getString("editorInventory.title")));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Inventory getInventory(String name) {
        Yaml configuration = harimeltTags.getConfiguration();
        Inventory inventory = Bukkit.createInventory(null, 45, title);
        // Panels
        ItemStack panel = configuration.getItemStack("editorInventory.items.panel");
        for (int i = 0; i < 45; i++) {
            inventory.setItem(i, panel);
        }
        // Tag
        TagManager tagManager = harimeltTags.getTagManager();
        Tag tag = tagManager.get(name);
        // Prefix
        inventory.setItem(16, configuration.getItemStack("editorInventory.items.prefix"));
        // Suffix
        inventory.setItem(25, configuration.getItemStack("editorInventory.items.suffix"));
        // Rename
        inventory.setItem(34, configuration.getItemStack("editorInventory.items.rename"));
        // Information
        ItemStack information = configuration.getItemStack("editorInventory.items.information");
        ItemMeta informationMeta = information.getItemMeta();
        List<String> lore = informationMeta.getLore();
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            line = line.replaceAll("%name%", tag.getName());
            line = line.replaceAll("%prefix%", tag.getPrefix());
            line = line.replaceAll("%suffix%", tag.getSuffix());
            line = ChatColor.translateAlternateColorCodes('&', line);
            lore.set(i, line);
        }
        informationMeta.setLore(lore);
        information.setItemMeta(informationMeta);

        inventory.setItem(22, information);
        // Items
        inventory.setItem(19, configuration.getItemStack("editorInventory.items.close"));
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(title)) {
            event.setCancelled(true);
            Yaml messages = harimeltTags.getMessages();
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();
            String name = harimeltTags.getEditing().get(player.getUniqueId())[1];
            int slot = event.getSlot();
            switch (slot) {
                case 19: // Close
                    harimeltTags.getEditing().remove(uuid);
                    player.closeInventory();
                    messages.sendMessage(player, "EditTag.menuClose");
                    break;
                case 16: // Prefix
                    harimeltTags.getEditing().put(uuid, new String[] {"PREFIX", name});
                    player.closeInventory();
                    PrefixInput prefixInput = new PrefixInput(harimeltTags, player);
                    harimeltTags.getBookInputManager().addBookInput(prefixInput);
                    harimeltTags.getBookInputManager().addBookToPlayer(prefixInput);
                    messages.sendMessage(player, "EditTag.inputPrefix");
                    break;
                case 25: // Suffix
                    harimeltTags.getEditing().put(uuid, new String[] {"SUFFIX", name});
                    player.closeInventory();
                    SuffixInput suffixInput = new SuffixInput(harimeltTags, player);
                    harimeltTags.getBookInputManager().addBookInput(suffixInput);
                    harimeltTags.getBookInputManager().addBookToPlayer(suffixInput);
                    messages.sendMessage(player, "EditTag.suffixInput");
                    break;
                case 34: // Rename
                    harimeltTags.getEditing().put(uuid, new String[] {"RENAME", name});
                    player.closeInventory();
                    RenameInput renameInput = new RenameInput(harimeltTags, player);
                    harimeltTags.getBookInputManager().addBookInput(renameInput);
                    harimeltTags.getBookInputManager().addBookToPlayer(renameInput);
                    messages.sendMessage(player, "EditTag.inputRename");
                    break;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(title)) {
            Player player = (Player) event.getPlayer();
            UUID uuid = player.getUniqueId();
            if (harimeltTags.getEditing().containsKey(uuid)) {
                String status = harimeltTags.getEditing().get(uuid)[0];
                if (status.equals("MENU")) {
                    OpenInventoryTask openInventoryTask = new OpenInventoryTask(harimeltTags, player, event.getInventory());
                    openInventoryTask.startScheduler();
                }
            }
        }
    }

}