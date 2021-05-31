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

package org.kayteam.moretags.storage.storages;

import org.bukkit.configuration.file.FileConfiguration;
import org.kayteam.moretags.MoreTags;
import org.kayteam.moretags.storage.Storage;
import org.kayteam.moretags.storage.enums.StorageType;
import org.kayteam.moretags.util.yaml.Yaml;

import java.util.UUID;

public class YamlStorage extends Storage {

    private final MoreTags moreTags;

    public YamlStorage(MoreTags moreTags) {
        super(StorageType.YAML);
        this.moreTags = moreTags;
    }

    @Override
    public String getTag(UUID uuid) {
        Yaml yaml = new Yaml(moreTags, "players", uuid.toString());
        yaml.registerFileConfiguration();
        FileConfiguration fileConfiguration = yaml.getFileConfiguration();
        return fileConfiguration.getString("tag", "");
    }

    @Override
    public void setTag(UUID uuid, String prefix) {
        Yaml yaml = new Yaml(moreTags, "players", uuid.toString());
        yaml.registerFileConfiguration();
        FileConfiguration fileConfiguration = yaml.getFileConfiguration();
        fileConfiguration.set("tag", prefix);
        yaml.saveFileConfiguration();
    }

}
