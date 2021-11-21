package omen44.omens_economy.datamanager;

import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    ConfigTools ct = new ConfigTools();
    FileConfiguration config = ct.getFileConfig("config.yml");

    private String host = config.getString("database.host");
    private String port = config.getString("database.port");
    private String database = config.getString("database.database");
    private String username = config.getString("database.user");
    String pass = config.getString("database.password");
    private String password = pass;

    private Connection connection;

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        String pass = config.getString("database.password");
        if (pass.equalsIgnoreCase("blank")){
            password = "";
        }

        if (!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSl=false", username, password);
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}