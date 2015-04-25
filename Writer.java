import java.io.*;
import java.util.*;

public class Writer {
  public static String[] getLines(String filename) {
    ArrayList <String> lines = new ArrayList <String> ();

    try {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      String line;
      while ((line = br.readLine()) != null) {
        lines.add(line);
      }
      br.close();
    }
    catch (Exception e) {
      return new String[1];
    }

    return lines.toArray(new String[0]);
  } /* getLines */

  public static void save(String filename, String stuff_to_write) {
    PrintWriter out = null;

    try {
      out = new PrintWriter(filename);
      out.println(stuff_to_write);
    }
    catch (Exception e) {
      System.out.println("Something went wrong");
    }
    finally {
      out.close();
    }
  } /* save */

  public static void saveLine(String filename, String stuff_to_write) {
    PrintWriter out = null;
    try {
      out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));;
      out.println(stuff_to_write.replaceAll(" +", " "));
    }
    catch (Exception e) {
      System.out.println("Something went wrong");
    }
    finally {
      out.close();
    }
  } /* saveLine */

  public static List<String[]> matchesMission(String type, String line) {
    List<String[]> response = new ArrayList<String[]>();
    WordList options  = KeynoteWriter.getList(type);
    
    for (String option : options) {
      if (option.matches("^\\$\\$")) {
        response.addAll(matchesMission(option.replace("$$", ""), line));
      }
      else {
        //System.out.printf("%s\n(?i)[-^\\s\\.;/.]%s\n\n", line, option);
        if (line.matches(String.format("(?i).*[-^\\s\\.;/]%s[-^\\s\\.;/].*", option))) {
          response.add(new String[] {type, option});
        }
      }
    }
    return response;
  } /* matchesMission */

  public static String randomWord(String[] options) {
    return options[(int)(Math.random() * options.length)];
  } /* randomWord */

  public static String randomWord(ArrayList<String> options) {
    return options.get((int)(Math.random() * options.size()));
  } /* randomWord */

  public static String randomWord(WordList options) {
    return options.get((int)(Math.random() * options.size()));
  } /* randomWord */

  public static String randomWord(String name) {
    WordList options = KeynoteWriter.getList(name);
    return options.get((int)(Math.random() * options.size()));
  } /* randomWord */
}

