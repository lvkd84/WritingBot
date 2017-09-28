/**A writer that help write out unintelligable, meaningless words based on patterns of writings from authors 
  *@author Kha Dinh Luong  
  */

import java.util.*;
import java.io.*;

public class GibberishWriter implements Iterator<String> {
  
  /**Data list on how words follow contexts in the original text*/  
  private ArrayList<ContextData> contextData;
  
  /**How many words in the context*/
  private int contextSize;
  
  /**The last context used in next() */
  private Context lastContext; 
  
  /**Constructor that creates a GibberishWriter with the input context size 
    *@param contextSize input size of the context  
    */ 
  public GibberishWriter(int contextSize) {
    this.contextSize = contextSize;
    contextData = new ArrayList<ContextData>(0);
  }
  
  /**Override the hasNext() method of the Iterator interface.
    *@return true if there is data inputted in the object GibberishWriter
    */
  @Override
  public boolean hasNext() {
    return contextData.size() > 0;   
  }
  
   /**Override the next() method of the Iterator interface
    *@return the next word to print out 
    */
  @Override
  public String next() {
    /*The random to choose the first context and the word that follow each context*/
    Random random = new Random();
    if (lastContext == null)
      lastContext = contextData.get(random.nextInt(contextData.size())).getContext();
    /*The context data of the current context*/
    ContextData lastContextData = new ContextData(lastContext);
    lastContextData = contextData.get((java.util.Collections.binarySearch(contextData, lastContextData)));
    /*The result string that will be return from this method*/
    String result = lastContextData.getFollowingWord(random.nextInt(lastContextData.numOccurrences()) + 1);
    /*The context of the next time next() is called*/
    String[] nextContext = new String[contextSize];
    /*Construct the context for the next time next() is called*/
    for (int i = 0; i < contextSize - 1; i++)
      nextContext[i] = lastContext.getWord(i + 1);
    nextContext[contextSize - 1] = result;
    lastContext = new Context(nextContext);
    return result;
  }
  
  /**The remove() method of the Iterator interface. Not supported*/
  @Override
  public void remove() {
    throw new UnsupportedOperationException();   
  }
    
  /**Get the context size
    *@return the size of the context  
    */
  public int getContextSize() {
    return contextSize; 
  }
  
  /**Get a particular context data stored 
    *@param index the index of the context data in the array list 
    *@return the context data at the index place in the array list 
    */
  public ContextData getContextData(int index) {
    return contextData.get(index); 
  }
  
  /**Add context data and return that context data  
    *@param context the context to be added
    *@param contextData the linked list that will store this context data
    *@return the context data added 
    */
  public static ContextData addContextData(Context context, LinkedList<GibberishWriter.ContextData> contextData) {
    /*The iterator for the input context data*/
    LLIterator<GibberishWriter.ContextData> it = contextData.iterator();
    /*The remember for the current context data that has just been returned by next()*/
    ContextData save;
    /*Use the iterator to search if the input context data is already in the list. If not, search for the place to put it in*/
    while (it.hasNext()) {
      save = it.next();
      if (save.getContext().equals(context))
        return save;
      if (save.getContext().compareTo(context) > 0) { 
        save = new ContextData(context);
        it.addBefore(save);
        return save;
      }
    }
    save = new ContextData(context);
    contextData.addToEnd(save);
    return save;
  }
  
  /** A helper method that scan through the input file and return its content 
   *@param filePath the name of the input file 
   *@return a array of string containing the words in the input in the exact order
   */
  public String[] scan(String filePath) throws FileNotFoundException {
    /*The scanner to scan through the file*/
    Scanner scanner = new Scanner(new File(filePath));
    /*The array list that store the words of the file*/
    ArrayList<String> text = new ArrayList<String>(0);
    /*Scan through the file with the scanner's iterator*/
    while (scanner.hasNext())
      text.add(scanner.next());
    String[] result = new String[text.size()];
    text.toArray(result);
    return result;
  }
  
