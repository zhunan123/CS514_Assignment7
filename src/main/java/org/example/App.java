package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.*;

public class App {

  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_GREEN = "\u001B[32m";
  private static final String ANSI_PURPLE = "\u001B[35m";
  private static final String ANSI_CYAN = "\u001B[36m";
  private static int ID = 0;

  public static void startProgram() {
    System.out.println(
            ":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::" + "\n" +
                    ":::::::::::::::" + ANSI_PURPLE + "Welcome to Zhunan's Music Manager APP" + ANSI_RESET +":::::::::::::::::::" + "\n" +
                    "::::::::::::::" + ANSI_GREEN + "If you are existed User, Please Press 1" + ANSI_RESET + "::::::::::::::::::" + "\n" +
                    ":::::::::::::" + ANSI_CYAN + "If you are New User, Please Press 2" + ANSI_RESET + ":::::::::::::::::::::::" + "\n" +
                    ":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::"
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
                ":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::" + "\n" +
                        "::::::::::::::" + ANSI_PURPLE + "" + username.toUpperCase() + ", " + ANSI_RESET + "Welcome Back to Music Manager::::::::::::::::::::" + "\n" +
                        ":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::" + "\n" +
                        ":::::>>> " + ANSI_CYAN + "Press 1 to see all the songs/albums/artists available in the song store" + ANSI_RESET + "\n" +
                        ":::::>>> " + ANSI_GREEN + "Press 2 to generate the playlist in XML files and play all the songs" + ANSI_RESET + "\n" +
                        ":::::>>> " + ANSI_CYAN + "Press 3 if you want to search new songs" + ANSI_RESET
        );
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(">> ");
        String s1 = null;
        try {
          s1 = br1.readLine();
          if (s1.equals("1")) {
            System.out.println(":::::::::::::Below is all the songs available:::::::::::::");
            displayAllSongs();
            System.out.println(":::::::::::::Below is all the albums available:::::::::::::");
            displayAllAlbums();
            System.out.println(":::::::::::::Below is all the artists available:::::::::::::");
            displayAllArtists();
            System.out.println(":::::::::::::What would you like to do next? Generate all songs we have in playList? Enter(Y/N): ");
            System.out.print("> ");
            String s2 = br1.readLine();
            if (s2.equals("Y")) {
              generatePlaylist();
            }
            System.out.println(":::::::::::::What would you like to do next? Do you want to search for other songs? Enter(Y/N): ");
            System.out.print("> ");
            String s3 = br1.readLine();
            if (s3.equals("Y")) {
              searchSongsInGeneral();
            }
          }

          if (s1.equals("2")) {
            generatePlaylist();
          }
          if (s1.equals("3")) {
            searchSongsInGeneral();
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void searchSongsInGeneral() {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println(
            ":::::::::::::How Do you want to search for songs?:::::::::::::" + "\n" +
                    "[1] Artist's name" + "\n" +
                    "[2] Song's title" + "\n" +
                    "[3] Album's name" + "\n"
    );
    System.out.print(">> Please Enter your response: ");
    String s2 = null;
    try {
      s2 = br.readLine();
      if (s2.equals("1")){
        System.out.println(
                ":::::::::::::Those are the available artists, if not please enter other artist's name:::::::::::::"
        );
        displayAllArtists();
        System.out.print(">> Please Enter the artist name you want to search: ");
        String s3 = br.readLine();
        searchAllAlbums(s3);
      }
      if (s2.equals("2")){
        System.out.println(
                ":::::::::::::Those are the available songs, if not please enter other artist's name:::::::::::::"
        );
        displayAllSongs();
        System.out.println(">> Please Enter the artist's name you want to search: (Please in this case only use coldplay for testing since AudioDB API only support Coldplay testing to search for songs)");
        System.out.print(">> ");
        String s3 = br.readLine();
        System.out.println(":::::::::::::Below is the song list of "+ s3 +":::::::::::::");
        searchSongs(s3);
      }
      if (s2.equals("3")){
        System.out.println(
                ":::::::::::::Those are the available albums, if not please enter other album's name:::::::::::::"
        );
        displayAllAlbums();
        System.out.print(">> Please Enter the artist's name you want to search for albums: ");
        String s3 = br.readLine();
        searchAllAlbums(s3);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void searchSongs(String artistName) {
    String requestURL = "https://www.theaudiodb.com/api/v1/json/2/search.php?s=";
    StringBuilder response = new StringBuilder();
    URL u;
    try {
      u = new URL(requestURL + artistName);
    } catch (MalformedURLException e) {
      System.out.println("Malformed URL");
      return;
    }

    try {
      URLConnection connection = u.openConnection();
      HttpURLConnection httpConnection = (HttpURLConnection) connection;
      int code = httpConnection.getResponseCode();

      String message = httpConnection.getResponseMessage();
      if (code != HttpURLConnection.HTTP_OK) {
        return;
      }
      InputStream instream = connection.getInputStream();
      Scanner in = new Scanner(instream);
      while (in.hasNextLine()) {
        response.append(in.nextLine());
      }
    } catch (IOException e) {
      System.out.println("Error reading response");
      return;
    }

    try {
      //get Artist ID
      JSONParser parser = new JSONParser();
      Object obj = parser.parse(response.toString());
      JSONObject jsonObject = (JSONObject) obj;
      JSONArray artists = (JSONArray)jsonObject.get("artists");
      JSONObject artistDetail = (JSONObject) artists.get(0);

      String ArtistID = String.valueOf(artistDetail.get("idArtist"));

      //get Album ID
      String requestURL2 = "https://www.theaudiodb.com/api/v1/json/2/album.php?i=";
      StringBuilder response2 = new StringBuilder();
      URL u2;
      try {
        u2 = new URL(requestURL2 + ArtistID);
      } catch (MalformedURLException e) {
        System.out.println("Malformed URL");
        return;
      }

      try {
        URLConnection connection = u2.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) connection;
        int code = httpConnection.getResponseCode();

        String message = httpConnection.getResponseMessage();
        if (code != HttpURLConnection.HTTP_OK) {
          return;
        }
        InputStream instream = connection.getInputStream();
        Scanner in = new Scanner(instream);
        while (in.hasNextLine()) {
          response2.append(in.nextLine());
        }
      } catch (IOException e) {
        System.out.println("Error reading response");
        return;
      }
      try {
        Map<Integer, String> map2 = new HashMap<>();
        //get albumID
        JSONParser parser2 = new JSONParser();
        Object obj2 = parser2.parse(response2.toString());
        JSONObject jsonObject2 = (JSONObject) obj2;
        JSONArray albums = (JSONArray)jsonObject2.get("album");

        for (int i = 1; i < albums.size(); i++) {
          JSONObject album = (JSONObject) albums.get(i);
          map2.put(i, String.valueOf(album.get("idAlbum")));
        }

        //get Trackname
        String requestURL3 = "https://www.theaudiodb.com/api/v1/json/2/track.php?m=";
        StringBuilder response3 = new StringBuilder();
        URL u3;
        try {
          for (int i = 1; i < map2.size(); i++) {
            u3 = new URL(requestURL3 + map2.get(i));
            try {
              URLConnection connection = u3.openConnection();
              HttpURLConnection httpConnection = (HttpURLConnection) connection;
              int code = httpConnection.getResponseCode();

              String message = httpConnection.getResponseMessage();
              if (code != HttpURLConnection.HTTP_OK) {
                return;
              }
              InputStream instream = connection.getInputStream();
              Scanner in = new Scanner(instream);
              while (in.hasNextLine()) {
                response3.append(in.nextLine());
              }
            } catch (IOException e) {
              System.out.println("Error reading response");
              return;
            }
            try {
              Map<Integer, String> map3 = new HashMap<>();
              Map<Integer, String> map4 = new HashMap<>();
              JSONParser parser3 = new JSONParser();
              Object obj3 = parser3.parse(response3.toString());
              JSONObject jsonObject3 = (JSONObject) obj3;
              JSONArray tracks = (JSONArray)jsonObject3.get("track");
              for (int j = 1; j < tracks.size(); j++) {
                JSONObject track = (JSONObject) tracks.get(j);
                map3.put(j, String.valueOf(track.get("strTrack")));
                map4.put(j, String.valueOf(track.get("strAlbum")));
                System.out.println(" ["+ j +"] "+ track.get("strTrack") +" ");
              }

              System.out.println(":::::::::::::Which song you want to listen?:::::::::::::");
              BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
              String ans = null;
              try {
                System.out.print("Please enter song number: >>");
                ans = br.readLine();
                String track = map3.get(Integer.parseInt(ans));
                String album = map4.get(Integer.parseInt(ans));
                System.out.println("::::::::::::::::::::::::::::::::::");
                System.out.println(ANSI_CYAN + "♫♪˙‿˙♫♪" + ANSI_RESET + "Lets play [ "+ track +" - "+ artistName +" ]" + ANSI_CYAN + "♫♪˙‿˙♫♪" + ANSI_RESET );
                System.out.println(">> Do you want to add this song to your playlist? Enter Y/N");
                System.out.print(">>");

                BufferedReader br3 = new BufferedReader(new InputStreamReader(System.in));
                String ans2 = null;
                try {
                  ans2 = br3.readLine();
                  if (ans2.equals("Y")) {
                    addSongToPlaylist(track, album, artistName);
                    System.out.println("selected track has been successfully added to playlist, would you like to generate playlist again? Enter (Y/N)");
                    System.out.print(">> ");
                    BufferedReader br4 = new BufferedReader(new InputStreamReader(System.in));
                    String ans3 = null;
                    try {
                      ans3 = br4.readLine();
                      if (ans3.equals("Y")) {
                        generatePlaylist();
                        System.out.println("New playlist has been successfully generated!");
                      }
                    } catch (IOException e) {
                      throw new RuntimeException(e);
                    }
                  }
                }catch (IOException e) {
                  throw new RuntimeException(e);
                }
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            } catch(ParseException e) {
              return;
            }
          }
        } catch (MalformedURLException e) {
          System.out.println("Malformed URL");
          return;
        }
      } catch(ParseException e) {
        System.out.println("Error parsing JSON2");
        return;
      }
    } catch(ParseException e) {
      System.out.println("Error parsing JSON3");
      return;
    }
  }

  private static void addSongToPlaylist(String track, String albumname, String artistname) {

    Connection connection = null;
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:musicManager.db");
      Statement statement = connection.createStatement();
      ID = getPreviousTableID("songs");
      ID++;
      statement.executeUpdate("insert into songs (id, name, album, artist) values ("+ ID + ", '"+ track +"', '"+ albumname +"', '"+ artistname +"');");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static void searchAllAlbums(String artistName) {
    String requestURL = "https://www.theaudiodb.com/api/v1/json/2/discography.php?s=";
    StringBuilder response = new StringBuilder();
    URL u;
    try {
      u = new URL(requestURL + artistName);
    } catch (MalformedURLException e) {
      System.out.println("Malformed URL");
      return;
    }
    try {
      URLConnection connection = u.openConnection();
      HttpURLConnection httpConnection = (HttpURLConnection) connection;
      int code = httpConnection.getResponseCode();

      String message = httpConnection.getResponseMessage();
      System.out.println("Search all albums is successful!");
      if (code != HttpURLConnection.HTTP_OK) {
        return;
      }
      InputStream instream = connection.getInputStream();
      Scanner in = new Scanner(instream);
      while (in.hasNextLine()) {
        response.append(in.nextLine());
      }
    } catch (IOException e) {
      System.out.println("Error reading response");
      return;
    }
    try {
      Map<Integer, String> map = new HashMap<>();
      JSONParser parser = new JSONParser();
      Object obj = parser.parse(response.toString());
      JSONObject jsonObject = (JSONObject) obj;
      JSONArray artists = (JSONArray)jsonObject.get("album");
      System.out.println(":::::::::::::Below are all available albums related to "+ artistName +" :::::::::::::");
      for (int i = 1; i < artists.size(); i++) {
        JSONObject album = (JSONObject) artists.get(i);
        map.put(i, String.valueOf(album.get("strAlbum")));
        System.out.println(" ["+ i +"] "+ album.get("strAlbum") +" ");
      }
      System.out.print(">> Please enter album number to see years created: ");
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      String s5 = null;
      try {
        s5 = br.readLine();
        for (int i = 1; i <= map.size(); i++) {
          if (s5.equals("" + i + "")) {
            JSONObject album = (JSONObject) artists.get(i);
            System.out.println(""+ album.get("intYearReleased") +"");
          }
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      System.out.print("Do You want to search for "+ artistName +"'s other songs?: (Y/N): ");
      try {
        String s6 = br.readLine();
        if (s6.equals("Y")) {
          System.out.println("Please enter artist's name you like to search: (Please in this case only use coldplay for testing since AudioDB API only support Coldplay testing to search for songs)" );
          System.out.print(">> ");
          try {
            String s7 = br.readLine();
            System.out.println(":::::::::::::Below is the song list of "+ s7 +":::::::::::::");
            System.out.println("You have successfully search for "+ s7 +"'s songs");
            searchSongs(s7);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } catch(ParseException e) {
      System.out.println("Error parsing JSON");
      return;
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
