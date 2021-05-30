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

package org.kayteam.moreprefixes.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.kayteam.moreprefixes.MorePrefixes;
import org.kayteam.moreprefixes.playerdata.PlayerDataManager;

import java.util.UUID;

public class AsyncPlayerPreLoginListener implements Listener {

    private final MorePrefixes morePrefixes;

    public AsyncPlayerPreLoginListener(MorePrefixes morePrefixes) {
        this.morePrefixes = morePrefixes;
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)) {
            PlayerDataManager playerDataManager = morePrefixes.getPlayerDataManager();
            UUID uuid = event.getUniqueId();
            playerDataManager.load(uuid);
        }
    }

}
