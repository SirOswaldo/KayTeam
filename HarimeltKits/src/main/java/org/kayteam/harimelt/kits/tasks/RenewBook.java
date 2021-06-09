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

package org.kayteam.harimelt.kits.tasks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.utils.task.Task;
import org.kayteam.harimelt.kits.utils.yaml.Yaml;

import java.util.List;

public class RenewBook extends Task {

    private final HarimeltKits plugin;
    private final Player player;

    public RenewBook(HarimeltKits plugin, Player player) {
        super(plugin, 20L);
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void actions() {
        if (player.isOnline()) {
            Yaml configuration = plugin.getConfiguration();
            ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
            BookMeta bookMeta = (BookMeta) book.getItemMeta();
            if (configuration.contains("editClaimTimeBookText")) {
                if (configuration.getFileConfiguration().isList("editClaimTimeBookText")) {
                    List<String> pages = configuration.getFileConfiguration().getStringList("editClaimTimeBookText");
                    if (bookMeta != null) {
                        for (String page:pages) {
                            bookMeta.addPage(page);
                        }
                    }
                }
            } else {
                if (bookMeta != null) {
                    bookMeta.addPage("Firm the book with the new claim time");
                }
            }
            book.setItemMeta(bookMeta);
            player.getInventory().setItemInOffHand(book);
        }
        stopScheduler();
    }
}
