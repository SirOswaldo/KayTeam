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

package org.kayteam.moreprefixes.storage.storages;

import org.bukkit.configuration.file.FileConfiguration;
import org.kayteam.moreprefixes.MorePrefixes;
import org.kayteam.moreprefixes.storage.Storage;
import org.kayteam.moreprefixes.storage.enums.StorageType;
import org.kayteam.moreprefixes.util.yaml.Yaml;

import java.util.UUID;

public class YamlStorage extends Storage {

    private final MorePrefixes morePrefixes;

    public YamlStorage(MorePrefixes morePrefixes) {
        super(StorageType.YAML);
        this.morePrefixes = morePrefixes;
    }

    @Override
    public String getPrefix(UUID uuid) {
        Yaml yaml = new Yaml(morePrefixes, "players", uuid.toString());
        yaml.registerFileConfiguration();
        FileConfiguration fileConfiguration = yaml.getFileConfiguration();
        return fileConfiguration.getString("prefix", "");
    }

    @Override
    public void setPrefix(UUID uuid, String prefix) {
        Yaml yaml = new Yaml(morePrefixes, "players", uuid.toString());
        yaml.registerFileConfiguration();
        FileConfiguration fileConfiguration = yaml.getFileConfiguration();
        fileConfiguration.set("prefix", prefix);
        yaml.saveFileConfiguration();
    }

}
