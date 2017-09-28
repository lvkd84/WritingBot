/**Tester for the LinkedList class 
  *@author Kha Dinh Luong 
  */

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LinkedListTester {
  
  /**Test the LinkedList class*/
  @Test
  public void linkedListTester() {
    LinkedList<Integer> list = new LinkedList<Integer>();
    /*Test the isEmpty() method*/
    assertTrue("Test the isEmpty() method, case empty", list.isEmpty());
    list.addToFront(1);
    assertFalse("Test the isEmpty() method, case not empty", list.isEmpty()); //End of isEmpty() test//
    list.addToFront(2);
    list.addToFront(3);
    list.addToEnd(0);
    list.addToEnd(-1);
    list.addToEnd(-2);
    LLIterator<Integer> it = list.iterator();
    /*Test the addToFront() method*/
    assertEquals(3, it.next(), 0);
    assertEquals(2, it.next(), 0);
    assertEquals(1, it.next(), 0); //End of addToFront() test//
    /*Test the addToEnd() method*/
    assertEquals(0, it.next(), 0);
    assertEquals(-1, it.next(), 0);
    assertEquals(-2, it.next(), 0); //End of addToEnd() test//
    /*Test the length() method*/
    assertEquals("Test the length() medhod, case empty list", 0, new LinkedList<Integer>().length(), 0);
    assertEquals("Test the length() method", 6, list.length(), 0); //End of length() test//
    it = list.iterator();
    it.next();
    it.next();
    it.addBefore(4);
    it.addAfter(5);
    it = list.iterator();
    it.next();
    /*Test the addBefore() method*/
    assertEquals("Test the addBefore() method", 4, it.next(), 0); //End of addBefore() test//
    it.next();
    /*Test the addAfter() method*/
    assertEquals("Test the addAfter() method, case normal", 5, it.next(), 0);
    it = list.iterator();
    it.addAfter(6);
    assertEquals("Test the addAfter() method, case next has not been called", 6, it.next(), 0);
    LinkedList<Integer> emptyList = new LinkedList<Integer>();
    it = emptyList.iterator();
    it.addAfter(1);
    assertEquals("Test the addAfter() method, case empty list", 1, it.next(), 0); //End of addAfter() test//
    /*Test the toArrayList() method*/
    ArrayList<Integer> arrayList = new ArrayList<Integer>();
    arrayList.add(6);
    arrayList.add(3);
    arrayList.add(4);
    arrayList.add(2);
    arrayList.add(5);
    arrayList.add(1);
    arrayList.add(0);
    arrayList.add(-1);
    arrayList.add(-2);
    assertEquals("Test the toArrayList() method", arrayList, list.toArrayList()); //End of the toArrayList() test//
  } 
}