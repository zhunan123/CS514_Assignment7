package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

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
                        "************" + ANSI_PURPLE + "" + username.toUpperCase() + " " + ANSI_RESET + "Welcome Back to Music Manager******************" + "\n" +
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
          if (s1.equals("1")) {
            System.out.println("********Below is all the songs available*********");
            displayAllSongs();
            System.out.println("********Below is all the albums available*********");
            displayAllAlbums();
            System.out.println("********Below is all the artists available*********");
            displayAllArtists();
          }

          if (s1.equals("2")) {
            generatePlaylist();
          }
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

  private static void generatePlaylist() {
    Playlist playlist = new Playlist();

    Connection connection = null;
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:musicManager.db");
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from songs");

      while (rs.next()){
        try {
          String name = rs.getString("name");
          String albumname = rs.getString("album");
          String artistname = rs.getString("artist");
          int nLikes = new Random(). nextInt(100);

          Song song = new Song();
          Album album = new Album(albumname);
          Artist artist = new Artist(artistname);

          song.setTitle(name);
          song.setAlbum(album);
          song.setPerformer(artist);
          song.setLikes(nLikes);

          playlist.addSong(song);
          playlist.sort();
          writeToXML(playlist);

        } catch(SQLException e) {
          System.out.println("SQL Exception" + e.getMessage());
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static void writeToXML(Playlist pl) {
    try {
      DocumentBuilderFactory db = DocumentBuilderFactory.newInstance();
      DocumentBuilder build = db.newDocumentBuilder();
      Document doc = build.newDocument();

      Element root = doc.createElement("playlist");
      doc.appendChild(root);

      Element songs = doc.createElement("songs");
      root.appendChild(songs);

      ArrayList<Song> list = pl.songlist;
      for (Song s : list) {
        Element song = doc.createElement("song");
        songs.appendChild(song);

        Element title = doc.createElement("title");
        title.appendChild(doc.createTextNode(String.valueOf(s.getTitle())));
        song.appendChild(title);

        Element artist = doc.createElement("artist");
        artist.appendChild(doc.createTextNode(String.valueOf(s.getPerformer())));
        song.appendChild(artist);

        Element album = doc.createElement("album");
        album.appendChild(doc.createTextNode(String.valueOf(s.getAlbum())));
        song.appendChild(album);

        Element likes = doc.createElement("likes");
        likes.appendChild(doc.createTextNode(String.valueOf(s.getLikes())));
        song.appendChild(likes);
      }

      TransformerFactory tranFactory = TransformerFactory.newInstance();
      Transformer tf = tranFactory.newTransformer();

      tf.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
      tf.setOutputProperty(
              "{http://xml.apache.org/xslt}indent-amount", "4");
      tf.setOutputProperty(OutputKeys.INDENT, "yes");

      DOMSource sc = new DOMSource(doc);
      try {
        FileWriter fos = new FileWriter("././././outputPlaylist.xml");
        StreamResult result = new StreamResult(fos);
        tf.transform(sc, result);
      } catch (IOException e) {
        e.printStackTrace();
      }

    } catch (TransformerException ex) {
      System.out.println("Error outputting document");
    } catch (ParserConfigurationException ex) {
      System.out.println("Error building document");
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

  public static void displayAllSongs() {
    Connection connection = null;
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:musicManager.db");
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from songs");

      while (rs.next()){
        try {
          ID = rs.getInt("id");
          String name = rs.getString("name");
          System.out.println(
                  "["+ ID + "] "+ name + ""
          );
        } catch(SQLException e) {
          System.out.println("SQL Exception" + e.getMessage());
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void displayAllAlbums() {
    Connection connection = null;
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:musicManager.db");
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from albums");

      while (rs.next()){
        try {
          ID = rs.getInt("id");
          String name = rs.getString("name");
          System.out.println(
                  "["+ ID + "] "+ name + ""
          );
        } catch(SQLException e) {
          System.out.println("SQL Exception" + e.getMessage());
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void displayAllArtists() {
    Connection connection = null;
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:musicManager.db");
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from artists");

      while (rs.next()){
        try {
          ID = rs.getInt("id");
          String name = rs.getString("name");
          System.out.println(
                  "["+ ID + "] "+ name + ""
          );
        } catch(SQLException e) {
          System.out.println("SQL Exception" + e.getMessage());
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
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
