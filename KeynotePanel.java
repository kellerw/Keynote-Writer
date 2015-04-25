import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KeynotePanel extends JPanel {
  private JLabel      labelQuoteStart;
  private JLabel      labelQuoteMiddle;
  private JLabel      labelQuoteEnd;
  private JTextField  inputPage;
  private JTextArea   inputAnalysis;
  
  public KeynotePanel() {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    
    //START Quote Panel 
    JPanel quotePanel = new JPanel();
    quotePanel.setLayout(new BorderLayout());
    c.fill        = GridBagConstraints.HORIZONTAL;
    c.gridx       =  0;
    c.gridy       =  0;
    c.gridwidth   =  2;
    c.gridheight  =  3;
    c.ipady       = 30;
    add(quotePanel, c);
    
    labelQuoteStart = new JLabel(String.format("<html><div style=\"width:250px;\">\"%s\"</div><html>", KeynoteWriter.getQuote()));
    //labelQuoteStart = new JLabel(String.format("<html><div style=\"text-align:justify;text-justify:inter-word;width:250px;\">%s</div><html>", "\"Oh, please. That doesn\'t even sound like him! The President\'s an idiot, you don\'t sound like an idiot. If you don\'t wanna talk to your mother, just avoid me like usual, huh? I\'ll just throw myself in traffic. I\'ll just Saran Wrap myself to the bed and pretend my child is suffocating me... \" "));
    labelQuoteStart.setFont(new Font("Serif", Font.PLAIN, 15));
    quotePanel.add(labelQuoteStart, BorderLayout.NORTH);
    
    JPanel citationPanel = new JPanel();
    citationPanel.setLayout(new FlowLayout());
    quotePanel.add(citationPanel, BorderLayout.CENTER);
    
    labelQuoteMiddle = new JLabel(String.format("(%s ", KeynoteWriter.getAuthor()));
    //labelQuoteMiddle = new JLabel("(Korben's Mother ");
    labelQuoteMiddle.setFont(new Font("Serif", Font.PLAIN, 15));
    citationPanel.add(labelQuoteMiddle);
    
    inputPage = new JTextField(String.valueOf(KeynoteWriter.getPage()), 3);
    inputPage = new JTextField("96", 3);
    inputPage.setHorizontalAlignment(SwingConstants.CENTER);
    citationPanel.add(inputPage);
    
    labelQuoteEnd = new JLabel(").");
    labelQuoteEnd.setFont(new Font("Serif",Font.PLAIN, 15));
    citationPanel.add(labelQuoteEnd);
    //END Quote Panel
    
    //START Buttons
    JButton nextAnalysisButton = new JButton("Re-Analyze");
    nextAnalysisButton.addActionListener(new NextListener());
    c.fill        = GridBagConstraints.HORIZONTAL;
    c.anchor      = GridBagConstraints.NORTH;
    c.gridx       = 3;
    c.gridy       = 0;
    c.gridwidth   = 1;
    c.gridheight  = 1;
    c.ipady       = 1;
    add(nextAnalysisButton, c);
    
    JButton addQuoteButton = new JButton("Add");
    addQuoteButton.addActionListener(new AddListener());
    c.fill        = GridBagConstraints.HORIZONTAL;
    c.anchor      = GridBagConstraints.NORTH;
    c.gridx       = 3;
    c.gridy       = 1;
    c.gridwidth   = 1;
    c.gridheight  = 1;
    c.ipady       = 1;
    add(addQuoteButton, c);
    
    JButton cancelQuoteButton = new JButton("Cancel");
    cancelQuoteButton.addActionListener(new CancelListener());
    c.fill        = GridBagConstraints.HORIZONTAL;
    c.anchor      = GridBagConstraints.NORTH;
    c.gridx       = 3;
    c.gridy       = 2;
    c.gridwidth   = 1;
    c.gridheight  = 1;
    c.ipady       = 1;
    add(cancelQuoteButton, c);
    //END Buttons
    
    //START Analysis Box
    inputAnalysis = new JTextArea(KeynoteWriter.getAnalysis());
    //inputAnalysis = new JTextArea("An analysis");
    inputAnalysis.setLineWrap(true);
    inputAnalysis.setWrapStyleWord(true);
    inputAnalysis.setRows(5);
    JScrollPane scrollPane = new JScrollPane(inputAnalysis);
    c.fill        = GridBagConstraints.BOTH;
    c.gridx       = 0;
    c.gridy       = 4;
    c.gridwidth   = 0;
    c.ipady       = 0;
    add(scrollPane, c);
    //END Analysis Box
    
  }
  
  private class NextListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      KeynoteWriter.nextAnalysis();
      inputAnalysis.setText(KeynoteWriter.getAnalysis());
    }
  }
  
  private class AddListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      KeynoteWriter.save();
      KeynoteWriter.nextQuote();
      
      labelQuoteStart.setText(String.format("<html><div style=\"width:250px;\">\"%s\" %s</div><html>", KeynoteWriter.getQuote()));
      inputPage.setText(String.valueOf(KeynoteWriter.getPage()));
      inputAnalysis.setText(KeynoteWriter.getAnalysis());
    }
  }
  
  private class CancelListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      KeynoteWriter.nextQuote();
      
      labelQuoteStart.setText(String.format("\"%s\" ", KeynoteWriter.getQuote()));
      inputPage.setText(String.valueOf(KeynoteWriter.getPage()));
      inputAnalysis.setText(KeynoteWriter.getAnalysis());
    }
  }
}
