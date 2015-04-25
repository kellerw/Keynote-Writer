//Andriy Zasypkin, 2014/05/08
//Unit 3 - GUIs
//Lab03  - Hailstones
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelHailstones extends JPanel {
  private JLabel labelQuoteStart;
  private JLabel labelQuoteEnd;
  private JTextField inputPage;
  private JTextField inputAnalysis;
  
  public PanelHailstones() {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    
    //START Quote Panel 
    JPanel quotePanel = new JPanel();
    quotePanel.setLayout(new FlowLayout());
    add(quotePanel, c);
    
    labelQuoteStart = new JLabel(String.format("\"%s\" (%s ", KeynoteWriter.getQuote(), KeynoteWriter.getAuthor()));
    labelQuoteStart.setFont(new Font("Serif",Font.PLAIN, 15));
    quotePanel.add(labelQuoteStart);
    
    inputPage = new JTextField("0", 3);
    inputPage.setHorizontalAlignment(SwingConstants.CENTER);
    quotePanel.add(inputPage);
    
    labelQuoteEnd = new JLabel(").");
    labelQuoteEnd.setFont(new Font("Serif",Font.PLAIN, 15));
    quotePanel.add(labelQuoteEnd);
    //END Quote Panel
    
    //START Buttons
    JButton setButton = new JButton("Set");
    setButton.addActionListener(new setListener());
    panel.add(setButton, c);
    //END Buttons
    
    //START Analysis Box
    inputAnalysis = new JTextField("0", 3);
    inputAnalysis.setHorizontalAlignment(SwingConstants.LEFT);
    quotePanel.add(inputAnalysis);
    //END Analysis Box
    
  }
  private class setListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      int num = Integer.parseInt(userInputBox.getText());
      
      if(1 > num || num > 100)
        labelError.setText("ERROR - Number must be in range 1-100");
      else{
        nNum = num;
        labelError.setText("");
      }
      
      labelNumber.setText(String.format("%d", nNum));
      labelCounter.setText(String.format("%d", nCount=0));
    }
  }
  private class NextListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if(nNum % 2 == 0)
        nNum = nNum/2;
      else
        nNum = nNum*3 + 1;
      
      labelError.setText("");
      labelNumber.setText(String.format("%d", nNum));
      labelCounter.setText(String.format("%d", ++nCount));
    }
  }
  private class RandomListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      nNum = (int)(Math.random()*100 + 1);
      
      labelError.setText("");
      labelNumber.setText(String.format("%d", nNum));
      labelCounter.setText(String.format("%d", nCount=0));
    }
  }
}
