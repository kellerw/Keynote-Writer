//Andriy Zasypkin, 2014/05/08
//Unit 3 - GUIs
//Lab03  - Hailstones
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KeynotePanel extends JPanel {
  private JLabel      labelQuoteStart;
  private JLabel      labelQuoteEnd;
  private JTextField  inputPage;
  private JTextArea   inputAnalysis;
  
  public KeynotePanel() {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    
    //START Quote Panel 
    JPanel quotePanel = new JPanel();
    quotePanel.setLayout(new FlowLayout());
    add(quotePanel, c);
    
    //labelQuoteStart = new JLabel(String.format("\"%s\" (%s ", KeynoteWriter.getQuote(), KeynoteWriter.getAuthor()));
    labelQuoteStart = new JLabel("\"Quote\" (Author ");
    labelQuoteStart.setFont(new Font("Serif", Font.PLAIN, 15));
    quotePanel.add(labelQuoteStart);
    
    //inputPage = new JTextField(String.valueOf(KeynoteWriter.getPage()), 3);
    inputPage = new JTextField("", 3);
    inputPage.setHorizontalAlignment(SwingConstants.CENTER);
    quotePanel.add(inputPage);
    
    labelQuoteEnd = new JLabel(").");
    labelQuoteEnd.setFont(new Font("Serif",Font.PLAIN, 15));
    quotePanel.add(labelQuoteEnd);
    //END Quote Panel
    
    //START Buttons
    JButton nextAnalysisButton = new JButton("Re-Analyze");
    nextAnalysisButton.addActionListener(new NextListener());
    add(nextAnalysisButton, c);
    
    JButton addQuoteButton = new JButton("Add");
    addQuoteButton.addActionListener(new AddListener());
    add(addQuoteButton, c);
    
    JButton cancelQuoteButton = new JButton("Cancel");
    cancelQuoteButton.addActionListener(new CancelListener());
    add(cancelQuoteButton, c);
    //END Buttons
    
    //START Analysis Box
    //inputAnalysis = new JTextArea(KeynoteWriter.getAnalysis(), 70, 15);
    inputAnalysis = new JTextArea("An analysis", 70, 15);
    //inputAnalysis.setHorizontalAlignment(SwingConstants.LEFT);
    add(inputAnalysis, c);
    //END Analysis Box
    
  }
  
  private class NextListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      
    }
  }
  
  private class AddListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      
    }
  }
  
  private class CancelListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      
    }
  }
}
