import javax.swing.*;

import java.awt.*;

public class second extends JFrame{
private static JLabel label;

public second() {
	label = new JLabel("Audio-Visio");
	setLayout(new FlowLayout());
	setSize(350, 300);
	setTitle("309 Test Menu");
	getContentPane().setBackground(Color.WHITE);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	add(label);
}
}
