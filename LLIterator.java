/**The iterator specified for linked list
  *@author Kha Dinh Luong
  */

import java.util.*;

public interface LLIterator<E> extends Iterator<E> {
  
  /**Add a new LLNode that contains the input element before the LLNode that contains the last element returned by next
    *@param element the element to add to the list  
    */
  public void addBefore(E element);
  
  /**Add a new LLNode that contains the input element after the LLNode that contains the last element returned by next
    *@param element the element to add to the list  
    */
  public void addAfter(E element);
}