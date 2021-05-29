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

package org.kayteam.moreprefixes;

import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.moreprefixes.commands.MorePrefixesAdminCommand;
import org.kayteam.moreprefixes.commands.MorePrefixesCommand;
import org.kayteam.moreprefixes.util.yaml.Yaml;

public class MorePrefixes extends JavaPlugin {

    private final Yaml configuration = new Yaml(this, "configuration");
    public Yaml getConfiguration() {
        return configuration;
    }
    private final Yaml messages = new Yaml(this, "messages");
    public Yaml getMessages() {
        return messages;
    }

    @Override
    public void onEnable() {
        configuration.registerFileConfiguration();
        messages.registerFileConfiguration();
        getCommand("moreprefixesadmin").setExecutor(new MorePrefixesAdminCommand(this));
        getCommand("moreprefixes").setExecutor(new MorePrefixesCommand(this));

    }


}
