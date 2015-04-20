import java.io.*;
import java.util.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import java.text.BreakIterator;
import org.xml.sax.helpers.DefaultHandler;

public class KeynoteWriter {
  public static ArrayList<WordList> lists;
  public static ArrayList<String>   types;
  public static final String version = "Keynote Writer - version 1.06";
  
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
          System.out.printf("%s\n  Options:\n    -h/--help\n      print this help page and exit\n    -l/--list-types\n      list avalilible figurative language options and exit\n    -v/--version\n      print this version and exit\n", version);
          System.exit(0);
          break;
      }
    }
    
    String strTmp = "";
    for(String abbreviation : getList("abbreviations"))
      strTmp += abbreviation + "|\\b!";
    String strSplit = String.format("(?<!%s[A-Z])\\.\\s*", strTmp);
    
    List<String> missions = Arrays.asList(mission).subList(6, mission.length);
    if(missions.contains("all"))
      missions = types;
    
    for (String type : missions) {
      int j = 0;
      if (!startSent.matches("start line")) {
        while (lines[j].indexOf(startSent) == -1) {
          j++;
        }
      }
      boolean done = false;
      while (lines[j].indexOf(endSent) == -1) {
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
              String finalkeynote = generateAnalysis(author, typeA[0], typeA[1]);
              System.out.println(keynote);
              System.out.print(finalkeynote);
              String response = keyboard.nextLine();
              if (!response.matches("")) {
                finalkeynote = finalkeynote + response;
                Writer.saveLine("keynotes.txt", keynote);
                Writer.saveLine("keynotes.txt", finalkeynote);
                done = true;
              }
              System.out.println();
            }
          }
        }
        j++;
      }
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

		sp.parse("lists.xml", handler);
		
		WordList toIgnore = getList("ignore");
		toIgnore.add("ignore");
		for(WordList list : lists) {
		  if(!toIgnore.contains(list.getName()))
		    types.add(list.getName().toLowerCase());
		}
		
		System.gc();//Try to delete no longer needed memory
  }
  
  public static String generateAnalysis(String author, String analysisType, String option) {
    String strAnalysis = Writer.randomWord("formats")
      .replaceAll("{author}", author)
      .replaceAll("{use}",    Writer.randomWord("useWords"))
      .replaceAll("{type}", analysisType)
      .replaceAll("{through}", Writer.randomWord("throughWords"))
      .replaceAll("{option}", option);
    return strAnalysis;
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
      while(hasAbbreviation(strTmp.substring(strTmp.lastIndexOf(" ")+1))) {
        end = iterator.next();
        if(end == BreakIterator.DONE)
          break;
        strTmp = strText.substring(start,end).replaceAll(" $", "");
      }
      sentenceList.add(strText.substring(start,end));
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

