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

package org.kayteam.harimelt.kits.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.inventories.MenuEditorInventory;
import org.kayteam.harimelt.kits.kit.Kit;
import org.kayteam.harimelt.kits.tasks.RenewBook;
import org.kayteam.harimelt.kits.utils.yaml.Yaml;

public class PlayerEditBookListener implements Listener {

    private final HarimeltKits plugin;

    public PlayerEditBookListener(HarimeltKits plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onAnvilClick(PlayerEditBookEvent event) {
        Player player = event.getPlayer();
        if (plugin.getEditing().containsKey(player.getUniqueId())) {
            String status = plugin.getEditing().get(player.getUniqueId()).split(":")[0];
            if (status.equals("CLAIM-TIME")) {
                String text = event.getNewBookMeta().getTitle();
                if (text != null) {
                    Yaml messages = plugin.getMessages();
                    try {
                        int claimTime = Integer.parseInt(text);
                        if (claimTime >= 0) {
                            String name = plugin.getEditing().get(player.getUniqueId()).split(":")[1];
                            Kit kit = plugin.getKitManager().getKit(name);
                            kit.setClaimTime(claimTime);
                            plugin.getKitManager().saveKit(name);
                            player.getInventory().getItemInOffHand().setAmount(0);
                            MenuEditorInventory menuEditorInventory = new MenuEditorInventory(plugin);
                            player.openInventory(menuEditorInventory.getInventory(name));
                            plugin.getEditing().put(player.getUniqueId(), "MENU:" + name);
                        } else {
                            RenewBook renewBook = new RenewBook(plugin, player);
                            renewBook.startScheduler();
                            messages.sendMessage(player, "EditKit.waitTimeIsNegative");
                        }
                    } catch (NumberFormatException e) {
                        RenewBook renewBook = new RenewBook(plugin, player);
                        renewBook.startScheduler();
                        if (text.contains(" ")) {
                            messages.sendMessage(player, "EditKit.waitTimeContainSpace");
                        } else {
                            messages.sendMessage(player, "EditKit.waitTimeContainChars");
                        }
                    }
                }
            }
        }
    }

}