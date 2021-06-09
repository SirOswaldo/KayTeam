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

package org.kayteam.harimelt.tags.util.input;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.harimelt.tags.util.input.tasks.RenewBook;

import java.util.HashMap;
import java.util.UUID;

public class BookInputManager implements Listener {

    private final JavaPlugin javaPlugin;

    public BookInputManager(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);
        javaPlugin.getLogger().info("BookInput has been registered.");
    }

    private final HashMap<UUID, BookInput> bookInputs = new HashMap<>();

    public void addBookInput(BookInput bookInput) {
        bookInputs.put(bookInput.getPlayer().getUniqueId(), bookInput);
    }

    public void addBookToPlayer(UUID uuid) {
        RenewBook renewBook = new RenewBook(javaPlugin, bookInputs.get(uuid));
        renewBook.startScheduler();
    }
    public void addBookToPlayer(BookInput bookInput) {
        RenewBook renewBook = new RenewBook(javaPlugin, bookInput);
        renewBook.startScheduler();
    }

    @EventHandler
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (bookInputs.containsKey(uuid)) {
            if (event.getNewBookMeta().getTitle() != null) {
                BookInput bookInput = bookInputs.get(uuid);
                if (bookInput.onFirm(player, event.getNewBookMeta().getTitle())) {
                    player.getInventory().getItemInOffHand().setAmount(0);
                    player.updateInventory();
                    bookInputs.remove(uuid);
                } else {
                    RenewBook renewBook = new RenewBook(javaPlugin, bookInput);
                    renewBook.startScheduler();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (bookInputs.containsKey(uuid)) {
            BookInput bookInput = bookInputs.get(uuid);
            bookInput.onSneak(player);
            bookInputs.remove(uuid);
        }
    }

}