package com.psunset.cloudlauncher.database;

import com.psunset.cloudlauncher.util.FileHelper;
import com.psunset.cloudlauncher.util.path.MCPathHelper;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class ConfigHelper {
    public static final String DB_URL = "jdbc:mysql://uto6xl4qzwme1laj:5o7ZhOvwwUIh7FNdiANa@bfrxojhf2xfiyhkhhdym-mysql.services.clever-cloud.com:3306/bfrxojhf2xfiyhkhhdym";
    public static final String USERNAME = "uto6xl4qzwme1laj";
    public static final String PASSWORD = "5o7ZhOvwwUIh7FNdiANa";

    public static final File idFile = new File(MCPathHelper.getOS().getMc() + "cl-config-user-id.txt");

    public static int id = 0; // id should be >= 1

    private static Connection CONN;

    private static Connection getConnection() throws SQLException {
        if (CONN == null){
            CONN = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        }
        return CONN;
    }

    /**
     * First time to connect to database.
     * After first time please use {@link ConfigHelper#refresh()}.
     * @return {@code true} if connection success;
     * {@code false} if connection failed
     */
    public static boolean init(){
        try{
            Statement statement = getConnection().createStatement();
            ResultSet resultSet;
            
            // Get user id
            if(id == 0){
                if(idFile.createNewFile() || FileHelper.readFile(idFile).isBlank()){
                    for (int i = 1; i < 2147483647; i++) {
                        try {
                            resultSet = statement.executeQuery("SELECT * FROM `configs` WHERE id=" + i);
                            if(!resultSet.next()){
                                statement.execute("INSERT INTO configs(id, javaPath, mcPath, maxRam, jvmArgs, lastGameVersion) VALUES (" + i + ", \"\", \"\", \"\", \"\", \"\");");
                                id = i;
                                break;
                            }
                        }catch (SQLException e){
                            System.out.println("Database connection failed.");
                            e.printStackTrace();

                            return false;
                        }
                    }

                    FileHelper.writeToFile(idFile.toPath(), Integer.toString(id));
                } else {
                    id = Integer.parseInt(FileHelper.readFile(idFile));
                }
            }

            refresh();

            System.out.println("Successfully connect to database.");

            System.out.println("Configs User ID: " + id);
            return true;

        }catch (SQLException | IOException e){
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Refresh data from database.
     * @return {@code true} if connection success;
     * {@code false} if connection failed or connection not init
     */
    public static boolean refresh(){
        if (id == 0){
            return false;
        }
        try {
            Statement statement = getConnection().createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM `configs` WHERE id=" + id);

            if(resultSet.next()){
                Type.JAVA_PATH.setValue(resultSet.getString("javaPath").isBlank()? "" : resultSet.getString("javaPath"));
                Type.MC_PATH.setValue(resultSet.getString("mcPath").isBlank()? "" : resultSet.getString("mcPath"));
                Type.MAX_RAM.setValue(resultSet.getString("maxRam").isBlank()? "" : resultSet.getString("maxRam"));
                Type.JVM_ARGS.setValue(resultSet.getString("jvmArgs").isBlank()? "" : resultSet.getString("jvmArgs"));
                Type.LAST_GAME_VERSION.setValue(resultSet.getString("lastGameVersion").isBlank()? "" : resultSet.getString("lastGameVersion"));
                Type.SELECT_VERSION.setValue(resultSet.getString("selectVersion").isBlank()? "" : resultSet.getString("selectVersion"));
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void closeConn() throws SQLException {
        getConnection().close();
    }

    public enum Type {
        JAVA_PATH("javaPath"),
        MC_PATH("mcPath"),
        MAX_RAM("maxRam"),
        JVM_ARGS("jvmArgs"),
        SELECT_VERSION("selectVersion"),
        LAST_GAME_VERSION("lastGameVersion");

        private String k;
        private String v;

        private Type(String k){
            this.k = k;
        }

        private Type(String k, String v){
            this.k = k;
            this.v = v;
        }

        public String getValue(){
            return this.v;
        }

        public void setValue(String v){
            this.v = v;
        }

        public String getKey(){
            return k;
        }

        public String getFormattedKeyValue(){
            return k + ": " + v;
        }
    }
}
