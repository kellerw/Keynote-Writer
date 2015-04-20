import java.util.*;

public class WordList extends ArrayList<String> {
  private String            strName;

  public WordList() {
    super();
    this.strName = "";
  }

  public WordList(String strName) {
    super();
    this.strName = strName;
  }

  public void addWord(String word) {
    add(word);
  }

  public String getName() {
    return strName;
  }

  public void setName(String strName) {
    this.strName = strName;
  }
  
  @Override
  public boolean contains(Object o) {
    String paramStr = (String)o;
    for (String s : this) {
      if(paramStr.equalsIgnoreCase(s))
        return true;
    }
    return false;
  }
}
