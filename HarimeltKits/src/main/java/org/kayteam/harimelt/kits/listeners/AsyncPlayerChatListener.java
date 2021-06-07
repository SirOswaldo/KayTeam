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

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.inventories.MenuEditorInventory;
import org.kayteam.harimelt.kits.kit.Kit;

public class AsyncPlayerChatListener implements Listener {

    private final HarimeltKits harimeltKits;

    public AsyncPlayerChatListener(HarimeltKits harimeltKits) {
        this.harimeltKits = harimeltKits;
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (harimeltKits.getEditing().containsKey(player.getUniqueId())) {
            String status = harimeltKits.getEditing().get(player.getUniqueId()).split(":")[0];
            if (status.equals("DELAY")) {
                String secondsString = event.getMessage();
                if (!secondsString.contains(" ")) {
                    try {
                        int seconds = Integer.parseInt(secondsString);
                        if (seconds >= 0) {
                            String name = harimeltKits.getEditing().get(player.getUniqueId()).split(":")[1];
                            Kit kit = harimeltKits.getKitManager().getKit(name);
                            kit.setClaimTime(seconds);
                            harimeltKits.getKitManager().saveKit(name);
                            MenuEditorInventory menuEditorInventory = new MenuEditorInventory(harimeltKits);
                            player.openInventory(menuEditorInventory.getInventory(name));
                            harimeltKits.getEditing().put(player.getUniqueId(), "MENU:" + name);
                        } else {
                            harimeltKits.getMessages().sendMessage(player, "EditKit.waitTimeIsNegative", true);
                        }
                    } catch (NumberFormatException e) {
                        harimeltKits.getMessages().sendMessage(player, "EditKit.waitTimeContainChars", true);
                    }
                } else {
                    harimeltKits.getMessages().sendMessage(player, "EditKit.waitTimeContainSpace", true);
                }
            }
        }
    }
}
