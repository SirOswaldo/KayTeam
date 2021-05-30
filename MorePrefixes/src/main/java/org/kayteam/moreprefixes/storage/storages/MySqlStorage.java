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

package org.kayteam.moreprefixes.storage.storages;

import org.bukkit.configuration.file.FileConfiguration;
import org.kayteam.moreprefixes.MorePrefixes;
import org.kayteam.moreprefixes.storage.Storage;
import org.kayteam.moreprefixes.storage.enums.StorageType;
import org.kayteam.moreprefixes.util.mysql.MySQL;

import java.util.UUID;

public class MySqlStorage extends Storage {

    private final MorePrefixes morePrefixes;
    private final MySQL mySQL;

    public MySqlStorage(MorePrefixes morePrefixes) {
        super(StorageType.MYSQL);
        this.morePrefixes = morePrefixes;
        FileConfiguration configuration = morePrefixes.getConfiguration().getFileConfiguration();
        String ip = configuration.getString("mysql.ip");
        String port = configuration.getString("mysql.port");;
        String username = configuration.getString("mysql.username");;
        String password = configuration.getString("mysql.password");;
        String database = configuration.getString("mysql.database");
        String table = configuration.getString("mysql.table");
        String[][] values = new String[][] {
                {"UUID", "VARCHAR(64)"},
                {"PREFIX", "VARCHAR(32)"}
        };
        mySQL = new MySQL(ip,port,username,password, database, table, values);
        if (!mySQL.startConnection()) {
            morePrefixes.getLogger().info("MYSQL: FAILED TO CONNECT, VERIFY AND RESTART.");
            morePrefixes.getPluginLoader().disablePlugin(morePrefixes);
        } else {
            mySQL.createTable();
            mySQL.closeConnection();
        }
    }

    @Override
    public String getPrefix(UUID uuid) {
        String result = "";
        mySQL.startConnection();
        if (mySQL.existsPrimaryKey(uuid.toString())) {
            result = mySQL.getString(uuid.toString(), "PREFIX");
        }
        mySQL.closeConnection();
        return result;
    }

    @Override
    public void setPrefix(UUID uuid, String prefix) {
        mySQL.startConnection();
        if (mySQL.existsPrimaryKey(uuid.toString())) {
            mySQL.setString(uuid.toString(), "PREFIX", prefix);
        }
        mySQL.closeConnection();
    }

}