  /** A helper method that reset the current context on each loop of the addDataFile() method 
    *@param text the array of string that contains data from the inptut
    *@param startPosition the position of the first word of the context on the text
    *@param contextSize the context size
    *@return the new current context 
    */
  public Context resetContext(String[] text, int startPosition, int contextSize) {
    /*The string array that store the reset context*/
    String[] returnContext = new String[contextSize]; 
    for (int i = 0 ; i < contextSize; i++)
      returnContext[i] = text[i + startPosition - 1];
    return new Context(returnContext);
  }
  
  /**Takes a file  as input and prepare the data for the GibberishWriter  
   *@param filePath the file name 
   */
  public void addDataFile(String filePath) throws FileNotFoundException {
    /*The linked list that stores the context data*/
    LinkedList<ContextData> contextDataList = new LinkedList<ContextData>(); 
    for (ContextData data : contextData)
      contextDataList.addToEnd(data);
    /*The array that stores the words from the input file*/
    String[] text = scan(filePath);
    /*The current context of the method*/
    Context currentContext = resetContext(text, 1, contextSize);
    /*The position of the at which the current context has slided to on the array that contains words from the input file*/
    int currentHeadPosition = contextSize;
    /*Loop through the array that contains words from the input file and add data to the GibberishWriter object*/
    while (currentHeadPosition < text.length - 1) {
      addContextData(currentContext, contextDataList).addFollowingWord(text[currentHeadPosition]);
      currentContext = resetContext(text, currentHeadPosition - contextSize + 2, contextSize);
      currentHeadPosition++;
    }
    /*Because the last context from the file does not have word to follow it. This case have to be done outside the loop*/
    addContextData(currentContext, contextDataList).addFollowingWord(text[currentHeadPosition]);
    contextData = contextDataList.toArrayList();
  }
  
  /**The main method*/
  public static void main(String[] args) {
    try {
      boolean valid = true;  
      if (args.length != 3) {
        System.out.println("Invalid input. Need 3 parameters: String, int, int");
        valid = false;
      }
      if (valid) {
        GibberishWriter gb = new GibberishWriter(Integer.parseInt(args[1]));
        gb.addDataFile(args[0]);
        if (!gb.hasNext()) 
          System.out.println("Found no material to read");
        for (int i = Integer.parseInt(args[2]); i > 0; i--) 
          System.out.print(gb.next() + " ");
        System.out.println();
      }
    } catch (NumberFormatException e1) {
      System.out.println("Invalid input. Need 3 parameters: String, int, int");
    } catch (FileNotFoundException e2) {
      System.out.println("File not found"); 
    }
  }
  
  /**The class that contains data for words of contexts  
   */
  public static class WordData {
    
    /**The word*/
    String word; 
    
    /**The number of time the word appear in the context*/
    int count; 
    
    /**Constructor that takes a string as input
      *@param word the word 
      */
    public WordData(String word) {
      this.word = word;
      this.count = 1;
    }
    
    /**Increse the count for this word by one*/
    public void incrementCount() {
      count++; 
    }
    
    /**Get the word
      *@return the word  
      */
    public String getWord() {
      return word; 
    }
    
    /**Get the number of time this word appears in a given context
      *@return the number of time the word appears
      */
    public int getCount() {
      return count;   
    }
  }
  
  /**The class that represents a context*/
  public static class Context implements Comparable<Context> {
    
    /**A array list that stores words in this context*/
    ArrayList<String> context = new ArrayList<String>(0);
    
    /**Constructor that takes an array of words as input and create a context out of those words*/
    public Context(String[] context) {
      /*Append the array list that store the words of the context*/
      for (int i = 0; i < context.length; i++)
        this.context.add(context[i]);
    }
    
    /**Get the length of the context
      *@return how many words are there in the context 
      */
    public int length() {
      return context.size(); 
    }
    
    /**Override the toString method of Object
      *@return a string that represents this context 
      */
    public String toString() {
      StringBuilder result = new StringBuilder();
      /*Append the string builder*/
      for (int i = 0; i < length() - 1; i++)
        result.append(context.get(i) + " ");
      result.append(context.get(length() - 1));
      return result.toString();
    }
    
    /**Return the word at a specific place of the context
      *@param index the place of the word in the context
      *@return the word at the specified place
      */
    public String getWord(int index) {
      return context.get(index); 
    }
    
