import java.util.*;

public class WordList {
  private String            strName;
  private ArrayList<String> strWords;
  
  public WordList() {
    this.strName = "";
    this.strWords = new ArrayList<String>();
  }
  
  public WordList(String strName) {
    this.strName = strName;
    this.strWords = new ArrayList<String>();
  }
  
  public ArrayList<String> getWords() {
    return strWords;
  }
  
  public void addWord(String word) {
    strWords.add(word);
  }
  
  public String get(int i) {
    return strWords.get(i);
  }
  
  public int size() {
    return strWords.size();
  }
  
  public String getName() {
    return strName;
  }
  
  public void setName(String strName) {
    this.strName = strName;
  }
}
