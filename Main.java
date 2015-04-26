import javax.xml.parsers.*;
import org.xml.sax.*;
import java.io.*;

public class Main {
  public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
    boolean gui = false;
    KeynoteWriter.init();
    
    for(String arg : args) {
      switch(arg) {
        case "-g":
        case "--gui":
          gui = true;
          break;
        case "-v":
        case "--version":
          System.out.println(KeynoteWriter.version);
          System.exit(0);
          break;
        case "-l":
        case "--list-types":
          System.out.println("Options:\n  all");
          for(String type : KeynoteWriter.types)
            System.out.printf(" %s\n", type);
          System.exit(0);
          break;
        case "-h":
        case "--help":
          System.out.printf("%s\n  Options:\n    -h/--help\n      print this help page and exit\n    -g/--gui\n      enable gui\n    -l/--list-types\n      list avalilible figurative language options and exit\n    -v/--version\n      print this version and exit\n", KeynoteWriter.version);
          System.exit(0);
          break;
      }
    }
    
    if(gui)
      KeynoteWriter.gui();
    else
      KeynoteWriter.cli();
  }
}
