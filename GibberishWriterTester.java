/**Tester for the GibberishWriter class
  *@author Kha Dinh Luong  
  */

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

public class GibberishWriterTester {
  
  /**Test the WordData class*/
  @Test 
  public void wordDataTester() {
    GibberishWriter.WordData wordData = new GibberishWriter.WordData("Hello");
    assertEquals("Test the getWord() method", "Hello", wordData.getWord());
    assertEquals("Test the getCount() method", 1, wordData.getCount(), 0);
    wordData.incrementCount();
    assertEquals("Test the incrementCount() method", 2, wordData.getCount(), 0);
  }
  
  /**Test the Context class*/
  @Test
  public void contextTester() {
    GibberishWriter.Context context1 = new GibberishWriter.Context(new String[]{"Case", "Western", "Reserve", "EECS", "132"});
    GibberishWriter.Context context2 = new GibberishWriter.Context(new String[]{"Case", "Western", "Reserve", "EECS", "132"});
    GibberishWriter.Context context3 = new GibberishWriter.Context(new String[]{"Case", "Western", "EECS", "Reserve", "132"});
    GibberishWriter.Context context4 = new GibberishWriter.Context(new String[]{"Case", "Western", "Reserve", "EECS"});
    /*Test the length() method*/
    assertEquals("Test the length() method", 5, context1.length(), 0);
    /*Test the getWord() method*/
    assertEquals("Test the getWord() method, case 0", "Case", context1.getWord(0));
    assertEquals("Test the getWord() method, case 1", "Western", context1.getWord(1));
    assertEquals("Test the getWord() method, case many", "EECS", context1.getWord(3));
    /*Test the equals() method*/
    assertTrue("Test the equals() method, case identical contexts", context1.equals(context2));
    assertFalse("Test the equals() method, case same length, same elements, different order", context1.equals(context3));
    assertFalse("Test the equals() method, case different length", context1.equals(context4));
    /*Test the compareTo() method*/
    assertEquals("Test the compareTo() method, case indentical contexts", 0, context1.compareTo(context2));
    assertTrue("Test the compareTo() method, case same length, different element order", context1.compareTo(context3) > 0);
    assertTrue("Test the compareTo() method, case different length", context1.compareTo(context4) > 0);
    /*Test the toString() method*/
    assertEquals("Test the toString() method", "Case Western Reserve EECS 132", context1.toString());
  }
  
  /**Test the ContextData class*/
  /*There is no specific test for addFollowingWord(). The correctness of addFollowingWord() is reflected through the tests for other methods*/
  @Test
  public void contextDataTester() {
    GibberishWriter.Context context1 = new GibberishWriter.Context(new String[]{"apple", "orrange"}); 
    GibberishWriter.Context context2 = new GibberishWriter.Context(new String[]{"bean"}); 
    GibberishWriter.Context context3 = new GibberishWriter.Context(new String[]{"apple", "orrange"});
    GibberishWriter.ContextData contextData1 = new GibberishWriter.ContextData(context1); 
    GibberishWriter.ContextData contextData2 = new GibberishWriter.ContextData(context2);
    GibberishWriter.ContextData contextData3 = new GibberishWriter.ContextData(context3);
    /*Test the getContext() method*/
    assertEquals("Test the getContext() method, test 1", "apple orrange", contextData1.getContext().toString());
    assertEquals("Test the getContext() method, test 2", "bean", contextData2.getContext().toString());
    /*Test the compareTo() method*/
    assertTrue("Test the compareTo(), test 1", contextData1.compareTo(contextData3) == 0);
    assertTrue("Test the compareTo(), test 2", contextData1.compareTo(contextData2) < 0);
    assertTrue("Test the compareTo(), test 3", contextData2.compareTo(contextData1) > 0);
    /*Test the numOccurrences() method*/
    assertEquals("Test the numOccurrences() method, case initial value", 0, contextData1.numOccurrences());
    contextData1.addFollowingWord("fruit");
    contextData1.addFollowingWord("vitamin");
    contextData1.addFollowingWord("fruit");
    contextData1.addFollowingWord("healthy");
    assertEquals("Test the numOccurrences() method, case after adding words", 4, contextData1.numOccurrences());
    /*Test the getFollowingWord() method*/
    assertEquals("Test the getFollowingWord() method, test 1", "fruit", contextData1.getFollowingWord(1));
    assertEquals("Test the getFollowingWord() method, test 2", "fruit", contextData1.getFollowingWord(2));
    assertEquals("Test the getFollowingWord() method, test 3", "healthy", contextData1.getFollowingWord(3));
    assertEquals("Test the getFollowingWord() method, test 4", "vitamin", contextData1.getFollowingWord(4));
  }
  
