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
  public static final String        version = "Keynote Writer - version 1.07";
  
  private static Scanner        keyboard;
  private static String         startSent;
  private static String         endSent;
  private static List<String>   lstrSentences;
  private static List<String[]> lstrAnalyses;
  private static List<String>   lstrMissions;
  private static String         strAuthor;
  private static String         strQuote;
  private static String         strAnalysis;
  private static String         strText;
  private static int            nPage;
  private static int            nSentance;
  private static int            nAnalysis = 0;
  private static int            nStartPage;
  private static int            nPages;
  
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
    while(++nSentance < 0);
    nAnalysis = 0;
    if(nSentance >= lstrSentences.size())
      System.exit(0);
    
    strQuote     = lstrSentences.get(nSentance).replaceAll("^ ", "").replaceAll("\\.?\\s*$", "");
    nPage        = (int)(nPages * ((strText.indexOf(lstrSentences.get(nSentance)) + 1.0) / strText.length()) + 0.4 + nStartPage);
    lstrAnalyses = generateAnalyses();
    if(lstrAnalyses.size() == 0)
      nextQuote();
    else
      strAnalysis  = generateAnalysis(lstrAnalyses.get(nAnalysis)[0], lstrAnalyses.get(nAnalysis)[1]);
  }
  
  public static void nextQuote(int nQuote) {
    nSentance = nQuote-1;
    nextQuote();
  }
  
  public static void nextAnalysis() {
    while(++nAnalysis < 0);
    
    //System.out.printf("nAnalysis: %d\nSize:      %d\n\n", nAnalysis, lstrAnalyses.size());
    if(nAnalysis >= lstrAnalyses.size())
      nextQuote();
    else
      strAnalysis = generateAnalysis(lstrAnalyses.get(nAnalysis)[0], lstrAnalyses.get(nAnalysis)[1]);
  }
  
  public static void nextAnalysis(int nIndex) {
    nAnalysis = nIndex-1;
    nextAnalysis();
  }
  
  public static void save() {
    Writer.saveLine("keynotes.txt",
      String.format("\"%s\" (%s %d).\n\n%s\n\n---------------------------------\n",
        strQuote,
        strAuthor,
        nPage,
        strAnalysis));
  }

  public static void init() throws IOException, SAXException, ParserConfigurationException {
    String[] mission = Writer.getLines("assignment.txt");
    String fileName  = mission[0] + ".txt";
    strAuthor        = mission[1];
    startSent        = mission[2];
    endSent          = mission[3];
    nStartPage       = Integer.parseInt(mission[4]);
    nPages           = Integer.parseInt(mission[5]) - nStartPage;
    String[] lines   = Writer.getLines(fileName);
    
    strQuote    = "";
    strAnalysis = "";
    strText     = "";
    nPage       = 0;
    nSentance   = 0;
    
    load();
    keyboard = new Scanner(System.in);
    if (endSent.equals("end line")) {
      endSent = lines[lines.length - 1];
    }
    
    lstrMissions = Arrays.asList(mission).subList(6, mission.length);
    if(lstrMissions.contains("all"))
      lstrMissions = types;
    
    int j = 0;
    
    if (!startSent.matches("start line")) {
      while (lines[j].matches("(?i)" + startSent)) {
        j++;
      }
    }
    for(; j<lines.length; j++) {
      strText += lines[j] + " ";
    }
    
    lstrSentences = breakSentence(strText);
    nextQuote(0);
  }
  
  public static void gui() {
    frame = new JFrame("Keynote Writer");
    frame.setSize(490, 440);
    frame.setLocation(200, 100);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new KeynotePanel());
    frame.setVisible(true);
  }
  
  public static void cli() {
    while(!strQuote.matches("(?i)" + endSent)) {
      System.out.printf("\"%s\" (%s %.0f).\n",
        strQuote,
        strAuthor,
        nPage);
      System.out.print(strAnalysis);
      String response = keyboard.nextLine();
      if (!response.matches("")) {
        save();
        nextQuote();
      }
      else {
        nextAnalysis();
      }
      System.out.println();
    }
  }
  public static List<String[]> generateAnalyses() {
    List<String[]> analyses = new ArrayList<String[]>();
    for(String mission : lstrMissions) {
      analyses.addAll(Writer.matchesMission(mission, strQuote));
    }
    return analyses;
  }
  
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

