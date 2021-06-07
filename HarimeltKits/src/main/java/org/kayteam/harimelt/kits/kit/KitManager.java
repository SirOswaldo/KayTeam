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

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.utils.yaml.Yaml;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class KitManager {

    private final HarimeltKits harimeltKits;
    public KitManager(HarimeltKits harimeltKits) {
        this.harimeltKits = harimeltKits;
    }

    private final HashMap<String, Kit> kits = new HashMap<>();


    public void loadAllKits() {
        File directory = new File(harimeltKits.getDataFolder() + File.separator + "kits");
        if (directory.exists()) {
            if (directory.isDirectory()) {
                File[] files = directory.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".yml");
                    }
                });
                if (files != null) {
                    for (File file:files) {
                        String name = file.getName().split("\\.")[0];
                        Yaml kitYaml = new Yaml(harimeltKits, "kits", name);
                        kitYaml.registerFileConfiguration();
                        FileConfiguration kitConfiguration = kitYaml.getFileConfiguration();
                        int delay = 0;
                        if (kitConfiguration.contains("delay")) {
                            if (kitConfiguration.isInt("delay")) {
                                delay = kitConfiguration.getInt("delay");
                            }
                        }
                        List<?> itemsRaw = new ArrayList<>();
                        List<ItemStack> items = new ArrayList<>();
                        if (kitConfiguration.contains("items")) {
                            if (kitConfiguration.isList("items")) {
                                itemsRaw = kitConfiguration.getList("items");
                            }
                        }
                        if (itemsRaw != null) {
                            if (!itemsRaw.isEmpty()) {
                                for (Object o : itemsRaw) {
                                    ItemStack itemStack = (ItemStack) o;
                                    if (itemStack != null) {
                                        items.add(itemStack);
                                    }
                                }
                            }
                        }
                        Kit kit = new Kit(name);
                        kit.setClaimTime(delay);
                        kit.setItems(items);
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
        Yaml yaml = new Yaml(harimeltKits, "kits", name);
        yaml.registerFileConfiguration();
        FileConfiguration configuration = yaml.getFileConfiguration();
        configuration.set("delay", kit.getClaimTime());
        configuration.set("items", kit.getItems());
        yaml.saveFileConfiguration();
    }

    public boolean existKit(String name) {
        return kits.containsKey(name);
    }

    public void createKit(String name) {
        Yaml yaml = new Yaml(harimeltKits, "kits", name);
        yaml.registerFileConfiguration();
        FileConfiguration kit = yaml.getFileConfiguration();
        FileConfiguration configuration = harimeltKits.getConfiguration().getFileConfiguration();
        kit.set("delay", configuration.getInt("default.delay", 60));
        yaml.saveFileConfiguration();
    }

    public void createKit(String name, int delay) {
        Yaml yaml = new Yaml(harimeltKits, "kits", name);
        yaml.registerFileConfiguration();
        FileConfiguration kit = yaml.getFileConfiguration();
        kit.set("delay", delay);
        yaml.saveFileConfiguration();
    }

    public boolean deleteKit(String name) {
        Yaml yaml = new Yaml(harimeltKits, "kits", name);
        if (yaml.deleteFileConfiguration()) {
            kits.remove(name);
            return true;
        }
        return false;
    }

    public Kit getKit(String name) {
        return kits.get(name);
    }

    public List<String> getKitsNames() {
        return new ArrayList<>(kits.keySet());
    }

    // Utility
    public boolean canClaim(Player player, String kitName) {
        if (!player.hasPermission("harimelt.claim." + kitName)) return false;
        Yaml yaml = new Yaml(harimeltKits, "players", player.getName());
        yaml.registerFileConfiguration();
        FileConfiguration file = yaml.getFileConfiguration();
        Kit kit = kits.get(kitName);
        if (!file.contains(kitName)) {
            return true;
        } else {
            int lastClaim = file.getInt(kitName);
            int currentTime = (int) (System.currentTimeMillis() / 1000);
            int claimTime = kit.getClaimTime();
            return currentTime - lastClaim > claimTime;
        }
    }

    public boolean canClaimOneTime(Player player, String kitName) {
        if (!player.hasPermission("harimelt.claim." + kitName)) return false;
        Yaml yaml = new Yaml(harimeltKits, "players", player.getName());
        yaml.registerFileConfiguration();
        FileConfiguration file = yaml.getFileConfiguration();
        Kit kit = kits.get(kitName);
        return file.contains(kitName);
    }

    public void updateClaimTime(Player player, String kitName) {
        Yaml yaml = new Yaml(harimeltKits, "players", player.getName());
        yaml.registerFileConfiguration();
        FileConfiguration file = yaml.getFileConfiguration();
        file.set(kitName, System.currentTimeMillis() / 1000);
        yaml.saveFileConfiguration();
    }

    public void claimKit(Player player, String kitName) {
        Kit kit = kits.get(kitName);
        for (ItemStack itemStack:kit.getItems()) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().setItem(player.getInventory().firstEmpty(), itemStack);
            } else {
                Objects.requireNonNull(player.getLocation().getWorld()).dropItem(player.getLocation(), itemStack);
            }
        }
    }

}
