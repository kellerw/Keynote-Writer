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
  
  //Accessor methods 'nuff said
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
  
  //Set stuff used by gui
  public static void setAnalysis(String strAnalysis) {
    KeynoteWriter.strAnalysis = strAnalysis;
  }
  
  public static void setPage(int nPage) {
    KeynoteWriter.nPage = nPage;
  }
  
  //Looks for the next quote in text segment
  //  if next quote has no analysis - keeps searching(recusive)
  //  if no more quotes to look at  - exits w/ status 0
  public static void nextQuote() {
    while(++nSentance < 0);//To ensure index is non-negative - also adds 1 to index no mater what
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
  
  //Calls previouse function(method) but selects an excact quote to analyze
  //  subtracts one form index b/c nextQuote(void) adds one to index
  public static void nextQuote(int nQuote) {
    nSentance = nQuote-1;
    nextQuote();
  }
  
  //Selects next Analysis for current quote
  //  if none found - go on to next quote by calling nextQuote(void) - see nextQuote()
  public static void nextAnalysis() {
    while(++nAnalysis < 0);
    
    //System.out.printf("nAnalysis: %d\nSize:      %d\n\n", nAnalysis, lstrAnalyses.size());
    if(nAnalysis >= lstrAnalyses.size())
      nextQuote();
    else
      strAnalysis = generateAnalysis(lstrAnalyses.get(nAnalysis)[0], lstrAnalyses.get(nAnalysis)[1]);
  }
  
  //Same idea as nextQuote(int) but for analyses
  public static void nextAnalysis(int nIndex) {
    nAnalysis = nIndex-1;
    nextAnalysis();
  }
  
  //Saves current quote/citation/analysis as stored in static variable declared
  //  above into "keynotes.txt"
  public static void save() {
    Writer.saveLine("keynotes.txt",
      String.format("\"%s\" (%s %d).\n\n%s\n\n---------------------------------\n",
        strQuote,
        strAuthor,
        nPage,
        strAnalysis));
  }
  
  //Setup of class so that it is possible to start up gui/cli
  //  - load assignment
  //  - load text
  //  - load possible types of analyses
  //  - generate fist quote/analysis
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
    
    int j = -1;
    while (!lines[++j].matches(String.format("(?i).*%s.*", startSent)) && !startSent.matches("(?i)start line"));
    
    while(j<lines.length && !lines[j].matches(String.format("(?i).*%s.*", endSent)))
      strText += lines[j++] + " ";

    lstrSentences = breakSentence(strText);
    nextQuote(0);
  }
  
  //Start gui
  public static void gui() {
    frame = new JFrame("Keynote Writer");
    frame.setSize(490, 440);
    frame.setLocation(200, 100);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new KeynotePanel());
    frame.setVisible(true);
  }
  
  //Start cli
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
  
  //Generates and returns all possible analyses(as selected by user) for
  //  current quote and returns list
  public static List<String[]> generateAnalyses() {
    List<String[]> analyses = new ArrayList<String[]>();
    for(String mission : lstrMissions) {
      analyses.addAll(Writer.matchesMission(mission, strQuote));
    }
    return analyses;
  }
  
  //Find list of word(see lists.xml) by name of list and return
  //  if not found return empty default(nameless) list
  public static WordList getList(String name) {
    for(WordList list : lists) {
      if(list.getName().equalsIgnoreCase(name))
        return list;
    }
    return new WordList();
  }
  
  //Load xml file(from jar) into list of WordList(s)
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
  
  //Select a random format for formats(see '<list name="formats">' in lists.xml)
  //  and return after replacing place holders with current quote/analysis information
  public static String generateAnalysis(String analysisType, String option) {
    return Writer.randomWord("formats")
      .replaceAll("(?i)\\{author\\}",  strAuthor)
      .replaceAll("(?i)\\{use\\}",     Writer.randomWord("useWords"))
      .replaceAll("(?i)\\{type\\}",    analysisType)
      .replaceAll("(?i)\\{through\\}", Writer.randomWord("throughWords"))
      .replaceAll("(?i)\\{option\\}",  option);
  }
  
  //Split text selection - initialized in init()
  //  by sentances
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
  
  //Checks if string(usually last word of sentance is passed to this method)
  //  has an abbreviation in it(see '<list name="abbreviations">' in lists.xml)
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

