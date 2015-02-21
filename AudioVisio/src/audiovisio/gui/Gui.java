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

import audiovisio.utils.LogHelper;

public class Gui {

	JPanel panel = new JPanel();// different screens
	JPanel main = new JPanel();
	JPanel joinScreen = new JPanel();
	JPanel hostScreen = new JPanel();
	JPanel settingsScreen = new JPanel();

	JButton host = new JButton("Host");// different buttons to access other
										// screens
	JButton join = new JButton("Join");
	JButton sett = new JButton("Settings");
	JButton back = new JButton("Back");
	JButton submit = new JButton("Submit");
	CardLayout c = new CardLayout();// don't quite understand this completely
									// but it helps
	// manage the panels and buttons
	JFrame f = new JFrame("Audio Visio"); // new window

	String ipString;

	public Gui() throws UnknownHostException {

		f.setSize(1000, 700);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setLayout(c);

		main.add(join);
		main.add(host);
		main.add(sett);

		panel.add(main, "1");// adds the screens to the window
		panel.add(joinScreen, "2");
		panel.add(settingsScreen, "3");
		panel.add(hostScreen, "4");

		c.show(panel, "1");

		ipString = getIp();

		initMainScreen();
		initJoinScreen();
		initHostScreen();
		actions();

	}

	public void start() {

		f.setVisible(true);// make the window visible
	}

	public void initMainScreen() {
		JLabel mainlbl = new JLabel("Audio Visio Main Menu"); // the font on the
																// pages.
		mainlbl.setFont(new Font("Verdana", 1, 20));
		main.add(mainlbl);
	}

	public void initJoinScreen() {
		JLabel jnlbl = new JLabel("Enter An IP Address:  ");
		jnlbl.setFont(new Font("Verdana", 1, 20));
		joinScreen.add(jnlbl);
		JTextField ip = new JTextField(10); // the text box for ip address.
		joinScreen.add(ip);
		joinScreen.add(submit);
	}

	public void initHostScreen() {
		JLabel hstlbl = new JLabel("Your IP Address Is: " + ipString);
		hstlbl.setFont(new Font("Verdana", 1, 20));
		hostScreen.add(hstlbl);
	}

	public void actions() {
		host.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c.show(panel, "4");
				hostScreen.add(back);
			}
		});
		join.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				c.show(panel, "2");
				joinScreen.add(back);
			}
		});
		sett.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				c.show(panel, "3");
				settingsScreen.add(back);
			}
		});

		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.show(panel, "1");
			}
		});

		f.add(panel);
	}

	public String getIp() {
		String temp = "";
		try {
			temp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			LogHelper.warn("UnkownHostException");
		}
		return temp;
	}
}
