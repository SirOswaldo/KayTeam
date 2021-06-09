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

package org.kayteam.harimelt.tags.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.kayteam.harimelt.tags.HarimeltTags;
import org.kayteam.harimelt.tags.playerdata.PlayerDataManager;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final HarimeltTags harimeltTags;

    public PlayerQuitListener(HarimeltTags harimeltTags) {
        this.harimeltTags = harimeltTags;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerDataManager playerDataManager = harimeltTags.getPlayerDataManager();
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        playerDataManager.unloadPlayerData(uuid);
    }

}
