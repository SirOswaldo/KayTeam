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
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.inventories.MenuEditorInventory;

public class PlayerToggleSneakListener implements Listener {

    private final HarimeltKits plugin;

    public PlayerToggleSneakListener(HarimeltKits plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (plugin.getEditing().containsKey(player.getUniqueId())) {
            String status = plugin.getEditing().get(player.getUniqueId()).split(":")[0];
            if (status.equals("CLAIM-TIME")) {
                String name = plugin.getEditing().get(player.getUniqueId()).split(":")[1];
                player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                MenuEditorInventory menuEditorInventory = new MenuEditorInventory(plugin);
                player.openInventory(menuEditorInventory.getInventory(name));
                plugin.getEditing().put(player.getUniqueId(), "MENU:" + name);
            }
        }
    }

}