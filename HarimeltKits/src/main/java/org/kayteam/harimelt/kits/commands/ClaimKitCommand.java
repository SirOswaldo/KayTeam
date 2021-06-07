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

package org.kayteam.harimelt.kits.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kayteam.harimelt.kits.HarimeltKits;
import org.kayteam.harimelt.kits.kit.Kit;
import org.kayteam.harimelt.kits.kit.KitManager;
import org.kayteam.harimelt.kits.utils.command.SimpleCommand;

import java.util.List;

public class ClaimKitCommand extends SimpleCommand {

    private final HarimeltKits harimeltKits;

    public ClaimKitCommand(HarimeltKits harimeltKits) {
        super(harimeltKits, "", "");
        this.harimeltKits = harimeltKits;
    }


    @Override
    public void onCommand(CommandSender sender, String command, String[] strings) {
        if (isPlayer(sender)) {
            Player player = getPlayer(sender);
            if (strings.length > 0) {
                String kitName = strings[0];
                KitManager kitManager = getHarimeltKits().getKitManager();
                if (kitManager.existKit(kitName)) {
                    if (kitManager.canClaim(player, kitName)) {
                        Kit kit = kitManager.getKit(kitName);
                        if (kit.getClaimTime() == 0) {
                            if (kitManager.canClaimOneTime(player, kitName)) {
                                kitManager.claimKit(player, kitName);
                                kitManager.updateClaimTime(player, kitName);
                                sendMessage(player, "ClaimKit.kitClaimComplete", new String[][] {{"%name%", kitName}});
                            } else {
                                if (player.hasPermission("harimelt.bypass.claim.time")) {
                                    kitManager.claimKit(player, kitName);
                                    sendMessage(player, "ClaimKit.kitClaimComplete", new String[][] {{"%name%", kitName}});
                                } else {
                                    sendMessage(player, "ClaimKit.oneTimeClaim", new String[][] {{"%name%", kitName}});
                                }
                            }
                        } else {
                            kitManager.claimKit(player, kitName);
                            kitManager.updateClaimTime(player, kitName);
                            sendMessage(player, "ClaimKit.kitClaimComplete", new String[][] {{"%name%", kitName}});
                        }
                    } else {
                        sendMessage(player, "ClaimKit.noPermissionForTheKit", new String[][] {{"%name%", kitName}});
                    }
                } else {
                    sendMessage(player, "ClaimKit.invalidKitName", new String[][] {{"%name%", kitName}});
                }
            } else {
                sendMessage(player, "ClaimKit.emptyKitName");
            }
        } else {

        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String[] strings) {
        return null;
    }
}
