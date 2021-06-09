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

package org.kayteam.harimelt.tags.tag;

import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.harimelt.tags.util.yaml.Yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TagManager {

    private final JavaPlugin javaPlugin;
    private final HashMap<String, Tag> tags = new HashMap<>();

    public TagManager(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public void loadAll() {
        File directory = new File(javaPlugin.getDataFolder() + File.separator + "tags");
        if (directory.exists()) {
            if (directory.isDirectory()) {
                File[] files = directory.listFiles(pathname -> pathname.getName().endsWith(".yml"));
                if (files != null) {
                    for (File file:files) {
                        String name = file.getName().replace(".yml", "");
                        javaPlugin.getLogger().info("Loading '" + name + "' tag");
                        Yaml yaml = new Yaml(javaPlugin, "tags", name);
                        yaml.registerFileConfiguration();
                        String prefix = "";
                        String suffix = "";
                        if (yaml.contains("prefix")) {
                            if (yaml.isString("prefix")) {
                                prefix = yaml.getString("prefix");
                            } else {
                                javaPlugin.getLogger().info("The tag '" + name + "' contain prefix but this no is a text.");
                            }
                        } else {
                            javaPlugin.getLogger().info("The tag '" + name + "' no prefix and it has been set to empty text.");
                        }
                        if (yaml.contains("suffix")) {
                            if (yaml.isString("suffix")) {
                                suffix = yaml.getString("suffix");
                            } else {
                                javaPlugin.getLogger().info("The tag '" + name + "' contain suffix but this no is a text.");
                            }
                        } else {
                            javaPlugin.getLogger().info("The tag '" + name + "' no suffix and it has been set to empty text.");
                        }
                        Tag tag = new Tag(name);
                        tag.setPrefix(prefix);
                        tag.setSuffix(suffix);
                        tags.put(name, tag);
                        javaPlugin.getLogger().info("The tag '" + name + "' has been loaded.");
                    }
                }
            }
        }
    }

    public void unloadAll() {
        if (!tags.isEmpty()) {
            for (String name:tags.keySet()) {
                tags.remove(name);
            }
        }
    }

    public void save(String name) {
        Yaml yaml = new Yaml(javaPlugin, "tags", name);
        yaml.registerFileConfiguration();
        Tag tag = tags.get(name);
        yaml.set("prefix", tag.getPrefix());
        yaml.set("suffix", tag.getSuffix());
        yaml.saveFileConfiguration();
    }

    public void delete(String name) {
        Yaml yaml = new Yaml(javaPlugin, "tags", name);
        if (yaml.deleteFileConfiguration()) {
            tags.remove(name);
        }
    }

    public void rename(String name, String newName) {
        Tag tag = tags.get(name);
        Tag newTag = new Tag(newName);
        newTag.setPrefix(tag.getPrefix());
        newTag.setSuffix(tag.getSuffix());
        delete(name);
        add(newTag);
        save(newName);
    }

    public void add(Tag tag) {
        tags.put(tag.getName(), tag);
    }

    public boolean exist(String name) {
        return tags.containsKey(name);
    }

    public Tag get(String name) {
        return tags.get(name);
    }

    public List<String> getNames() {
        return new ArrayList<>(tags.keySet());
    }

}
