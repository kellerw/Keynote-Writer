import java.util.*;
import java.io.*;

public class Writer
{
   public static String[] getLines(String filename){
      ArrayList<String> lines = new ArrayList<String>();
    
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
   }
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
   }
   public static void saveLine(String filename, String stuff_to_write) {
      String[] oldlines = getLines(filename);
      PrintWriter out = null;
      try {
         out = new PrintWriter(filename);
         for(int i = 0; i < oldlines.length; i++)
         {
            if(oldlines[i] != null)
            {
               out.println(oldlines[i]);
            }
         }
         stuff_to_write.replaceAll("   ","");
         out.println(stuff_to_write);
      }
      catch (Exception e) {
         System.out.println("Something went wrong");
      }
      finally {
         out.close();
      }
   }
   public static String[] matchesMission(String type, String line)
   {
      String[] response = new String[2];
      String[] options = getLines(type + ".txt");
      for(int i = 0; i < options.length; i++)
      {
         if(options[i].indexOf("$$") == -1)
         {
            if(line.indexOf(options[i]) > -1)
            {
               response[0] = type;
               response[1] = options[i];
               return(response);
            }
         }
         else
         {
            String[] typea = matchesMission(options[i].replace("$$", ""), line);
            if(!typea[0].matches("nope"))
            {
               return(typea);
            }
         }
      }
      response[0] = "nope";
      response[1] = "nope";
      return(response);
   }
   public static String randomWord(String file)
   {
      String[] options = getLines(file);
      return(options[(int)(Math.random()*options.length)]);    
   }
}