package common;

import java.sql.*;

public class JdbcConnection_1 {

    public static void main(String[] args){
        try {
            Statement stmt = getDbConnection().createStatement();
            stmt.executeUpdate("INSERT INTO accounts( \"accoutName\", \"addressSet\") VALUES ('10amPZeLn93CJnu8r4hdeFfRtQyF3mXNRQy', '{sfdgsdfgsfd}');");

            ResultSet rs1 = stmt.executeQuery("SELECT * from accounts");
            while (rs1.next()) {
                System.out.println(rs1.getString(1));
            }
        }catch (Exception e ){

        }
    }

    public Connection JdbcConnection (){
       return getDbConnection();
    }

    public static  Connection getDbConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver?");
            e.printStackTrace();
        }
        System.out.println("PostgreSQL JDBC Driver Registered!");

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres","1199");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
            return connection;
        } else {
            System.out.println("Failed to make connection!");
        } return connection;
    }

    public static void writeToAccountBook(){


    }
}