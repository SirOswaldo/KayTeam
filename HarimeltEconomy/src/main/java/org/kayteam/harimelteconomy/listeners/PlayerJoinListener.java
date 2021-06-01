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

package org.kayteam.harimelteconomy.listeners;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import org.kayteam.harimelteconomy.HarimeltEconomy;
import org.kayteam.harimelteconomy.utils.yaml.Yaml;

public class PlayerJoinListener implements Listener {

    private final HarimeltEconomy harimeltEconomy;

    public PlayerJoinListener(HarimeltEconomy harimeltEconomy) {
        this.harimeltEconomy = harimeltEconomy;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Economy economy = harimeltEconomy.getEconomy();
        if (economy.hasAccount(player)) {
            economy.createPlayerAccount(player);
        }
        Yaml yaml = new Yaml(harimeltEconomy, "pepe", "prueba");
        yaml.registerFileConfiguration();
        FileConfiguration fileConfiguration = yaml.getFileConfiguration();
        fileConfiguration.set("prueba", "Probando una cosa");
        yaml.saveFileConfiguration();
    }

}