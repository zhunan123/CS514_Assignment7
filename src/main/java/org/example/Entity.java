package org.example;

import java.util.Date;

public class Entity {
  protected String name;
  protected static int counter = 0;
  public static int entityID;
  protected Date dateCreated;
  protected int nSongs;
  protected int nAlbums;

  public Entity() {
    this.name = "";
    counter++;
    this.entityID = counter;
    dateCreated = new Date();
    this.nSongs = 0;
    this.nAlbums = 0;
  }

  public boolean equals(Entity otherEntity) {
    return entityID == otherEntity.entityID;
  }


  public Entity(String name) {
    this.name = name;
    counter++;
    this.entityID = counter;
    dateCreated = new Date();
    this.nSongs = 0;
    this.nAlbums = 0;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String toString() {
    return "Name: " + this.name + " Entity ID: " + this.entityID;
  }
  public String toHTML() {
    return "<b>" + this.name + "</b><i> " + this.entityID + "</i>";
  }
  public String toXML() {
    return "<entity><name>" + this.name + "</name><ID> " + this.entityID + "</ID></entity>";
  }
}
