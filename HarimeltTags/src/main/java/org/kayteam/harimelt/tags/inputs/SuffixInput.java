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

package org.kayteam.harimelt.tags.inputs;

import org.bukkit.entity.Player;
import org.kayteam.harimelt.tags.HarimeltTags;
import org.kayteam.harimelt.tags.inventories.TagEditInventory;
import org.kayteam.harimelt.tags.tag.Tag;
import org.kayteam.harimelt.tags.tag.TagManager;
import org.kayteam.harimelt.tags.util.input.BookInput;
import org.kayteam.harimelt.tags.util.yaml.Yaml;

import java.util.UUID;

public class SuffixInput extends BookInput {

    private final HarimeltTags harimeltTags;

    public SuffixInput(HarimeltTags harimeltTags, Player player) {
        super(player);
        this.harimeltTags = harimeltTags;
        Yaml configuration = harimeltTags.getConfiguration();
        setPages(configuration.getFileConfiguration().getStringList("editorInventory.bookInputText.suffix"));
    }

    @Override
    public boolean onFirm(Player player, String firm) {
        UUID uuid = player.getUniqueId();
        if (harimeltTags.getEditing().containsKey(uuid)) {
            String name = harimeltTags.getEditing().get(uuid)[1];
            TagManager tagManager = harimeltTags.getTagManager();
            Tag tag = tagManager.get(name);
            tag.setSuffix(firm);
            tagManager.save(name);
            harimeltTags.getEditing().put(uuid, new String[] {"MENU", name});
            TagEditInventory tagEditInventory = new TagEditInventory(harimeltTags);
            player.openInventory(tagEditInventory.getInventory(name));
        }
        return true;
    }

    @Override
    public void onSneak(Player player) {
        UUID uuid = player.getUniqueId();
        if (harimeltTags.getEditing().containsKey(uuid)) {
            String name = harimeltTags.getEditing().get(uuid)[1];
            harimeltTags.getEditing().put(uuid, new String[] {"MENU", name});
            TagEditInventory tagEditInventory = new TagEditInventory(harimeltTags);
            player.openInventory(tagEditInventory.getInventory(name));
        }
    }

}
