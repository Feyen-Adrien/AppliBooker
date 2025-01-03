package model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectDB {
    private static Connection connection = null;

    public Connection getConnection() {
        return connection;
    }
    public ConnectDB()
    {
        try
        {
            if(connection == null || connection.isClosed())
            {
                // Lis les données du fichier properties
                String filePath = "app.properties";
                String dataBaseName;
                String userName;
                String passWord;
                Properties prop = new Properties();

                try(FileInputStream inputStream = new FileInputStream(filePath)) {
                    prop.load(inputStream);
                    dataBaseName = prop.getProperty("dataBaseName");
                    userName = prop.getProperty("userName");
                    passWord = prop.getProperty("passWord");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(dataBaseName,userName,passWord);
            }
        } catch (SQLException | ClassNotFoundException e) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    public static void close()
    {
        try {
            connection.close();
            System.out.println("Connection DB closed");

        } catch (SQLException e) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE,null,e);
        }
    }
}
