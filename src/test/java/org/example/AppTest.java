package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import static org.example.App.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
  public void testDisplayAllSongs() {
      displayAllSongs();
      Integer ID = getPreviousTableID("songs");
      assertTrue(ID > 0);
  }
  public void testDisplayAllAlbums() {
      displayAllAlbums();
    Integer ID = getPreviousTableID("albums");
    assertTrue(ID > 0);
  }
  public void testDisplayAllArtists() {
      displayAllArtists();
    Integer ID = getPreviousTableID("artists");
    assertTrue(ID > 0);
  }

//    public void testSearchSongs() {
//        searchSongs("coldplay");
//    }
}
