package database;

import app.MyApplication;
import config.MyConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;



public class DBManager {

    MyConfiguration configuration;

    public Connection getConnection() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println(configuration.getDataSourceFactory().getUrl());
             Connection connection = DriverManager.getConnection(
                    configuration.getDataSourceFactory().getUrl(),configuration.getDataSourceFactory().getUser(),configuration.getDataSourceFactory().getPassword());
             return connection;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }



    public DBManager() {
        this.configuration = MyApplication.getMyConfiguration();
    }

}
