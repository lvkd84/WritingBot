/**The class represent linked lists. Each node of a linked list contains an element and a reference to the next node
  *@author Kha Dinh Luong  
  */

import java.util.*;

public class LinkedList<E> implements Iterable<E> {
  
  /**The first node of the linked list*/
  private LLNode<E> front;
  
  /**Constructor
    *Create a new empty linked list  
    */
  public LinkedList() {
    front = null;
  }
  
  /**Get the first node of the linked list 
    *@return thee first node of the linked list  
    */
  protected LLNode<E> getFront() {
    return front;
  }
  
  /**Set the first node of the linked list 
    *@param node the node to be set as front
    */
  protected void setFront(LLNode<E> node) {
    this.front = node;
  }
  
  /**Add a new LLNode that contains the input element in front of the linked list
    *@param element the element to add to the list  
    */
  public void addToFront(E element) {
    setFront(new LLNode<E>(element, getFront()));
  }
   
  /**Add a new LLNode that contains the input element to the end of the linked list
    *@param element the element to add to the list  
    */
  public void addToEnd(E element) {
    if (isEmpty()) 
      addToFront(element);
    else {
      LLNode<E> nodeptr = getFront();
      // the loop will end with nodeptr looking at the last node in list
      while (nodeptr.getNext() != null)
        nodeptr = nodeptr.getNext();
      nodeptr.setNext(new LLNode<E>(element, null));
    }
  }
  
  /**Check whether the linked list is empty or not 
    *@return true if the list is empty, false if the list is not empty 
    */
  public boolean isEmpty() {
    return (getFront() == null);
  }
  
  /**Get the length - number of nodes - of the linked list 
    *@return the length of the linked list  
    */
  public int length() {
    int count = 0;                      // counts number of nodes seen
    LLNode<E> nodeptr = getFront();
    while (nodeptr != null) {
      count++;
      nodeptr = nodeptr.getNext();
    }
    return count;
  }
  
   /**Get the iterator of the linked list 
    *@return an interator of type LLIterator  
    */
  @Override
  public LLIterator<E> iterator() {     
    return new LLIterator<E>() {                 /*Annonymous class defining LLIterator*/
      /*The node pointer of the iterator*/
      private LLNode<E> nodeptr = front;
      /*Save the last node returned by next*/
      private LLNode<E> save;
      /*The number of times next() was called*/
      private int nextCount = 0;
      /*Check whether the list still have a node after the last node returned by next()*/
      public boolean hasNext() {
        return nodeptr != null;
      }
      /*Return the element of the next node in the list*/
      public E next() {
        nextCount++; 
        save = nodeptr;
        nodeptr = nodeptr.getNext();
        return save.getElement();
      }
      /*Not supported method*/
      public void remove() {
        throw new UnsupportedOperationException();
      }
      /*Add a new LLNode that contains the input element before the LLNode that contains the last element returned by next*/
      public void addBefore(E element) {
        if ((front == null) && (nextCount == 0))
          throw new NoSuchElementException();
        else if (nextCount == 1) 
          addToFront(element);
        else {
          LLNode<E> addLocation = getFront();
          for (int i = 1; i <= nextCount - 2; i++)
            addLocation = addLocation.getNext();
          addLocation.setNext(new LLNode<E>(element, addLocation.getNext()));
        }
      }
      /*Add a new LLNode that contains the input element after the LLNode that contains the last element returned by next*/
      public void addAfter(E element) {
        if ((front == null) || (nextCount == 0)) {
          addToFront(element);
          nodeptr = front;
        } else {
          save.setNext(new LLNode<E>(element, save.getNext())); 
        }
      }
    };
  }
  
  /**Change the linked list to arraylist  
   *@return the ArrayList containing the elements of the linked list in the same order 
   */
  public ArrayList<E> toArrayList() {
    ArrayList<E> result = new ArrayList<E>(0);
    LLIterator<E> iterator = this.iterator();
    while(iterator.hasNext())
      result.add(iterator.next());
    return result;
  }
  
  /**The class that represents the node of the linked list*/
  public class LLNode<E> {
    
    /**The element stored in the node*/ 
    private E element;
  
    /**The node that follow this node*/
    private LLNode<E> next; 
  
    /**The constructor that take an element and a following node, and create this node
      *@param element the element of this node 
      *@param next the node that follow this node 
      */
    public LLNode(E element, LLNode<E> next) {
      this.element = element;
      this.next = next;
    }
    /**Get the element of this node 
      *@return the element of this node 
      */
    public E getElement() {
      return element;  
    }
  
    /**Get the node that follow this node 
      *@return the node that follow this node 
      */
    public LLNode<E> getNext() {
      return next; 
    }
    
    /**Set a new node to be the node that follow this node 
      *@param next the node to be set to follow this node 
      */
    public void setNext(LLNode<E> next) {
      this.next = next; 
    }
  }
}