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

package org.kayteam.harimelt.tags.storage.storages;

import org.bukkit.configuration.file.FileConfiguration;
import org.kayteam.harimelt.tags.storage.Storage;
import org.kayteam.harimelt.tags.storage.enums.StorageType;
import org.kayteam.harimelt.tags.HarimeltTags;
import org.kayteam.harimelt.tags.util.mysql.MySQL;

import java.util.UUID;

public class MySqlStorage extends Storage {

    private final MySQL mySQL;

    public MySqlStorage(HarimeltTags harimeltTags) {
        super(StorageType.MYSQL);
        FileConfiguration configuration = harimeltTags.getConfiguration().getFileConfiguration();
        String ip = configuration.getString("mysql.ip");
        String port = configuration.getString("mysql.port");
        String username = configuration.getString("mysql.username");
        String password = configuration.getString("mysql.password");
        String database = configuration.getString("mysql.database");
        String table = configuration.getString("mysql.table");
        String[][] values = new String[][] {
                {"UUID", "VARCHAR(64)"},
                {"TAG", "VARCHAR(32)"}
        };
        mySQL = new MySQL(ip,port,username,password, database, table, values);
        if (!mySQL.startConnection()) {
            harimeltTags.getLogger().info("MYSQL: FAILED TO CONNECT, VERIFY AND RESTART.");
            harimeltTags.getPluginLoader().disablePlugin(harimeltTags);
        } else {
            mySQL.createTable();
            mySQL.closeConnection();
        }
    }

    @Override
    public String getTag(UUID uuid) {
        String result = "";
        mySQL.startConnection();
        if (mySQL.existsPrimaryKey(uuid.toString())) {
            result = mySQL.getString(uuid.toString(), "TAG");
        }
        mySQL.closeConnection();
        return result;
    }

    @Override
    public void setTag(UUID uuid, String tag) {
        mySQL.startConnection();
        if (!mySQL.existsPrimaryKey(uuid.toString())) {
            mySQL.createRow(new Object[] {uuid, tag});
        }
        mySQL.setString(uuid.toString(), "TAG", tag);
        mySQL.closeConnection();
    }

}
