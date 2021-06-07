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

package org.kayteam.harimelt.kits.kit;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Kit {

    private final String name;
    public String getName() {
        return name;
    }

    public Kit(String name) {
        this.name = name;
    }

    private int claimTime = 0;
    public int getClaimTime() {
        return claimTime;
    }
    public void setClaimTime(int claimTime) {
        this.claimTime = claimTime;
    }

    private List<ItemStack> items = new ArrayList<>();
    public List<ItemStack> getItems() {
        return items;
    }
    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

}