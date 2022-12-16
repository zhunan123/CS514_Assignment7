package org.example;

import junit.framework.TestCase;

public class PlaylistTest extends TestCase {

  public void testAddSong() {
    Playlist playlist1 = new Playlist();
    Song s1 = new Song();
    s1 = new Song();
    Album album1 = new Album("The White Album");
    s1.setAlbum(album1);
    Artist artist1 = new Artist("The Beatles");
    s1.setPerformer(artist1);
    s1.setLikes(20);
    s1.setTitle("Back in the USSR");
    s1.setBPM(110);

    playlist1.addSong(s1);
    assertEquals(playlist1.songlist.size(), 1);
  }
}