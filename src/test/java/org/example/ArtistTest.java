package org.example;

import junit.framework.TestCase;

public class ArtistTest extends TestCase {

  public void testGetSongs() {
    Artist a1 = new Artist("The Beatles");
    assertEquals(a1.getName(), "The Beatles");
  }
}