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

package org.kayteam.harimelt.kits.utils.input;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.HashMap;
import java.util.List;

public abstract class BookInput {

    private final Player player;
    public Player getPlayer() {
        return player;
    }

    private final List<String> pages;
    public List<String> getPages() {
        return pages;
    }

    private final HashMap<String, String> data = new HashMap<>();
    public boolean containsData(String key) { return data.containsKey(key); }
    public String getData(String key) { return data.get(key); }
    public void setData(String key, String value) { data.put(key, value); }

    public BookInput(Player player, List<String> pages) {
        this.player = player;
        this.pages = pages;
    }

    public ItemStack getBook() {
        ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        if (pages.isEmpty()) {
            if (bookMeta != null) {
                bookMeta.addPage("Firm the book with the new claim time");
            }
        } else {
            if (bookMeta != null) {
                for (String page:pages) {
                    bookMeta.addPage(ChatColor.translateAlternateColorCodes('&', page));
                }
            }
        }
        book.setItemMeta(bookMeta);
        return book;
    }

    public abstract boolean onFirm(Player player, String firm);

    public abstract void onSneak(Player player);

}