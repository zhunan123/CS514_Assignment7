package org.example;

import junit.framework.TestCase;

public class SongTest extends TestCase {

  public void testGetPerformer() {
    Song s1 = new Song("Yikes");
    assertEquals(s1.getName(), "Yikes");
  }
}