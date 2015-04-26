import javax.xml.parsers.*;
import org.xml.sax.*;
import java.io.*;

public class Main_GUI {
  public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
    KeynoteWriter.init();
    KeynoteWriter.gui();
  }
}
