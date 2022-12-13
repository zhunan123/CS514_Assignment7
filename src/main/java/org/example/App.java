package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class App extends Entity{

  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_GREEN = "\u001B[32m";
  private static final String ANSI_PURPLE = "\u001B[35m";
  private static final String ANSI_CYAN = "\u001B[36m";
  private static int ID = 0;

  public static void startProgram() {
    System.out.println(
            "*****************************************************************" + "\n" +
                    "*************" + ANSI_PURPLE + "Welcome to Zhunan's Music Manager APP" + ANSI_RESET +"***************" + "\n" +
                    "**********" + ANSI_GREEN + "If you are existed User, Please Press 1" + ANSI_RESET + "***********" + "\n" +
                    "**********" + ANSI_CYAN + "If you are New User, Please Press 2" + ANSI_RESET + "***********" + "\n" +
                    "*****************************************************************"
    );

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.print(">> ");
    String s = null;
    String username = null;
    String pw = null;
    try {
      s = br.readLine();
      if (s.equals("2")) {
        System.out.print(">> Please Create Your User name: ");
        username = br.readLine();
        System.out.print(">> Please Create Your Password: ");
        pw = br.readLine();
        getPreviousUserID();
        ID++;
        String sql =  "insert into users (id, username, password) values ("+ ID + ", '" + username + "', '"
                + pw + "');";
        connectToDB(sql);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Integer getPreviousUserID() {
    Connection connection = null;
    String dbName = "musicManager.db";
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);

      ResultSet rs = statement.executeQuery("select * from users");
      while (rs.next()) {
        ID = rs.getInt("id");
      }
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    }
    return ID;
  }

  public static void connectToDB(String sql) {
    Connection connection = null;
    String dbName = "musicManager.db";
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);

      ResultSet rs = connection.getMetaData().getTables(null, null, null, null);

//      statement.execute("drop table if exists users");
//      statement.execute("drop table if exists songs");
//      statement.execute("drop table if exists albums");
//      statement.execute("drop table if exists artists");
      if (!rs.next()) {
        statement.executeUpdate("create table users (id integer, username String, password String)");
        statement.executeUpdate("create table songs (id integer, name String, album String, artist String)");
        statement.executeUpdate("create table albums (id integer, name String, nSongs integer, artist String)");
        statement.executeUpdate("create table artists (id integer, name String, nAlbums integer, nSongs integer)");
      }
      statement.executeUpdate(sql);

    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        if (connection != null)
          connection.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
  }
  public static void main( String[] args )
  {
    startProgram();
  }

}
