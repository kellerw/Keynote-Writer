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

  public static String[] matchesMission(String type, String line) {
    String[] response = new String[2];
    WordList options  = KeynoteWriter.getList(type);
    
    for (String option : options) {
      if (option.indexOf("$$") == -1) {
        System.out.printf("%s\n(?i)[^\\s-\\.;/.]%s\n\n", line, option);
        if (line.matches(String.format("(?i)[-^\\s\\.;/]%s", option))) {
          response[0] = type;
          response[1] = option;
          return response;
        }
      }
      else {
        String[] typeA = matchesMission(option.replace("$$", ""), line);
        if (!typeA[0].matches("nope")) {
          return typeA;
        }
      }
    }
    response[0] = "nope";
    response[1] = "nope";
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

