package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class App {

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
        getPreviousTableID("users");
        ID++;
        String sql =  "insert into users (id, username, password) values ("+ ID + ", '" + username + "', '"
                + pw + "');";
        connectToDB(sql);
      }
      if (s.equals("1")) {
        System.out.print(">> Please Enter Your User name: ");
        username = br.readLine();
        System.out.print(">> Please Enter Your Password: ");
        pw = br.readLine();
        System.out.println(
                "*****************************************************************" + "\n" +
                        "************" + ANSI_PURPLE + "" + username.toUpperCase() + "" + ANSI_RESET + "Welcome Back to Music Manager******************" + "\n" +
                        "*****************************************************************" + "\n" +
                        "*******" + ANSI_CYAN + "Press 1 to see all the songs/albums/artists available in the song store" + ANSI_RESET + "********" + "\n" +
                        "*******" + ANSI_GREEN + "Press 2 to generate the playlist in XML files and play all the songs" + ANSI_RESET + "********" + "\n" +
                        "*******" + ANSI_CYAN + "Press 3 if you want to search new songs" + ANSI_RESET + "************"
        );
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(">> ");
        String s1 = null;
        try {
          s1 = br.readLine();
          if (s1.equals("3")) {
            System.out.println(
                    "************How Do you want to search for songs?***********" + "\n" +
                            "[1] Artist's name" + "\n" +
                            "[2] Song's title" + "\n" +
                            "[3] Album's name" + "\n" +
                            ">> Please Enter your response: "
            );
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Integer getPreviousTableID(String tableName) {
    Connection connection = null;
    String dbName = "musicManager.db";
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);
        ResultSet rs = statement.executeQuery("select * from "+ tableName + "");
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
    Integer ID = 1;
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
        //fill in all the default songs
        statement.executeUpdate("insert into songs (id, name, album, artist) values ("+ ID + ", 'Love You Too', 'Revolver', 'The Beatles');");
        ID = getPreviousTableID("songs");
        ID++;
        statement.executeUpdate("insert into songs (id, name, album, artist) values ("+ ID + ", 'Dig a Pony', 'Let It Be', 'The Beatles');");
        ID = getPreviousTableID("songs");
        ID++;
        statement.executeUpdate("insert into songs (id, name, album, artist) values ("+ ID + ", 'Taxman', 'Revolver', 'The Beatles');");
        ID = getPreviousTableID("songs");
        ID++;
        statement.executeUpdate("insert into songs (id, name, album, artist) values ("+ ID + ", 'higher Power', 'Music of the Spheres', 'Coldplay');");
        ID = getPreviousTableID("songs");
        ID++;
        statement.executeUpdate("insert into songs (id, name, album, artist) values ("+ ID + ", 'Yikes', 'Ye', 'Kanye West');");
        ID = 1;
        //fill in all the default albums
        statement.executeUpdate("insert into albums (id, name, nSongs, artist) values ("+ ID + ", 'Revolver', 10, 'The Beatles');");
        ID = getPreviousTableID("albums");
        ID++;
        statement.executeUpdate("insert into albums (id, name, nSongs, artist) values ("+ ID + ", 'Ye', 7, 'Kanye West');");
        ID = getPreviousTableID("albums");
        ID++;
        statement.executeUpdate("insert into albums (id, name, nSongs, artist) values ("+ ID + ", 'Let It Be', 12, 'The Beatles');");
        ID = getPreviousTableID("albums");
        ID++;
        statement.executeUpdate("insert into albums (id, name, nSongs, artist) values ("+ ID + ", 'Music of the Spheres', 12, 'Coldplay');");
        ID = 1;
        //fill in all the default albums
        statement.executeUpdate("insert into artists (id, name, nAlbums, nSongs) values ("+ ID + ", 'The Beatles', 12, 213);");
        ID = getPreviousTableID("artists");
        ID++;
        statement.executeUpdate("insert into artists (id, name, nAlbums, nSongs) values ("+ ID + ", 'Kanye West', 8, 138);");
        ID = getPreviousTableID("artists");
        ID++;
        statement.executeUpdate("insert into artists (id, name, nAlbums, nSongs) values ("+ ID + ", 'Coldplay', 10, 175);");
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
