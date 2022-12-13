package org.example;

import java.util.ArrayList;
import java.util.Collections;

public class Playlist {

  protected ArrayList<Song> songlist;

  public Playlist() {
    songlist = new ArrayList<Song>();
  }

  public void addSong(Song s) {
    songlist.add(s);
  }

  public void merge(Playlist other) {
    this.songlist.addAll(other.songlist);

    ArrayList<Song> newlist = new ArrayList<Song>();
    for (Song song : songlist) {
      if (!newlist.contains(song)) {
        newlist.add(song);
      } else {
        songlist.remove(song);
      }
    }
  }

  public void deleteSong(Song s) {
    if (songlist.contains(s)) {
      songlist.remove(s);
    } else {
      System.out.printf("%s is not in the playlist\n", s.toString());
    }
  }

  public void sort() {
    this.songlist.sort((l1, l2) -> l2.likes.compareTo(l1.likes));
  }

  public void shuffle() {
    Collections.shuffle(this.songlist);
  }

  public Playlist createPlaylistBasedBPM() {
    Playlist newPlist = new Playlist();

    this.songlist.removeIf(l -> l.BPM < 120);
    for (Song song : songlist) {
      newPlist.addSong(song);
    }

    Collections.shuffle(newPlist.songlist);
    return newPlist;
  }
}
