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

package org.kayteam.harimelt.kits.utils.input.tasks;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.harimelt.kits.utils.input.BookInput;
import org.kayteam.harimelt.kits.utils.task.Task;

public class RenewBook extends Task {

        private final BookInput bookInput;

        public RenewBook(JavaPlugin javaPlugin, BookInput bookInput) {
            super(javaPlugin, 20L);
            this.bookInput = bookInput;
        }

        @Override
        public void actions() {
            Player player = bookInput.getPlayer();
            if (player.isOnline()) {
                player.getInventory().setItemInOffHand(bookInput.getBook());
            }
            stopScheduler();
        }
    }