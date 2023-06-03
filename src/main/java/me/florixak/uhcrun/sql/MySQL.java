package me.florixak.uhcrun.sql;

import me.florixak.uhcrun.UHCRun;
import me.florixak.uhcrun.player.UHCPlayer;
import me.florixak.uhcrun.utils.TextUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private Connection con;

    private final String host;
    private final String port;
    private final String database;
    private final String user;
    private final String password;

    public MySQL(String host, String port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;

        connect();
    }

    public void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
        } catch (SQLException e) {
            UHCRun.getInstance().getLogger().info(TextUtils.color("&cMySQL can not be connected!"));
        }
    }

    public void disconnect() {
        try {
            if (this.hasConnection()) {
                this.con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasConnection() {
        return this.con != null ? true : false;
    }

    public Connection getConnection() {
        return this.con;
    }
}
