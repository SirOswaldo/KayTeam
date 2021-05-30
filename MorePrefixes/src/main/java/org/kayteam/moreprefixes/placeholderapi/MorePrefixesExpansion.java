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

package org.kayteam.moreprefixes.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.kayteam.moreprefixes.MorePrefixes;
import org.kayteam.moreprefixes.playerdata.PlayerData;
import org.kayteam.moreprefixes.playerdata.PlayerDataManager;

public class MorePrefixesExpansion extends PlaceholderExpansion {

    private final MorePrefixes morePrefixes;

    public MorePrefixesExpansion(MorePrefixes morePrefixes) {
        this.morePrefixes = morePrefixes;
    }

    @Override
    public String getIdentifier() {
        return "moreprefixes";
    }

    @Override
    public String getAuthor() {
        return "SirOswaldo";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        PlayerDataManager playerDataManager = morePrefixes.getPlayerDataManager();
        if (playerDataManager.contain(p.getUniqueId())) {
            if (params.equals("prefix")) {
                PlayerData playerData = playerDataManager.get(p.getUniqueId());
                return playerData.getPrefix();
            }
        }
        return "";
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        PlayerDataManager playerDataManager = morePrefixes.getPlayerDataManager();
        if (playerDataManager.contain(p.getUniqueId())) {
            if (params.equals("prefix")) {
                PlayerData playerData = playerDataManager.get(p.getUniqueId());
                return playerData.getPrefix();
            }
        }
        return "";
    }

}
