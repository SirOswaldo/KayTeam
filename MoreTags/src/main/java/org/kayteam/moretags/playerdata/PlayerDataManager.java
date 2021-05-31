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

package org.kayteam.moretags.playerdata;

import org.bukkit.entity.Player;
import org.kayteam.moretags.MoreTags;
import org.kayteam.moretags.storage.Storage;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {

    private final MoreTags moreTags;

    public PlayerDataManager(MoreTags moreTags) {
        this.moreTags = moreTags;
    }

    private final HashMap<UUID, PlayerData> players = new HashMap<>();

    public void loadOnlinePlayerData() {
        for (Player player:moreTags.getServer().getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            loadPlayerData(uuid);
        }
    }

    public void loadPlayerData(UUID uuid) {
        Storage storage = moreTags.getStorage();
        String tag = storage.getTag(uuid);
        PlayerData playerData = new PlayerData(tag);
        players.put(uuid, playerData);
    }

    public void savePlayerData(UUID uuid) {
        Storage storage = moreTags.getStorage();
        PlayerData playerData = players.get(uuid);
        String tag = playerData.getTag();
        storage.setTag(uuid, tag);
    }

    public void unloadOnlinePlayerData() {
        for (Player player:moreTags.getServer().getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            unloadPlayerData(uuid);
        }
    }

    public void unloadPlayerData(UUID uuid) {
        savePlayerData(uuid);
        players.remove(uuid);
    }

    public PlayerData getPlayerData(UUID uuid) {
        return players.get(uuid);
    }

    public boolean containPlayerData(UUID uuid) {
        return players.containsKey(uuid);
    }

}
