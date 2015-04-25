import java.io.*;
import java.util.*;
import org.xml.sax.*;
import javax.swing.JFrame;
import javax.xml.parsers.*;
import java.text.BreakIterator;
import org.xml.sax.helpers.DefaultHandler;

public class KeynoteWriter {
  public static ArrayList<WordList> lists;
  public static ArrayList<String>   types;
  public static JFrame              frame;
  public static boolean             gui = false;
  public static final String version = "Keynote Writer - version 1.06";
  
  private static String strAuthor;
  private static String strQuote;
  private static String strAnalysis;
  private static int    nPage;
  
  public static String getQuote() {
    return strQuote;
  }
  
  public static String getAuthor() {
    return strAuthor;
  }
  
  public static String getAnalysis() {
    return strAnalysis;
  }
  
  public static int getPage() {
    return nPage;
  }
  
  public static void setAnalysis(String strAnalysis) {
    KeynoteWriter.strAnalysis = strAnalysis;
  }
  
  public static void setPage(int nPage) {
    KeynoteWriter.nPage = nPage;
  }
  
  public static void nextQuote() {
    //TODO
  }
  
  public static void nextAnalysis() {
    //TODO
  }
  
  public static void save() {
    Writer.saveLine("keynotes.txt",
      String.format("\"%s\" (%s %d).\n\n%s\n\n---------------------------------\n\n",
        strQuote,
        strAuthor,
        nPage,
        strAnalysis));
  }

  public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
    String[] mission = Writer.getLines("assignment.txt");
    String fileName  = mission[0] + ".txt";
    String author    = mission[1];
    String startSent = mission[2];
    String endSent   = mission[3];
    int nStartPage   = Integer.parseInt(mission[4]);
    int nEndPage     = Integer.parseInt(mission[5]);
    int nPages       = nEndPage - nStartPage;
    String[] lines   = Writer.getLines(fileName);
    
    load();
    Scanner keyboard = new Scanner(System.in);
    if (endSent.equals("end line")) {
      endSent = lines[lines.length - 1];
    }
    
    for(String arg : args) {
      switch(arg) {
        case "-g":
        case "--gui":
          gui = true;
          
          frame = new JFrame("Keynote Writer");
          frame.setSize(460, 300);
          frame.setLocation(200, 100);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setContentPane(new KeynotePanel());
          frame.setVisible(true);
          break;
        case "-v":
        case "--version":
          System.out.println(version);
          System.exit(0);
          break;
        case "-l":
        case "--list-types":
          System.out.println("Options:\n  all");
          for(String type : types)
            System.out.printf(" %s\n", type);
          System.exit(0);
          break;
        case "-h":
        case "--help":
          System.out.printf("%s\n  Options:\n    -h/--help\n      print this help page and exit\n    -g/--gui\n      enable gui\n    -l/--list-types\n      list avalilible figurative language options and exit\n    -v/--version\n      print this version and exit\n", version);
          System.exit(0);
          break;
      }
    }
    
    List<String> missions = Arrays.asList(mission).subList(6, mission.length);
    if(missions.contains("all"))
      missions = types;
    
    int j = 0;
    String strText = "";
    if (!startSent.matches("start line")) {
      while (lines[j].matches("(?i)" + startSent)) {
        j++;
      }
    }
    for(; j<lines.length; j++) {
      strText += lines[j] + " ";
    }
    
