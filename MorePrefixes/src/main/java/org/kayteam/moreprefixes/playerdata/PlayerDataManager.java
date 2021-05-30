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

package org.kayteam.moreprefixes.playerdata;

import org.kayteam.moreprefixes.MorePrefixes;
import org.kayteam.moreprefixes.storage.Storage;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {

    private final MorePrefixes morePrefixes;

    public PlayerDataManager(MorePrefixes morePrefixes) {
        this.morePrefixes = morePrefixes;
    }

    private final HashMap<UUID, PlayerData> players = new HashMap<>();

    public void load(UUID uuid) {
        Storage storage = morePrefixes.getStorage();
        String prefix = storage.getPrefix(uuid);
        PlayerData playerData = new PlayerData(prefix);
        players.put(uuid, playerData);
    }

    public void save(UUID uuid) {
        Storage storage = morePrefixes.getStorage();
        PlayerData playerData = players.get(uuid);
        String prefix = playerData.getPrefix();
        storage.setPrefix(uuid, prefix);
    }

    public void unload(UUID uuid) {
        save(uuid);
        players.remove(uuid);
    }

    public PlayerData get(UUID uuid) {
        return players.get(uuid);
    }

    public boolean contain(UUID uuid) {
        return players.containsKey(uuid);
    }

}
