package org.example;

import junit.framework.TestCase;
import static org.example.Album.*;

public class AlbumTest extends TestCase {
  Album a1 = new Album("Ye");
  public void testGetArtist() {
    assertEquals(a1.getName(), "Ye");
  }
}