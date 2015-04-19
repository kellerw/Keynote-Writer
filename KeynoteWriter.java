//v. 1.03
import java.util.*;
import java.io.*;

public class KeynoteWriter
{
   public static void main(String[] args) 
   {
      String[] mission = Writer.getLines("_assignment - Copy.txt");
      String fileName = mission[0] + ".txt";
      String author = mission[1];
      String startsent = mission[2];
      String endsent = mission[3];
      int startpage =  Integer.parseInt(mission[4]);
      int endpage =  Integer.parseInt(mission[5]);
      int pages = endpage - startpage;
      String[] lines = Writer.getLines(fileName);
      Scanner keyboard = new Scanner(System.in);
      if(endsent.equals("end line"))
         {
            endsent = lines[lines.length-1];
         }
      for(int i = 6; i < mission.length; i++)
      {
         String type = mission[i];
         int j = 0;
         if(!startsent.matches("start line"))
         {
            while(lines[j].indexOf(startsent)==-1)
            {
               j++;
            }
         }
         boolean done = false;
         while(lines[j].indexOf(endsent)==-1)
         {
            String[] line = lines[j].split("\\.");
            for(int k = 0; k < line.length; k++)
            {
               if(!done)
               {
                  String typea[] = Writer.matchesMission(type, line[k]);
                  if(!typea[0].matches("nope"))
                  {
                     String keynote = line[k].replaceAll("\\.", "");
                     keynote = keynote.replaceAll("^ ", "");
                     keynote = keynote + "(" + author + " " + String.valueOf((int)((pages * ((j + 1.0)/(lines.length + 0.0))) + 0.4 + startpage)) + ").";
                     String finalkeynote = author + " " + Writer.randomWord("useWords.txt") + " " 
                        + typea[0] + " " + Writer.randomWord("throughWords.txt") + " the " + typea[1]
                        + " ";
                     System.out.println(keynote);
                     System.out.println(finalkeynote);
                     String response = keyboard.nextLine();
                     if(!response.matches(""))
                     {
                        finalkeynote = finalkeynote + response;
                        Writer.saveLine("_keynotes.txt", keynote);
                        Writer.saveLine("_keynotes.txt", finalkeynote);
                        done = true;
                     }
                  }
               }
            }
            j++;
         }
      } 
   }
}