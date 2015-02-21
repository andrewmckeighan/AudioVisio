package audiovisio.gui;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Main {
	
	JPanel panel = new JPanel();//different screens
    JPanel main = new JPanel();
    JPanel joinscn = new JPanel();
    JPanel hostscn = new JPanel();
    JPanel settings = new JPanel();
    
    JButton host = new JButton("Host");//different buttons to access other screens
    JButton join = new JButton("Join");
    JButton sett = new JButton("Settings");
    JButton back = new JButton("Back");
    JButton submit = new JButton("Submit");
    
    
    CardLayout c = new CardLayout();//dont quite understand this completely but it helps
    								//manage the panels and buttons
    
  Main() throws UnknownHostException {
    JFrame f = new JFrame("Audio Visio"); //new window
    f.setSize(1000, 700);


    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    
    panel.setLayout(c);
    main.add(join);
    main.add(host);
    main.add(sett);
   
    
    
    panel.add(main, "1");//adds the screens to the window
    panel.add(joinscn, "2");
    panel.add(settings, "3");
    panel.add(hostscn, "4");
    
    c.show(panel, "1");
    
    String ipad = InetAddress.getLocalHost().getHostAddress();
    JLabel mainlbl = new JLabel("Audio Visio Main Menu"); //the font on the pages.
    mainlbl.setFont(new Font("Verdana",1,20));
    main.add(mainlbl);
    JLabel jnlbl = new JLabel("Enter An IP Address:  ");
    jnlbl.setFont(new Font("Verdana",1,20));
    joinscn.add(jnlbl);
    JLabel hstlbl = new JLabel("Your IP Address Is: " + ipad);
    hstlbl.setFont(new Font("Verdana",1,20));
    hostscn.add(hstlbl);
    
    
    JTextField ip = new JTextField(10); //the text box for ip address.
    joinscn.add(ip);
    joinscn.add(submit);
    

    f.setVisible(true);//make the window visible
    
    /**
     * these make the buttons actually work. I'm
     * mainly using them to navigate between windows.
     */
    host.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			c.show(panel, "4");
			hostscn.add(back);
		}
    });
    join.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			c.show(panel, "2");
			joinscn.add(back);
		}
    });
    sett.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			c.show(panel, "3");
			settings.add(back);
		}
    });
    
    
    back.addActionListener(new ActionListener(){
    	public void actionPerformed(ActionEvent e) {
    		c.show(panel, "1");
    	}
    });
    
    f.add(panel);
  }
  public static void main(String args[]) throws UnknownHostException {
    new Main();
  }
}