  /**Test the GibberishWriter class*/
  @Test
  public void gibberishWriterTester() throws FileNotFoundException {
    GibberishWriter gb = new GibberishWriter(2);
    LinkedList<GibberishWriter.ContextData> list = new LinkedList<GibberishWriter.ContextData>();
    LLIterator<GibberishWriter.ContextData> it;
    list.addToEnd(new GibberishWriter.ContextData(new GibberishWriter.Context(new String[]{"Apple", "apple"})));
    list.addToEnd(new GibberishWriter.ContextData(new GibberishWriter.Context(new String[]{"Banana", "banana"})));
    list.addToEnd(new GibberishWriter.ContextData(new GibberishWriter.Context(new String[]{"Durian", "durian"})));
    /*Test the getContextSize() method*/
    assertEquals(2, gb.getContextSize(), 0); 
    /*Test the addContextData() method*/
    it = list.iterator();
    it.next();
    assertEquals("Test the addContextData() method, case input has already in the list", it.next(), gb.addContextData(new GibberishWriter.Context(new String[]{"Banana", "banana"}), list));
    assertEquals(3, list.length(), 0);
    gb.addContextData(new GibberishWriter.Context(new String[]{"Cat", "cat"}), list);
    it = list.iterator();
    it.next();
    it.next();
    assertTrue("Test the addContextData() method, case input has not been in the list", it.next().compareTo(new GibberishWriter.ContextData(new GibberishWriter.Context(new String[]{"Cat", "cat"}))) == 0);
    /*Test the scan() method*/
    String[] scannedText = new GibberishWriter(1).scan("ScanTest.txt");
    assertEquals("Case", scannedText[0]);
    assertEquals("Western", scannedText[1]);
    assertEquals("Reserve", scannedText[2]);
    assertEquals("University", scannedText[3]);
    assertEquals("EECS", scannedText[4]);
    assertEquals("132", scannedText[5]);
    /*Test the resetContext() method*/
    assertTrue(new GibberishWriter.Context(new String[]{"Reserve", "University", "EECS"}).compareTo(new GibberishWriter(1).resetContext(scannedText, 3, 3)) == 0);
    /*Test the addDataFile() method*/
    gb.addDataFile("AddDataFileTest.txt");
    assertEquals("Test the addDataFile() method, test 1", "a b", gb.getContextData(0).getContext().toString());
    assertEquals("Test the addDataFile() method, test 2", "b c", gb.getContextData(1).getContext().toString());
    assertEquals("Test the addDataFile() method, test 3", "b d", gb.getContextData(2).getContext().toString());
    assertEquals("Test the addDataFile() method, test 4", "c a", gb.getContextData(3).getContext().toString());
    assertEquals("Check number of occurrences, test 1", 2, gb.getContextData(0).numOccurrences());
    assertEquals("Check number of occurrences, test 2", 1, gb.getContextData(1).numOccurrences());
    assertEquals("Check number of occurrences, test 3", 1, gb.getContextData(2).numOccurrences());
    assertEquals("Check number of occurrences, test 4", 1, gb.getContextData(3).numOccurrences());
    /*Test the hasNext() method*/
    GibberishWriter gb2 = new GibberishWriter(2);
    assertFalse("Test the hasNext() method, case no element in the array list", gb2.hasNext());
    assertTrue("Test the hasNext() method, case there are elements in the array list", gb.hasNext());
  }
}