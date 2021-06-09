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

import org.bukkit.Keyed;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.utils.yaml.Yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class KitManager {

    private final HarimeltKits plugin;
    public KitManager(HarimeltKits plugin) {
        this.plugin = plugin;
    }

    private final HashMap<String, Kit> kits = new HashMap<>();

    public void loadAllKits() {
        File directory = new File(plugin.getDataFolder() + File.separator + "kits");
        if (directory.exists()) {
            if (directory.isDirectory()) {
                File[] files = directory.listFiles(pathname -> pathname.getName().endsWith(".yml"));
                if (files != null) {
                    for (File file:files) {
                        String name = file.getName().replace(".yml", "");
                        plugin.getLogger().info("Loading '" + name + "' kit");
                        Yaml kitYaml = new Yaml(plugin, "kits", name);
                        kitYaml.registerFileConfiguration();
                        FileConfiguration kitConfiguration = kitYaml.getFileConfiguration();
                        int claimTime = 0;
                        if (kitConfiguration.contains("claim-time")) {
                            if (kitConfiguration.isInt("claim-time")) {
                                claimTime = kitConfiguration.getInt("claim-time");
                            } else {
                                plugin.getLogger().info("The kit '" + name + "' contain claim time but this no is a number.");
                            }
                        } else {
                            plugin.getLogger().info("The kit '" + name + "' no contain claim time and it has been set to 0.");
                        }
                        List<ItemStack> items = new ArrayList<>();
                        if (kitYaml.contains("items")) {
                            Set<String> strings = kitYaml.getFileConfiguration().getConfigurationSection("items").getValues(false).keySet();
                            for (String string:strings) {
                                items.add(kitYaml.getItemStack("items." + string));
                            }
                        }
                        Kit kit = new Kit(name);
                        kit.setClaimTime(claimTime);
                        kit.setItems(items);
                        kits.put(kit.getName(), kit);
                        plugin.getLogger().info("The kit '" + name + "' has been loaded.");
                    }
                }
            }
        }
    }

    public void unloadAllKits() {
        if (!kits.isEmpty()) {
            for (String name:kits.keySet()) {
                kits.remove(name);
            }
        }
    }

    public void saveKit(String name) {
        Kit kit = kits.get(name);
        Yaml yaml = new Yaml(plugin, "kits", name);
        yaml.registerFileConfiguration();
        FileConfiguration configuration = yaml.getFileConfiguration();
        configuration.set("claim-time", kit.getClaimTime());
        for (int i = 0; i < kit.getItems().size(); i++) {
            ItemStack itemStack = kit.getItems().get(i);
            if (itemStack != null) {
                yaml.setItemStack("items."+ i, itemStack);
            }
        }
        yaml.saveFileConfiguration();
    }

    public boolean existKit(String name) {
        return kits.containsKey(name);
    }

    public void addKit(Kit kit) {
        kits.put(kit.getName(), kit);
    }

    public void deleteKit(String name) {
        Yaml yaml = new Yaml(plugin, "kits", name);
        if (yaml.deleteFileConfiguration()) {
            kits.remove(name);
        }
    }

    public Kit getKit(String name) {
        return kits.get(name);
    }

    public List<String> getKitsNames() {
        return new ArrayList<>(kits.keySet());
    }

}
