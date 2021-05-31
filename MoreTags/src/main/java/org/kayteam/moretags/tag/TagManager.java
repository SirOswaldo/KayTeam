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

package org.kayteam.moretags.tag;

import org.bukkit.configuration.file.FileConfiguration;
import org.kayteam.moretags.MoreTags;
import org.kayteam.moretags.util.yaml.Yaml;

import java.util.HashMap;

public class TagManager {

    private final MoreTags moreTags;

    public TagManager(MoreTags moreTags) {
        this.moreTags = moreTags;
    }

    private final HashMap<String, Tag> tags = new HashMap<>();

    public void loadAllTags() {
        Yaml configuration = moreTags.getConfiguration();
        for (String name:configuration.getFileConfiguration().getConfigurationSection("tags").getKeys(false)) {
            loadTag(name);
        }
    }

    public void unloadAllTags() {
        for (String name:tags.keySet()) {
            unloadTag(name);
        }
    }

    public void loadTag(String name) {
        Yaml configuration = moreTags.getConfiguration();
        FileConfiguration fileConfiguration = configuration.getFileConfiguration();
        if (fileConfiguration.contains("tags." + name)) {
            String prefix = fileConfiguration.getString("tags." + name + ".prefix", "");
            String suffix = fileConfiguration.getString("tags." + name + ".suffix", "");
            Tag tag = new Tag(name, prefix, suffix );
            tags.put(name, tag);
        }
    }

    public void unloadTag(String name) {
        Yaml configuration = moreTags.getConfiguration();
        FileConfiguration fileConfiguration = configuration.getFileConfiguration();
        Tag tag = tags.get(name);
        String prefix = tag.getPrefix();
        String suffix = tag.getSuffix();
        fileConfiguration.set("tags." + name + ".prefix", prefix);
        fileConfiguration.set("tags." + name + ".suffix", suffix);
        configuration.saveFileConfiguration();
        tags.remove(name);
    }

    public boolean containTag(String name) {
        return tags.containsKey(name);
    }

    public Tag getTag(String name) {
        return tags.get(name);
    }

}
