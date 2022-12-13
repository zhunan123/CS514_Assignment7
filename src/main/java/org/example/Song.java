package org.example;

public class Song extends Entity{
  protected Album album;
  protected Artist performer;
  protected String genre;
  protected String title;
  protected Integer likes;
  protected Integer BPM;

  public Song () {

  }

  public Song(String name) {
    super(name);
    album = new Album();
    performer = new Artist();
    genre = "";
    title = "";
    BPM = 0;
  }
  public Song(String name, int length) {
    super(name);
    genre = "";
    title = "";
    BPM = 0;
  }

  public void setLikes(int likes) {
    this.likes = likes;
  }

  public Integer getLikes() {
    return likes;
  }


  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setBPM(int BPM) {
    this.BPM = BPM;
  }

  public Integer getBPM() {
    return BPM;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }


  protected Album getAlbum() {
    return album;
  }

  protected void setAlbum(Album album) {
    this.album = album;
  }

  public Artist getPerformer() {
    return performer;
  }

  public void setPerformer(Artist performer) {
    this.performer = performer;
  }

  public String toString() {
    return "artist: " + this.performer + " " + "album: " + this.album + " " + "title: " + this.title + " " + "likes: " + this.likes + " " + "BPM: " + this.BPM;
  }
}
