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

package org.kayteam.moretags.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kayteam.moretags.MoreTags;

public class MorePrefixesCommand implements CommandExecutor {

    private final MoreTags moreTags;

    public MorePrefixesCommand(MoreTags moreTags) {
        this.moreTags = moreTags;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("moretags.user")) {
                if (strings.length > 0) {

                } else {

                }
            } else {
                moreTags.getMessages().sendMessage(player, "moreTags.noPermission", true);
            }
        } else {
            moreTags.getMessages().sendMessage(commandSender, "moreTags.isConsole", true);
        }
        return true;
    }
}
