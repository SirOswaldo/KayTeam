package org.kayteam.harimelt.kits.utils.itemstack;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStackUtil {

    public static ItemStack parseString(String string) {
        Material type = Material.AIR;
        int amount = 0;
        String displayName = "";
        int customModelData = 0;
        List<String> lore = new ArrayList<>();
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        List<ItemFlag> flags = new ArrayList<>();
        // type:AIR,display-name:Name,lore:Line1,lore:Line2,enchantment:ALL_DAMAGE:1,custom-model-data:1
        String[] attributes = string.split(",");
        for (String attribute:attributes) {
            String key = attribute.split(":")[0];
            switch (key) {
                case "type":
                    type = Material.getMaterial(attribute.split(":")[1]);
                    if (type == null) {
                        return new ItemStack(Material.AIR);
                    }
                    break;
                case "amount":
                    try {
                        amount = Integer.parseInt(attribute.split(":")[1]);
                    } catch (NumberFormatException ignored) {}
                case "custom-model-data":
                    try {
                        customModelData = Integer.parseInt(attribute.split(":")[1]);
                    } catch (NumberFormatException ignored) {}
                case "display-name":
                    displayName = ChatColor.translateAlternateColorCodes('&', attribute.split(":")[1]);
                    break;
                case "lore":
                    lore.add(ChatColor.translateAlternateColorCodes('&', attribute.split(":")[1]));
                    break;
                case "enchantment":
                    Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(attribute.split(":")[1]));
                    if (enchantment != null) {
                        try {
                            int level = Integer.parseInt(attribute.split(":")[2]);
                            enchantments.put(enchantment, level);
                        } catch (NumberFormatException ignored) { }
                    }
                    break;
                case "item-flag":
                    ItemFlag itemFlag = ItemFlag.valueOf(attribute.split(":")[1]);
                    flags.add(itemFlag);
                    break;
            }
        }
        // Construct item
        ItemStack itemStack = new ItemStack(type, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        if (customModelData != 0) {
            itemMeta.setCustomModelData(customModelData);
        }
        for (ItemFlag itemFlag:flags) {
            itemMeta.addItemFlags(itemFlag);
        }
        itemStack.setItemMeta(itemMeta);
        if (!enchantments.isEmpty()) {
            itemStack.addEnchantments(enchantments);
        }
        return itemStack;
    }

    public static String generateString(ItemStack itemStack) {
        StringBuilder result = new StringBuilder();
        ItemMeta itemMeta = itemStack.getItemMeta();
        result.append("type:").append(itemStack.getType());
        result.append(",amount:").append(itemStack.getAmount());
        if (itemMeta.hasDisplayName()) {
            result.append(",display-name:").append(itemMeta.getDisplayName());
        }
        if (itemMeta.hasLore()) {
            for (int i = 0; i < itemMeta.getLore().size(); i++) {
                String text = itemMeta.getLore().get(i);
                result.append(",lore:").append(text);
            }
        }
        if (!itemStack.getEnchantments().isEmpty()) {
            for (Enchantment enchantment:itemStack.getEnchantments().keySet()) {
                result.append(",enchantment:").append(enchantment.getKey()).append(":").append(itemStack.getEnchantmentLevel(enchantment));
            }
        }
        if (!itemMeta.getItemFlags().isEmpty()) {
            for (ItemFlag itemFlag:itemMeta.getItemFlags()) {
                result.append(",flag:").append(itemFlag);
            }
        }
        return result.toString();
    }
}
