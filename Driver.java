import javax.swing.JFrame;

public class Driver {
  public static void main(String[] args) {
    JFrame frame = new JFrame("Hailstones");
    
    frame.setSize(350, 300);
    frame.setLocation(200, 100);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new KeynotePanel());
    frame.setVisible(true);
  }
}