    /**Override the equals() method of Object. 
      *@param comparingContext the context to compare to  
      *@return true if the two context are the same. Return false otherwise
      */
    @Override
    public boolean equals(Object comparingContext) {
      if (comparingContext instanceof Context) {
        /*The temporary context that saves the input object*/
        Context tempContext = (Context)comparingContext;
        if (this.length() != tempContext.length())
          return false;
        for (int i = 0; i < length(); i++)
          if (this.getWord(i).compareTo(tempContext.getWord(i)) != 0)
            return false;
        return true; 
      }
      return false;
    }
    
    /**Override the compareTo() context of the Comparable interface. Compare this context with the input context alphabetically 
      *@param comparingContext the context to compare to
      *@return an int < 0 if this context is lesser, 0 if this context is equal, and an int > 0 if this context is larger 
      */
    public int compareTo(Context comparingContext) {
      /*The result int that will be returned by this method*/
      int result = 0;
      /*The length of the shorter context*/
      int comparingRange = java.lang.Math.min(length(), comparingContext.length());
      /*Compare the two contexts word by word*/
      for (int i = 0; i < comparingRange; i++) {
        result = this.getWord(i).compareTo(comparingContext.getWord(i));
        if (result != 0)
          return result;
      }
      return length() - comparingContext.length();
    }
  }
  
  /**The class that store data for a context*/
  public static class ContextData implements Comparable<ContextData> {
    
    /**The context of which data is stored*/
    private Context context;
    
    /**The number of times this context appears in a text*/ 
    private int numOccurrences; 
    
    /**The list of words that can follow this context*/
    private LinkedList<WordData> followingWordsData;
    
    /**The constructor that get a context and create context data for that context 
      *@param context the input context
      */
    public ContextData(Context context) {
      this.context = context;
      this.numOccurrences = 0;
      this.followingWordsData = new LinkedList<WordData>();
    }
    
    /**Get the context
      *@return the context of this context data 
      */
    public Context getContext() {
      return context; 
    }
    
    /**Get the number of occurrence 
      *@return the number of time this context appears in the text
      */
    public int numOccurrences() {
      return numOccurrences; 
    }
     
    /**Override the compareTo() context of the Comparable interface. Compare two context data by their contexts
      *@param comparingContextData the context data to compare to 
      *@return an int < 0 if this context data is lesser, 0 if this context data is equal, and an int > 0 if this context data is larger
      */
    @Override
    public int compareTo(ContextData comparingContextData) {
      return context.compareTo(comparingContextData.getContext()); 
    }
    
    /**The method that add words that can follow the context of this context data object to this context data object
      *@param word the word to add 
      */
    public void addFollowingWord(String word) {
      /*The iterator for the followingWordData field*/
      LLIterator<WordData> it = followingWordsData.iterator();
      /*Whether the word to be added has been found in the followingWordsData or not*/
      boolean wordSearchStop = false;
      /*The local field to store the WordData value after each loop*/
      WordData save;
      /*Keep looping through the array that contains following words until the input word has been found*/
      while (wordSearchStop == false) {
        if (it.hasNext()) {
          save = it.next();
          if (save.getWord().compareTo(word) == 0) {
            save.incrementCount();
            wordSearchStop = true;
          }
          if (save.getWord().compareTo(word) > 0) {
            it.addBefore(new WordData(word));
            wordSearchStop = true;
          }
        } else {
          it.addAfter(new WordData(word));
          wordSearchStop = true; 
        }
      }
      numOccurrences++;
    }
    
    /**The method that get a word that follow this object. The word is specified by an int value 
      *@param value the index of the word 
      *@return the following word 
      */
    public String getFollowingWord (int value) {
      /*The index that increases after each loop to find the required word*/
      int sum = 0;
      /*The local field the find the current word data that is returned by next()*/
      WordData save = null;
      /*The iterator of the followingWordsData field*/
      LLIterator<WordData> it = followingWordsData.iterator();
      if (value <= 0)
        throw new IndexOutOfBoundsException();
      /*Use the iterator to loop through the array to find the required word*/
      while (sum < value) {
        if (it.hasNext() == false)
          throw new NoSuchElementException();
        save = it.next();
        sum += save.getCount(); 
      }
      return save.getWord();
    }
  }
}