    for (String strSentence : breakSentence(strText)) {
      if(strSentence.matches("(?i)" + endSent))
        break;
      //System.out.printf("%s\n=====================\n", strSentence);
      for (String type : missions) {
        for (String strSentence : breakSentence(lines[j])) {
          //System.out.printf("%s\n=====================\n", strSentence);
          if (!done) {
            String typeA[] = Writer.matchesMission(type, strSentence);
            if (!typeA[0].matches("nope")) {
              String keynote = strSentence;
              keynote = String.format("\"%s\" (%s %.0f).",
                                      keynote.replaceAll("^ ", "").replaceAll("\\.?\\s*$", ""),
                                      author,
                                      nPages * ((j + 1.0) / lines.length) + 0.4 + nStartPage);
              String finalkeynote = generateAnalysis(typeA[0], typeA[1]);
              System.out.print(keynote);
              System.out.print(finalkeynote);
              String response = keyboard.nextLine();
              if (!response.matches("")) {
                save();
                done = true;
              }
              System.out.println();
            }
          }
          System.out.println();
        }
      }
      /*
      System.out.println(strSentence);
      System.out.println();
      */
    }
  } /* main */
  
  public static WordList getList(String name) {
    for(WordList list : lists) {
      if(list.getName().equalsIgnoreCase(name))
        return list;
    }
    return new WordList();
  }
  
  public static void load() throws IOException, SAXException, ParserConfigurationException {
  	lists = new ArrayList<WordList>();
		types = new ArrayList<String>();
		
  	SAXParserFactory spfac = SAXParserFactory.newInstance();
    SAXParser sp = spfac.newSAXParser();
    
    DefaultHandler handler = new DefaultHandler() {
			WordList wordList;
			String temp;
			
			public void characters(char[] buffer, int start, int length) {
				temp = new String(buffer, start, length);
			}

			public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
				temp = "";
				if (qName.equalsIgnoreCase("list")) {
					wordList = new WordList();
					wordList.setName(attributes.getValue("name"));
				}
			}
			
			public void endElement(String uri, String localName, String qName) throws SAXException {
				if (qName.equalsIgnoreCase("word")) {
					wordList.addWord(temp);
				}
				else if (qName.equalsIgnoreCase("list")) {
					lists.add(wordList);
				}
			}
    };
    
    InputStream in = KeynoteWriter.class.getResourceAsStream("/lists.xml");
		sp.parse(in, handler);
		
		WordList toIgnore = getList("ignore");
		toIgnore.add("ignore");
		for(WordList list : lists) {
		  if(!toIgnore.contains(list.getName()))
		    types.add(list.getName().toLowerCase());
		}
		
		System.gc();//Try to delete no longer needed memory
  }
  
  public static String generateAnalysis(String analysisType, String option) {
    return Writer.randomWord("formats")
      .replaceAll("(?i)\\{author\\}",  strAuthor)
      .replaceAll("(?i)\\{use\\}",     Writer.randomWord("useWords"))
      .replaceAll("(?i)\\{type\\}",    analysisType)
      .replaceAll("(?i)\\{through\\}", Writer.randomWord("throughWords"))
      .replaceAll("(?i)\\{option\\}",  option);
  }
  
  public static List<String> breakSentence(String strText) {
    ArrayList<String> sentenceList = new ArrayList<String>();
    BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
    iterator.setText(strText);
    
    int start = iterator.first();
    for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
      String strTmp = strText.substring(start,end).replaceAll(" $", "");
      /*
      System.out.println("-----------");
      System.out.println(strTmp);
      System.out.println(strTmp.substring(strTmp.lastIndexOf(" ")+1) + ": " + hasAbbreviation(strTmp.substring(strTmp.lastIndexOf(" ")+1)));
      System.out.println("-----------");
      */
      
      /*While the last word in the /sentence/ is an abbreviation(Mr., Ms., Dr., ...)
       *OR the /sentence/ has an unmatched(uneven number of) double quotes,
       *expand it to the next /break point/
       */
      while(hasAbbreviation(strTmp.substring(strTmp.lastIndexOf(" ")+1)) || strTmp.replaceAll("[^\"]", "").length()%2 == 1) {
        end = iterator.next();
        if(end == BreakIterator.DONE)
          break;
        strTmp = strText.substring(start,end).replaceAll(" $", "");
      }
      //Add sentance to list, replacing double quotes to single quotes
      sentenceList.add(strTmp.replaceAll("\"", "\'"));
    }
    return sentenceList;
  }

  private static boolean hasAbbreviation(String sentence) {
    if (sentence == null || sentence.isEmpty()) {
      return false;
    }
    for (String w : getList("abbreviations")) {
      if (sentence.contains(w)) {
        return true;
      }
    }
    return false;
  }
}

