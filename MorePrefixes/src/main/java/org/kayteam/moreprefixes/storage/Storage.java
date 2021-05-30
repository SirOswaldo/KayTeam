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

package org.kayteam.moreprefixes.storage;

import org.kayteam.moreprefixes.storage.enums.StorageType;

import java.util.UUID;

public abstract class Storage {

    private final StorageType storageType;
    public StorageType getStorageType() {
        return storageType;
    }

    public Storage(StorageType storageType) {
        this.storageType = storageType;
    }

    public abstract String getPrefix(UUID uuid);
    public abstract void setPrefix(UUID uuid, String prefix);

}
