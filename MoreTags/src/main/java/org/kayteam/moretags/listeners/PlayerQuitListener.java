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

package org.kayteam.moretags.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.kayteam.moretags.MoreTags;
import org.kayteam.moretags.playerdata.PlayerDataManager;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final MoreTags moreTags;

    public PlayerQuitListener(MoreTags moreTags) {
        this.moreTags = moreTags;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerDataManager playerDataManager = moreTags.getPlayerDataManager();
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        playerDataManager.unloadPlayerData(uuid);
    }

}
